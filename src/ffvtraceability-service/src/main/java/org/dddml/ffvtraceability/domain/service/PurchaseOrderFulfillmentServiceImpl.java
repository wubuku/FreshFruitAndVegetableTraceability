package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.order.*;
import org.dddml.ffvtraceability.domain.repository.BffReceivingDocumentItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffReceivingRepository;
import org.dddml.ffvtraceability.domain.shipmentreceipt.*;
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
        // 计算需求和履行数量
        Map<OrderItemId, BigDecimal> demandQuantities = calculateDemandQuantities(orderHeaderState, orderId);
        List<BffReceivingDocumentItemProjection> receivingItems = bffReceivingRepository.findReceivingItemsByOrderId(orderId);
        Map<String, BigDecimal> fulfillmentQuantities = calculateFulfillmentQuantities(receivingItems);
        // 执行两步分配
        AllocationResult allocationResult = performTwoStepAllocation(
                orderHeaderState, orderId, demandQuantities, receivingItems, fulfillmentQuantities
        );
        // 更新收货单的分配信息
        updateReceiptAllocations(allocationResult, c);
        // 计算并返回履行状态
        return calculateFulfillmentStatus(orderHeaderState, allocationResult.getTotalAllocatedQuantities(), c);
    }

    private Map<OrderItemId, BigDecimal> calculateDemandQuantities(OrderHeaderState orderHeaderState, String orderId) {
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
            String orderId,
            Map<OrderItemId, BigDecimal> demandQuantities,
            List<BffReceivingDocumentItemProjection> receivingItems,
            Map<String, BigDecimal> fulfillmentQuantities) {

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
                performSecondStepAllocation(orderHeaderState, orderId, receivingItems,
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
            String orderId,
            List<BffReceivingDocumentItemProjection> receivingItems,
            Map<OrderItemId, BigDecimal> remainingDemandQuantities,
            Map<String, BigDecimal> remainingFulfillmentQuantities,
            List<QuantityAllocationService.UnallocatedFulfillment<String>> totalUnallocatedFulfillments
    ) {
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
            BigDecimal unallocatedQuantity = unallocatedFulfillmentQuantities.getOrDefault(receiptId, BigDecimal.ZERO);
            List<QuantityAllocationService.AllocationResult<OrderItemId, String>> allocations =
                    allocationsByReceiptId.getOrDefault(receiptId, Collections.emptyList());

            ShipmentReceiptState receiptState = shipmentReceiptApplicationService.get(receiptId);
            if (receiptState == null) {
                throw new IllegalArgumentException("Receipt not found: " + receiptId);
            }

            AbstractShipmentReceiptCommand.SimpleMergePatchShipmentReceipt mergePatchReceipt =
                    new AbstractShipmentReceiptCommand.SimpleMergePatchShipmentReceipt();
            mergePatchReceipt.setReceiptId(receiptId);
            mergePatchReceipt.setQuantityUnallocated(unallocatedQuantity);

            for (QuantityAllocationService.AllocationResult<OrderItemId, String> allocation : allocations) {
                OrderItemId orderItemId = allocation.getDemandItemId();
                ShipmentReceiptOrderAllocationState allocationState = receiptState.getOrderAllocations().get(orderItemId);
                if (allocationState == null) {
                    ShipmentReceiptOrderAllocationCommand.CreateShipmentReceiptOrderAllocation
                            createAllocation = mergePatchReceipt.newCreateShipmentReceiptOrderAllocation();
                    createAllocation.setOrderItemId(orderItemId);
                    createAllocation.setQuantityAllocated(allocation.getAllocatedQuantity());
                    mergePatchReceipt.getShipmentReceiptOrderAllocationCommands().add(createAllocation);
                } else {
                    ShipmentReceiptOrderAllocationCommand.MergePatchShipmentReceiptOrderAllocation
                            mergePatchAllocation = mergePatchReceipt.newMergePatchShipmentReceiptOrderAllocation();
                    mergePatchAllocation.setOrderItemId(orderItemId);
                    mergePatchAllocation.setQuantityAllocated(allocation.getAllocatedQuantity());
                    mergePatchReceipt.getShipmentReceiptOrderAllocationCommands().add(mergePatchAllocation);
                }
            }

            Set<OrderItemId> oldOrderItemIds = receiptState.getOrderAllocations().stream()
                    .map(ShipmentReceiptOrderAllocationState::getOrderItemId).collect(Collectors.toSet());
            Set<OrderItemId> newOrderItemIds = allocations.stream()
                    .map(QuantityAllocationService.AllocationResult::getDemandItemId).collect(Collectors.toSet());
            oldOrderItemIds.removeAll(newOrderItemIds);

            for (OrderItemId orderItemId : oldOrderItemIds) {
                ShipmentReceiptOrderAllocationCommand.RemoveShipmentReceiptOrderAllocation removeAllocation =
                        mergePatchReceipt.newRemoveShipmentReceiptOrderAllocation();
                removeAllocation.setOrderItemId(orderItemId);
                mergePatchReceipt.getShipmentReceiptOrderAllocationCommands().add(removeAllocation);
            }

            mergePatchReceipt.setVersion(receiptState.getVersion());
            mergePatchReceipt.setRequesterId(c.getRequesterId());
            mergePatchReceipt.setCommandId(UUID.randomUUID().toString());
            shipmentReceiptApplicationService.when(mergePatchReceipt);
        }
    }

    private String calculateFulfillmentStatus(
            OrderHeaderState orderHeaderState,
            Map<OrderItemId, BigDecimal> totalAllocatedQuantities,
            Command c
    ) {
        AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder = new AbstractOrderCommand.SimpleMergePatchOrder();
        mergePatchOrder.setOrderId(orderHeaderState.getOrderId());
        mergePatchOrder.setVersion(orderHeaderState.getVersion());

        boolean allFulfilled = true;
        boolean anyFulfilled = false;

        // 遍历订单项并更新它们的履行状态
        for (OrderItemState orderItem : orderHeaderState.getOrderItems()) {
            OrderItemId itemId = new OrderItemId(orderHeaderState.getOrderId(), orderItem.getOrderItemSeqId());
            BigDecimal totalAllocated = totalAllocatedQuantities.getOrDefault(itemId, BigDecimal.ZERO);
            BigDecimal demandQuantity = orderItem.getQuantity();
            // 更新订单项的履行状态
            updateOrderItemFulfillmentStatus(mergePatchOrder, orderItem, totalAllocated, demandQuantity);
            // 更新整体履行状态标志
            if (totalAllocated.compareTo(demandQuantity) < 0) {
                allFulfilled = false;
            }
            if (totalAllocated.compareTo(BigDecimal.ZERO) > 0) {
                anyFulfilled = true;
            }
        }
        // 确定整体履行状态
        String fulfillmentStatusId = determineFulfillmentStatus(allFulfilled, anyFulfilled);
        // 更新订单并返回状态
        return updateOrderFulfillmentStatus(mergePatchOrder, fulfillmentStatusId, c);
    }

    private void updateOrderItemFulfillmentStatus(
            AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder,
            OrderItemState orderItem,
            BigDecimal totalAllocated,
            BigDecimal demandQuantity
    ) {
        OrderItemCommand.MergePatchOrderItem mergePatchOrderItem = mergePatchOrder.newMergePatchOrderItem();
        mergePatchOrderItem.setOrderItemSeqId(orderItem.getOrderItemSeqId());
        mergePatchOrderItem.setFulfillmentStatusId(
                calculateItemFulfillmentStatus(totalAllocated, demandQuantity)
        );
        mergePatchOrder.getOrderItemCommands().add(mergePatchOrderItem);
    }

    private String calculateItemFulfillmentStatus(BigDecimal totalAllocated, BigDecimal demandQuantity) {
        if (totalAllocated.compareTo(demandQuantity) >= 0) {
            return FULFILLMENT_STATUS_FULFILLED;
        } else if (totalAllocated.compareTo(BigDecimal.ZERO) > 0) {
            return FULFILLMENT_STATUS_PARTIALLY_FULFILLED;
        } else {
            return FULFILLMENT_STATUS_NOT_FULFILLED;
        }
    }

    private String determineFulfillmentStatus(boolean allFulfilled, boolean anyFulfilled) {
        if (allFulfilled) {
            return FULFILLMENT_STATUS_FULFILLED;
        } else if (anyFulfilled) {
            return FULFILLMENT_STATUS_PARTIALLY_FULFILLED;
        } else {
            return FULFILLMENT_STATUS_NOT_FULFILLED;
        }
    }

    private String updateOrderFulfillmentStatus(
            AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder,
            String fulfillmentStatusId,
            Command c
    ) {
        mergePatchOrder.setFulfillmentStatusId(fulfillmentStatusId);
        mergePatchOrder.setCommandId(UUID.randomUUID().toString());
        mergePatchOrder.setRequesterId(c.getRequesterId());
        orderApplicationService.when(mergePatchOrder);
        return fulfillmentStatusId;
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