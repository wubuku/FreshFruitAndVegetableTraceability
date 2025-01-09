package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffPurchaseOrderDto;
import org.dddml.ffvtraceability.domain.BffPurchaseOrderItemDto;
import org.dddml.ffvtraceability.domain.mapper.BffPurchaseOrderMapper;
import org.dddml.ffvtraceability.domain.order.AbstractOrderCommand;
import org.dddml.ffvtraceability.domain.order.OrderApplicationService;
import org.dddml.ffvtraceability.domain.order.OrderItemCommand;
import org.dddml.ffvtraceability.domain.order.OrderRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.repository.BffOrderRepository;
import org.dddml.ffvtraceability.domain.repository.BffPurchaseOrderAndItemProjection;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.dddml.ffvtraceability.domain.constants.BffOrderConstants.*;

@Service
public class BffPurchaseOrderApplicationServiceImpl implements BffPurchaseOrderApplicationService {
    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private BffOrderRepository bffOrderRepository;

    @Autowired
    private BffPurchaseOrderMapper bffPurchaseOrderMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffPurchaseOrderDto> when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItems c) {
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

        return order;
    }

    @Override
    public BffPurchaseOrderItemDto when(BffPurchaseOrderServiceCommands.GetPurchaseOrderItem c) {
        BffPurchaseOrderAndItemProjection projection =
                bffOrderRepository.findPurchaseOrderItem(c.getOrderId(), c.getOrderItemSeqId());
        if (projection == null) {
            return null;
        }
        return bffPurchaseOrderMapper.toBffPurchaseOrderItemDto(projection);
    }

    @Override
    public String when(BffPurchaseOrderServiceCommands.CreatePurchaseOrder c) {
        BffPurchaseOrderDto purchaseOrder = c.getPurchaseOrder();
        // Create order header
        AbstractOrderCommand.SimpleCreateOrder createOrder = new AbstractOrderCommand.SimpleCreateOrder();
        createOrder.setOrderId(purchaseOrder.getOrderId() != null ? purchaseOrder.getOrderId() : IdUtils.randomId());
        createOrder.setOrderTypeId(PURCHASE_ORDER);

        // Set additional order header fields
        createOrder.setOrderName(purchaseOrder.getOrderName());
        createOrder.setExternalId(purchaseOrder.getExternalId());
        createOrder.setOrderDate(OffsetDateTime.now()); //todo Is this correct?
        createOrder.setEntryDate(OffsetDateTime.now());
        //createOrder.setStatusId(purchaseOrder.getStatusId());
        createOrder.setCurrencyUomId(purchaseOrder.getCurrencyUomId());
        createOrder.setOriginFacilityId(purchaseOrder.getOriginFacilityId());
        createOrder.setMemo(purchaseOrder.getMemo());

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
                // Set required fields
                createOrderItem.setOrderItemTypeId(PRODUCT_ORDER_ITEM);
                createOrderItem.setOrderItemSeqId(itemSeq++ + "");
                createOrderItem.setProductId(item.getProductId());
                // Set quantities and prices
                createOrderItem.setQuantity(item.getQuantity());
                createOrderItem.setUnitPrice(item.getUnitPrice());
                // Set dates
                createOrderItem.setEstimatedShipDate(item.getEstimatedShipDate());
                createOrderItem.setEstimatedDeliveryDate(item.getEstimatedDeliveryDate());
                // Set additional item fields
                createOrderItem.setItemDescription(item.getItemDescription());
                createOrderItem.setComments(item.getComments());
                createOrderItem.setExternalId(item.getExternalId());
                createOrderItem.setStatusId(item.getStatusId());
                createOrderItem.setSupplierProductId(item.getSupplierProductId());
                // Add item to order
                createOrder.getCreateOrderItemCommands().add(createOrderItem);
            }
        }

        orderApplicationService.when(createOrder);
        return createOrder.getOrderId();
    }

    @Override
    public String when(BffPurchaseOrderServiceCommands.CreatePurchaseOrderItem c) {
        return "";
    }

    @Override
    public void when(BffPurchaseOrderServiceCommands.DeletePurchaseOrderItem c) {

    }

    @Override
    public void when(BffPurchaseOrderServiceCommands.UpdatePurchaseOrderItem c) {

    }
}
