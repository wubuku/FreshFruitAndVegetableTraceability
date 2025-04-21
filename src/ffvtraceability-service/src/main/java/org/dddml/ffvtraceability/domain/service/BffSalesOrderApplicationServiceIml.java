package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffSalesOrderDto;
import org.dddml.ffvtraceability.domain.BffSalesOrderItemDto;
import org.dddml.ffvtraceability.domain.SalesOrderItemVo;
import org.dddml.ffvtraceability.domain.SalesOrderVo;
import org.dddml.ffvtraceability.domain.documentnumbergenerator.DocumentNumberGeneratorApplicationService;
import org.dddml.ffvtraceability.domain.documentnumbergenerator.DocumentNumberGeneratorCommands;
import org.dddml.ffvtraceability.domain.order.AbstractOrderCommand;
import org.dddml.ffvtraceability.domain.order.OrderApplicationService;
import org.dddml.ffvtraceability.domain.order.OrderItemCommand;
import org.dddml.ffvtraceability.domain.order.OrderRoleCommand;
import org.dddml.ffvtraceability.domain.partyrole.PartyRoleId;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.dddml.ffvtraceability.domain.constants.BffOrderConstants.*;

@Service
public class BffSalesOrderApplicationServiceIml implements BffSalesOrderApplicationService {
    @Autowired
    private DocumentNumberGeneratorApplicationService documentNumberGeneratorApplicationService;
    @Autowired
    private OrderApplicationService orderApplicationService;

    @Override
    public Page<BffSalesOrderDto> when(BffSalesOrderServiceCommands.GetSalesOrders c) {
        return null;
    }

    @Override
    public BffSalesOrderDto when(BffSalesOrderServiceCommands.GetSalesOrder c) {
        return null;
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
        if(salesOrder.getOrderId()!=null&&!salesOrder.getOrderId().isBlank()){
            createOrder.setOrderId(salesOrder.getOrderId());
        }else{
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
            int itemSeq = 1;
            for (SalesOrderItemVo item : salesOrder.getOrderItems()) {
                OrderItemCommand.CreateOrderItem createOrderItem = createOrder.newCreateOrderItem();
                item.setOrderItemSeqId(itemSeq++ + "");
                setupCreateOrderItemCommand(createOrderItem, item);
                createOrder.getCreateOrderItemCommands().add(createOrderItem);
            }
        }

        orderApplicationService.when(createOrder);
        return createOrder.getOrderId();
    }


    private void setupCreateOrderItemCommand(OrderItemCommand.CreateOrderItem createOrderItem,
                                             SalesOrderItemVo item
    ) {
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
    public void when(BffSalesOrderServiceCommands.UpdateSalesOrder c) {

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
}
