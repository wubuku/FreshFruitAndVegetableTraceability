package org.dddml.ffvtraceability.domain.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class DelayedProcessingQueue<ID, CONTEXT, T extends DelayedProcessingQueue.DelayedItem<ID, CONTEXT>> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DelayQueue<T> processingQueue;
    private final Set<ID> queuedIds;
    private final Executor executor;
    private final BiConsumer<ID, CONTEXT> processor;
    private final String componentName;
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);
    private volatile boolean isShutdown = false;

    public DelayedProcessingQueue(
            Executor executor,
            BiConsumer<ID, CONTEXT> processor,
            String componentName) {
        this.executor = executor;
        this.processor = processor;
        this.componentName = componentName;
        this.processingQueue = new DelayQueue<>();
        this.queuedIds = new HashSet<>();
    }


    public void queueItemForProcessing(ID id, CONTEXT context, T delayedItem) {
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
            } catch (Exception ex) {
                logger.error("Error processing delayed item {}: {}", componentName, id, ex);
                // 如果处理失败，可以选择重新入队
                if (!isShutdown) {
                    synchronized (queuedIds) {
                        queuedIds.add(id);
                        processingQueue.offer(delayedItem);
                    }
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


    /**
     * 关闭队列，等待当前处理完成
     * 调用此方法后，队列将不再接受新的处理请求
     */
    public void close() {
        isShutdown = true;
        // 等待当前处理完成
        while (isProcessing.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public interface DelayedItem<ID, CONTEXT> extends Delayed {
        ID getId();

        CONTEXT getContext();
    }
} 