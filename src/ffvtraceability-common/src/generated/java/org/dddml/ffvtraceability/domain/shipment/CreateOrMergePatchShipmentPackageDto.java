// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipment;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchShipmentPackageDto extends AbstractShipmentPackageCommandDto implements ShipmentPackageCommand.CreateOrMergePatchShipmentPackage {

    /**
     * Shipment Box Type Id
     */
    private String shipmentBoxTypeId;

    public String getShipmentBoxTypeId()
    {
        return this.shipmentBoxTypeId;
    }

    public void setShipmentBoxTypeId(String shipmentBoxTypeId)
    {
        this.shipmentBoxTypeId = shipmentBoxTypeId;
    }

    /**
     * Date Created
     */
    private OffsetDateTime dateCreated;

    public OffsetDateTime getDateCreated()
    {
        return this.dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    /**
     * This field store the length of package; if a shipmentBoxTypeId is specified then this overrides the dimension specified there; this field is meant to be used when there is no applicable ShipmentBoxType
     */
    private java.math.BigDecimal boxLength;

    public java.math.BigDecimal getBoxLength()
    {
        return this.boxLength;
    }

    public void setBoxLength(java.math.BigDecimal boxLength)
    {
        this.boxLength = boxLength;
    }

    /**
     * This field store the height of package; if a shipmentBoxTypeId is specified then this overrides the dimension specified there; this field is meant to be used when there is no applicable ShipmentBoxType
     */
    private java.math.BigDecimal boxHeight;

    public java.math.BigDecimal getBoxHeight()
    {
        return this.boxHeight;
    }

    public void setBoxHeight(java.math.BigDecimal boxHeight)
    {
        this.boxHeight = boxHeight;
    }

    /**
     * This field store the width of package; if a shipmentBoxTypeId is specified then this overrides the dimension specified there; this field is meant to be used when there is no applicable ShipmentBoxType
     */
    private java.math.BigDecimal boxWidth;

    public java.math.BigDecimal getBoxWidth()
    {
        return this.boxWidth;
    }

    public void setBoxWidth(java.math.BigDecimal boxWidth)
    {
        this.boxWidth = boxWidth;
    }

    /**
     * This field store the unit of measurement of dimension (length, width and height)
     */
    private String dimensionUomId;

    public String getDimensionUomId()
    {
        return this.dimensionUomId;
    }

    public void setDimensionUomId(String dimensionUomId)
    {
        this.dimensionUomId = dimensionUomId;
    }

    /**
     * Weight
     */
    private java.math.BigDecimal weight;

    public java.math.BigDecimal getWeight()
    {
        return this.weight;
    }

    public void setWeight(java.math.BigDecimal weight)
    {
        this.weight = weight;
    }

    /**
     * Weight Uom Id
     */
    private String weightUomId;

    public String getWeightUomId()
    {
        return this.weightUomId;
    }

    public void setWeightUomId(String weightUomId)
    {
        this.weightUomId = weightUomId;
    }

    /**
     * Insured Value
     */
    private java.math.BigDecimal insuredValue;

    public java.math.BigDecimal getInsuredValue()
    {
        return this.insuredValue;
    }

    public void setInsuredValue(java.math.BigDecimal insuredValue)
    {
        this.insuredValue = insuredValue;
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


    private CreateOrMergePatchShipmentPackageContentDto[] shipmentPackageContents = new CreateOrMergePatchShipmentPackageContentDto[0];

    public CreateOrMergePatchShipmentPackageContentDto[] getShipmentPackageContents()
    {
        return this.shipmentPackageContents;
    }

    public void setShipmentPackageContents(CreateOrMergePatchShipmentPackageContentDto[] shipmentPackageContents)
    {
        this.shipmentPackageContents = shipmentPackageContents;
    }

    private Boolean isPropertyShipmentBoxTypeIdRemoved;

    public Boolean getIsPropertyShipmentBoxTypeIdRemoved()
    {
        return this.isPropertyShipmentBoxTypeIdRemoved;
    }

    public void setIsPropertyShipmentBoxTypeIdRemoved(Boolean removed)
    {
        this.isPropertyShipmentBoxTypeIdRemoved = removed;
    }

    private Boolean isPropertyDateCreatedRemoved;

    public Boolean getIsPropertyDateCreatedRemoved()
    {
        return this.isPropertyDateCreatedRemoved;
    }

    public void setIsPropertyDateCreatedRemoved(Boolean removed)
    {
        this.isPropertyDateCreatedRemoved = removed;
    }

    private Boolean isPropertyBoxLengthRemoved;

    public Boolean getIsPropertyBoxLengthRemoved()
    {
        return this.isPropertyBoxLengthRemoved;
    }

    public void setIsPropertyBoxLengthRemoved(Boolean removed)
    {
        this.isPropertyBoxLengthRemoved = removed;
    }

    private Boolean isPropertyBoxHeightRemoved;

    public Boolean getIsPropertyBoxHeightRemoved()
    {
        return this.isPropertyBoxHeightRemoved;
    }

    public void setIsPropertyBoxHeightRemoved(Boolean removed)
    {
        this.isPropertyBoxHeightRemoved = removed;
    }

    private Boolean isPropertyBoxWidthRemoved;

    public Boolean getIsPropertyBoxWidthRemoved()
    {
        return this.isPropertyBoxWidthRemoved;
    }

    public void setIsPropertyBoxWidthRemoved(Boolean removed)
    {
        this.isPropertyBoxWidthRemoved = removed;
    }

    private Boolean isPropertyDimensionUomIdRemoved;

    public Boolean getIsPropertyDimensionUomIdRemoved()
    {
        return this.isPropertyDimensionUomIdRemoved;
    }

    public void setIsPropertyDimensionUomIdRemoved(Boolean removed)
    {
        this.isPropertyDimensionUomIdRemoved = removed;
    }

    private Boolean isPropertyWeightRemoved;

    public Boolean getIsPropertyWeightRemoved()
    {
        return this.isPropertyWeightRemoved;
    }

    public void setIsPropertyWeightRemoved(Boolean removed)
    {
        this.isPropertyWeightRemoved = removed;
    }

    private Boolean isPropertyWeightUomIdRemoved;

    public Boolean getIsPropertyWeightUomIdRemoved()
    {
        return this.isPropertyWeightUomIdRemoved;
    }

    public void setIsPropertyWeightUomIdRemoved(Boolean removed)
    {
        this.isPropertyWeightUomIdRemoved = removed;
    }

    private Boolean isPropertyInsuredValueRemoved;

    public Boolean getIsPropertyInsuredValueRemoved()
    {
        return this.isPropertyInsuredValueRemoved;
    }

    public void setIsPropertyInsuredValueRemoved(Boolean removed)
    {
        this.isPropertyInsuredValueRemoved = removed;
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

    public void copyTo(CreateOrMergePatchShipmentPackage command)
    {
        ((AbstractShipmentPackageCommandDto) this).copyTo(command);
        command.setShipmentBoxTypeId(this.getShipmentBoxTypeId());
        command.setDateCreated(this.getDateCreated());
        command.setBoxLength(this.getBoxLength());
        command.setBoxHeight(this.getBoxHeight());
        command.setBoxWidth(this.getBoxWidth());
        command.setDimensionUomId(this.getDimensionUomId());
        command.setWeight(this.getWeight());
        command.setWeightUomId(this.getWeightUomId());
        command.setInsuredValue(this.getInsuredValue());
        command.setActive(this.getActive());
    }

    public ShipmentPackageCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractShipmentPackageCommand.SimpleCreateShipmentPackage command = new AbstractShipmentPackageCommand.SimpleCreateShipmentPackage();
            copyTo((AbstractShipmentPackageCommand.AbstractCreateShipmentPackage) command);
            if (this.getShipmentPackageContents() != null) {
                for (CreateOrMergePatchShipmentPackageContentDto cmd : this.getShipmentPackageContents()) {
                    command.getShipmentPackageContents().add((ShipmentPackageContentCommand.CreateShipmentPackageContent) cmd.toCommand());
                }
            }
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractShipmentPackageCommand.SimpleMergePatchShipmentPackage command = new AbstractShipmentPackageCommand.SimpleMergePatchShipmentPackage();
            copyTo((AbstractShipmentPackageCommand.SimpleMergePatchShipmentPackage) command);
            if (this.getShipmentPackageContents() != null) {
                for (CreateOrMergePatchShipmentPackageContentDto cmd : this.getShipmentPackageContents()) {
                    command.getShipmentPackageContentCommands().add(cmd.toCommand());
                }
            }
            return command;
        } 
        else if (COMMAND_TYPE_REMOVE.equals(getCommandType())) {
            AbstractShipmentPackageCommand.SimpleRemoveShipmentPackage command = new AbstractShipmentPackageCommand.SimpleRemoveShipmentPackage();
            ((AbstractShipmentPackageCommandDto) this).copyTo(command);
            return command;
        }
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public ShipmentPackageCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            CreateShipmentPackageDto command = new CreateShipmentPackageDto();
            copyTo((CreateShipmentPackage) command);
            if (this.getShipmentPackageContents() != null) {
                for (CreateOrMergePatchShipmentPackageContentDto cmd : this.getShipmentPackageContents()) {
                    if (cmd.getCommandType() == null) { cmd.setCommandType(COMMAND_TYPE_CREATE); }
                    command.getCreateShipmentPackageContentCommands().add((ShipmentPackageContentCommand.CreateShipmentPackageContent) cmd.toSubclass());
                }
            }
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            MergePatchShipmentPackageDto command = new MergePatchShipmentPackageDto();
            copyTo((MergePatchShipmentPackage) command);
            if (this.getShipmentPackageContents() != null) {
                for (CreateOrMergePatchShipmentPackageContentDto cmd : this.getShipmentPackageContents()) {
                    command.getShipmentPackageContentCommands().add(cmd.toSubclass());
                }
            }
            return command;
        } 
        else if (COMMAND_TYPE_REMOVE.equals(getCommandType())) {
            RemoveShipmentPackageDto command = new RemoveShipmentPackageDto();
            ((AbstractShipmentPackageCommandDto) this).copyTo(command);
            return command;
        }
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    public void copyTo(CreateShipmentPackage command)
    {
        copyTo((CreateOrMergePatchShipmentPackage) command);
    }

    public void copyTo(MergePatchShipmentPackage command)
    {
        copyTo((CreateOrMergePatchShipmentPackage) command);
        command.setIsPropertyShipmentBoxTypeIdRemoved(this.getIsPropertyShipmentBoxTypeIdRemoved());
        command.setIsPropertyDateCreatedRemoved(this.getIsPropertyDateCreatedRemoved());
        command.setIsPropertyBoxLengthRemoved(this.getIsPropertyBoxLengthRemoved());
        command.setIsPropertyBoxHeightRemoved(this.getIsPropertyBoxHeightRemoved());
        command.setIsPropertyBoxWidthRemoved(this.getIsPropertyBoxWidthRemoved());
        command.setIsPropertyDimensionUomIdRemoved(this.getIsPropertyDimensionUomIdRemoved());
        command.setIsPropertyWeightRemoved(this.getIsPropertyWeightRemoved());
        command.setIsPropertyWeightUomIdRemoved(this.getIsPropertyWeightUomIdRemoved());
        command.setIsPropertyInsuredValueRemoved(this.getIsPropertyInsuredValueRemoved());
        command.setIsPropertyActiveRemoved(this.getIsPropertyActiveRemoved());
    }

    public static class CreateShipmentPackageDto extends CreateOrMergePatchShipmentPackageDto implements ShipmentPackageCommand.CreateShipmentPackage
    {
        public CreateShipmentPackageDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public ShipmentPackageCommand.CreateShipmentPackage toCreateShipmentPackage()
        {
            return (ShipmentPackageCommand.CreateShipmentPackage) toCommand();
        }


        @Override
        public CreateShipmentPackageContentCommandCollection getCreateShipmentPackageContentCommands() {
            return new CreateShipmentPackageContentCommandCollection() {
                @Override
                public void add(ShipmentPackageContentCommand.CreateShipmentPackageContent c) {
                    java.util.List<CreateOrMergePatchShipmentPackageContentDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentPackageContents()));
                    list.add((CreateOrMergePatchShipmentPackageContentDto) c);
                    setShipmentPackageContents(list.toArray(new CreateOrMergePatchShipmentPackageContentDto[0]));
                }

                @Override
                public void remove(ShipmentPackageContentCommand.CreateShipmentPackageContent c) {
                    java.util.List<CreateOrMergePatchShipmentPackageContentDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentPackageContents()));
                    list.remove((CreateOrMergePatchShipmentPackageContentDto) c);
                    setShipmentPackageContents(list.toArray(new CreateOrMergePatchShipmentPackageContentDto[0]));
                }

                @Override
                public void clear() {
                    setShipmentPackageContents(new CreateOrMergePatchShipmentPackageContentDto[]{});
                }

                @Override
                public java.util.Iterator<ShipmentPackageContentCommand.CreateShipmentPackageContent> iterator() {
                    return java.util.Arrays.stream(getShipmentPackageContents())
                            .map(e -> {if (e.getCommandType()==null) e.setCommandType(COMMAND_TYPE_CREATE);return (ShipmentPackageContentCommand.CreateShipmentPackageContent) e.toSubclass();}).iterator();
                }
            };
        }

        @Override
        public ShipmentPackageContentCommand.CreateShipmentPackageContent newCreateShipmentPackageContent() {
            return new CreateOrMergePatchShipmentPackageContentDto.CreateShipmentPackageContentDto();
        }

    }

    public static class MergePatchShipmentPackageDto extends CreateOrMergePatchShipmentPackageDto implements ShipmentPackageCommand.MergePatchShipmentPackage
    {
        public MergePatchShipmentPackageDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public ShipmentPackageCommand.MergePatchShipmentPackage toMergePatchShipmentPackage()
        {
            return (ShipmentPackageCommand.MergePatchShipmentPackage) toCommand();
        }


        @Override
        public ShipmentPackageContentCommandCollection getShipmentPackageContentCommands() {
            return new ShipmentPackageContentCommandCollection() {
                @Override
                public void add(ShipmentPackageContentCommand c) {
                    java.util.List<CreateOrMergePatchShipmentPackageContentDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentPackageContents()));
                    list.add((CreateOrMergePatchShipmentPackageContentDto) c);
                    setShipmentPackageContents(list.toArray(new CreateOrMergePatchShipmentPackageContentDto[0]));
                }

                @Override
                public void remove(ShipmentPackageContentCommand c) {
                    java.util.List<CreateOrMergePatchShipmentPackageContentDto> list = new java.util.ArrayList<>(java.util.Arrays.asList(getShipmentPackageContents()));
                    list.remove((CreateOrMergePatchShipmentPackageContentDto) c);
                    setShipmentPackageContents(list.toArray(new CreateOrMergePatchShipmentPackageContentDto[0]));
                }

                @Override
                public void clear() {
                    setShipmentPackageContents(new CreateOrMergePatchShipmentPackageContentDto[]{});
                }

                @Override
                public java.util.Iterator<ShipmentPackageContentCommand> iterator() {
                    return java.util.Arrays.stream(getShipmentPackageContents())
                            .map(e -> (ShipmentPackageContentCommand) e.toSubclass()).iterator();
                }
            };
        }

        @Override
        public ShipmentPackageContentCommand.CreateShipmentPackageContent newCreateShipmentPackageContent() {
            return new CreateOrMergePatchShipmentPackageContentDto.CreateShipmentPackageContentDto();
        }

        @Override
        public ShipmentPackageContentCommand.MergePatchShipmentPackageContent newMergePatchShipmentPackageContent() {
            return new CreateOrMergePatchShipmentPackageContentDto.MergePatchShipmentPackageContentDto();
        }

        @Override
        public ShipmentPackageContentCommand.RemoveShipmentPackageContent newRemoveShipmentPackageContent() {
            return new RemoveShipmentPackageContentDto();
        }

    }

}
