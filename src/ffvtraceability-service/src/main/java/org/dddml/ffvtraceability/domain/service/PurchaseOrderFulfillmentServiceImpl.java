package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.OrderItemQuantityAllocationValue;
import org.dddml.ffvtraceability.domain.order.*;
import org.dddml.ffvtraceability.domain.repository.BffReceivingDocumentItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffReceivingRepository;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptCommands;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffOrderConstants.*;

@Service
@Transactional
public class PurchaseOrderFulfillmentServiceImpl implements PurchaseOrderFulfillmentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private BffReceivingRepository bffReceivingRepository;

    @Autowired
    private PurchaseOrderQuantityAllocationService purchaseOrderQuantityAllocationService;

    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;

    @Override
    public String allocateAndUpdateFulfillmentStatus(String orderId, Command c) {
        OrderHeaderState orderHeaderState = getAndValidateOrder(orderId);
        logger.info("Processing order {} with version {}", orderId, orderHeaderState.getVersion());

        // 计算需求和履行数量
        Map<OrderItemId, BigDecimal> demandQuantities = calculateDemandQuantities(orderHeaderState);
        List<BffReceivingDocumentItemProjection> receivingItems = bffReceivingRepository.findReceivingItemsByOrderId(orderId);
        Map<String, BigDecimal> fulfillmentQuantities = calculateFulfillmentQuantities(receivingItems);
        // 执行两步分配
        AllocationResult allocationResult = performTwoStepAllocation(
                orderHeaderState, demandQuantities, receivingItems, fulfillmentQuantities
        );
        // 更新收货单的分配信息
        updateReceiptAllocations(allocationResult, c);
        // 计算并返回履行状态
        OrderCommands.UpdateFulfillmentStatus updateFulfillmentStatus = new OrderCommands.UpdateFulfillmentStatus();
        updateFulfillmentStatus.setOrderId(orderId);

        Map<OrderItemId, BigDecimal> allocatedQuantities = allocationResult.getTotalAllocatedQuantities();
        OrderItemQuantityAllocationValue[] orderItemQuantityAllocations = allocatedQuantities.entrySet().stream()
            .map(entry -> new OrderItemQuantityAllocationValue(
                entry.getKey().getOrderId(),
                entry.getKey().getOrderItemSeqId(),
                entry.getValue()
            ))
            .toArray(OrderItemQuantityAllocationValue[]::new);
            
        updateFulfillmentStatus.setOrderItemAllocations(orderItemQuantityAllocations);
        updateFulfillmentStatus.setVersion(orderHeaderState.getVersion());
        updateFulfillmentStatus.setCommandId(c.getCommandId());
        updateFulfillmentStatus.setRequesterId(c.getRequesterId());
        orderApplicationService.when(updateFulfillmentStatus);
        String result = orderHeaderState.getFulfillmentStatusId();
        logger.info("Completed processing order {} with version {}", orderId, orderHeaderState.getVersion());
        return result;
    }

    private Map<OrderItemId, BigDecimal> calculateDemandQuantities(OrderHeaderState orderHeaderState) {
        String orderId = orderHeaderState.getOrderId();
        return orderHeaderState.getOrderItems().stream()
                .filter(x -> x.getQuantity() != null)
                .collect(Collectors.toMap(
                        x -> new OrderItemId(orderId, x.getOrderItemSeqId()),
                        x -> x.getQuantity().subtract(x.getCancelQuantity() != null ? x.getCancelQuantity() : BigDecimal.ZERO)
                ));
    }

    private Map<String, BigDecimal> calculateFulfillmentQuantities(List<BffReceivingDocumentItemProjection> receivingItems) {
        return receivingItems.stream()
                .filter(x -> x.getQuantityAccepted() != null)
                .collect(Collectors.toMap(
                        BffReceivingDocumentItemProjection::getReceiptId,
                        BffReceivingDocumentItemProjection::getQuantityAccepted
                ));
    }

    private AllocationResult performTwoStepAllocation(
            OrderHeaderState orderHeaderState,
            Map<OrderItemId, BigDecimal> demandQuantities,
            List<BffReceivingDocumentItemProjection> receivingItems,
            Map<String, BigDecimal> fulfillmentQuantities) {
        String orderId = orderHeaderState.getOrderId();
        // 初始化
        List<QuantityAllocationService.AllocationResult<OrderItemId, String>> firstStepResults = new ArrayList<>();
        List<QuantityAllocationService.UnallocatedFulfillment<String>> totalUnallocatedFulfillments = new ArrayList<>();
        Map<OrderItemId, BigDecimal> remainingDemandQuantities = new HashMap<>(demandQuantities);
        Map<String, BigDecimal> remainingFulfillmentQuantities = new HashMap<>(fulfillmentQuantities);

        // 第一步分配
        performFirstStepAllocation(orderId, receivingItems, remainingDemandQuantities,
                remainingFulfillmentQuantities, firstStepResults, totalUnallocatedFulfillments);

        // 第二步分配
        List<QuantityAllocationService.AllocationResult<OrderItemId, String>> secondStepResults =
                performSecondStepAllocation(orderHeaderState, receivingItems,
                        remainingDemandQuantities, remainingFulfillmentQuantities, totalUnallocatedFulfillments);

        // 合并结果
        List<QuantityAllocationService.AllocationResult<OrderItemId, String>> finalResults = new ArrayList<>(firstStepResults);
        finalResults.addAll(secondStepResults);

        // 计算总分配数量
        Map<OrderItemId, BigDecimal> totalAllocatedQuantities = calculateTotalAllocatedQuantities(finalResults);

        return new AllocationResult(finalResults, totalUnallocatedFulfillments, totalAllocatedQuantities);
    }

    private void performFirstStepAllocation(
            String orderId,
            List<BffReceivingDocumentItemProjection> receivingItems,
            Map<OrderItemId, BigDecimal> remainingDemandQuantities,
            Map<String, BigDecimal> remainingFulfillmentQuantities,
            List<QuantityAllocationService.AllocationResult<OrderItemId, String>> firstStepResults,
            List<QuantityAllocationService.UnallocatedFulfillment<String>> totalUnallocatedFulfillments
    ) {
        // 处理明确指定了 orderItemSeqId 的收货行项
        List<BffReceivingDocumentItemProjection> firstStepReceivingItems = receivingItems.stream()
                .filter(x -> x.getOrderItemSeqId() != null && x.getQuantityAccepted() != null)
                .collect(Collectors.toList());
        // 按 orderItemSeqId 分组
        Map<OrderItemId, List<BffReceivingDocumentItemProjection>> receivingItemsByOrderItem = firstStepReceivingItems.stream()
                .filter(x -> {
                    OrderItemId itemId = new OrderItemId(orderId, x.getOrderItemSeqId());
                    return remainingDemandQuantities.containsKey(itemId);
                })
                .collect(Collectors.groupingBy(x -> new OrderItemId(orderId, x.getOrderItemSeqId())));
        // 对每个 orderItemSeqId 进行匹配
        for (Map.Entry<OrderItemId, List<BffReceivingDocumentItemProjection>> entry : receivingItemsByOrderItem.entrySet()) {
            OrderItemId orderItemId = entry.getKey();
            List<BffReceivingDocumentItemProjection> receivingItemsForOrderItem = entry.getValue();

            QuantityAllocationService.AllocationResultWrapper<OrderItemId, String> allocationResultWrapper =
                    purchaseOrderQuantityAllocationService.allocateQuantities(
                            receivingItemsForOrderItem.stream()
                                    .map(x -> PurchaseOrderQuantityAllocationService.createFulfillmentItem(
                                            x.getReceiptId(),
                                            x.getQuantityAccepted()
                                    )).collect(Collectors.toList()),
                            Collections.singletonList(
                                    PurchaseOrderQuantityAllocationService.createDemandItem(
                                            orderItemId,
                                            remainingDemandQuantities.get(orderItemId)
                                    )
                            )
                    );

            // 添加分配结果
            firstStepResults.addAll(allocationResultWrapper.getAllocatedResults());
            totalUnallocatedFulfillments.addAll(allocationResultWrapper.getUnallocatedFulfillments());

            // 更新剩余数量
            allocationResultWrapper.getAllocatedResults().forEach(allocation -> {
                // 更新需求剩余量
                OrderItemId demandId = allocation.getDemandItemId();
                BigDecimal remainingDemand = remainingDemandQuantities.get(demandId)
                        .subtract(allocation.getAllocatedQuantity());
                remainingDemandQuantities.put(demandId, remainingDemand);

                // 更新履行剩余量
                String fulfillmentId = allocation.getFulfillmentItemId();
                BigDecimal remainingFulfillment = remainingFulfillmentQuantities.get(fulfillmentId)
                        .subtract(allocation.getAllocatedQuantity());
                remainingFulfillmentQuantities.put(fulfillmentId, remainingFulfillment);
            });
        }
    }

    private List<QuantityAllocationService.AllocationResult<OrderItemId, String>> performSecondStepAllocation(
            OrderHeaderState orderHeaderState,
            List<BffReceivingDocumentItemProjection> receivingItems,
            Map<OrderItemId, BigDecimal> remainingDemandQuantities,
            Map<String, BigDecimal> remainingFulfillmentQuantities,
            List<QuantityAllocationService.UnallocatedFulfillment<String>> totalUnallocatedFulfillments
    ) {
        String orderId = orderHeaderState.getOrderId();
        List<QuantityAllocationService.AllocationResult<OrderItemId, String>> secondStepResults = new ArrayList<>();
        // 按 productId 分组
        Map<String, List<OrderItemState>> orderItemsByProduct = orderHeaderState.getOrderItems().stream()
                .filter(x -> remainingDemandQuantities.containsKey(new OrderItemId(orderId, x.getOrderItemSeqId())))
                .collect(Collectors.groupingBy(OrderItemState::getProductId));
        Map<String, List<BffReceivingDocumentItemProjection>> receivingItemsByProduct = receivingItems.stream()
                .filter(x -> x.getOrderItemSeqId() == null) // 过滤掉已经在第一步处理过的项
                .filter(x -> remainingFulfillmentQuantities.containsKey(x.getReceiptId()))
                .collect(Collectors.groupingBy(BffReceivingDocumentItemProjection::getProductId));

        // 对每个产品进行匹配
        for (String productId : receivingItemsByProduct.keySet()) {
            List<OrderItemState> matchedOrderItems = orderItemsByProduct.get(productId);
            if (matchedOrderItems == null || matchedOrderItems.isEmpty()) {
                continue;
            }
            List<BffReceivingDocumentItemProjection> receivingItemsForProduct = receivingItemsByProduct.get(productId);
            QuantityAllocationService.AllocationResultWrapper<OrderItemId, String> allocationResultWrapper =
                    purchaseOrderQuantityAllocationService.allocateQuantities(
                            receivingItemsForProduct.stream()
                                    .filter(x -> x.getQuantityAccepted() != null)
                                    .map(x -> PurchaseOrderQuantityAllocationService.createFulfillmentItem(
                                            x.getReceiptId(),
                                            remainingFulfillmentQuantities.get(x.getReceiptId())
                                    )).collect(Collectors.toList()),
                            matchedOrderItems.stream()
                                    .filter(x -> x.getQuantity() != null)
                                    .map(x -> {
                                        OrderItemId itemId = new OrderItemId(orderId, x.getOrderItemSeqId());
                                        return PurchaseOrderQuantityAllocationService.createDemandItem(
                                                itemId,
                                                remainingDemandQuantities.get(itemId)
                                        );
                                    }).collect(Collectors.toList())
                    );

            secondStepResults.addAll(allocationResultWrapper.getAllocatedResults());
            totalUnallocatedFulfillments.addAll(allocationResultWrapper.getUnallocatedFulfillments());
        }

        return secondStepResults;
    }

    private void updateReceiptAllocations(AllocationResult allocationResult, Command c) {
        // 将 totalUnallocatedFulfillments 转换为 Map<String, BigDecimal>
        Map<String, BigDecimal> unallocatedFulfillmentQuantities = allocationResult.getTotalUnallocatedFulfillments().stream()
                .collect(Collectors.toMap(
                        QuantityAllocationService.UnallocatedFulfillment::getFulfillmentItemId,
                        QuantityAllocationService.UnallocatedFulfillment::getUnallocatedQuantity
                ));

        // 将 finalResults 以 receiptId 进行分组
        Map<String, List<QuantityAllocationService.AllocationResult<OrderItemId, String>>> allocationsByReceiptId
                = allocationResult.getFinalResults().stream()
                .collect(Collectors.groupingBy(
                        QuantityAllocationService.AllocationResult::getFulfillmentItemId
                ));

        Set<String> allReceiptIds = new HashSet<>(unallocatedFulfillmentQuantities.keySet());
        allReceiptIds.addAll(allocationsByReceiptId.keySet());

        for (String receiptId : allReceiptIds) {

            ShipmentReceiptState receiptState = shipmentReceiptApplicationService.get(receiptId);
            if (receiptState == null) {
                throw new IllegalArgumentException("Receipt not found: " + receiptId);
            }

            BigDecimal unallocatedQuantity = unallocatedFulfillmentQuantities.getOrDefault(receiptId, BigDecimal.ZERO);
            List<QuantityAllocationService.AllocationResult<OrderItemId, String>> allocations =
                    allocationsByReceiptId.getOrDefault(receiptId, Collections.emptyList());
            ShipmentReceiptCommands.UpdateOrderAllocation updateOrderAllocation = new ShipmentReceiptCommands.UpdateOrderAllocation();
            updateOrderAllocation.setReceiptId(receiptId);
            updateOrderAllocation.setUnallocatedQuantity(unallocatedQuantity);
            updateOrderAllocation.setVersion(receiptState.getVersion());
            OrderItemQuantityAllocationValue[] alValues = allocations.stream().map(x -> new OrderItemQuantityAllocationValue(
                    x.getDemandItemId().getOrderId(),
                    x.getDemandItemId().getOrderItemSeqId(),
                    x.getAllocatedQuantity()
            )).toArray(OrderItemQuantityAllocationValue[]::new);

            updateOrderAllocation.setOrderItemAllocations(alValues);
            updateOrderAllocation.setCommandId(UUID.randomUUID().toString());//
            updateOrderAllocation.setRequesterId(c.getRequesterId());
            shipmentReceiptApplicationService.when(updateOrderAllocation);
        } // end for
    }

    private Map<OrderItemId, BigDecimal> calculateTotalAllocatedQuantities(
            List<QuantityAllocationService.AllocationResult<OrderItemId, String>> finalResults
    ) {
        return finalResults.stream()
                .collect(Collectors.groupingBy(
                        QuantityAllocationService.AllocationResult::getDemandItemId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                QuantityAllocationService.AllocationResult::getAllocatedQuantity,
                                BigDecimal::add
                        )
                ));
    }


    private OrderHeaderState getAndValidateOrder(String orderId) {
        OrderHeaderState orderHeaderState = orderApplicationService.get(orderId);
        if (orderHeaderState == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return orderHeaderState;
    }


} 