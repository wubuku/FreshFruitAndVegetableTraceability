// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipmentreceipt;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.partyrole.*;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchShipmentReceiptDto extends AbstractShipmentReceiptCommandDto implements ShipmentReceiptCommand.CreateOrMergePatchShipmentReceipt {

    /**
     * Inventory Item Id
     */
    private String inventoryItemId;

    public String getInventoryItemId()
    {
        return this.inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId)
    {
        this.inventoryItemId = inventoryItemId;
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
     * Shipment Id
     */
    private String shipmentId;

    public String getShipmentId()
    {
        return this.shipmentId;
    }

    public void setShipmentId(String shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    /**
     * Shipment Item Seq Id
     */
    private String shipmentItemSeqId;

    public String getShipmentItemSeqId()
    {
        return this.shipmentItemSeqId;
    }

    public void setShipmentItemSeqId(String shipmentItemSeqId)
    {
        this.shipmentItemSeqId = shipmentItemSeqId;
    }

    /**
     * Shipment Package Seq Id
     */
    private String shipmentPackageSeqId;

    public String getShipmentPackageSeqId()
    {
        return this.shipmentPackageSeqId;
    }

    public void setShipmentPackageSeqId(String shipmentPackageSeqId)
    {
        this.shipmentPackageSeqId = shipmentPackageSeqId;
    }

    /**
     * Order Id
     */
    private String orderId;

    public String getOrderId()
    {
        return this.orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    /**
     * Order Item Seq Id
     */
    private String orderItemSeqId;

    public String getOrderItemSeqId()
    {
        return this.orderItemSeqId;
    }

    public void setOrderItemSeqId(String orderItemSeqId)
    {
        this.orderItemSeqId = orderItemSeqId;
    }

    /**
     * Return Id
     */
    private String returnId;

    public String getReturnId()
    {
        return this.returnId;
    }

    public void setReturnId(String returnId)
    {
        this.returnId = returnId;
    }

    /**
     * Return Item Seq Id
     */
    private String returnItemSeqId;

    public String getReturnItemSeqId()
    {
        return this.returnItemSeqId;
    }

    public void setReturnItemSeqId(String returnItemSeqId)
    {
        this.returnItemSeqId = returnItemSeqId;
    }

    /**
     * Rejection Id
     */
    private String rejectionId;

    public String getRejectionId()
    {
        return this.rejectionId;
    }

    public void setRejectionId(String rejectionId)
    {
        this.rejectionId = rejectionId;
    }

    /**
     * Received By User Login Id
     */
    private String receivedByUserLoginId;

    public String getReceivedByUserLoginId()
    {
        return this.receivedByUserLoginId;
    }

    public void setReceivedByUserLoginId(String receivedByUserLoginId)
    {
        this.receivedByUserLoginId = receivedByUserLoginId;
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
     * Item Description
     */
    private String itemDescription;

    public String getItemDescription()
    {
        return this.itemDescription;
    }

    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    /**
     * Quantity Accepted
     */
    private java.math.BigDecimal quantityAccepted;

    public java.math.BigDecimal getQuantityAccepted()
    {
        return this.quantityAccepted;
    }

    public void setQuantityAccepted(java.math.BigDecimal quantityAccepted)
    {
        this.quantityAccepted = quantityAccepted;
    }

    /**
     * Quantity Rejected
     */
    private java.math.BigDecimal quantityRejected;

    public java.math.BigDecimal getQuantityRejected()
    {
        return this.quantityRejected;
    }

    public void setQuantityRejected(java.math.BigDecimal quantityRejected)
    {
        this.quantityRejected = quantityRejected;
    }

    /**
     * Active
     */
    private Boolean active;

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }


    private CreateOrMergePatchShipmentReceiptRoleDto[] shipmentReceiptRoles = new CreateOrMergePatchShipmentReceiptRoleDto[0];

    public CreateOrMergePatchShipmentReceiptRoleDto[] getShipmentReceiptRoles()
    {
        return this.shipmentReceiptRoles;
    }

    public void setShipmentReceiptRoles(CreateOrMergePatchShipmentReceiptRoleDto[] shipmentReceiptRoles)
    {
        this.shipmentReceiptRoles = shipmentReceiptRoles;
    }

    private Boolean isPropertyInventoryItemIdRemoved;

    public Boolean getIsPropertyInventoryItemIdRemoved()
    {
        return this.isPropertyInventoryItemIdRemoved;
    }

    public void setIsPropertyInventoryItemIdRemoved(Boolean removed)
    {
        this.isPropertyInventoryItemIdRemoved = removed;
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

    private Boolean isPropertyShipmentIdRemoved;

    public Boolean getIsPropertyShipmentIdRemoved()
    {
        return this.isPropertyShipmentIdRemoved;
    }

    public void setIsPropertyShipmentIdRemoved(Boolean removed)
    {
        this.isPropertyShipmentIdRemoved = removed;
    }

    private Boolean isPropertyShipmentItemSeqIdRemoved;

    public Boolean getIsPropertyShipmentItemSeqIdRemoved()
    {
        return this.isPropertyShipmentItemSeqIdRemoved;
    }

    public void setIsPropertyShipmentItemSeqIdRemoved(Boolean removed)
    {
        this.isPropertyShipmentItemSeqIdRemoved = removed;
    }

    private Boolean isPropertyShipmentPackageSeqIdRemoved;

    public Boolean getIsPropertyShipmentPackageSeqIdRemoved()
    {
        return this.isPropertyShipmentPackageSeqIdRemoved;
    }

    public void setIsPropertyShipmentPackageSeqIdRemoved(Boolean removed)
    {
        this.isPropertyShipmentPackageSeqIdRemoved = removed;
    }

    private Boolean isPropertyOrderIdRemoved;

    public Boolean getIsPropertyOrderIdRemoved()
    {
        return this.isPropertyOrderIdRemoved;
    }

    public void setIsPropertyOrderIdRemoved(Boolean removed)
    {
        this.isPropertyOrderIdRemoved = removed;
    }

    private Boolean isPropertyOrderItemSeqIdRemoved;

    public Boolean getIsPropertyOrderItemSeqIdRemoved()
    {
        return this.isPropertyOrderItemSeqIdRemoved;
    }

    public void setIsPropertyOrderItemSeqIdRemoved(Boolean removed)
    {
        this.isPropertyOrderItemSeqIdRemoved = removed;
    }

    private Boolean isPropertyReturnIdRemoved;

    public Boolean getIsPropertyReturnIdRemoved()
    {
        return this.isPropertyReturnIdRemoved;
    }

    public void setIsPropertyReturnIdRemoved(Boolean removed)
    {
        this.isPropertyReturnIdRemoved = removed;
    }

    private Boolean isPropertyReturnItemSeqIdRemoved;

    public Boolean getIsPropertyReturnItemSeqIdRemoved()
    {
        return this.isPropertyReturnItemSeqIdRemoved;
    }

    public void setIsPropertyReturnItemSeqIdRemoved(Boolean removed)
    {
        this.isPropertyReturnItemSeqIdRemoved = removed;
    }

    private Boolean isPropertyRejectionIdRemoved;

    public Boolean getIsPropertyRejectionIdRemoved()
    {
        return this.isPropertyRejectionIdRemoved;
    }

    public void setIsPropertyRejectionIdRemoved(Boolean removed)
    {
        this.isPropertyRejectionIdRemoved = removed;
    }

    private Boolean isPropertyReceivedByUserLoginIdRemoved;

    public Boolean getIsPropertyReceivedByUserLoginIdRemoved()
    {
        return this.isPropertyReceivedByUserLoginIdRemoved;
    }

    public void setIsPropertyReceivedByUserLoginIdRemoved(Boolean removed)
    {
        this.isPropertyReceivedByUserLoginIdRemoved = removed;
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

    private Boolean isPropertyItemDescriptionRemoved;

    public Boolean getIsPropertyItemDescriptionRemoved()
    {
        return this.isPropertyItemDescriptionRemoved;
    }

    public void setIsPropertyItemDescriptionRemoved(Boolean removed)
    {
        this.isPropertyItemDescriptionRemoved = removed;
    }

    private Boolean isPropertyQuantityAcceptedRemoved;

    public Boolean getIsPropertyQuantityAcceptedRemoved()
    {
        return this.isPropertyQuantityAcceptedRemoved;
    }

    public void setIsPropertyQuantityAcceptedRemoved(Boolean removed)
    {
        this.isPropertyQuantityAcceptedRemoved = removed;
    }

    private Boolean isPropertyQuantityRejectedRemoved;

    public Boolean getIsPropertyQuantityRejectedRemoved()
    {
        return this.isPropertyQuantityRejectedRemoved;
    }

    public void setIsPropertyQuantityRejectedRemoved(Boolean removed)
    {
        this.isPropertyQuantityRejectedRemoved = removed;
    }

    private Boolean isPropertyActiveRemoved;

    public Boolean getIsPropertyActiveRemoved()
    {
        return this.isPropertyActiveRemoved;
    }

    public void setIsPropertyActiveRemoved(Boolean removed)
    {
        this.isPropertyActiveRemoved = removed;
    }

    public void copyTo(CreateOrMergePatchShipmentReceipt command)
    {
        ((AbstractShipmentReceiptCommandDto) this).copyTo(command);
        command.setInventoryItemId(this.getInventoryItemId());
        command.setProductId(this.getProductId());
        command.setShipmentId(this.getShipmentId());
        command.setShipmentItemSeqId(this.getShipmentItemSeqId());
        command.setShipmentPackageSeqId(this.getShipmentPackageSeqId());
        command.setOrderId(this.getOrderId());
        command.setOrderItemSeqId(this.getOrderItemSeqId());
        command.setReturnId(this.getReturnId());
        command.setReturnItemSeqId(this.getReturnItemSeqId());
        command.setRejectionId(this.getRejectionId());
        command.setReceivedByUserLoginId(this.getReceivedByUserLoginId());
        command.setDatetimeReceived(this.getDatetimeReceived());
        command.setItemDescription(this.getItemDescription());
        command.setQuantityAccepted(this.getQuantityAccepted());
        command.setQuantityRejected(this.getQuantityRejected());
        command.setActive(this.getActive());
    }

    public ShipmentReceiptCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractShipmentReceiptCommand.SimpleCreateShipmentReceipt command = new AbstractShipmentReceiptCommand.SimpleCreateShipmentReceipt();
            copyTo((AbstractShipmentReceiptCommand.AbstractCreateShipmentReceipt) command);
            if (this.getShipmentReceiptRoles() != null) {
                for (CreateOrMergePatchShipmentReceiptRoleDto cmd : this.getShipmentReceiptRoles()) {
                    command.getShipmentReceiptRoles().add((ShipmentReceiptRoleCommand.CreateShipmentReceiptRole) cmd.toCommand());
                }
            }
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractShipmentReceiptCommand.SimpleMergePatchShipmentReceipt command = new AbstractShipmentReceiptCommand.SimpleMergePatchShipmentReceipt();
            copyTo((AbstractShipmentReceiptCommand.SimpleMergePatchShipmentReceipt) command);
            if (this.getShipmentReceiptRoles() != null) {
                for (CreateOrMergePatchShipmentReceiptRoleDto cmd : this.getShipmentReceiptRoles()) {
                    command.getShipmentReceiptRoleCommands().add(cmd.toCommand());
                }
            }
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public ShipmentReceiptCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            CreateShipmentReceiptDto command = new CreateShipmentReceiptDto();
            copyTo((CreateShipmentReceipt) command);
            if (this.getShipmentReceiptRoles() != null) {
                for (CreateOrMergePatchShipmentReceiptRoleDto cmd : this.getShipmentReceiptRoles()) {
                    if (cmd.getCommandType() == null) { cmd.setCommandType(COMMAND_TYPE_CREATE); }
                    command.getCreateShipmentReceiptRoleCommands().add((ShipmentReceiptRoleCommand.CreateShipmentReceiptRole) cmd.toSubclass());
                }
            }
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            MergePatchShipmentReceiptDto command = new MergePatchShipmentReceiptDto();
            copyTo((MergePatchShipmentReceipt) command);
            if (this.getShipmentReceiptRoles() != null) {
                for (CreateOrMergePatchShipmentReceiptRoleDto cmd : this.getShipmentReceiptRoles()) {
                    command.getShipmentReceiptRoleCommands().add(cmd.toSubclass());
                }
            }
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    public void copyTo(CreateShipmentReceipt command)
    {
        copyTo((CreateOrMergePatchShipmentReceipt) command);
    }

    public void copyTo(MergePatchShipmentReceipt command)
    {
        copyTo((CreateOrMergePatchShipmentReceipt) command);
        command.setIsPropertyInventoryItemIdRemoved(this.getIsPropertyInventoryItemIdRemoved());
        command.setIsPropertyProductIdRemoved(this.getIsPropertyProductIdRemoved());
        command.setIsPropertyShipmentIdRemoved(this.getIsPropertyShipmentIdRemoved());
        command.setIsPropertyShipmentItemSeqIdRemoved(this.getIsPropertyShipmentItemSeqIdRemoved());
        command.setIsPropertyShipmentPackageSeqIdRemoved(this.getIsPropertyShipmentPackageSeqIdRemoved());
        command.setIsPropertyOrderIdRemoved(this.getIsPropertyOrderIdRemoved());
        command.setIsPropertyOrderItemSeqIdRemoved(this.getIsPropertyOrderItemSeqIdRemoved());
        command.setIsPropertyReturnIdRemoved(this.getIsPropertyReturnIdRemoved());
        command.setIsPropertyReturnItemSeqIdRemoved(this.getIsPropertyReturnItemSeqIdRemoved());
        command.setIsPropertyRejectionIdRemoved(this.getIsPropertyRejectionIdRemoved());
        command.setIsPropertyReceivedByUserLoginIdRemoved(this.getIsPropertyReceivedByUserLoginIdRemoved());
        command.setIsPropertyDatetimeReceivedRemoved(this.getIsPropertyDatetimeReceivedRemoved());
        command.setIsPropertyItemDescriptionRemoved(this.getIsPropertyItemDescriptionRemoved());
        command.setIsPropertyQuantityAcceptedRemoved(this.getIsPropertyQuantityAcceptedRemoved());
        command.setIsPropertyQuantityRejectedRemoved(this.getIsPropertyQuantityRejectedRemoved());
        command.setIsPropertyActiveRemoved(this.getIsPropertyActiveRemoved());
    }

    public static class CreateShipmentReceiptDto extends CreateOrMergePatchShipmentReceiptDto implements ShipmentReceiptCommand.CreateShipmentReceipt
    {
        public CreateShipmentReceiptDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public ShipmentReceiptCommand.CreateShipmentReceipt toCreateShipmentReceipt()
        {
            return (ShipmentReceiptCommand.CreateShipmentReceipt) toCommand();
        }


        @Override
        public CreateShipmentReceiptRoleCommandCollection getCreateShipmentReceiptRoleCommands() {
            return new CreateShipmentReceiptRoleCommandCollection() {
                @Override
                public void add(ShipmentReceiptRoleCommand.CreateShipmentReceiptRole c) {
                    java.util.List<CreateOrMergePatchShipmentReceiptRoleDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentReceiptRoles()));
                    list.add((CreateOrMergePatchShipmentReceiptRoleDto) c);
                    setShipmentReceiptRoles(list.toArray(new CreateOrMergePatchShipmentReceiptRoleDto[0]));
                }

                @Override
                public void remove(ShipmentReceiptRoleCommand.CreateShipmentReceiptRole c) {
                    java.util.List<CreateOrMergePatchShipmentReceiptRoleDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentReceiptRoles()));
                    list.remove((CreateOrMergePatchShipmentReceiptRoleDto) c);
                    setShipmentReceiptRoles(list.toArray(new CreateOrMergePatchShipmentReceiptRoleDto[0]));
                }

                @Override
                public void clear() {
                    setShipmentReceiptRoles(new CreateOrMergePatchShipmentReceiptRoleDto[]{});
                }

                @Override
                public java.util.Iterator<ShipmentReceiptRoleCommand.CreateShipmentReceiptRole> iterator() {
                    return java.util.Arrays.stream(getShipmentReceiptRoles())
                            .map(e -> {if (e.getCommandType()==null) e.setCommandType(COMMAND_TYPE_CREATE);return (ShipmentReceiptRoleCommand.CreateShipmentReceiptRole) e.toSubclass();}).iterator();
                }
            };
        }

        @Override
        public ShipmentReceiptRoleCommand.CreateShipmentReceiptRole newCreateShipmentReceiptRole() {
            return new CreateOrMergePatchShipmentReceiptRoleDto.CreateShipmentReceiptRoleDto();
        }

    }

    public static class MergePatchShipmentReceiptDto extends CreateOrMergePatchShipmentReceiptDto implements ShipmentReceiptCommand.MergePatchShipmentReceipt
    {
        public MergePatchShipmentReceiptDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public ShipmentReceiptCommand.MergePatchShipmentReceipt toMergePatchShipmentReceipt()
        {
            return (ShipmentReceiptCommand.MergePatchShipmentReceipt) toCommand();
        }


        @Override
        public ShipmentReceiptRoleCommandCollection getShipmentReceiptRoleCommands() {
            return new ShipmentReceiptRoleCommandCollection() {
                @Override
                public void add(ShipmentReceiptRoleCommand c) {
                    java.util.List<CreateOrMergePatchShipmentReceiptRoleDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentReceiptRoles()));
                    list.add((CreateOrMergePatchShipmentReceiptRoleDto) c);
                    setShipmentReceiptRoles(list.toArray(new CreateOrMergePatchShipmentReceiptRoleDto[0]));
                }

                @Override
                public void remove(ShipmentReceiptRoleCommand c) {
                    java.util.List<CreateOrMergePatchShipmentReceiptRoleDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentReceiptRoles()));
                    list.remove((CreateOrMergePatchShipmentReceiptRoleDto) c);
                    setShipmentReceiptRoles(list.toArray(new CreateOrMergePatchShipmentReceiptRoleDto[0]));
                }

                @Override
                public void clear() {
                    setShipmentReceiptRoles(new CreateOrMergePatchShipmentReceiptRoleDto[]{});
                }

                @Override
                public java.util.Iterator<ShipmentReceiptRoleCommand> iterator() {
                    return java.util.Arrays.stream(getShipmentReceiptRoles())
                            .map(e -> (ShipmentReceiptRoleCommand) e.toSubclass()).iterator();
                }
            };
        }

        @Override
        public ShipmentReceiptRoleCommand.CreateShipmentReceiptRole newCreateShipmentReceiptRole() {
            return new CreateOrMergePatchShipmentReceiptRoleDto.CreateShipmentReceiptRoleDto();
        }

        @Override
        public ShipmentReceiptRoleCommand.MergePatchShipmentReceiptRole newMergePatchShipmentReceiptRole() {
            return new CreateOrMergePatchShipmentReceiptRoleDto.MergePatchShipmentReceiptRoleDto();
        }

        @Override
        public ShipmentReceiptRoleCommand.RemoveShipmentReceiptRole newRemoveShipmentReceiptRole() {
            return new RemoveShipmentReceiptRoleDto();
        }

    }

}
