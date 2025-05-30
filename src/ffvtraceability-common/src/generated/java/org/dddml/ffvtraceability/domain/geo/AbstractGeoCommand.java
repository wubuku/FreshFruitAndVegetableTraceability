// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.geo;

import java.util.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractGeoCommand extends AbstractCommand implements GeoCommand {

    private String geoId;

    public String getGeoId()
    {
        return this.geoId;
    }

    public void setGeoId(String geoId)
    {
        this.geoId = geoId;
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


    public static abstract class AbstractCreateOrMergePatchGeo extends AbstractGeoCommand implements CreateOrMergePatchGeo
    {
        private String geoTypeId;

        public String getGeoTypeId()
        {
            return this.geoTypeId;
        }

        public void setGeoTypeId(String geoTypeId)
        {
            this.geoTypeId = geoTypeId;
        }

        private String geoName;

        public String getGeoName()
        {
            return this.geoName;
        }

        public void setGeoName(String geoName)
        {
            this.geoName = geoName;
        }

        private String geoCode;

        public String getGeoCode()
        {
            return this.geoCode;
        }

        public void setGeoCode(String geoCode)
        {
            this.geoCode = geoCode;
        }

        private String geoSecCode;

        public String getGeoSecCode()
        {
            return this.geoSecCode;
        }

        public void setGeoSecCode(String geoSecCode)
        {
            this.geoSecCode = geoSecCode;
        }

        private String abbreviation;

        public String getAbbreviation()
        {
            return this.abbreviation;
        }

        public void setAbbreviation(String abbreviation)
        {
            this.abbreviation = abbreviation;
        }

        private String wellKnownText;

        public String getWellKnownText()
        {
            return this.wellKnownText;
        }

        public void setWellKnownText(String wellKnownText)
        {
            this.wellKnownText = wellKnownText;
        }

        private Long sequenceNumber;

        public Long getSequenceNumber()
        {
            return this.sequenceNumber;
        }

        public void setSequenceNumber(Long sequenceNumber)
        {
            this.sequenceNumber = sequenceNumber;
        }

    }

    public static abstract class AbstractCreateGeo extends AbstractCreateOrMergePatchGeo implements CreateGeo
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchGeo extends AbstractCreateOrMergePatchGeo implements MergePatchGeo
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

        private Boolean isPropertyGeoTypeIdRemoved;

        public Boolean getIsPropertyGeoTypeIdRemoved()
        {
            return this.isPropertyGeoTypeIdRemoved;
        }

        public void setIsPropertyGeoTypeIdRemoved(Boolean removed)
        {
            this.isPropertyGeoTypeIdRemoved = removed;
        }

        private Boolean isPropertyGeoNameRemoved;

        public Boolean getIsPropertyGeoNameRemoved()
        {
            return this.isPropertyGeoNameRemoved;
        }

        public void setIsPropertyGeoNameRemoved(Boolean removed)
        {
            this.isPropertyGeoNameRemoved = removed;
        }

        private Boolean isPropertyGeoCodeRemoved;

        public Boolean getIsPropertyGeoCodeRemoved()
        {
            return this.isPropertyGeoCodeRemoved;
        }

        public void setIsPropertyGeoCodeRemoved(Boolean removed)
        {
            this.isPropertyGeoCodeRemoved = removed;
        }

        private Boolean isPropertyGeoSecCodeRemoved;

        public Boolean getIsPropertyGeoSecCodeRemoved()
        {
            return this.isPropertyGeoSecCodeRemoved;
        }

        public void setIsPropertyGeoSecCodeRemoved(Boolean removed)
        {
            this.isPropertyGeoSecCodeRemoved = removed;
        }

        private Boolean isPropertyAbbreviationRemoved;

        public Boolean getIsPropertyAbbreviationRemoved()
        {
            return this.isPropertyAbbreviationRemoved;
        }

        public void setIsPropertyAbbreviationRemoved(Boolean removed)
        {
            this.isPropertyAbbreviationRemoved = removed;
        }

        private Boolean isPropertyWellKnownTextRemoved;

        public Boolean getIsPropertyWellKnownTextRemoved()
        {
            return this.isPropertyWellKnownTextRemoved;
        }

        public void setIsPropertyWellKnownTextRemoved(Boolean removed)
        {
            this.isPropertyWellKnownTextRemoved = removed;
        }

        private Boolean isPropertySequenceNumberRemoved;

        public Boolean getIsPropertySequenceNumberRemoved()
        {
            return this.isPropertySequenceNumberRemoved;
        }

        public void setIsPropertySequenceNumberRemoved(Boolean removed)
        {
            this.isPropertySequenceNumberRemoved = removed;
        }


    }

    public static class SimpleCreateGeo extends AbstractCreateGeo
    {
    }

    
    public static class SimpleMergePatchGeo extends AbstractMergePatchGeo
    {
    }

    
    public static class SimpleDeleteGeo extends AbstractGeoCommand implements DeleteGeo
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_DELETE;
        }
    }

    

}

