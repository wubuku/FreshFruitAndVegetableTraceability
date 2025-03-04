// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attribute;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class AttributeStateDto {

    private String attributeId;

    public String getAttributeId()
    {
        return this.attributeId;
    }

    public void setAttributeId(String attributeId)
    {
        this.attributeId = attributeId;
    }

    private String attributeType;

    public String getAttributeType()
    {
        return this.attributeType;
    }

    public void setAttributeType(String attributeType)
    {
        this.attributeType = attributeType;
    }

    private String attributeName;

    public String getAttributeName()
    {
        return this.attributeName;
    }

    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }

    private String description;

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    private String isMandatory;

    public String getIsMandatory()
    {
        return this.isMandatory;
    }

    public void setIsMandatory(String isMandatory)
    {
        this.isMandatory = isMandatory;
    }

    private Long attributeLength;

    public Long getAttributeLength()
    {
        return this.attributeLength;
    }

    public void setAttributeLength(Long attributeLength)
    {
        this.attributeLength = attributeLength;
    }

    private String isEnumeration;

    public String getIsEnumeration()
    {
        return this.isEnumeration;
    }

    public void setIsEnumeration(String isEnumeration)
    {
        this.isEnumeration = isEnumeration;
    }

    private Long scale;

    public Long getScale()
    {
        return this.scale;
    }

    public void setScale(Long scale)
    {
        this.scale = scale;
    }

    private String truncatedTo;

    public String getTruncatedTo()
    {
        return this.truncatedTo;
    }

    public void setTruncatedTo(String truncatedTo)
    {
        this.truncatedTo = truncatedTo;
    }

    private Boolean active;

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
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

    private String createdBy;

    public String getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    private OffsetDateTime createdAt;

    public OffsetDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    private String updatedBy;

    public String getUpdatedBy()
    {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    private OffsetDateTime updatedAt;

    public OffsetDateTime getUpdatedAt()
    {
        return this.updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    private AttributeValueStateDto[] attributeValues;

    public AttributeValueStateDto[] getAttributeValues()
    {
        return this.attributeValues;
    }    

    public void setAttributeValues(AttributeValueStateDto[] attributeValues)
    {
        this.attributeValues = attributeValues;
    }


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{"AttributeValues"});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public AttributeStateDto[] toAttributeStateDtoArray(Iterable<AttributeState> states) {
            return toAttributeStateDtoList(states).toArray(new AttributeStateDto[0]);
        }

        public List<AttributeStateDto> toAttributeStateDtoList(Iterable<AttributeState> states) {
            ArrayList<AttributeStateDto> stateDtos = new ArrayList();
            for (AttributeState s : states) {
                AttributeStateDto dto = toAttributeStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public AttributeStateDto toAttributeStateDto(AttributeState state)
        {
            if(state == null) {
                return null;
            }
            AttributeStateDto dto = new AttributeStateDto();
            if (returnedFieldsContains("AttributeId")) {
                dto.setAttributeId(state.getAttributeId());
            }
            if (returnedFieldsContains("AttributeType")) {
                dto.setAttributeType(state.getAttributeType());
            }
            if (returnedFieldsContains("AttributeName")) {
                dto.setAttributeName(state.getAttributeName());
            }
            if (returnedFieldsContains("Description")) {
                dto.setDescription(state.getDescription());
            }
            if (returnedFieldsContains("IsMandatory")) {
                dto.setIsMandatory(state.getIsMandatory());
            }
            if (returnedFieldsContains("AttributeLength")) {
                dto.setAttributeLength(state.getAttributeLength());
            }
            if (returnedFieldsContains("IsEnumeration")) {
                dto.setIsEnumeration(state.getIsEnumeration());
            }
            if (returnedFieldsContains("Scale")) {
                dto.setScale(state.getScale());
            }
            if (returnedFieldsContains("TruncatedTo")) {
                dto.setTruncatedTo(state.getTruncatedTo());
            }
            if (returnedFieldsContains("Active")) {
                dto.setActive(state.getActive());
            }
            if (returnedFieldsContains("Version")) {
                dto.setVersion(state.getVersion());
            }
            if (returnedFieldsContains("CreatedBy")) {
                dto.setCreatedBy(state.getCreatedBy());
            }
            if (returnedFieldsContains("CreatedAt")) {
                dto.setCreatedAt(state.getCreatedAt());
            }
            if (returnedFieldsContains("UpdatedBy")) {
                dto.setUpdatedBy(state.getUpdatedBy());
            }
            if (returnedFieldsContains("UpdatedAt")) {
                dto.setUpdatedAt(state.getUpdatedAt());
            }
            if (returnedFieldsContains("AttributeValues")) {
                ArrayList<AttributeValueStateDto> arrayList = new ArrayList();
                if (state.getAttributeValues() != null) {
                    AttributeValueStateDto.DtoConverter conv = new AttributeValueStateDto.DtoConverter();
                    String returnFS = CollectionUtils.mapGetValueIgnoringCase(getReturnedFields(), "AttributeValues");
                    if(returnFS != null) { conv.setReturnedFieldsString(returnFS); } else { conv.setAllFieldsReturned(this.getAllFieldsReturned()); }
                    for (AttributeValueState s : state.getAttributeValues()) {
                        arrayList.add(conv.toAttributeValueStateDto(s));
                    }
                }
                dto.setAttributeValues(arrayList.toArray(new AttributeValueStateDto[0]));
            }
            return dto;
        }

    }
}

