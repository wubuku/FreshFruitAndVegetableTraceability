// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.lot;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class LotStateDto {

    private String lotId;

    public String getLotId()
    {
        return this.lotId;
    }

    public void setLotId(String lotId)
    {
        this.lotId = lotId;
    }

    private String supplierId;

    public String getSupplierId()
    {
        return this.supplierId;
    }

    public void setSupplierId(String supplierId)
    {
        this.supplierId = supplierId;
    }

    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private java.math.BigDecimal quantity;

    public java.math.BigDecimal getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    private OffsetDateTime expirationDate;

    public OffsetDateTime getExpirationDate()
    {
        return this.expirationDate;
    }

    public void setExpirationDate(OffsetDateTime expirationDate)
    {
        this.expirationDate = expirationDate;
    }

    private String lotTypeId;

    public String getLotTypeId()
    {
        return this.lotTypeId;
    }

    public void setLotTypeId(String lotTypeId)
    {
        this.lotTypeId = lotTypeId;
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

    private String gtin;

    public String getGtin()
    {
        return this.gtin;
    }

    public void setGtin(String gtin)
    {
        this.gtin = gtin;
    }

    private String gs1Batch;

    public String getGs1Batch()
    {
        return this.gs1Batch;
    }

    public void setGs1Batch(String gs1Batch)
    {
        this.gs1Batch = gs1Batch;
    }

    private String sourceFacilityId;

    public String getSourceFacilityId()
    {
        return this.sourceFacilityId;
    }

    public void setSourceFacilityId(String sourceFacilityId)
    {
        this.sourceFacilityId = sourceFacilityId;
    }

    private String internalId;

    public String getInternalId()
    {
        return this.internalId;
    }

    public void setInternalId(String internalId)
    {
        this.internalId = internalId;
    }

    private String palletSscc;

    public String getPalletSscc()
    {
        return this.palletSscc;
    }

    public void setPalletSscc(String palletSscc)
    {
        this.palletSscc = palletSscc;
    }

    private OffsetDateTime packDate;

    public OffsetDateTime getPackDate()
    {
        return this.packDate;
    }

    public void setPackDate(OffsetDateTime packDate)
    {
        this.packDate = packDate;
    }

    private OffsetDateTime harvestDate;

    public OffsetDateTime getHarvestDate()
    {
        return this.harvestDate;
    }

    public void setHarvestDate(OffsetDateTime harvestDate)
    {
        this.harvestDate = harvestDate;
    }

    private String serialNumber;

    public String getSerialNumber()
    {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
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

    private LotIdentificationStateDto[] lotIdentifications;

    public LotIdentificationStateDto[] getLotIdentifications()
    {
        return this.lotIdentifications;
    }    

    public void setLotIdentifications(LotIdentificationStateDto[] lotIdentifications)
    {
        this.lotIdentifications = lotIdentifications;
    }


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{"LotIdentifications"});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public LotStateDto[] toLotStateDtoArray(Iterable<LotState> states) {
            return toLotStateDtoList(states).toArray(new LotStateDto[0]);
        }

        public List<LotStateDto> toLotStateDtoList(Iterable<LotState> states) {
            ArrayList<LotStateDto> stateDtos = new ArrayList();
            for (LotState s : states) {
                LotStateDto dto = toLotStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public LotStateDto toLotStateDto(LotState state)
        {
            if(state == null) {
                return null;
            }
            LotStateDto dto = new LotStateDto();
            if (returnedFieldsContains("LotId")) {
                dto.setLotId(state.getLotId());
            }
            if (returnedFieldsContains("SupplierId")) {
                dto.setSupplierId(state.getSupplierId());
            }
            if (returnedFieldsContains("ProductId")) {
                dto.setProductId(state.getProductId());
            }
            if (returnedFieldsContains("Quantity")) {
                dto.setQuantity(state.getQuantity());
            }
            if (returnedFieldsContains("ExpirationDate")) {
                dto.setExpirationDate(state.getExpirationDate());
            }
            if (returnedFieldsContains("LotTypeId")) {
                dto.setLotTypeId(state.getLotTypeId());
            }
            if (returnedFieldsContains("Active")) {
                dto.setActive(state.getActive());
            }
            if (returnedFieldsContains("Gtin")) {
                dto.setGtin(state.getGtin());
            }
            if (returnedFieldsContains("Gs1Batch")) {
                dto.setGs1Batch(state.getGs1Batch());
            }
            if (returnedFieldsContains("SourceFacilityId")) {
                dto.setSourceFacilityId(state.getSourceFacilityId());
            }
            if (returnedFieldsContains("InternalId")) {
                dto.setInternalId(state.getInternalId());
            }
            if (returnedFieldsContains("PalletSscc")) {
                dto.setPalletSscc(state.getPalletSscc());
            }
            if (returnedFieldsContains("PackDate")) {
                dto.setPackDate(state.getPackDate());
            }
            if (returnedFieldsContains("HarvestDate")) {
                dto.setHarvestDate(state.getHarvestDate());
            }
            if (returnedFieldsContains("SerialNumber")) {
                dto.setSerialNumber(state.getSerialNumber());
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
            if (returnedFieldsContains("LotIdentifications")) {
                ArrayList<LotIdentificationStateDto> arrayList = new ArrayList();
                if (state.getLotIdentifications() != null) {
                    LotIdentificationStateDto.DtoConverter conv = new LotIdentificationStateDto.DtoConverter();
                    String returnFS = CollectionUtils.mapGetValueIgnoringCase(getReturnedFields(), "LotIdentifications");
                    if(returnFS != null) { conv.setReturnedFieldsString(returnFS); } else { conv.setAllFieldsReturned(this.getAllFieldsReturned()); }
                    for (LotIdentificationState s : state.getLotIdentifications()) {
                        arrayList.add(conv.toLotIdentificationStateDto(s));
                    }
                }
                dto.setLotIdentifications(arrayList.toArray(new LotIdentificationStateDto[0]));
            }
            return dto;
        }

    }
}

