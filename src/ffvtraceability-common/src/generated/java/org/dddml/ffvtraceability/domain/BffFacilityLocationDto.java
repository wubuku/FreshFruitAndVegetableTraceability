// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain;

import java.io.Serializable;
import org.dddml.ffvtraceability.domain.*;

public class BffFacilityLocationDto implements Serializable {
    private String locationSeqId;

    public String getLocationSeqId()
    {
        return this.locationSeqId;
    }

    public void setLocationSeqId(String locationSeqId)
    {
        this.locationSeqId = locationSeqId;
    }

    private String locationTypeEnumId;

    public String getLocationTypeEnumId()
    {
        return this.locationTypeEnumId;
    }

    public void setLocationTypeEnumId(String locationTypeEnumId)
    {
        this.locationTypeEnumId = locationTypeEnumId;
    }

    private String areaId;

    public String getAreaId()
    {
        return this.areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    private String aisleId;

    public String getAisleId()
    {
        return this.aisleId;
    }

    public void setAisleId(String aisleId)
    {
        this.aisleId = aisleId;
    }

    private String sectionId;

    public String getSectionId()
    {
        return this.sectionId;
    }

    public void setSectionId(String sectionId)
    {
        this.sectionId = sectionId;
    }

    private String levelId;

    public String getLevelId()
    {
        return this.levelId;
    }

    public void setLevelId(String levelId)
    {
        this.levelId = levelId;
    }

    private String positionId;

    public String getPositionId()
    {
        return this.positionId;
    }

    public void setPositionId(String positionId)
    {
        this.positionId = positionId;
    }

    private String geoPointId;

    public String getGeoPointId()
    {
        return this.geoPointId;
    }

    public void setGeoPointId(String geoPointId)
    {
        this.geoPointId = geoPointId;
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

    public BffFacilityLocationDto()
    {
    }

    public BffFacilityLocationDto(String locationSeqId, String locationTypeEnumId, String areaId, String aisleId, String sectionId, String levelId, String positionId, String geoPointId, String active)
    {
        this.locationSeqId = locationSeqId;
        this.locationTypeEnumId = locationTypeEnumId;
        this.areaId = areaId;
        this.aisleId = aisleId;
        this.sectionId = sectionId;
        this.levelId = levelId;
        this.positionId = positionId;
        this.geoPointId = geoPointId;
        this.active = active;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        BffFacilityLocationDto other = (BffFacilityLocationDto)obj;
        return true 
            && (locationSeqId == other.locationSeqId || (locationSeqId != null && locationSeqId.equals(other.locationSeqId)))
            && (locationTypeEnumId == other.locationTypeEnumId || (locationTypeEnumId != null && locationTypeEnumId.equals(other.locationTypeEnumId)))
            && (areaId == other.areaId || (areaId != null && areaId.equals(other.areaId)))
            && (aisleId == other.aisleId || (aisleId != null && aisleId.equals(other.aisleId)))
            && (sectionId == other.sectionId || (sectionId != null && sectionId.equals(other.sectionId)))
            && (levelId == other.levelId || (levelId != null && levelId.equals(other.levelId)))
            && (positionId == other.positionId || (positionId != null && positionId.equals(other.positionId)))
            && (geoPointId == other.geoPointId || (geoPointId != null && geoPointId.equals(other.geoPointId)))
            && (active == other.active || (active != null && active.equals(other.active)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.locationSeqId != null) {
            hash += 13 * this.locationSeqId.hashCode();
        }
        if (this.locationTypeEnumId != null) {
            hash += 13 * this.locationTypeEnumId.hashCode();
        }
        if (this.areaId != null) {
            hash += 13 * this.areaId.hashCode();
        }
        if (this.aisleId != null) {
            hash += 13 * this.aisleId.hashCode();
        }
        if (this.sectionId != null) {
            hash += 13 * this.sectionId.hashCode();
        }
        if (this.levelId != null) {
            hash += 13 * this.levelId.hashCode();
        }
        if (this.positionId != null) {
            hash += 13 * this.positionId.hashCode();
        }
        if (this.geoPointId != null) {
            hash += 13 * this.geoPointId.hashCode();
        }
        if (this.active != null) {
            hash += 13 * this.active.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "BffFacilityLocationDto{" +
                "locationSeqId=" + '\'' + locationSeqId + '\'' +
                ", locationTypeEnumId=" + '\'' + locationTypeEnumId + '\'' +
                ", areaId=" + '\'' + areaId + '\'' +
                ", aisleId=" + '\'' + aisleId + '\'' +
                ", sectionId=" + '\'' + sectionId + '\'' +
                ", levelId=" + '\'' + levelId + '\'' +
                ", positionId=" + '\'' + positionId + '\'' +
                ", geoPointId=" + '\'' + geoPointId + '\'' +
                ", active=" + '\'' + active + '\'' +
                '}';
    }


}

