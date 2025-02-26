package org.dddml.ffvtraceability.domain.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class DelayedProcessingQueue<ID, CONTEXT, T extends DelayedProcessingQueue.DelayedItem<ID, CONTEXT>> {
    // 最大重试次数
    private static final int MAX_RETRY_COUNT = 3;
    // 基础延迟时间 (毫秒)
    private static final long BASE_DELAY_MS = 1000L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DelayQueue<T> processingQueue;
    private final Set<ID> queuedIds;
    private final Executor executor;
    private final BiConsumer<ID, CONTEXT> processor;
    private final String componentName;
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);
    // 用于跟踪重试次数的映射
    private final Map<ID, Integer> retryCountMap = new ConcurrentHashMap<>();
    // 用于创建新的延迟项的函数
    private final BiFunction<T, Long, T> itemFactory;
    private volatile boolean isShutdown = false;

    public DelayedProcessingQueue(
            Executor executor,
            BiConsumer<ID, CONTEXT> processor,
            String componentName) {
        this(executor, processor, componentName, null);
    }

    public DelayedProcessingQueue(
            Executor executor,
            BiConsumer<ID, CONTEXT> processor,
            String componentName,
            BiFunction<T, Long, T> itemFactory) {
        this.executor = executor;
        this.processor = processor;
        this.componentName = componentName;
        this.processingQueue = new DelayQueue<>();
        this.queuedIds = new HashSet<>();
        this.itemFactory = itemFactory;
    }

    public void queueItemForProcessing(T delayedItem) {
        ID id = delayedItem.getId();
        if (id == null) {
            return;
        }
        // NOTE: 虽然线程不会被永久占用，
        //   但是当有元素进入队列时，会暂时阻塞。
        //   处理完成后，如果没有新元素，线程会被释放回线程池。
        //   当新元素进来时，才会重新从线程池获取线程处理。

        synchronized (queuedIds) {
            if (queuedIds.contains(id)) {
                processingQueue.removeIf(delayed -> delayed.getId().equals(id));
            }
            queuedIds.add(id);
            processingQueue.offer(delayedItem);
            logger.debug("Queued {} ID: {} for delayed processing", componentName, id);
        }

        // 触发处理
        triggerProcessing();
    }

    private void triggerProcessing() {
        // 如果已经有处理任务在执行，就不需要再触发
        if (isProcessing.compareAndSet(false, true)) {
            executor.execute(this::processNextItem);
        }
    }

    private void processNextItem() {
        if (isShutdown) {
            return;
        }

        try {
            // 阻塞等待，直到有元素到期
            T delayedItem = processingQueue.take();

            ID id = delayedItem.getId();
            CONTEXT context = delayedItem.getContext();

            synchronized (queuedIds) {
                queuedIds.remove(id);
            }

            try {
                logger.info("Processing delayed item for {}: {}", componentName, id);
                processor.accept(id, context);
                // 处理成功，清除重试计数
                retryCountMap.remove(id);
            } catch (Exception ex) {
                // 获取当前重试次数并增加
                int retryCount = retryCountMap.getOrDefault(id, 0) + 1;
                logger.error("Error processing delayed item {}: {} (Attempt: {})", componentName, id, retryCount, ex);

                // 检查是否已达到最大重试次数
                if (!isShutdown && retryCount <= MAX_RETRY_COUNT) {
                    // 使用指数退避计算新的延迟时间
                    long newDelayMs = BASE_DELAY_MS * (1L << (retryCount - 1)); // 1s, 2s, 4s...
                    logger.info("Requeuing {} with delay {} ms (attempt {})", id, newDelayMs, retryCount);

                    retryCountMap.put(id, retryCount);
                    synchronized (queuedIds) {
                        queuedIds.add(id);

                        // 创建新的延迟项而不是重用旧项
                        T newDelayedItem;
                        if (itemFactory != null) {
                            // 使用自定义工厂创建新项
                            newDelayedItem = itemFactory.apply(delayedItem, newDelayMs);
                        } else if (delayedItem instanceof DelayedItemWithFactory) {
                            // 使用项目自身的克隆方法
                            @SuppressWarnings("unchecked")
                            DelayedItemWithFactory<ID, CONTEXT> factoryItem = (DelayedItemWithFactory<ID, CONTEXT>) delayedItem;
                            @SuppressWarnings("unchecked")
                            T newItem = (T) factoryItem.createWithNewDelay(newDelayMs);
                            newDelayedItem = newItem;
                        } else {
                            // 如果无法创建新项，记录警告并重用旧项（不理想但至少可以工作）
                            logger.warn("Cannot create new delayed item with updated delay - reusing old item");
                            newDelayedItem = delayedItem;
                        }

                        processingQueue.offer(newDelayedItem);
                    }
                } else {
                    // 超过最大重试次数
                    logger.error("Giving up on {} after {} failed attempts", id, retryCount);
                    retryCountMap.remove(id);
                }
            }

        } catch (InterruptedException e) {
            logger.warn("{} processing interrupted", componentName, e);
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            logger.error("Unexpected error in processing loop", ex);
        } finally {
            isProcessing.set(false);

            // 如果还有项目且未关闭，继续处理
            if (!isShutdown && !processingQueue.isEmpty()) {
                triggerProcessing();
            }
        }
    }

    public void close() {
        close(5000L); // 默认5秒超时
    }

    /**
     * 关闭队列，等待当前处理完成，带超时控制
     *
     * @param timeoutMs 等待处理完成的最大毫秒数
     * @return true 如果成功等待所有处理完成，false 如果超时
     */
    public boolean close(long timeoutMs) {
        isShutdown = true;

        if (!isProcessing.get()) {
            return true; // 没有正在进行的处理，直接返回
        }

        // 创建闭锁以等待处理完成
        final CountDownLatch latch = new CountDownLatch(1);

        // 启动监控线程
        Thread monitorThread = new Thread(() -> {
            while (isProcessing.get()) {
                try {
                    Thread.sleep(50); // 短暂检查
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            latch.countDown(); // 处理已完成
        });
        monitorThread.setDaemon(true);
        monitorThread.start();

        try {
            // 等待处理完成或超时
            boolean completed = latch.await(timeoutMs, TimeUnit.MILLISECONDS);
            if (!completed) {
                logger.warn("Closing {} queue timed out after {} ms", componentName, timeoutMs);
            } else {
                logger.info("Successfully closed {} queue", componentName);
            }
            return completed;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait for {} queue close was interrupted", componentName);
            return false;
        } finally {
            monitorThread.interrupt(); // 确保监控线程结束
        }
    }

    public interface DelayedItem<ID, CONTEXT> extends Delayed {
        ID getId();

        CONTEXT getContext();
    }

    /**
     * 扩展的延迟项接口，支持创建自身的新延迟版本
     */
    public interface DelayedItemWithFactory<ID, CONTEXT> extends DelayedItem<ID, CONTEXT> {
        /**
         * 创建具有新延迟时间的当前项的副本
         *
         * @param newDelayMs 新的延迟毫秒数
         * @return 具有相同ID和上下文但新延迟时间的新项
         */
        DelayedItem<ID, CONTEXT> createWithNewDelay(long newDelayMs);
    }
}