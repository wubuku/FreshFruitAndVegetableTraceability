// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.supplierproduct;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchSupplierProductDto extends AbstractSupplierProductCommandDto implements SupplierProductCommand.CreateOrMergePatchSupplierProduct {

    /**
     * Available Thru Date
     */
    private OffsetDateTime availableThruDate;

    public OffsetDateTime getAvailableThruDate()
    {
        return this.availableThruDate;
    }

    public void setAvailableThruDate(OffsetDateTime availableThruDate)
    {
        this.availableThruDate = availableThruDate;
    }

    /**
     * Supplier Pref Order Id
     */
    private String supplierPrefOrderId;

    public String getSupplierPrefOrderId()
    {
        return this.supplierPrefOrderId;
    }

    public void setSupplierPrefOrderId(String supplierPrefOrderId)
    {
        this.supplierPrefOrderId = supplierPrefOrderId;
    }

    /**
     * Supplier Rating Type Id
     */
    private String supplierRatingTypeId;

    public String getSupplierRatingTypeId()
    {
        return this.supplierRatingTypeId;
    }

    public void setSupplierRatingTypeId(String supplierRatingTypeId)
    {
        this.supplierRatingTypeId = supplierRatingTypeId;
    }

    /**
     * Standard Lead Time Days
     */
    private java.math.BigDecimal standardLeadTimeDays;

    public java.math.BigDecimal getStandardLeadTimeDays()
    {
        return this.standardLeadTimeDays;
    }

    public void setStandardLeadTimeDays(java.math.BigDecimal standardLeadTimeDays)
    {
        this.standardLeadTimeDays = standardLeadTimeDays;
    }

    /**
     * Order Qty Increments
     */
    private java.math.BigDecimal orderQtyIncrements;

    public java.math.BigDecimal getOrderQtyIncrements()
    {
        return this.orderQtyIncrements;
    }

    public void setOrderQtyIncrements(java.math.BigDecimal orderQtyIncrements)
    {
        this.orderQtyIncrements = orderQtyIncrements;
    }

    /**
     * Units Included
     */
    private java.math.BigDecimal unitsIncluded;

    public java.math.BigDecimal getUnitsIncluded()
    {
        return this.unitsIncluded;
    }

    public void setUnitsIncluded(java.math.BigDecimal unitsIncluded)
    {
        this.unitsIncluded = unitsIncluded;
    }

    /**
     * Quantity Uom Id
     */
    private String quantityUomId;

    public String getQuantityUomId()
    {
        return this.quantityUomId;
    }

    public void setQuantityUomId(String quantityUomId)
    {
        this.quantityUomId = quantityUomId;
    }

    /**
     * Agreement Id
     */
    private String agreementId;

    public String getAgreementId()
    {
        return this.agreementId;
    }

    public void setAgreementId(String agreementId)
    {
        this.agreementId = agreementId;
    }

    /**
     * Agreement Item Seq Id
     */
    private String agreementItemSeqId;

    public String getAgreementItemSeqId()
    {
        return this.agreementItemSeqId;
    }

    public void setAgreementItemSeqId(String agreementItemSeqId)
    {
        this.agreementItemSeqId = agreementItemSeqId;
    }

    /**
     * Last Price
     */
    private java.math.BigDecimal lastPrice;

    public java.math.BigDecimal getLastPrice()
    {
        return this.lastPrice;
    }

    public void setLastPrice(java.math.BigDecimal lastPrice)
    {
        this.lastPrice = lastPrice;
    }

    /**
     * Shipping Price
     */
    private java.math.BigDecimal shippingPrice;

    public java.math.BigDecimal getShippingPrice()
    {
        return this.shippingPrice;
    }

    public void setShippingPrice(java.math.BigDecimal shippingPrice)
    {
        this.shippingPrice = shippingPrice;
    }

    /**
     * Supplier Product Id
     */
    private String supplierProductId;

    public String getSupplierProductId()
    {
        return this.supplierProductId;
    }

    public void setSupplierProductId(String supplierProductId)
    {
        this.supplierProductId = supplierProductId;
    }

    /**
     * Supplier Product Name
     */
    private String supplierProductName;

    public String getSupplierProductName()
    {
        return this.supplierProductName;
    }

    public void setSupplierProductName(String supplierProductName)
    {
        this.supplierProductName = supplierProductName;
    }

    /**
     * Can Drop Ship
     */
    private String canDropShip;

    public String getCanDropShip()
    {
        return this.canDropShip;
    }

    public void setCanDropShip(String canDropShip)
    {
        this.canDropShip = canDropShip;
    }

    /**
     * Comments
     */
    private String comments;

    public String getComments()
    {
        return this.comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    /**
     * If Y the price field has tax included for the given taxAuthPartyId/taxAuthGeoId at the taxPercentage.
     */
    private String taxInPrice;

    public String getTaxInPrice()
    {
        return this.taxInPrice;
    }

    public void setTaxInPrice(String taxInPrice)
    {
        this.taxInPrice = taxInPrice;
    }

    /**
     * Tax Amount
     */
    private java.math.BigDecimal taxAmount;

    public java.math.BigDecimal getTaxAmount()
    {
        return this.taxAmount;
    }

    public void setTaxAmount(java.math.BigDecimal taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    /**
     * Tax Percentage
     */
    private java.math.BigDecimal taxPercentage;

    public java.math.BigDecimal getTaxPercentage()
    {
        return this.taxPercentage;
    }

    public void setTaxPercentage(java.math.BigDecimal taxPercentage)
    {
        this.taxPercentage = taxPercentage;
    }

    /**
     * Limit Quantity Per Customer
     */
    private java.math.BigDecimal limitQuantityPerCustomer;

    public java.math.BigDecimal getLimitQuantityPerCustomer()
    {
        return this.limitQuantityPerCustomer;
    }

    public void setLimitQuantityPerCustomer(java.math.BigDecimal limitQuantityPerCustomer)
    {
        this.limitQuantityPerCustomer = limitQuantityPerCustomer;
    }

    /**
     * Limit Quantity Per Order
     */
    private java.math.BigDecimal limitQuantityPerOrder;

    public java.math.BigDecimal getLimitQuantityPerOrder()
    {
        return this.limitQuantityPerOrder;
    }

    public void setLimitQuantityPerOrder(java.math.BigDecimal limitQuantityPerOrder)
    {
        this.limitQuantityPerOrder = limitQuantityPerOrder;
    }

    /**
     * Product Price Type Id
     */
    private String productPriceTypeId;

    public String getProductPriceTypeId()
    {
        return this.productPriceTypeId;
    }

    public void setProductPriceTypeId(String productPriceTypeId)
    {
        this.productPriceTypeId = productPriceTypeId;
    }

    /**
     * Shipment Method Type Id
     */
    private String shipmentMethodTypeId;

    public String getShipmentMethodTypeId()
    {
        return this.shipmentMethodTypeId;
    }

    public void setShipmentMethodTypeId(String shipmentMethodTypeId)
    {
        this.shipmentMethodTypeId = shipmentMethodTypeId;
    }


    private Boolean isPropertyAvailableThruDateRemoved;

    public Boolean getIsPropertyAvailableThruDateRemoved()
    {
        return this.isPropertyAvailableThruDateRemoved;
    }

    public void setIsPropertyAvailableThruDateRemoved(Boolean removed)
    {
        this.isPropertyAvailableThruDateRemoved = removed;
    }

    private Boolean isPropertySupplierPrefOrderIdRemoved;

    public Boolean getIsPropertySupplierPrefOrderIdRemoved()
    {
        return this.isPropertySupplierPrefOrderIdRemoved;
    }

    public void setIsPropertySupplierPrefOrderIdRemoved(Boolean removed)
    {
        this.isPropertySupplierPrefOrderIdRemoved = removed;
    }

    private Boolean isPropertySupplierRatingTypeIdRemoved;

    public Boolean getIsPropertySupplierRatingTypeIdRemoved()
    {
        return this.isPropertySupplierRatingTypeIdRemoved;
    }

    public void setIsPropertySupplierRatingTypeIdRemoved(Boolean removed)
    {
        this.isPropertySupplierRatingTypeIdRemoved = removed;
    }

    private Boolean isPropertyStandardLeadTimeDaysRemoved;

    public Boolean getIsPropertyStandardLeadTimeDaysRemoved()
    {
        return this.isPropertyStandardLeadTimeDaysRemoved;
    }

    public void setIsPropertyStandardLeadTimeDaysRemoved(Boolean removed)
    {
        this.isPropertyStandardLeadTimeDaysRemoved = removed;
    }

    private Boolean isPropertyOrderQtyIncrementsRemoved;

    public Boolean getIsPropertyOrderQtyIncrementsRemoved()
    {
        return this.isPropertyOrderQtyIncrementsRemoved;
    }

    public void setIsPropertyOrderQtyIncrementsRemoved(Boolean removed)
    {
        this.isPropertyOrderQtyIncrementsRemoved = removed;
    }

    private Boolean isPropertyUnitsIncludedRemoved;

    public Boolean getIsPropertyUnitsIncludedRemoved()
    {
        return this.isPropertyUnitsIncludedRemoved;
    }

    public void setIsPropertyUnitsIncludedRemoved(Boolean removed)
    {
        this.isPropertyUnitsIncludedRemoved = removed;
    }

    private Boolean isPropertyQuantityUomIdRemoved;

    public Boolean getIsPropertyQuantityUomIdRemoved()
    {
        return this.isPropertyQuantityUomIdRemoved;
    }

    public void setIsPropertyQuantityUomIdRemoved(Boolean removed)
    {
        this.isPropertyQuantityUomIdRemoved = removed;
    }

    private Boolean isPropertyAgreementIdRemoved;

    public Boolean getIsPropertyAgreementIdRemoved()
    {
        return this.isPropertyAgreementIdRemoved;
    }

    public void setIsPropertyAgreementIdRemoved(Boolean removed)
    {
        this.isPropertyAgreementIdRemoved = removed;
    }

    private Boolean isPropertyAgreementItemSeqIdRemoved;

    public Boolean getIsPropertyAgreementItemSeqIdRemoved()
    {
        return this.isPropertyAgreementItemSeqIdRemoved;
    }

    public void setIsPropertyAgreementItemSeqIdRemoved(Boolean removed)
    {
        this.isPropertyAgreementItemSeqIdRemoved = removed;
    }

    private Boolean isPropertyLastPriceRemoved;

    public Boolean getIsPropertyLastPriceRemoved()
    {
        return this.isPropertyLastPriceRemoved;
    }

    public void setIsPropertyLastPriceRemoved(Boolean removed)
    {
        this.isPropertyLastPriceRemoved = removed;
    }

    private Boolean isPropertyShippingPriceRemoved;

    public Boolean getIsPropertyShippingPriceRemoved()
    {
        return this.isPropertyShippingPriceRemoved;
    }

    public void setIsPropertyShippingPriceRemoved(Boolean removed)
    {
        this.isPropertyShippingPriceRemoved = removed;
    }

    private Boolean isPropertySupplierProductIdRemoved;

    public Boolean getIsPropertySupplierProductIdRemoved()
    {
        return this.isPropertySupplierProductIdRemoved;
    }

    public void setIsPropertySupplierProductIdRemoved(Boolean removed)
    {
        this.isPropertySupplierProductIdRemoved = removed;
    }

    private Boolean isPropertySupplierProductNameRemoved;

    public Boolean getIsPropertySupplierProductNameRemoved()
    {
        return this.isPropertySupplierProductNameRemoved;
    }

    public void setIsPropertySupplierProductNameRemoved(Boolean removed)
    {
        this.isPropertySupplierProductNameRemoved = removed;
    }

    private Boolean isPropertyCanDropShipRemoved;

    public Boolean getIsPropertyCanDropShipRemoved()
    {
        return this.isPropertyCanDropShipRemoved;
    }

    public void setIsPropertyCanDropShipRemoved(Boolean removed)
    {
        this.isPropertyCanDropShipRemoved = removed;
    }

    private Boolean isPropertyCommentsRemoved;

    public Boolean getIsPropertyCommentsRemoved()
    {
        return this.isPropertyCommentsRemoved;
    }

    public void setIsPropertyCommentsRemoved(Boolean removed)
    {
        this.isPropertyCommentsRemoved = removed;
    }

    private Boolean isPropertyTaxInPriceRemoved;

    public Boolean getIsPropertyTaxInPriceRemoved()
    {
        return this.isPropertyTaxInPriceRemoved;
    }

    public void setIsPropertyTaxInPriceRemoved(Boolean removed)
    {
        this.isPropertyTaxInPriceRemoved = removed;
    }

    private Boolean isPropertyTaxAmountRemoved;

    public Boolean getIsPropertyTaxAmountRemoved()
    {
        return this.isPropertyTaxAmountRemoved;
    }

    public void setIsPropertyTaxAmountRemoved(Boolean removed)
    {
        this.isPropertyTaxAmountRemoved = removed;
    }

    private Boolean isPropertyTaxPercentageRemoved;

    public Boolean getIsPropertyTaxPercentageRemoved()
    {
        return this.isPropertyTaxPercentageRemoved;
    }

    public void setIsPropertyTaxPercentageRemoved(Boolean removed)
    {
        this.isPropertyTaxPercentageRemoved = removed;
    }

    private Boolean isPropertyLimitQuantityPerCustomerRemoved;

    public Boolean getIsPropertyLimitQuantityPerCustomerRemoved()
    {
        return this.isPropertyLimitQuantityPerCustomerRemoved;
    }

    public void setIsPropertyLimitQuantityPerCustomerRemoved(Boolean removed)
    {
        this.isPropertyLimitQuantityPerCustomerRemoved = removed;
    }

    private Boolean isPropertyLimitQuantityPerOrderRemoved;

    public Boolean getIsPropertyLimitQuantityPerOrderRemoved()
    {
        return this.isPropertyLimitQuantityPerOrderRemoved;
    }

    public void setIsPropertyLimitQuantityPerOrderRemoved(Boolean removed)
    {
        this.isPropertyLimitQuantityPerOrderRemoved = removed;
    }

    private Boolean isPropertyProductPriceTypeIdRemoved;

    public Boolean getIsPropertyProductPriceTypeIdRemoved()
    {
        return this.isPropertyProductPriceTypeIdRemoved;
    }

    public void setIsPropertyProductPriceTypeIdRemoved(Boolean removed)
    {
        this.isPropertyProductPriceTypeIdRemoved = removed;
    }

    private Boolean isPropertyShipmentMethodTypeIdRemoved;

    public Boolean getIsPropertyShipmentMethodTypeIdRemoved()
    {
        return this.isPropertyShipmentMethodTypeIdRemoved;
    }

    public void setIsPropertyShipmentMethodTypeIdRemoved(Boolean removed)
    {
        this.isPropertyShipmentMethodTypeIdRemoved = removed;
    }

    public void copyTo(CreateOrMergePatchSupplierProduct command)
    {
        ((AbstractSupplierProductCommandDto) this).copyTo(command);
        command.setAvailableThruDate(this.getAvailableThruDate());
        command.setSupplierPrefOrderId(this.getSupplierPrefOrderId());
        command.setSupplierRatingTypeId(this.getSupplierRatingTypeId());
        command.setStandardLeadTimeDays(this.getStandardLeadTimeDays());
        command.setOrderQtyIncrements(this.getOrderQtyIncrements());
        command.setUnitsIncluded(this.getUnitsIncluded());
        command.setQuantityUomId(this.getQuantityUomId());
        command.setAgreementId(this.getAgreementId());
        command.setAgreementItemSeqId(this.getAgreementItemSeqId());
        command.setLastPrice(this.getLastPrice());
        command.setShippingPrice(this.getShippingPrice());
        command.setSupplierProductId(this.getSupplierProductId());
        command.setSupplierProductName(this.getSupplierProductName());
        command.setCanDropShip(this.getCanDropShip());
        command.setComments(this.getComments());
        command.setTaxInPrice(this.getTaxInPrice());
        command.setTaxAmount(this.getTaxAmount());
        command.setTaxPercentage(this.getTaxPercentage());
        command.setLimitQuantityPerCustomer(this.getLimitQuantityPerCustomer());
        command.setLimitQuantityPerOrder(this.getLimitQuantityPerOrder());
        command.setProductPriceTypeId(this.getProductPriceTypeId());
        command.setShipmentMethodTypeId(this.getShipmentMethodTypeId());
    }

    public SupplierProductCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractSupplierProductCommand.SimpleCreateSupplierProduct command = new AbstractSupplierProductCommand.SimpleCreateSupplierProduct();
            copyTo((AbstractSupplierProductCommand.AbstractCreateSupplierProduct) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractSupplierProductCommand.SimpleMergePatchSupplierProduct command = new AbstractSupplierProductCommand.SimpleMergePatchSupplierProduct();
            copyTo((AbstractSupplierProductCommand.SimpleMergePatchSupplierProduct) command);
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public SupplierProductCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            CreateSupplierProductDto command = new CreateSupplierProductDto();
            copyTo((CreateSupplierProduct) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            MergePatchSupplierProductDto command = new MergePatchSupplierProductDto();
            copyTo((MergePatchSupplierProduct) command);
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    public void copyTo(CreateSupplierProduct command)
    {
        copyTo((CreateOrMergePatchSupplierProduct) command);
    }

    public void copyTo(MergePatchSupplierProduct command)
    {
        copyTo((CreateOrMergePatchSupplierProduct) command);
        command.setIsPropertyAvailableThruDateRemoved(this.getIsPropertyAvailableThruDateRemoved());
        command.setIsPropertySupplierPrefOrderIdRemoved(this.getIsPropertySupplierPrefOrderIdRemoved());
        command.setIsPropertySupplierRatingTypeIdRemoved(this.getIsPropertySupplierRatingTypeIdRemoved());
        command.setIsPropertyStandardLeadTimeDaysRemoved(this.getIsPropertyStandardLeadTimeDaysRemoved());
        command.setIsPropertyOrderQtyIncrementsRemoved(this.getIsPropertyOrderQtyIncrementsRemoved());
        command.setIsPropertyUnitsIncludedRemoved(this.getIsPropertyUnitsIncludedRemoved());
        command.setIsPropertyQuantityUomIdRemoved(this.getIsPropertyQuantityUomIdRemoved());
        command.setIsPropertyAgreementIdRemoved(this.getIsPropertyAgreementIdRemoved());
        command.setIsPropertyAgreementItemSeqIdRemoved(this.getIsPropertyAgreementItemSeqIdRemoved());
        command.setIsPropertyLastPriceRemoved(this.getIsPropertyLastPriceRemoved());
        command.setIsPropertyShippingPriceRemoved(this.getIsPropertyShippingPriceRemoved());
        command.setIsPropertySupplierProductIdRemoved(this.getIsPropertySupplierProductIdRemoved());
        command.setIsPropertySupplierProductNameRemoved(this.getIsPropertySupplierProductNameRemoved());
        command.setIsPropertyCanDropShipRemoved(this.getIsPropertyCanDropShipRemoved());
        command.setIsPropertyCommentsRemoved(this.getIsPropertyCommentsRemoved());
        command.setIsPropertyTaxInPriceRemoved(this.getIsPropertyTaxInPriceRemoved());
        command.setIsPropertyTaxAmountRemoved(this.getIsPropertyTaxAmountRemoved());
        command.setIsPropertyTaxPercentageRemoved(this.getIsPropertyTaxPercentageRemoved());
        command.setIsPropertyLimitQuantityPerCustomerRemoved(this.getIsPropertyLimitQuantityPerCustomerRemoved());
        command.setIsPropertyLimitQuantityPerOrderRemoved(this.getIsPropertyLimitQuantityPerOrderRemoved());
        command.setIsPropertyProductPriceTypeIdRemoved(this.getIsPropertyProductPriceTypeIdRemoved());
        command.setIsPropertyShipmentMethodTypeIdRemoved(this.getIsPropertyShipmentMethodTypeIdRemoved());
    }

    public static class CreateSupplierProductDto extends CreateOrMergePatchSupplierProductDto implements SupplierProductCommand.CreateSupplierProduct
    {
        public CreateSupplierProductDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public SupplierProductCommand.CreateSupplierProduct toCreateSupplierProduct()
        {
            return (SupplierProductCommand.CreateSupplierProduct) toCommand();
        }

    }

    public static class MergePatchSupplierProductDto extends CreateOrMergePatchSupplierProductDto implements SupplierProductCommand.MergePatchSupplierProduct
    {
        public MergePatchSupplierProductDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public SupplierProductCommand.MergePatchSupplierProduct toMergePatchSupplierProduct()
        {
            return (SupplierProductCommand.MergePatchSupplierProduct) toCommand();
        }

    }

}

