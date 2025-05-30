// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.tenant;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchTenantDto extends AbstractTenantCommandDto implements TenantCommand.CreateOrMergePatchTenant {

    /**
     * Party Id
     */
    private String partyId;

    public String getPartyId()
    {
        return this.partyId;
    }

    public void setPartyId(String partyId)
    {
        this.partyId = partyId;
    }

    /**
     * Time Zone Id
     */
    private String timeZoneId;

    public String getTimeZoneId()
    {
        return this.timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId)
    {
        this.timeZoneId = timeZoneId;
    }

    /**
     * Date Time Format
     */
    private String dateTimeFormat;

    public String getDateTimeFormat()
    {
        return this.dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat)
    {
        this.dateTimeFormat = dateTimeFormat;
    }

    /**
     * Description
     */
    private String description;

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Long Description
     */
    private String longDescription;

    public String getLongDescription()
    {
        return this.longDescription;
    }

    public void setLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }


    private Boolean isPropertyPartyIdRemoved;

    public Boolean getIsPropertyPartyIdRemoved()
    {
        return this.isPropertyPartyIdRemoved;
    }

    public void setIsPropertyPartyIdRemoved(Boolean removed)
    {
        this.isPropertyPartyIdRemoved = removed;
    }

    private Boolean isPropertyTimeZoneIdRemoved;

    public Boolean getIsPropertyTimeZoneIdRemoved()
    {
        return this.isPropertyTimeZoneIdRemoved;
    }

    public void setIsPropertyTimeZoneIdRemoved(Boolean removed)
    {
        this.isPropertyTimeZoneIdRemoved = removed;
    }

    private Boolean isPropertyDateTimeFormatRemoved;

    public Boolean getIsPropertyDateTimeFormatRemoved()
    {
        return this.isPropertyDateTimeFormatRemoved;
    }

    public void setIsPropertyDateTimeFormatRemoved(Boolean removed)
    {
        this.isPropertyDateTimeFormatRemoved = removed;
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

    private Boolean isPropertyLongDescriptionRemoved;

    public Boolean getIsPropertyLongDescriptionRemoved()
    {
        return this.isPropertyLongDescriptionRemoved;
    }

    public void setIsPropertyLongDescriptionRemoved(Boolean removed)
    {
        this.isPropertyLongDescriptionRemoved = removed;
    }

    public void copyTo(CreateOrMergePatchTenant command)
    {
        ((AbstractTenantCommandDto) this).copyTo(command);
        command.setPartyId(this.getPartyId());
        command.setTimeZoneId(this.getTimeZoneId());
        command.setDateTimeFormat(this.getDateTimeFormat());
        command.setDescription(this.getDescription());
        command.setLongDescription(this.getLongDescription());
    }

    public TenantCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractTenantCommand.SimpleCreateTenant command = new AbstractTenantCommand.SimpleCreateTenant();
            copyTo((AbstractTenantCommand.AbstractCreateTenant) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractTenantCommand.SimpleMergePatchTenant command = new AbstractTenantCommand.SimpleMergePatchTenant();
            copyTo((AbstractTenantCommand.SimpleMergePatchTenant) command);
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public TenantCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            CreateTenantDto command = new CreateTenantDto();
            copyTo((CreateTenant) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            MergePatchTenantDto command = new MergePatchTenantDto();
            copyTo((MergePatchTenant) command);
            return command;
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    public void copyTo(CreateTenant command)
    {
        copyTo((CreateOrMergePatchTenant) command);
    }

    public void copyTo(MergePatchTenant command)
    {
        copyTo((CreateOrMergePatchTenant) command);
        command.setIsPropertyPartyIdRemoved(this.getIsPropertyPartyIdRemoved());
        command.setIsPropertyTimeZoneIdRemoved(this.getIsPropertyTimeZoneIdRemoved());
        command.setIsPropertyDateTimeFormatRemoved(this.getIsPropertyDateTimeFormatRemoved());
        command.setIsPropertyDescriptionRemoved(this.getIsPropertyDescriptionRemoved());
        command.setIsPropertyLongDescriptionRemoved(this.getIsPropertyLongDescriptionRemoved());
    }

    public static class CreateTenantDto extends CreateOrMergePatchTenantDto implements TenantCommand.CreateTenant
    {
        public CreateTenantDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public TenantCommand.CreateTenant toCreateTenant()
        {
            return (TenantCommand.CreateTenant) toCommand();
        }

    }

    public static class MergePatchTenantDto extends CreateOrMergePatchTenantDto implements TenantCommand.MergePatchTenant
    {
        public MergePatchTenantDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public TenantCommand.MergePatchTenant toMergePatchTenant()
        {
            return (TenantCommand.MergePatchTenant) toCommand();
        }

    }

}

