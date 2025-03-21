// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.enumeration;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchEnumerationDto extends AbstractEnumerationCommandDto implements EnumerationCommand.CreateOrMergePatchEnumeration {

    /**
     * Enum Type Id
     */
    private String enumTypeId;

    public String getEnumTypeId()
    {
        return this.enumTypeId;
    }

    public void setEnumTypeId(String enumTypeId)
    {
        this.enumTypeId = enumTypeId;
    }

    /**
     * Enum Code
     */
    private String enumCode;

    public String getEnumCode()
    {
        return this.enumCode;
    }

    public void setEnumCode(String enumCode)
    {
        this.enumCode = enumCode;
    }

    /**
     * Sequence Id
     */
    private String sequenceId;

    public String getSequenceId()
    {
        return this.sequenceId;
    }

    public void setSequenceId(String sequenceId)
    {
        this.sequenceId = sequenceId;
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


    private Boolean isPropertyEnumTypeIdRemoved;

    public Boolean getIsPropertyEnumTypeIdRemoved()
    {
        return this.isPropertyEnumTypeIdRemoved;
    }

    public void setIsPropertyEnumTypeIdRemoved(Boolean removed)
    {
        this.isPropertyEnumTypeIdRemoved = removed;
    }

    private Boolean isPropertyEnumCodeRemoved;

    public Boolean getIsPropertyEnumCodeRemoved()
    {
        return this.isPropertyEnumCodeRemoved;
    }

    public void setIsPropertyEnumCodeRemoved(Boolean removed)
    {
        this.isPropertyEnumCodeRemoved = removed;
    }

    private Boolean isPropertySequenceIdRemoved;

    public Boolean getIsPropertySequenceIdRemoved()
    {
        return this.isPropertySequenceIdRemoved;
    }

    public void setIsPropertySequenceIdRemoved(Boolean removed)
    {
        this.isPropertySequenceIdRemoved = removed;
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

    public EnumerationCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            return toCreateEnumeration();
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            return toMergePatchEnumeration();
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public EnumerationCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            return toCreateEnumeration();
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            return toMergePatchEnumeration();
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    protected EnumerationCommand.CreateEnumeration toCreateEnumeration() {
        return new EnumerationCommand.CreateEnumeration() {
            @Override
            public String getEnumId() {
                return CreateOrMergePatchEnumerationDto.this.getEnumId();
            }

            @Override
            public void setEnumId(String p) {
                CreateOrMergePatchEnumerationDto.this.setEnumId(p);
            }

            @Override
            public Long getVersion() {
                return CreateOrMergePatchEnumerationDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                CreateOrMergePatchEnumerationDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return CreateOrMergePatchEnumerationDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 CreateOrMergePatchEnumerationDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return CreateOrMergePatchEnumerationDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                CreateOrMergePatchEnumerationDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return CreateOrMergePatchEnumerationDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                CreateOrMergePatchEnumerationDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return CreateOrMergePatchEnumerationDto.this.getCommandContext();
            }

            @Override
            public String getEnumTypeId() {
                return CreateOrMergePatchEnumerationDto.this.getEnumTypeId();
            }

            @Override
            public void setEnumTypeId(String p) {
                CreateOrMergePatchEnumerationDto.this.setEnumTypeId(p);
            }

            @Override
            public String getEnumCode() {
                return CreateOrMergePatchEnumerationDto.this.getEnumCode();
            }

            @Override
            public void setEnumCode(String p) {
                CreateOrMergePatchEnumerationDto.this.setEnumCode(p);
            }

            @Override
            public String getSequenceId() {
                return CreateOrMergePatchEnumerationDto.this.getSequenceId();
            }

            @Override
            public void setSequenceId(String p) {
                CreateOrMergePatchEnumerationDto.this.setSequenceId(p);
            }

            @Override
            public String getDescription() {
                return CreateOrMergePatchEnumerationDto.this.getDescription();
            }

            @Override
            public void setDescription(String p) {
                CreateOrMergePatchEnumerationDto.this.setDescription(p);
            }


        };
    }

    protected EnumerationCommand.MergePatchEnumeration toMergePatchEnumeration() {
        return new EnumerationCommand.MergePatchEnumeration() {
            @Override
            public String getEnumId() {
                return CreateOrMergePatchEnumerationDto.this.getEnumId();
            }

            @Override
            public void setEnumId(String p) {
                CreateOrMergePatchEnumerationDto.this.setEnumId(p);
            }

            @Override
            public Long getVersion() {
                return CreateOrMergePatchEnumerationDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                CreateOrMergePatchEnumerationDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return CreateOrMergePatchEnumerationDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 CreateOrMergePatchEnumerationDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return CreateOrMergePatchEnumerationDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                CreateOrMergePatchEnumerationDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return CreateOrMergePatchEnumerationDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                CreateOrMergePatchEnumerationDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return CreateOrMergePatchEnumerationDto.this.getCommandContext();
            }

            @Override
            public String getEnumTypeId() {
                return CreateOrMergePatchEnumerationDto.this.getEnumTypeId();
            }

            @Override
            public void setEnumTypeId(String p) {
                CreateOrMergePatchEnumerationDto.this.setEnumTypeId(p);
            }

            @Override
            public String getEnumCode() {
                return CreateOrMergePatchEnumerationDto.this.getEnumCode();
            }

            @Override
            public void setEnumCode(String p) {
                CreateOrMergePatchEnumerationDto.this.setEnumCode(p);
            }

            @Override
            public String getSequenceId() {
                return CreateOrMergePatchEnumerationDto.this.getSequenceId();
            }

            @Override
            public void setSequenceId(String p) {
                CreateOrMergePatchEnumerationDto.this.setSequenceId(p);
            }

            @Override
            public String getDescription() {
                return CreateOrMergePatchEnumerationDto.this.getDescription();
            }

            @Override
            public void setDescription(String p) {
                CreateOrMergePatchEnumerationDto.this.setDescription(p);
            }

            @Override
            public Boolean getIsPropertyEnumTypeIdRemoved() {
                return CreateOrMergePatchEnumerationDto.this.getIsPropertyEnumTypeIdRemoved();
            }

            @Override
            public void setIsPropertyEnumTypeIdRemoved(Boolean removed) {
                CreateOrMergePatchEnumerationDto.this.setIsPropertyEnumTypeIdRemoved(removed);
            }

            @Override
            public Boolean getIsPropertyEnumCodeRemoved() {
                return CreateOrMergePatchEnumerationDto.this.getIsPropertyEnumCodeRemoved();
            }

            @Override
            public void setIsPropertyEnumCodeRemoved(Boolean removed) {
                CreateOrMergePatchEnumerationDto.this.setIsPropertyEnumCodeRemoved(removed);
            }

            @Override
            public Boolean getIsPropertySequenceIdRemoved() {
                return CreateOrMergePatchEnumerationDto.this.getIsPropertySequenceIdRemoved();
            }

            @Override
            public void setIsPropertySequenceIdRemoved(Boolean removed) {
                CreateOrMergePatchEnumerationDto.this.setIsPropertySequenceIdRemoved(removed);
            }

            @Override
            public Boolean getIsPropertyDescriptionRemoved() {
                return CreateOrMergePatchEnumerationDto.this.getIsPropertyDescriptionRemoved();
            }

            @Override
            public void setIsPropertyDescriptionRemoved(Boolean removed) {
                CreateOrMergePatchEnumerationDto.this.setIsPropertyDescriptionRemoved(removed);
            }


        };
    }

    public static class CreateEnumerationDto extends CreateOrMergePatchEnumerationDto implements EnumerationCommand.CreateEnumeration
    {
        public CreateEnumerationDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public EnumerationCommand.CreateEnumeration toCreateEnumeration()
        {
            return super.toCreateEnumeration();
        }

    }

    public static class MergePatchEnumerationDto extends CreateOrMergePatchEnumerationDto implements EnumerationCommand.MergePatchEnumeration
    {
        public MergePatchEnumerationDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public EnumerationCommand.MergePatchEnumeration toMergePatchEnumeration()
        {
            return super.toMergePatchEnumeration();
        }

    }

}

