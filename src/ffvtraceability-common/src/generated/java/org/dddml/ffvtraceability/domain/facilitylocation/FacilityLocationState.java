// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.facilitylocation;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;

public interface FacilityLocationState
{
    Long VERSION_ZERO = 0L;

    Long VERSION_NULL = VERSION_ZERO - 1;

    FacilityLocationId getFacilityLocationId();

    String getLocationTypeEnumId();

    String getAreaId();

    String getAisleId();

    String getSectionId();

    String getLevelId();

    String getPositionId();

    String getGeoPointId();

    String getActive();

    Long getVersion();

    String getCreatedBy();

    OffsetDateTime getCreatedAt();

    String getUpdatedBy();

    OffsetDateTime getUpdatedAt();

    String getCommandId();

    interface MutableFacilityLocationState extends FacilityLocationState {
        void setFacilityLocationId(FacilityLocationId facilityLocationId);

        void setLocationTypeEnumId(String locationTypeEnumId);

        void setAreaId(String areaId);

        void setAisleId(String aisleId);

        void setSectionId(String sectionId);

        void setLevelId(String levelId);

        void setPositionId(String positionId);

        void setGeoPointId(String geoPointId);

        void setActive(String active);

        void setVersion(Long version);

        void setCreatedBy(String createdBy);

        void setCreatedAt(OffsetDateTime createdAt);

        void setUpdatedBy(String updatedBy);

        void setUpdatedAt(OffsetDateTime updatedAt);

        void setCommandId(String commandId);

    }

    interface SqlFacilityLocationState extends MutableFacilityLocationState {

        boolean isStateUnsaved();

        boolean getForReapplying();
    }
}
