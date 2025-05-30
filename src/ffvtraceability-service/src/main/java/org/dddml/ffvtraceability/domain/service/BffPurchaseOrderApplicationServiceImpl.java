package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderItemDto;
import org.dddml.ffvtraceability.domain.documentnumbergenerator.DocumentNumberGeneratorApplicationService;
import org.dddml.ffvtraceability.domain.documentnumbergenerator.DocumentNumberGeneratorCommands;
import org.dddml.ffvtraceability.domain.mapper.BffPurchaseOrderMapper;
import org.dddml.ffvtraceability.domain.order.*;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderAndItemProjection;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderRepository;
import org.dddml.ffvtraceability.domain.repository.BffRawItemRepository;
import org.dddml.ffvtraceability.domain.repository.BffReceivingRepository;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffOrderConstants.*;

@Service
@Transactional
public class BffPurchaseOrderApplicationServiceImpl implements BffPurchaseOrderApplicationService {

    @Autowired
    private DocumentNumberGeneratorApplicationService documentNumberGeneratorApplicationService;
    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private BffPurchaseOrderRepository bffPurchaseOrderRepository;
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
    @Autowired
    private BffRawItemApplicationService rawItemApplicationService;
    @Autowired
    private ProductQueryService rawItemQueryService;
    @Autowired
    private BffRawItemRepository bffRawItemRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BffPurchaseOrderDto> when(BffPurchaseOrderServiceCommands.GetPurchaseOrders c) {
        int offset = c.getPage() * c.getSize();
        long totalElements = bffPurchaseOrderRepository.countTotalShipments(
                c.getOrderIdOrItem(),
                c.getSupplierId(),
                c.getOrderDateFrom(),
                c.getOrderDateTo()
        );

        List<BffPurchaseOrderAndItemProjection> projections =
                bffPurchaseOrderRepository.findAllPurchaseOrdersWithItems(
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
                .sorted(Comparator.comparing(BffPurchaseOrderDto::getCreatedAt).reversed()) // 显式按createdAt倒序
                .collect(Collectors.toList());

        if (c.getIncludesProductDetails() != null && c.getIncludesProductDetails()) {
            purchaseOrders.forEach(po -> {
                if (po.getOrderItems() != null) {
                    po.getOrderItems().forEach(item -> {
                        item.setProduct(rawItemQueryService.getRawItem(item.getProductId()));
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
    @Transactional(readOnly = true)
    public BffPurchaseOrderDto when(BffPurchaseOrderServiceCommands.GetPurchaseOrder c) {
        List<BffPurchaseOrderAndItemProjection> projections =
                bffPurchaseOrderRepository.findPurchaseOrderWithItems(c.getOrderId());

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
        if (c.getIncludesProductDetails() != null && c.getIncludesProductDetails()) {
            order.getOrderItems().forEach(orderItem -> {
                orderItem.setProduct(rawItemQueryService.getRawItem(orderItem.getProductId()));
            });
        }
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public BffPurchaseOrderItemDto when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItem c) {
        BffPurchaseOrderAndItemProjection projection =
                bffPurchaseOrderRepository.findPurchaseOrderItem(c.getOrderId(), c.getOrderItemSeqId());
        if (projection == null) {
            return null;
        }
        BffPurchaseOrderItemDto orderItem = bffPurchaseOrderMapper.toBffPurchaseOrderItemDto(projection);
        if (orderItem != null && c.getIncludesFulfillments() != null && c.getIncludesFulfillments()) {
            orderItem.setFulfillments(
                    bffReceivingRepository.findPurchaseOrderItemFulfillments(
                                    c.getOrderId(), c.getOrderItemSeqId()
                            ).stream()
                            .map(bffPurchaseOrderMapper::toBffPurchaseOrderFulfillmentDto)
                            .collect(Collectors.toList())
            );
        }
        if (orderItem != null && c.getIncludesProductDetails() != null && c.getIncludesProductDetails()) {
            orderItem.setProduct(rawItemQueryService.getRawItem(orderItem.getProductId()));
        }
        return orderItem;
    }

    @Override
    public BigDecimal when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItemOutstandingQuantity c) {
        return bffPurchaseOrderRepository.findPurchaseOrderItemOutstandingQuantity(c.getOrderId(), c.getOrderItemSeqId())
                .orElse(null);
    }

    @Override
    public BigDecimal when(BffPurchaseOrderServiceCommands.GetPurchaseOrderProductOutstandingQuantityByProductId c) {
        return bffPurchaseOrderRepository.findPurchaseOrderItemOutstandingQuantityByProductId(c.getOrderId(), c.getProductId())
                .orElse(null);
    }

    @Override
    @Transactional
    public String when(BffPurchaseOrderServiceCommands.CreatePurchaseOrder c) {
        BffPurchaseOrderDto purchaseOrder = c.getPurchaseOrder();
        if (purchaseOrder == null) {
            throw new IllegalArgumentException("Purchase order can't be null");
        }
        String orderId = purchaseOrder.getOrderId();
        if (orderId != null) {
            orderId = orderId.trim();
            purchaseOrder.setOrderId(orderId);
            if (orderApplicationService.get(orderId) != null) {
                throw new IllegalArgumentException("The purchase order already exists:" + orderId);
            }
        }
        if (purchaseOrder.getSupplierId() == null || purchaseOrder.getSupplierId().isBlank()) {
            throw new IllegalArgumentException("Vendor is required.");
        }
        // Create order header
        AbstractOrderCommand.SimpleCreateOrder createOrder = new AbstractOrderCommand.SimpleCreateOrder();
        if (purchaseOrder.getOrderId() != null && !purchaseOrder.getOrderId().isBlank()) {
            createOrder.setOrderId(purchaseOrder.getOrderId());
        } else {
            DocumentNumberGeneratorCommands.GenerateNextNumber generateNextNumber = new DocumentNumberGeneratorCommands.GenerateNextNumber();
            generateNextNumber.setGeneratorId(PURCHASE_ORDER);
            generateNextNumber.setRequesterId(c.getRequesterId());
            createOrder.setOrderId(documentNumberGeneratorApplicationService.when(generateNextNumber));
        }
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
                if (bffRawItemRepository.findSupplierRawItem(item.getProductId(), purchaseOrder.getSupplierId()) < 1) {
                    throw new IllegalArgumentException("Product:" + item.getProductId() + " and supplier:" + purchaseOrder.getSupplierId() + " mismatch");
                }
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

        //新增的行项目的itemSeqId不能与当前订单中已经存在的重复
        if (c.getPurchaseOrderItem().getOrderItemSeqId() != null
                && !c.getPurchaseOrderItem().getOrderItemSeqId().isEmpty()) {
            orderHeaderState.getOrderItems().forEach(orderItemState -> {
                if (c.getPurchaseOrderItem().getOrderItemSeqId().equals(orderItemState.getOrderItemSeqId())) {
                    throw new IllegalArgumentException(String.format("The Order Item Seq Id:%s already exists.",
                            c.getPurchaseOrderItem().getOrderItemSeqId()));
                }
            });
        }

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

        mergePatchOrderItem.setOrderItemSeqId(c.getOrderItemSeqId());
        mergePatchOrderItem.setQuantity(c.getQuantity());
        mergePatchOrderItem.setUnitPrice(c.getUnitPrice());
        mergePatchOrderItem.setEstimatedShipDate(c.getEstimatedShipDate());
        mergePatchOrderItem.setEstimatedDeliveryDate(c.getEstimatedDeliveryDate());
        mergePatchOrderItem.setItemDescription(c.getItemDescription());
        mergePatchOrderItem.setComments(c.getComments());
        //mergePatchOrderItem.setExternalId(c.getExternalId());
        mergePatchOrderItem.setSupplierProductId(c.getSupplierProductId());

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
            throw new IllegalArgumentException("Order Id is required.");
        }
        BffPurchaseOrderDto purchaseOrder = c.getPurchaseOrder();
        if (purchaseOrder.getSupplierId() == null || purchaseOrder.getSupplierId().isBlank()) {
            throw new IllegalArgumentException("Vendor is required.");
        }
        OrderHeaderState orderHeaderState = getAndValidateOrder(c.getOrderId());
        if (orderHeaderState.getOrderId() == null) {
            throw new IllegalArgumentException("Order not found: " + c.getOrderId());
        }
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

        if (orderHeaderState.getOrderRoles() != null) {
            orderHeaderState.getOrderRoles().stream().findFirst().ifPresent(orderRoleState -> {
                if (!orderRoleState.getPartyRoleId().getPartyId().equals(purchaseOrder.getSupplierId())) {
                    OrderRoleCommand.RemoveOrderRole removeOrderRole = mergePatchOrder.newRemoveOrderRole();
                    removeOrderRole.setPartyRoleId(orderRoleState.getPartyRoleId());
                    removeOrderRole.setCommandId(IdUtils.randomId());
                    removeOrderRole.setRequesterId(c.getRequesterId());
                    mergePatchOrder.getOrderRoleCommands().add(removeOrderRole);
                    OrderRoleCommand.CreateOrderRole mergePatchOrderRole = mergePatchOrder.newCreateOrderRole();
                    mergePatchOrderRole.setPartyRoleId(new PartyRoleId(purchaseOrder.getSupplierId(), ROLE_TYPE_SUPPLIER));
                    mergePatchOrderRole.setRequesterId(c.getRequesterId());
                    mergePatchOrderRole.setCommandId(IdUtils.randomId());
                    mergePatchOrder.getOrderRoleCommands().add(mergePatchOrderRole);
                }
            });
        } else {
            OrderRoleCommand.CreateOrderRole mergePatchOrderRole = mergePatchOrder.newCreateOrderRole();
            mergePatchOrderRole.setPartyRoleId(new PartyRoleId(purchaseOrder.getSupplierId(), ROLE_TYPE_SUPPLIER));
            mergePatchOrderRole.setRequesterId(c.getRequesterId());
            mergePatchOrderRole.setCommandId(IdUtils.randomId());
            mergePatchOrder.getOrderRoleCommands().add(mergePatchOrderRole);
        }
        List<String> orderItemSeqIds = new ArrayList<>();
        if (purchaseOrder.getOrderItems() != null) {
            for (BffPurchaseOrderItemDto item : purchaseOrder.getOrderItems()) {
                if (bffRawItemRepository.findSupplierRawItem(item.getProductId(), purchaseOrder.getSupplierId()) < 1) {
                    throw new IllegalArgumentException("Product:" + item.getProductId() + " and supplier:" + purchaseOrder.getSupplierId() + " mismatch");
                }
                if (item.getOrderItemSeqId() != null && !item.getOrderItemSeqId().isBlank()) {
                    orderItemSeqIds.add(item.getOrderItemSeqId());
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
        //原来存在的行项目，在这次前端传过来的行项中不存在
        orderHeaderState.getOrderItems().forEach(orderItemState -> {
            if (!orderItemSeqIds.contains(orderItemState.getOrderItemSeqId())) {
                OrderItemCommand.RemoveOrderItem removeOrderItem = mergePatchOrder.newRemoveOrderItem();
                removeOrderItem.setOrderItemSeqId(orderItemState.getOrderItemSeqId());
                removeOrderItem.setRequesterId(c.getRequesterId());
                mergePatchOrder.getOrderItemCommands().add(removeOrderItem);
            }
        });

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
        mergePatchOrderItem.setProductId(item.getProductId());
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
