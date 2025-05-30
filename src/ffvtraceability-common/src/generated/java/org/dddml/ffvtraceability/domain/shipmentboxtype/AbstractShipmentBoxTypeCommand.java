// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipmentboxtype;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractShipmentBoxTypeCommand extends AbstractCommand implements ShipmentBoxTypeCommand {

    private String shipmentBoxTypeId;

    public String getShipmentBoxTypeId()
    {
        return this.shipmentBoxTypeId;
    }

    public void setShipmentBoxTypeId(String shipmentBoxTypeId)
    {
        this.shipmentBoxTypeId = shipmentBoxTypeId;
    }

    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


    public static abstract class AbstractCreateOrMergePatchShipmentBoxType extends AbstractShipmentBoxTypeCommand implements CreateOrMergePatchShipmentBoxType
    {
        private String description;

        public String getDescription()
        {
            return this.description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        private String dimensionUomId;

        public String getDimensionUomId()
        {
            return this.dimensionUomId;
        }

        public void setDimensionUomId(String dimensionUomId)
        {
            this.dimensionUomId = dimensionUomId;
        }

        private java.math.BigDecimal boxLength;

        public java.math.BigDecimal getBoxLength()
        {
            return this.boxLength;
        }

        public void setBoxLength(java.math.BigDecimal boxLength)
        {
            this.boxLength = boxLength;
        }

        private java.math.BigDecimal boxWidth;

        public java.math.BigDecimal getBoxWidth()
        {
            return this.boxWidth;
        }

        public void setBoxWidth(java.math.BigDecimal boxWidth)
        {
            this.boxWidth = boxWidth;
        }

        private java.math.BigDecimal boxHeight;

        public java.math.BigDecimal getBoxHeight()
        {
            return this.boxHeight;
        }

        public void setBoxHeight(java.math.BigDecimal boxHeight)
        {
            this.boxHeight = boxHeight;
        }

        private String weightUomId;

        public String getWeightUomId()
        {
            return this.weightUomId;
        }

        public void setWeightUomId(String weightUomId)
        {
            this.weightUomId = weightUomId;
        }

        private java.math.BigDecimal boxWeight;

        public java.math.BigDecimal getBoxWeight()
        {
            return this.boxWeight;
        }

        public void setBoxWeight(java.math.BigDecimal boxWeight)
        {
            this.boxWeight = boxWeight;
        }

        private String active;

        public String getActive()
        {
            return this.active;
        }

        public void setActive(String active)
        {
            this.active = active;
        }

        private String boxTypeName;

        public String getBoxTypeName()
        {
            return this.boxTypeName;
        }

        public void setBoxTypeName(String boxTypeName)
        {
            this.boxTypeName = boxTypeName;
        }

    }

    public static abstract class AbstractCreateShipmentBoxType extends AbstractCreateOrMergePatchShipmentBoxType implements CreateShipmentBoxType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchShipmentBoxType extends AbstractCreateOrMergePatchShipmentBoxType implements MergePatchShipmentBoxType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

        private Boolean isPropertyDescriptionRemoved;

        public Boolean getIsPropertyDescriptionRemoved()
        {
            return this.isPropertyDescriptionRemoved;
        }

        public void setIsPropertyDescriptionRemoved(Boolean removed)
        {
            this.isPropertyDescriptionRemoved = removed;
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

        private Boolean isPropertyBoxLengthRemoved;

        public Boolean getIsPropertyBoxLengthRemoved()
        {
            return this.isPropertyBoxLengthRemoved;
        }

        public void setIsPropertyBoxLengthRemoved(Boolean removed)
        {
            this.isPropertyBoxLengthRemoved = removed;
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

        private Boolean isPropertyBoxHeightRemoved;

        public Boolean getIsPropertyBoxHeightRemoved()
        {
            return this.isPropertyBoxHeightRemoved;
        }

        public void setIsPropertyBoxHeightRemoved(Boolean removed)
        {
            this.isPropertyBoxHeightRemoved = removed;
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

        private Boolean isPropertyBoxWeightRemoved;

        public Boolean getIsPropertyBoxWeightRemoved()
        {
            return this.isPropertyBoxWeightRemoved;
        }

        public void setIsPropertyBoxWeightRemoved(Boolean removed)
        {
            this.isPropertyBoxWeightRemoved = removed;
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

        private Boolean isPropertyBoxTypeNameRemoved;

        public Boolean getIsPropertyBoxTypeNameRemoved()
        {
            return this.isPropertyBoxTypeNameRemoved;
        }

        public void setIsPropertyBoxTypeNameRemoved(Boolean removed)
        {
            this.isPropertyBoxTypeNameRemoved = removed;
        }


    }

    public static class SimpleCreateShipmentBoxType extends AbstractCreateShipmentBoxType
    {
    }

    
    public static class SimpleMergePatchShipmentBoxType extends AbstractMergePatchShipmentBoxType
    {
    }

    
    public static class SimpleDeleteShipmentBoxType extends AbstractShipmentBoxTypeCommand implements DeleteShipmentBoxType
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_DELETE;
        }
    }

    

}

