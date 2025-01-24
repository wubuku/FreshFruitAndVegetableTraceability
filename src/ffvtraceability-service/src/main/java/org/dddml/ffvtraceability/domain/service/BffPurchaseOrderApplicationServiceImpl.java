package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderItemDto;
import org.dddml.ffvtraceability.domain.mapper.BffPurchaseOrderMapper;
import org.dddml.ffvtraceability.domain.order.*;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.repository.BffOrderRepository;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderAndItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffReceivingRepository;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffOrderConstants.*;

@Service
@Transactional
public class BffPurchaseOrderApplicationServiceImpl implements BffPurchaseOrderApplicationService {

    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private BffOrderRepository bffOrderRepository;
    @Autowired
    private BffPurchaseOrderMapper bffPurchaseOrderMapper;
    @Autowired
    private PurchaseOrderQuantityAllocationService purchaseOrderQuantityAllocationService;
    @Autowired
    private BffReceivingRepository bffReceivingRepository;
    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;
    @Autowired
    private PurchaseOrderFulfillmentService purchaseOrderFulfillmentService;

    @Override
    @Transactional(readOnly = true)
    public Page<BffPurchaseOrderDto> when(BffPurchaseOrderServiceCommands.GetPurchaseOrders c) {
        int offset = c.getPage() * c.getSize();
        long totalElements = bffOrderRepository.countTotalShipments(
                c.getOrderIdOrItem(),
                c.getSupplierId(),
                c.getOrderDateFrom(),
                c.getOrderDateTo()
        );

        List<BffPurchaseOrderAndItemProjection> projections =
                bffOrderRepository.findAllPurchaseOrdersWithItems(
                        offset,
                        c.getSize(),
                        c.getOrderIdOrItem(),
                        c.getSupplierId(),
                        c.getOrderDateFrom(),
                        c.getOrderDateTo()
                );

        List<BffPurchaseOrderDto> purchaseOrders = projections.stream()
                .collect(Collectors.groupingBy(
                        proj -> bffPurchaseOrderMapper.toBffPurchaseOrderDto(proj),
                        Collectors.mapping(
                                proj -> proj.getOrderItemSeqId() != null
                                        ? bffPurchaseOrderMapper.toBffPurchaseOrderItemDto(proj)
                                        : null,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList())
                                )
                        )
                ))
                .entrySet().stream()
                .map(entry -> {
                    BffPurchaseOrderDto d = entry.getKey();
                    d.setOrderItems(entry.getValue());
                    return d;
                })
                .collect(Collectors.toList());

        return Page.builder(purchaseOrders)
                .totalElements(totalElements)
                .size(c.getSize())
                .number(c.getPage())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BffPurchaseOrderDto when(BffPurchaseOrderServiceCommands.GetPurchaseOrder c) {
        List<BffPurchaseOrderAndItemProjection> projections =
                bffOrderRepository.findPurchaseOrderWithItems(c.getOrderId());

        if (projections.isEmpty()) {
            return null;
        }
        // 构建采购订单DTO
        BffPurchaseOrderDto order = bffPurchaseOrderMapper.toBffPurchaseOrderDto(projections.get(0));
        // 添加行项
        order.setOrderItems(
                projections.stream()
                        .filter(p -> p.getOrderItemSeqId() != null)
                        .map(bffPurchaseOrderMapper::toBffPurchaseOrderItemDto)
                        .collect(Collectors.toList())
        );
        if (c.getIncludesItemFulfillments() != null && c.getIncludesItemFulfillments()) {
            for (BffPurchaseOrderItemDto item : order.getOrderItems()) {
                item.setFulfillments(
                        bffReceivingRepository.findPurchaseOrderItemFulfillments(
                                        c.getOrderId(), item.getOrderItemSeqId()
                                ).stream()
                                .map(bffPurchaseOrderMapper::toBffPurchaseOrderFulfillmentDto)
                                .collect(Collectors.toList())
                );
            }
        }

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public BffPurchaseOrderItemDto when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItem c) {
        BffPurchaseOrderAndItemProjection projection =
                bffOrderRepository.findPurchaseOrderItem(c.getOrderId(), c.getOrderItemSeqId());
        if (projection == null) {
            return null;
        }
        BffPurchaseOrderItemDto orderItem = bffPurchaseOrderMapper.toBffPurchaseOrderItemDto(projection);
        if (c.getIncludesFulfillments() != null && c.getIncludesFulfillments()) {
            orderItem.setFulfillments(
                    bffReceivingRepository.findPurchaseOrderItemFulfillments(
                                    c.getOrderId(), c.getOrderItemSeqId()
                            ).stream()
                            .map(bffPurchaseOrderMapper::toBffPurchaseOrderFulfillmentDto)
                            .collect(Collectors.toList())
            );
        }
        return orderItem;
    }

    @Override
    public BigDecimal when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItemOutstandingQuantity c) {
        return bffOrderRepository.findPurchaseOrderItemOutstandingQuantity(c.getOrderId(), c.getOrderItemSeqId())
                .orElse(null);
    }

    @Override
    public BigDecimal when(BffPurchaseOrderServiceCommands.GetPurchaseOrderProductOutstandingQuantityByProductId c) {
        return bffOrderRepository.findPurchaseOrderItemOutstandingQuantityByProductId(c.getOrderId(), c.getProductId())
                .orElse(null);
    }

    @Override
    @Transactional
    public String when(BffPurchaseOrderServiceCommands.CreatePurchaseOrder c) {
        BffPurchaseOrderDto purchaseOrder = c.getPurchaseOrder();
        // Create order header
        AbstractOrderCommand.SimpleCreateOrder createOrder = new AbstractOrderCommand.SimpleCreateOrder();
        createOrder.setOrderId(purchaseOrder.getOrderId() != null ? purchaseOrder.getOrderId() : IdUtils.randomId());
        createOrder.setOrderTypeId(PURCHASE_ORDER);

        // Set additional order header fields
        createOrder.setOrderName(purchaseOrder.getOrderName());
        createOrder.setExternalId(purchaseOrder.getExternalId());
        createOrder.setOrderDate(
                purchaseOrder.getOrderDate() != null ? purchaseOrder.getOrderDate() : OffsetDateTime.now()
        );
        createOrder.setEntryDate(OffsetDateTime.now());
        createOrder.setCurrencyUomId(purchaseOrder.getCurrencyUomId());
        createOrder.setOriginFacilityId(purchaseOrder.getOriginFacilityId());
        createOrder.setMemo(purchaseOrder.getMemo());
        createOrder.setContactDescription(purchaseOrder.getContactDescription());

        // Set audit fields
        createOrder.setCommandId(createOrder.getOrderId());
        createOrder.setRequesterId(c.getRequesterId());

        // Create supplier role
        OrderRoleCommand.CreateOrderRole createOrderRole = createOrder.newCreateOrderRole();
        createOrderRole.setPartyRoleId(new PartyRoleId(purchaseOrder.getSupplierId(), ROLE_TYPE_SUPPLIER));
        createOrder.getCreateOrderRoleCommands().add(createOrderRole);

        // Create order items
        if (purchaseOrder.getOrderItems() != null) {
            int itemSeq = 1;
            for (BffPurchaseOrderItemDto item : purchaseOrder.getOrderItems()) {
                OrderItemCommand.CreateOrderItem createOrderItem = createOrder.newCreateOrderItem();
                item.setOrderItemSeqId(itemSeq++ + "");
                setupCreateOrderItemCommand(createOrderItem, item);
                createOrder.getCreateOrderItemCommands().add(createOrderItem);
            }
        }

        orderApplicationService.when(createOrder);
        return createOrder.getOrderId();
    }

    @Override
    @Transactional
    public String when(BffPurchaseOrderServiceCommands.CreatePurchaseOrderItem c) {
        if (c.getOrderId() == null) {
            throw new IllegalArgumentException("OrderId is required.");
        }
        OrderHeaderState orderHeaderState = getAndValidateOrder(c.getOrderId());

        AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder = new AbstractOrderCommand.SimpleMergePatchOrder();
        mergePatchOrder.setOrderId(c.getOrderId());
        mergePatchOrder.setVersion(orderHeaderState.getVersion());

        OrderItemCommand.CreateOrderItem createOrderItem = mergePatchOrder.newCreateOrderItem();
        setupCreateOrderItemCommand(createOrderItem, c.getPurchaseOrderItem());
        mergePatchOrder.getOrderItemCommands().add(createOrderItem);

        mergePatchOrder.setCommandId(c.getCommandId() != null ?
                c.getCommandId() : IdUtils.randomId());
        mergePatchOrder.setRequesterId(c.getRequesterId());

        orderApplicationService.when(mergePatchOrder);
        return createOrderItem.getOrderItemSeqId();
    }

    @Override
    @Transactional
    public void when(BffPurchaseOrderServiceCommands.DeletePurchaseOrderItem c) {
        if (c.getOrderId() == null || c.getOrderItemSeqId() == null) {
            throw new IllegalArgumentException("OrderId and OrderItemSeqId are required.");
        }
        OrderHeaderState orderHeaderState = getAndValidateOrder(c.getOrderId());

        AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder =
                new AbstractOrderCommand.SimpleMergePatchOrder();
        mergePatchOrder.setOrderId(c.getOrderId());
        mergePatchOrder.setVersion(orderHeaderState.getVersion());

        OrderItemCommand.RemoveOrderItem removeOrderItem = mergePatchOrder.newRemoveOrderItem();
        removeOrderItem.setOrderItemSeqId(c.getOrderItemSeqId());

        mergePatchOrder.getOrderItemCommands().add(removeOrderItem);
        mergePatchOrder.setCommandId(c.getCommandId() != null ?
                c.getCommandId() : IdUtils.randomId());
        mergePatchOrder.setRequesterId(c.getRequesterId());

        orderApplicationService.when(mergePatchOrder);
    }

    @Override
    @Transactional
    public void when(BffPurchaseOrderServiceCommands.UpdatePurchaseOrderItem c) {
        if (c.getOrderId() == null || c.getOrderItemSeqId() == null) {
            throw new IllegalArgumentException("OrderId and OrderItemSeqId are required.");
        }
        OrderHeaderState orderHeaderState = getAndValidateOrder(c.getOrderId());
        AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder =
                new AbstractOrderCommand.SimpleMergePatchOrder();
        mergePatchOrder.setOrderId(c.getOrderId());
        mergePatchOrder.setVersion(orderHeaderState.getVersion());

        OrderItemCommand.MergePatchOrderItem mergePatchOrderItem = mergePatchOrder.newMergePatchOrderItem();
        BffPurchaseOrderItemDto item = new BffPurchaseOrderItemDto();
        item.setOrderItemSeqId(c.getOrderItemSeqId());
        item.setQuantity(c.getQuantity());
        item.setUnitPrice(c.getUnitPrice());
        item.setEstimatedShipDate(c.getEstimatedShipDate());
        item.setEstimatedDeliveryDate(c.getEstimatedDeliveryDate());
        item.setItemDescription(c.getItemDescription());
        item.setComments(c.getComments());
        item.setSupplierProductId(c.getSupplierProductId());

        setupMergePatchOrderItemCommand(mergePatchOrderItem, item);
        mergePatchOrder.getOrderItemCommands().add(mergePatchOrderItem);

        mergePatchOrder.setCommandId(c.getCommandId() != null ?
                c.getCommandId() : IdUtils.randomId());
        mergePatchOrder.setRequesterId(c.getRequesterId());

        orderApplicationService.when(mergePatchOrder);
    }


    @Override
    @Transactional
    public void when(BffPurchaseOrderServiceCommands.UpdatePurchaseOrder c) {
        if (c.getOrderId() == null) {
            throw new IllegalArgumentException("OrderId is required.");
        }
        OrderHeaderState orderHeaderState = getAndValidateOrder(c.getOrderId());

        BffPurchaseOrderDto purchaseOrder = c.getPurchaseOrder();
        AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder = new AbstractOrderCommand.SimpleMergePatchOrder();
        mergePatchOrder.setOrderId(c.getOrderId());
        mergePatchOrder.setVersion(orderHeaderState.getVersion());
        // Set order header fields
        mergePatchOrder.setOrderName(purchaseOrder.getOrderName());
        mergePatchOrder.setExternalId(purchaseOrder.getExternalId());
        mergePatchOrder.setOrderDate(purchaseOrder.getOrderDate());
        mergePatchOrder.setCurrencyUomId(purchaseOrder.getCurrencyUomId());
        mergePatchOrder.setOriginFacilityId(purchaseOrder.getOriginFacilityId());
        mergePatchOrder.setMemo(purchaseOrder.getMemo());

        if (purchaseOrder.getOrderItems() != null) {
            for (BffPurchaseOrderItemDto item : purchaseOrder.getOrderItems()) {
                if (item.getOrderItemSeqId() != null) {
                    orderHeaderState.getOrderItems().stream().filter(x ->
                            item.getOrderItemSeqId().equals(x.getOrderItemSeqId())
                    ).findFirst().ifPresentOrElse(orderItemState -> {
                                OrderItemCommand.MergePatchOrderItem mergePatchOrderItem =
                                        mergePatchOrder.newMergePatchOrderItem();
                                setupMergePatchOrderItemCommand(mergePatchOrderItem, item);
                                mergePatchOrder.getOrderItemCommands().add(mergePatchOrderItem);
                            },
                            () -> {
                                throw new IllegalArgumentException("Order item not found: " +
                                        item.getOrderItemSeqId());
                            }
                    );
                } else {
                    OrderItemCommand.CreateOrderItem createOrderItem = mergePatchOrder.newCreateOrderItem();
                    setupCreateOrderItemCommand(createOrderItem, item);
                    mergePatchOrder.getOrderItemCommands().add(createOrderItem);
                }
            }
        }

        mergePatchOrder.setCommandId(c.getCommandId() != null ?
                c.getCommandId() : IdUtils.randomId());
        mergePatchOrder.setRequesterId(c.getRequesterId());

        orderApplicationService.when(mergePatchOrder);
    }

    @Override
    @Transactional
    public String when(BffPurchaseOrderServiceCommands.RecalculateFulfillmentStatus c) {
        return purchaseOrderFulfillmentService.allocateAndUpdateFulfillmentStatus(c.getOrderId(), c);
    }

    private void setupMergePatchOrderItemCommand(OrderItemCommand.MergePatchOrderItem mergePatchOrderItem,
                                                 BffPurchaseOrderItemDto item
    ) {
        mergePatchOrderItem.setOrderItemSeqId(item.getOrderItemSeqId());
        mergePatchOrderItem.setQuantity(item.getQuantity());
        mergePatchOrderItem.setUnitPrice(item.getUnitPrice());
        mergePatchOrderItem.setEstimatedShipDate(item.getEstimatedShipDate());
        mergePatchOrderItem.setEstimatedDeliveryDate(item.getEstimatedDeliveryDate());
        mergePatchOrderItem.setItemDescription(item.getItemDescription());
        mergePatchOrderItem.setComments(item.getComments());
        mergePatchOrderItem.setExternalId(item.getExternalId());
        mergePatchOrderItem.setSupplierProductId(item.getSupplierProductId());
    }

    private void setupCreateOrderItemCommand(OrderItemCommand.CreateOrderItem createOrderItem,
                                             BffPurchaseOrderItemDto item
    ) {
        //TODO 这里如果item.getOrderItemSeqId() != null，但是数据库里面已经存在了呢？
        createOrderItem.setOrderItemSeqId(
                item.getOrderItemSeqId() != null ? item.getOrderItemSeqId() : IdUtils.randomId()
        );
        createOrderItem.setOrderItemTypeId(PRODUCT_ORDER_ITEM);
        createOrderItem.setProductId(item.getProductId());
        createOrderItem.setQuantity(item.getQuantity());
        createOrderItem.setUnitPrice(item.getUnitPrice());
        createOrderItem.setEstimatedShipDate(item.getEstimatedShipDate());
        createOrderItem.setEstimatedDeliveryDate(item.getEstimatedDeliveryDate());
        createOrderItem.setItemDescription(item.getItemDescription());
        createOrderItem.setComments(item.getComments());
        createOrderItem.setExternalId(item.getExternalId());
        createOrderItem.setSupplierProductId(item.getSupplierProductId());
    }

    private OrderHeaderState getAndValidateOrder(String orderId) {
        OrderHeaderState orderHeaderState = orderApplicationService.get(orderId);
        if (orderHeaderState == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return orderHeaderState;
    }
}
