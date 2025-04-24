package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.documentnumbergenerator.DocumentNumberGeneratorApplicationService;
import org.dddml.ffvtraceability.domain.documentnumbergenerator.DocumentNumberGeneratorCommands;
import org.dddml.ffvtraceability.domain.mapper.BffSalesOrderMapper;
import org.dddml.ffvtraceability.domain.order.*;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.repository.BffSalesOrderAndItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffSalesOrderRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffOrderConstants.*;

@Service
public class BffSalesOrderApplicationServiceIml implements BffSalesOrderApplicationService {
    @Autowired
    private DocumentNumberGeneratorApplicationService documentNumberGeneratorApplicationService;
    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private BffSalesOrderRepository bffSalesOrderRepository;
    @Autowired
    private BffSalesOrderMapper bffSalesOrderMapper;
    @Autowired
    private ProductQueryService productQueryService;

    @Override
    @Transactional(readOnly = true)
    public Page<BffSalesOrderDto> when(BffSalesOrderServiceCommands.GetSalesOrders c) {
        int offset = c.getPage() * c.getSize();
        long totalElements = bffSalesOrderRepository.countTotalSalesOrders(
                c.getOrderIdOrItem(),
                c.getCustomerId(),
                c.getOrderDateFrom(),
                c.getOrderDateTo()
        );

        List<BffSalesOrderAndItemProjection> projections =
                bffSalesOrderRepository.findAllSalesOrdersWithItems(
                        offset,
                        c.getSize(),
                        c.getOrderIdOrItem(),
                        c.getCustomerId(),
                        c.getOrderDateFrom(),
                        c.getOrderDateTo()
                );

        List<BffSalesOrderDto> purchaseOrders = projections.stream()
                .collect(Collectors.groupingBy(
                        proj -> bffSalesOrderMapper.toBffSalesOrderDto(proj),
                        Collectors.mapping(
                                proj -> proj.getOrderItemSeqId() != null
                                        ? bffSalesOrderMapper.toBffSalesOrderItemDto(proj)
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
                    BffSalesOrderDto d = entry.getKey();
                    d.setOrderItems(entry.getValue());
                    return d;
                })
                .collect(Collectors.toList());

        if (c.getIncludesProductDetails() != null && c.getIncludesProductDetails()) {
            purchaseOrders.forEach(po -> {
                if (po.getOrderItems() != null) {
                    po.getOrderItems().forEach(item -> {
                        item.setProduct(productQueryService.getProduct(item.getProductId()));
                    });
                }
            });
        }
        return Page.builder(purchaseOrders)
                .totalElements(totalElements)
                .size(c.getSize())
                .number(c.getPage())
                .build();
    }

    @Override
    public BffSalesOrderDto when(BffSalesOrderServiceCommands.GetSalesOrder c) {
        List<BffSalesOrderAndItemProjection> projections =
                bffSalesOrderRepository.findSalesOrderWithItems(c.getOrderId());

        if (projections.isEmpty()) {
            return null;
        }
        // 构建销售订单DTO
        BffSalesOrderDto order = bffSalesOrderMapper.toBffSalesOrderDto(projections.get(0));
        // 添加行项
        order.setOrderItems(
                projections.stream()
                        .filter(p -> p.getOrderItemSeqId() != null)
                        .map(bffSalesOrderMapper::toBffSalesOrderItemDto)
                        .collect(Collectors.toList())
        );
//        if (c.getIncludesItemFulfillments() != null && c.getIncludesItemFulfillments()) {
//            for (BffSalesOrderItemDto item : order.getOrderItems()) {
//                item.setFulfillments(
//                        bffReceivingRepository.findPurchaseOrderItemFulfillments(
//                                        c.getOrderId(), item.getOrderItemSeqId()
//                                ).stream()
//                                .map(bffPurchaseOrderMapper::toBffPurchaseOrderFulfillmentDto)
//                                .collect(Collectors.toList())
//                );
//            }
//        }
        if (c.getIncludesProductDetails() != null && c.getIncludesProductDetails()) {
            order.getOrderItems().forEach(orderItem -> {
                orderItem.setProduct(productQueryService.getProduct(orderItem.getProductId()));
            });
        }
        return order;
    }

    @Override
    public BffSalesOrderItemDto when(BffSalesOrderServiceCommands.GetSalesOrderItem c) {
        return null;
    }

    @Override
    public BigDecimal when(BffSalesOrderServiceCommands.GetSalesOrderItemOutstandingQuantity c) {
        return null;
    }

    @Override
    public BigDecimal when(BffSalesOrderServiceCommands.GetSalesProductOutstandingQuantityByProductId c) {
        return null;
    }

    @Override
    @Transactional
    public String when(BffSalesOrderServiceCommands.CreateSalesOrder c) {
        SalesOrderVo salesOrder = c.getSalesOrder();
        if (salesOrder == null) {
            throw new IllegalArgumentException("Sales order can't be null");
        }
        String orderId = salesOrder.getOrderId();
        if (orderId != null) {
            orderId = orderId.trim();
            salesOrder.setOrderId(orderId);
            if (orderApplicationService.get(orderId) != null) {
                throw new IllegalArgumentException("The sales order already exists:" + orderId);
            }
        }
        OffsetDateTime now = OffsetDateTime.now();
        // Create order header
        AbstractOrderCommand.SimpleCreateOrder createOrder = new AbstractOrderCommand.SimpleCreateOrder();
        if (salesOrder.getOrderId() != null && !salesOrder.getOrderId().isBlank()) {
            createOrder.setOrderId(salesOrder.getOrderId());
        } else {
            DocumentNumberGeneratorCommands.GenerateNextNumber generateNextNumber = new DocumentNumberGeneratorCommands.GenerateNextNumber();
            generateNextNumber.setGeneratorId(SALES_ORDER);
            generateNextNumber.setRequesterId(c.getRequesterId());
            createOrder.setOrderId(documentNumberGeneratorApplicationService.when(generateNextNumber));
        }
        createOrder.setOrderTypeId(SALES_ORDER);

        // Set additional order header fields
//        createOrder.setOrderName(salesOrder.getOrderName());
//        createOrder.setExternalId(salesOrder.getExternalId());
        createOrder.setOrderDate(salesOrder.getOrderDate() != null ? salesOrder.getOrderDate() : now);
        createOrder.setEntryDate(now);
        createOrder.setCurrencyUomId(salesOrder.getCurrencyUomId());
//        createOrder.setOriginFacilityId(salesOrder.getOriginFacilityId());
        createOrder.setMemo(salesOrder.getMemo());
//        createOrder.setContactDescription(salesOrder.getContactDescription());

        // Set audit fields
        createOrder.setCommandId(createOrder.getOrderId());
        createOrder.setRequesterId(c.getRequesterId());

        // Create supplier role
        OrderRoleCommand.CreateOrderRole createOrderRole = createOrder.newCreateOrderRole();
        createOrderRole.setPartyRoleId(new PartyRoleId(salesOrder.getCustomerId(), ROLE_TYPE_CUSTOMER));
        createOrder.getCreateOrderRoleCommands().add(createOrderRole);

        // Create order items
        if (salesOrder.getOrderItems() != null) {
            //int itemSeq = 1;
            for (SalesOrderItemVo item : salesOrder.getOrderItems()) {
                OrderItemCommand.CreateOrderItem createOrderItem = createOrder.newCreateOrderItem();
                //item.setOrderItemSeqId(itemSeq++ + "");
                setupCreateOrderItemCommand(createOrderItem, item);
                createOrder.getCreateOrderItemCommands().add(createOrderItem);
            }
        }

        orderApplicationService.when(createOrder);
        return createOrder.getOrderId();
    }


    private void setupCreateOrderItemCommand(OrderItemCommand.CreateOrderItem createOrderItem,
                                             SalesOrderItemVo item) {
        //TODO 这里如果item.getOrderItemSeqId() != null，但是数据库里面已经存在了呢？
        createOrderItem.setOrderItemSeqId(
                item.getOrderItemSeqId() != null ? item.getOrderItemSeqId() : IdUtils.randomId()
        );
        createOrderItem.setOrderItemTypeId(PRODUCT_ORDER_ITEM);
        createOrderItem.setProductId(item.getProductId());
        createOrderItem.setQuantity(item.getQuantity());
//        createOrderItem.setUnitPrice(item.getUnitPrice());
//        createOrderItem.setEstimatedShipDate(item.getEstimatedShipDate());
//        createOrderItem.setEstimatedDeliveryDate(item.getEstimatedDeliveryDate());
//        createOrderItem.setItemDescription(item.getItemDescription());
//        createOrderItem.setComments(item.getComments());
//        createOrderItem.setExternalId(item.getExternalId());
//        createOrderItem.setSupplierProductId(item.getCustomerProductId());
    }

    @Override
    @Transactional
    public void when(BffSalesOrderServiceCommands.UpdateSalesOrder c) {
        if (c.getOrderId() == null) {
            throw new IllegalArgumentException("OrderId is required.");
        }
        OrderHeaderState orderHeaderState = getAndValidateOrder(c.getOrderId());
        if (orderHeaderState.getOrderId() == null) {
            throw new IllegalArgumentException("Order not found: " + c.getOrderId());
        }
        UpdateSalesOrderVo salesOrder = c.getSalesOrder();
        AbstractOrderCommand.SimpleMergePatchOrder mergePatchOrder = new AbstractOrderCommand.SimpleMergePatchOrder();
        mergePatchOrder.setOrderId(c.getOrderId());
        mergePatchOrder.setVersion(orderHeaderState.getVersion());
        // Set order header fields
//        mergePatchOrder.setOrderName(salesOrder.getOrderName());
//        mergePatchOrder.setExternalId(salesOrder.getExternalId());
        mergePatchOrder.setOrderDate(salesOrder.getOrderDate());
        mergePatchOrder.setCurrencyUomId(salesOrder.getCurrencyUomId());
//        mergePatchOrder.setOriginFacilityId(salesOrder.getOriginFacilityId());
        mergePatchOrder.setMemo(salesOrder.getMemo());

        List<String> orderItemSeqIds = new ArrayList<>();
        if (salesOrder.getOrderItems() != null) {
            for (SalesOrderItemVo item : salesOrder.getOrderItems()) {
                if (item.getOrderItemSeqId() != null && !item.getOrderItemSeqId().isBlank()) {
                    orderItemSeqIds.add(item.getOrderItemSeqId());
                    orderHeaderState.getOrderItems().stream().filter(x ->
                            item.getOrderItemSeqId().equals(x.getOrderItemSeqId())
                    ).findFirst().ifPresentOrElse(orderItemState -> {
                                OrderItemCommand.MergePatchOrderItem mergePatchOrderItem = mergePatchOrder.newMergePatchOrderItem();
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
        //原来存在的行项目，在这次前端传过来的行项中不存在
        orderHeaderState.getOrderItems().forEach(orderItemState -> {
            if (!orderItemSeqIds.contains(orderItemState.getOrderItemSeqId())) {
                OrderItemCommand.RemoveOrderItem removeOrderItem = mergePatchOrder.newRemoveOrderItem();
                removeOrderItem.setOrderItemSeqId(orderItemState.getOrderItemSeqId());
                removeOrderItem.setRequesterId(c.getRequesterId());
                mergePatchOrder.getOrderItemCommands().add(removeOrderItem);
            }
        });

        mergePatchOrder.setCommandId(c.getCommandId() != null ? c.getCommandId() : IdUtils.randomId());
        mergePatchOrder.setRequesterId(c.getRequesterId());

        orderApplicationService.when(mergePatchOrder);

    }


    private void setupMergePatchOrderItemCommand(OrderItemCommand.MergePatchOrderItem mergePatchOrderItem,
                                                 SalesOrderItemVo item
    ) {
        mergePatchOrderItem.setOrderItemSeqId(item.getOrderItemSeqId());
        mergePatchOrderItem.setProductId(item.getProductId());
        mergePatchOrderItem.setQuantity(item.getQuantity());
//        mergePatchOrderItem.setUnitPrice(item.getUnitPrice());
//        mergePatchOrderItem.setEstimatedShipDate(item.getEstimatedShipDate());
//        mergePatchOrderItem.setEstimatedDeliveryDate(item.getEstimatedDeliveryDate());
//        mergePatchOrderItem.setItemDescription(item.getItemDescription());
//        mergePatchOrderItem.setComments(item.getComments());
//        mergePatchOrderItem.setExternalId(item.getExternalId());
//        mergePatchOrderItem.setSupplierProductId(item.getSupplierProductId());
    }

    @Override
    public String when(BffSalesOrderServiceCommands.RecalculateFulfillmentStatus c) {
        return "";
    }

    @Override
    public String when(BffSalesOrderServiceCommands.CreateSalesOrderItem c) {
        return "";
    }

    @Override
    public void when(BffSalesOrderServiceCommands.DeleteSalesOrderItem c) {

    }

    @Override
    public void when(BffSalesOrderServiceCommands.UpdateSalesOrderItem c) {

    }


    private OrderHeaderState getAndValidateOrder(String orderId) {
        OrderHeaderState orderHeaderState = orderApplicationService.get(orderId);
        if (orderHeaderState == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return orderHeaderState;
    }
}
