// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchInventoryItemDto extends AbstractInventoryItemCommandDto implements InventoryItemCommand.CreateOrMergePatchInventoryItem {

    /**
     * Inventory Item Type Id
     */
    private String inventoryItemTypeId;

    public String getInventoryItemTypeId()
    {
        return this.inventoryItemTypeId;
    }

    public void setInventoryItemTypeId(String inventoryItemTypeId)
    {
        this.inventoryItemTypeId = inventoryItemTypeId;
    }

    /**
     * Product Id
     */
    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * Party Id
     */
    private String partyId;

    public String getPartyId()
    {
        return this.partyId;
    }

    public void setPartyId(String partyId)
    {
        this.partyId = partyId;
    }

    /**
     * The owner of the inventory item.
     */
    private String ownerPartyId;

    public String getOwnerPartyId()
    {
        return this.ownerPartyId;
    }

    public void setOwnerPartyId(String ownerPartyId)
    {
        this.ownerPartyId = ownerPartyId;
    }

    /**
     * Status Id
     */
    private String statusId;

    public String getStatusId()
    {
        return this.statusId;
    }

    public void setStatusId(String statusId)
    {
        this.statusId = statusId;
    }

    /**
     * Datetime Received
     */
    private OffsetDateTime datetimeReceived;

    public OffsetDateTime getDatetimeReceived()
    {
        return this.datetimeReceived;
    }

    public void setDatetimeReceived(OffsetDateTime datetimeReceived)
    {
        this.datetimeReceived = datetimeReceived;
    }

    /**
     * Datetime Manufactured
     */
    private OffsetDateTime datetimeManufactured;

    public OffsetDateTime getDatetimeManufactured()
    {
        return this.datetimeManufactured;
    }

    public void setDatetimeManufactured(OffsetDateTime datetimeManufactured)
    {
        this.datetimeManufactured = datetimeManufactured;
    }

    /**
     * Expire Date
     */
    private OffsetDateTime expireDate;

    public OffsetDateTime getExpireDate()
    {
        return this.expireDate;
    }

    public void setExpireDate(OffsetDateTime expireDate)
    {
        this.expireDate = expireDate;
    }

    /**
     * Facility Id
     */
    private String facilityId;

    public String getFacilityId()
    {
        return this.facilityId;
    }

    public void setFacilityId(String facilityId)
    {
        this.facilityId = facilityId;
    }

    /**
     * Container Id
     */
    private String containerId;

    public String getContainerId()
    {
        return this.containerId;
    }

    public void setContainerId(String containerId)
    {
        this.containerId = containerId;
    }

    /**
     * Lot Id
     */
    private String lotId;

    public String getLotId()
    {
        return this.lotId;
    }

    public void setLotId(String lotId)
    {
        this.lotId = lotId;
    }

    /**
     * Uom Id
     */
    private String uomId;

    public String getUomId()
    {
        return this.uomId;
    }

    public void setUomId(String uomId)
    {
        this.uomId = uomId;
    }

    /**
     * Bin Number
     */
    private String binNumber;

    public String getBinNumber()
    {
        return this.binNumber;
    }

    public void setBinNumber(String binNumber)
    {
        this.binNumber = binNumber;
    }

    /**
     * Location Seq Id
     */
    private String locationSeqId;

    public String getLocationSeqId()
    {
        return this.locationSeqId;
    }

    public void setLocationSeqId(String locationSeqId)
    {
        this.locationSeqId = locationSeqId;
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
     * Quantity On Hand Total
     */
    private java.math.BigDecimal quantityOnHandTotal;

    public java.math.BigDecimal getQuantityOnHandTotal()
    {
        return this.quantityOnHandTotal;
    }

    public void setQuantityOnHandTotal(java.math.BigDecimal quantityOnHandTotal)
    {
        this.quantityOnHandTotal = quantityOnHandTotal;
    }

    /**
     * Available To Promise Total
     */
    private java.math.BigDecimal availableToPromiseTotal;

    public java.math.BigDecimal getAvailableToPromiseTotal()
    {
        return this.availableToPromiseTotal;
    }

    public void setAvailableToPromiseTotal(java.math.BigDecimal availableToPromiseTotal)
    {
        this.availableToPromiseTotal = availableToPromiseTotal;
    }

    /**
     * Accounting Quantity Total
     */
    private java.math.BigDecimal accountingQuantityTotal;

    public java.math.BigDecimal getAccountingQuantityTotal()
    {
        return this.accountingQuantityTotal;
    }

    public void setAccountingQuantityTotal(java.math.BigDecimal accountingQuantityTotal)
    {
        this.accountingQuantityTotal = accountingQuantityTotal;
    }

    /**
     * Serial Number
     */
    private String serialNumber;

    public String getSerialNumber()
    {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    /**
     * Soft Identifier
     */
    private String softIdentifier;

    public String getSoftIdentifier()
    {
        return this.softIdentifier;
    }

    public void setSoftIdentifier(String softIdentifier)
    {
        this.softIdentifier = softIdentifier;
    }

    /**
     * Activation Number
     */
    private String activationNumber;

    public String getActivationNumber()
    {
        return this.activationNumber;
    }

    public void setActivationNumber(String activationNumber)
    {
        this.activationNumber = activationNumber;
    }

    /**
     * Activation Valid Thru
     */
    private OffsetDateTime activationValidThru;

    public OffsetDateTime getActivationValidThru()
    {
        return this.activationValidThru;
    }

    public void setActivationValidThru(OffsetDateTime activationValidThru)
    {
        this.activationValidThru = activationValidThru;
    }

    /**
     * Higher precision in case it is a calculated number
     */
    private java.math.BigDecimal unitCost;

    public java.math.BigDecimal getUnitCost()
    {
        return this.unitCost;
    }

    public void setUnitCost(java.math.BigDecimal unitCost)
    {
        this.unitCost = unitCost;
    }

    /**
     * The currency Uom of the unit cost.
     */
    private String currencyUomId;

    public String getCurrencyUomId()
    {
        return this.currencyUomId;
    }

    public void setCurrencyUomId(String currencyUomId)
    {
        this.currencyUomId = currencyUomId;
    }

    /**
     * Fixed Asset Id
     */
    private String fixedAssetId;

    public String getFixedAssetId()
    {
        return this.fixedAssetId;
    }

    public void setFixedAssetId(String fixedAssetId)
    {
        this.fixedAssetId = fixedAssetId;
    }

    /**
     * Inventory Item Attribute Hash
     */
    private String inventoryItemAttributeHash;

    public String getInventoryItemAttributeHash()
    {
        return this.inventoryItemAttributeHash;
    }

    public void setInventoryItemAttributeHash(String inventoryItemAttributeHash)
    {
        this.inventoryItemAttributeHash = inventoryItemAttributeHash;
    }


    private Boolean isPropertyInventoryItemTypeIdRemoved;

    public Boolean getIsPropertyInventoryItemTypeIdRemoved()
    {
        return this.isPropertyInventoryItemTypeIdRemoved;
    }

    public void setIsPropertyInventoryItemTypeIdRemoved(Boolean removed)
    {
        this.isPropertyInventoryItemTypeIdRemoved = removed;
    }

    private Boolean isPropertyProductIdRemoved;

    public Boolean getIsPropertyProductIdRemoved()
    {
        return this.isPropertyProductIdRemoved;
    }

    public void setIsPropertyProductIdRemoved(Boolean removed)
    {
        this.isPropertyProductIdRemoved = removed;
    }

    private Boolean isPropertyPartyIdRemoved;

    public Boolean getIsPropertyPartyIdRemoved()
    {
        return this.isPropertyPartyIdRemoved;
    }

    public void setIsPropertyPartyIdRemoved(Boolean removed)
    {
        this.isPropertyPartyIdRemoved = removed;
    }

    private Boolean isPropertyOwnerPartyIdRemoved;

    public Boolean getIsPropertyOwnerPartyIdRemoved()
    {
        return this.isPropertyOwnerPartyIdRemoved;
    }

    public void setIsPropertyOwnerPartyIdRemoved(Boolean removed)
    {
        this.isPropertyOwnerPartyIdRemoved = removed;
    }

    private Boolean isPropertyStatusIdRemoved;

    public Boolean getIsPropertyStatusIdRemoved()
    {
        return this.isPropertyStatusIdRemoved;
    }

    public void setIsPropertyStatusIdRemoved(Boolean removed)
    {
        this.isPropertyStatusIdRemoved = removed;
    }

    private Boolean isPropertyDatetimeReceivedRemoved;

    public Boolean getIsPropertyDatetimeReceivedRemoved()
    {
        return this.isPropertyDatetimeReceivedRemoved;
    }

    public void setIsPropertyDatetimeReceivedRemoved(Boolean removed)
    {
        this.isPropertyDatetimeReceivedRemoved = removed;
    }

    private Boolean isPropertyDatetimeManufacturedRemoved;

    public Boolean getIsPropertyDatetimeManufacturedRemoved()
    {
        return this.isPropertyDatetimeManufacturedRemoved;
    }

    public void setIsPropertyDatetimeManufacturedRemoved(Boolean removed)
    {
        this.isPropertyDatetimeManufacturedRemoved = removed;
    }

    private Boolean isPropertyExpireDateRemoved;

    public Boolean getIsPropertyExpireDateRemoved()
    {
        return this.isPropertyExpireDateRemoved;
    }

    public void setIsPropertyExpireDateRemoved(Boolean removed)
    {
        this.isPropertyExpireDateRemoved = removed;
    }

    private Boolean isPropertyFacilityIdRemoved;

    public Boolean getIsPropertyFacilityIdRemoved()
    {
        return this.isPropertyFacilityIdRemoved;
    }

    public void setIsPropertyFacilityIdRemoved(Boolean removed)
    {
        this.isPropertyFacilityIdRemoved = removed;
    }

    private Boolean isPropertyContainerIdRemoved;

    public Boolean getIsPropertyContainerIdRemoved()
    {
        return this.isPropertyContainerIdRemoved;
    }

    public void setIsPropertyContainerIdRemoved(Boolean removed)
    {
        this.isPropertyContainerIdRemoved = removed;
    }

    private Boolean isPropertyLotIdRemoved;

    public Boolean getIsPropertyLotIdRemoved()
    {
        return this.isPropertyLotIdRemoved;
    }

    public void setIsPropertyLotIdRemoved(Boolean removed)
    {
        this.isPropertyLotIdRemoved = removed;
    }

    private Boolean isPropertyUomIdRemoved;

    public Boolean getIsPropertyUomIdRemoved()
    {
        return this.isPropertyUomIdRemoved;
    }

    public void setIsPropertyUomIdRemoved(Boolean removed)
    {
        this.isPropertyUomIdRemoved = removed;
    }

    private Boolean isPropertyBinNumberRemoved;

    public Boolean getIsPropertyBinNumberRemoved()
    {
        return this.isPropertyBinNumberRemoved;
    }

    public void setIsPropertyBinNumberRemoved(Boolean removed)
    {
        this.isPropertyBinNumberRemoved = removed;
    }

    private Boolean isPropertyLocationSeqIdRemoved;

    public Boolean getIsPropertyLocationSeqIdRemoved()
    {
        return this.isPropertyLocationSeqIdRemoved;
    }

    public void setIsPropertyLocationSeqIdRemoved(Boolean removed)
    {
        this.isPropertyLocationSeqIdRemoved = removed;
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

    private Boolean isPropertyQuantityOnHandTotalRemoved;

    public Boolean getIsPropertyQuantityOnHandTotalRemoved()
    {
        return this.isPropertyQuantityOnHandTotalRemoved;
    }

    public void setIsPropertyQuantityOnHandTotalRemoved(Boolean removed)
    {
        this.isPropertyQuantityOnHandTotalRemoved = removed;
    }

    private Boolean isPropertyAvailableToPromiseTotalRemoved;

    public Boolean getIsPropertyAvailableToPromiseTotalRemoved()
    {
        return this.isPropertyAvailableToPromiseTotalRemoved;
    }

    public void setIsPropertyAvailableToPromiseTotalRemoved(Boolean removed)
    {
        this.isPropertyAvailableToPromiseTotalRemoved = removed;
    }

    private Boolean isPropertyAccountingQuantityTotalRemoved;

    public Boolean getIsPropertyAccountingQuantityTotalRemoved()
    {
        return this.isPropertyAccountingQuantityTotalRemoved;
    }

    public void setIsPropertyAccountingQuantityTotalRemoved(Boolean removed)
    {
        this.isPropertyAccountingQuantityTotalRemoved = removed;
    }

    private Boolean isPropertySerialNumberRemoved;

    public Boolean getIsPropertySerialNumberRemoved()
    {
        return this.isPropertySerialNumberRemoved;
    }

    public void setIsPropertySerialNumberRemoved(Boolean removed)
    {
        this.isPropertySerialNumberRemoved = removed;
    }

    private Boolean isPropertySoftIdentifierRemoved;

    public Boolean getIsPropertySoftIdentifierRemoved()
    {
        return this.isPropertySoftIdentifierRemoved;
    }

    public void setIsPropertySoftIdentifierRemoved(Boolean removed)
    {
        this.isPropertySoftIdentifierRemoved = removed;
    }

    private Boolean isPropertyActivationNumberRemoved;

    public Boolean getIsPropertyActivationNumberRemoved()
    {
        return this.isPropertyActivationNumberRemoved;
    }

    public void setIsPropertyActivationNumberRemoved(Boolean removed)
    {
        this.isPropertyActivationNumberRemoved = removed;
    }

    private Boolean isPropertyActivationValidThruRemoved;

    public Boolean getIsPropertyActivationValidThruRemoved()
    {
        return this.isPropertyActivationValidThruRemoved;
    }

    public void setIsPropertyActivationValidThruRemoved(Boolean removed)
    {
        this.isPropertyActivationValidThruRemoved = removed;
    }

    private Boolean isPropertyUnitCostRemoved;

    public Boolean getIsPropertyUnitCostRemoved()
    {
        return this.isPropertyUnitCostRemoved;
    }

    public void setIsPropertyUnitCostRemoved(Boolean removed)
    {
        this.isPropertyUnitCostRemoved = removed;
    }

    private Boolean isPropertyCurrencyUomIdRemoved;

    public Boolean getIsPropertyCurrencyUomIdRemoved()
    {
        return this.isPropertyCurrencyUomIdRemoved;
    }

    public void setIsPropertyCurrencyUomIdRemoved(Boolean removed)
    {
        this.isPropertyCurrencyUomIdRemoved = removed;
    }

    private Boolean isPropertyFixedAssetIdRemoved;

    public Boolean getIsPropertyFixedAssetIdRemoved()
    {
        return this.isPropertyFixedAssetIdRemoved;
    }

    public void setIsPropertyFixedAssetIdRemoved(Boolean removed)
    {
        this.isPropertyFixedAssetIdRemoved = removed;
    }

    private Boolean isPropertyInventoryItemAttributeHashRemoved;

    public Boolean getIsPropertyInventoryItemAttributeHashRemoved()
    {
        return this.isPropertyInventoryItemAttributeHashRemoved;
    }

    public void setIsPropertyInventoryItemAttributeHashRemoved(Boolean removed)
    {
        this.isPropertyInventoryItemAttributeHashRemoved = removed;
    }

    public void copyTo(CreateOrMergePatchInventoryItem command)
    {
        ((AbstractInventoryItemCommandDto) this).copyTo(command);
        command.setInventoryItemTypeId(this.getInventoryItemTypeId());
        command.setProductId(this.getProductId());
        command.setPartyId(this.getPartyId());
        command.setOwnerPartyId(this.getOwnerPartyId());
        command.setStatusId(this.getStatusId());
        command.setDatetimeReceived(this.getDatetimeReceived());
        command.setDatetimeManufactured(this.getDatetimeManufactured());
        command.setExpireDate(this.getExpireDate());
        command.setFacilityId(this.getFacilityId());
        command.setContainerId(this.getContainerId());
        command.setLotId(this.getLotId());
        command.setUomId(this.getUomId());
        command.setBinNumber(this.getBinNumber());
        command.setLocationSeqId(this.getLocationSeqId());
        command.setComments(this.getComments());
        command.setQuantityOnHandTotal(this.getQuantityOnHandTotal());
        command.setAvailableToPromiseTotal(this.getAvailableToPromiseTotal());
        command.setAccountingQuantityTotal(this.getAccountingQuantityTotal());
        command.setSerialNumber(this.getSerialNumber());
        command.setSoftIdentifier(this.getSoftIdentifier());
        command.setActivationNumber(this.getActivationNumber());
        command.setActivationValidThru(this.getActivationValidThru());
        command.setUnitCost(this.getUnitCost());
        command.setCurrencyUomId(this.getCurrencyUomId());
        command.setFixedAssetId(this.getFixedAssetId());
        command.setInventoryItemAttributeHash(this.getInventoryItemAttributeHash());
    }

    public InventoryItemCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractInventoryItemCommand.SimpleCreateInventoryItem command = new AbstractInventoryItemCommand.SimpleCreateInventoryItem();
            copyTo((AbstractInventoryItemCommand.AbstractCreateInventoryItem) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractInventoryItemCommand.SimpleMergePatchInventoryItem command = new AbstractInventoryItemCommand.SimpleMergePatchInventoryItem();
            copyTo((AbstractInventoryItemCommand.SimpleMergePatchInventoryItem) command);
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public InventoryItemCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            CreateInventoryItemDto command = new CreateInventoryItemDto();
            copyTo((CreateInventoryItem) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            MergePatchInventoryItemDto command = new MergePatchInventoryItemDto();
            copyTo((MergePatchInventoryItem) command);
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    public void copyTo(CreateInventoryItem command)
    {
        copyTo((CreateOrMergePatchInventoryItem) command);
    }

    public void copyTo(MergePatchInventoryItem command)
    {
        copyTo((CreateOrMergePatchInventoryItem) command);
        command.setIsPropertyInventoryItemTypeIdRemoved(this.getIsPropertyInventoryItemTypeIdRemoved());
        command.setIsPropertyProductIdRemoved(this.getIsPropertyProductIdRemoved());
        command.setIsPropertyPartyIdRemoved(this.getIsPropertyPartyIdRemoved());
        command.setIsPropertyOwnerPartyIdRemoved(this.getIsPropertyOwnerPartyIdRemoved());
        command.setIsPropertyStatusIdRemoved(this.getIsPropertyStatusIdRemoved());
        command.setIsPropertyDatetimeReceivedRemoved(this.getIsPropertyDatetimeReceivedRemoved());
        command.setIsPropertyDatetimeManufacturedRemoved(this.getIsPropertyDatetimeManufacturedRemoved());
        command.setIsPropertyExpireDateRemoved(this.getIsPropertyExpireDateRemoved());
        command.setIsPropertyFacilityIdRemoved(this.getIsPropertyFacilityIdRemoved());
        command.setIsPropertyContainerIdRemoved(this.getIsPropertyContainerIdRemoved());
        command.setIsPropertyLotIdRemoved(this.getIsPropertyLotIdRemoved());
        command.setIsPropertyUomIdRemoved(this.getIsPropertyUomIdRemoved());
        command.setIsPropertyBinNumberRemoved(this.getIsPropertyBinNumberRemoved());
        command.setIsPropertyLocationSeqIdRemoved(this.getIsPropertyLocationSeqIdRemoved());
        command.setIsPropertyCommentsRemoved(this.getIsPropertyCommentsRemoved());
        command.setIsPropertyQuantityOnHandTotalRemoved(this.getIsPropertyQuantityOnHandTotalRemoved());
        command.setIsPropertyAvailableToPromiseTotalRemoved(this.getIsPropertyAvailableToPromiseTotalRemoved());
        command.setIsPropertyAccountingQuantityTotalRemoved(this.getIsPropertyAccountingQuantityTotalRemoved());
        command.setIsPropertySerialNumberRemoved(this.getIsPropertySerialNumberRemoved());
        command.setIsPropertySoftIdentifierRemoved(this.getIsPropertySoftIdentifierRemoved());
        command.setIsPropertyActivationNumberRemoved(this.getIsPropertyActivationNumberRemoved());
        command.setIsPropertyActivationValidThruRemoved(this.getIsPropertyActivationValidThruRemoved());
        command.setIsPropertyUnitCostRemoved(this.getIsPropertyUnitCostRemoved());
        command.setIsPropertyCurrencyUomIdRemoved(this.getIsPropertyCurrencyUomIdRemoved());
        command.setIsPropertyFixedAssetIdRemoved(this.getIsPropertyFixedAssetIdRemoved());
        command.setIsPropertyInventoryItemAttributeHashRemoved(this.getIsPropertyInventoryItemAttributeHashRemoved());
    }

    public static class CreateInventoryItemDto extends CreateOrMergePatchInventoryItemDto implements InventoryItemCommand.CreateInventoryItem
    {
        public CreateInventoryItemDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public InventoryItemCommand.CreateInventoryItem toCreateInventoryItem()
        {
            return (InventoryItemCommand.CreateInventoryItem) toCommand();
        }

    }

    public static class MergePatchInventoryItemDto extends CreateOrMergePatchInventoryItemDto implements InventoryItemCommand.MergePatchInventoryItem
    {
        public MergePatchInventoryItemDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public InventoryItemCommand.MergePatchInventoryItem toMergePatchInventoryItem()
        {
            return (InventoryItemCommand.MergePatchInventoryItem) toCommand();
        }

    }

}

