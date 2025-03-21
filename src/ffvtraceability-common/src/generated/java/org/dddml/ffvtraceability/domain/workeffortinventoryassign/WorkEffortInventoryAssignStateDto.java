// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortinventoryassign;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class WorkEffortInventoryAssignStateDto {

    private WorkEffortInventoryAssignId workEffortInventoryAssignId;

    public WorkEffortInventoryAssignId getWorkEffortInventoryAssignId()
    {
        return this.workEffortInventoryAssignId;
    }

    public void setWorkEffortInventoryAssignId(WorkEffortInventoryAssignId workEffortInventoryAssignId)
    {
        this.workEffortInventoryAssignId = workEffortInventoryAssignId;
    }

    private String statusId;

    public String getStatusId()
    {
        return this.statusId;
    }

    public void setStatusId(String statusId)
    {
        this.statusId = statusId;
    }

    private Double quantity;

    public Double getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(Double quantity)
    {
        this.quantity = quantity;
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


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public WorkEffortInventoryAssignStateDto[] toWorkEffortInventoryAssignStateDtoArray(Iterable<WorkEffortInventoryAssignState> states) {
            return toWorkEffortInventoryAssignStateDtoList(states).toArray(new WorkEffortInventoryAssignStateDto[0]);
        }

        public List<WorkEffortInventoryAssignStateDto> toWorkEffortInventoryAssignStateDtoList(Iterable<WorkEffortInventoryAssignState> states) {
            ArrayList<WorkEffortInventoryAssignStateDto> stateDtos = new ArrayList();
            for (WorkEffortInventoryAssignState s : states) {
                WorkEffortInventoryAssignStateDto dto = toWorkEffortInventoryAssignStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public WorkEffortInventoryAssignStateDto toWorkEffortInventoryAssignStateDto(WorkEffortInventoryAssignState state)
        {
            if(state == null) {
                return null;
            }
            WorkEffortInventoryAssignStateDto dto = new WorkEffortInventoryAssignStateDto();
            if (returnedFieldsContains("WorkEffortInventoryAssignId")) {
                dto.setWorkEffortInventoryAssignId(state.getWorkEffortInventoryAssignId());
            }
            if (returnedFieldsContains("StatusId")) {
                dto.setStatusId(state.getStatusId());
            }
            if (returnedFieldsContains("Quantity")) {
                dto.setQuantity(state.getQuantity());
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
            return dto;
        }

    }
}

