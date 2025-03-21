// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.workeffortinventoryproduced;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class WorkEffortInventoryProducedStateDto {

    private WorkEffortInventoryProducedId workEffortInventoryProducedId;

    public WorkEffortInventoryProducedId getWorkEffortInventoryProducedId()
    {
        return this.workEffortInventoryProducedId;
    }

    public void setWorkEffortInventoryProducedId(WorkEffortInventoryProducedId workEffortInventoryProducedId)
    {
        this.workEffortInventoryProducedId = workEffortInventoryProducedId;
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

        public WorkEffortInventoryProducedStateDto[] toWorkEffortInventoryProducedStateDtoArray(Iterable<WorkEffortInventoryProducedState> states) {
            return toWorkEffortInventoryProducedStateDtoList(states).toArray(new WorkEffortInventoryProducedStateDto[0]);
        }

        public List<WorkEffortInventoryProducedStateDto> toWorkEffortInventoryProducedStateDtoList(Iterable<WorkEffortInventoryProducedState> states) {
            ArrayList<WorkEffortInventoryProducedStateDto> stateDtos = new ArrayList();
            for (WorkEffortInventoryProducedState s : states) {
                WorkEffortInventoryProducedStateDto dto = toWorkEffortInventoryProducedStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public WorkEffortInventoryProducedStateDto toWorkEffortInventoryProducedStateDto(WorkEffortInventoryProducedState state)
        {
            if(state == null) {
                return null;
            }
            WorkEffortInventoryProducedStateDto dto = new WorkEffortInventoryProducedStateDto();
            if (returnedFieldsContains("WorkEffortInventoryProducedId")) {
                dto.setWorkEffortInventoryProducedId(state.getWorkEffortInventoryProducedId());
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

