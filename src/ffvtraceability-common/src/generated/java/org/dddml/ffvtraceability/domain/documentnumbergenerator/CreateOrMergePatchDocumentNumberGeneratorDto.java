// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.documentnumbergenerator;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public class CreateOrMergePatchDocumentNumberGeneratorDto extends AbstractDocumentNumberGeneratorCommandDto implements DocumentNumberGeneratorCommand.CreateOrMergePatchDocumentNumberGenerator {

    /**
     * Document number prefix, e.g. ASN
     */
    private String prefix;

    public String getPrefix()
    {
        return this.prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * Date format pattern, e.g. yyyyMMdd
     */
    private String dateFormat;

    public String getDateFormat()
    {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat)
    {
        this.dateFormat = dateFormat;
    }

    /**
     * Time zone ID, e.g. Asia/Shanghai
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
     * Length of the sequence number part, e.g. 5 means 5 digits
     */
    private Long sequenceLength;

    public Long getSequenceLength()
    {
        return this.sequenceLength;
    }

    public void setSequenceLength(Long sequenceLength)
    {
        this.sequenceLength = sequenceLength;
    }

    /**
     * The date when the last number was generated
     */
    private String lastGeneratedDate;

    public String getLastGeneratedDate()
    {
        return this.lastGeneratedDate;
    }

    public void setLastGeneratedDate(String lastGeneratedDate)
    {
        this.lastGeneratedDate = lastGeneratedDate;
    }

    /**
     * Current sequence number
     */
    private Long currentSequence;

    public Long getCurrentSequence()
    {
        return this.currentSequence;
    }

    public void setCurrentSequence(Long currentSequence)
    {
        this.currentSequence = currentSequence;
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


    private Boolean isPropertyPrefixRemoved;

    public Boolean getIsPropertyPrefixRemoved()
    {
        return this.isPropertyPrefixRemoved;
    }

    public void setIsPropertyPrefixRemoved(Boolean removed)
    {
        this.isPropertyPrefixRemoved = removed;
    }

    private Boolean isPropertyDateFormatRemoved;

    public Boolean getIsPropertyDateFormatRemoved()
    {
        return this.isPropertyDateFormatRemoved;
    }

    public void setIsPropertyDateFormatRemoved(Boolean removed)
    {
        this.isPropertyDateFormatRemoved = removed;
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

    private Boolean isPropertySequenceLengthRemoved;

    public Boolean getIsPropertySequenceLengthRemoved()
    {
        return this.isPropertySequenceLengthRemoved;
    }

    public void setIsPropertySequenceLengthRemoved(Boolean removed)
    {
        this.isPropertySequenceLengthRemoved = removed;
    }

    private Boolean isPropertyLastGeneratedDateRemoved;

    public Boolean getIsPropertyLastGeneratedDateRemoved()
    {
        return this.isPropertyLastGeneratedDateRemoved;
    }

    public void setIsPropertyLastGeneratedDateRemoved(Boolean removed)
    {
        this.isPropertyLastGeneratedDateRemoved = removed;
    }

    private Boolean isPropertyCurrentSequenceRemoved;

    public Boolean getIsPropertyCurrentSequenceRemoved()
    {
        return this.isPropertyCurrentSequenceRemoved;
    }

    public void setIsPropertyCurrentSequenceRemoved(Boolean removed)
    {
        this.isPropertyCurrentSequenceRemoved = removed;
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

    public DocumentNumberGeneratorCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            return toCreateDocumentNumberGenerator();
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            return toMergePatchDocumentNumberGenerator();
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }


    public DocumentNumberGeneratorCommand toSubclass() {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType()) || null == getCommandType()) {
            return toCreateDocumentNumberGenerator();
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            return toMergePatchDocumentNumberGenerator();
        } 
        throw new UnsupportedOperationException("Unknown command type:" + getCommandType());
    }

    protected DocumentNumberGeneratorCommand.CreateDocumentNumberGenerator toCreateDocumentNumberGenerator() {
        return new DocumentNumberGeneratorCommand.CreateDocumentNumberGenerator() {
            @Override
            public String getGeneratorId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getGeneratorId();
            }

            @Override
            public void setGeneratorId(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setGeneratorId(p);
            }

            @Override
            public Long getVersion() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 CreateOrMergePatchDocumentNumberGeneratorDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCommandContext();
            }

            @Override
            public String getPrefix() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getPrefix();
            }

            @Override
            public void setPrefix(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setPrefix(p);
            }

            @Override
            public String getDateFormat() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getDateFormat();
            }

            @Override
            public void setDateFormat(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setDateFormat(p);
            }

            @Override
            public String getTimeZoneId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getTimeZoneId();
            }

            @Override
            public void setTimeZoneId(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setTimeZoneId(p);
            }

            @Override
            public Long getSequenceLength() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getSequenceLength();
            }

            @Override
            public void setSequenceLength(Long p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setSequenceLength(p);
            }

            @Override
            public String getLastGeneratedDate() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getLastGeneratedDate();
            }

            @Override
            public void setLastGeneratedDate(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setLastGeneratedDate(p);
            }

            @Override
            public Long getCurrentSequence() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCurrentSequence();
            }

            @Override
            public void setCurrentSequence(Long p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setCurrentSequence(p);
            }

            @Override
            public String getDescription() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getDescription();
            }

            @Override
            public void setDescription(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setDescription(p);
            }


        };
    }

    protected DocumentNumberGeneratorCommand.MergePatchDocumentNumberGenerator toMergePatchDocumentNumberGenerator() {
        return new DocumentNumberGeneratorCommand.MergePatchDocumentNumberGenerator() {
            @Override
            public String getGeneratorId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getGeneratorId();
            }

            @Override
            public void setGeneratorId(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setGeneratorId(p);
            }

            @Override
            public Long getVersion() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 CreateOrMergePatchDocumentNumberGeneratorDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCommandContext();
            }

            @Override
            public String getPrefix() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getPrefix();
            }

            @Override
            public void setPrefix(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setPrefix(p);
            }

            @Override
            public String getDateFormat() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getDateFormat();
            }

            @Override
            public void setDateFormat(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setDateFormat(p);
            }

            @Override
            public String getTimeZoneId() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getTimeZoneId();
            }

            @Override
            public void setTimeZoneId(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setTimeZoneId(p);
            }

            @Override
            public Long getSequenceLength() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getSequenceLength();
            }

            @Override
            public void setSequenceLength(Long p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setSequenceLength(p);
            }

            @Override
            public String getLastGeneratedDate() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getLastGeneratedDate();
            }

            @Override
            public void setLastGeneratedDate(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setLastGeneratedDate(p);
            }

            @Override
            public Long getCurrentSequence() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getCurrentSequence();
            }

            @Override
            public void setCurrentSequence(Long p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setCurrentSequence(p);
            }

            @Override
            public String getDescription() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getDescription();
            }

            @Override
            public void setDescription(String p) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setDescription(p);
            }

            @Override
            public Boolean getIsPropertyPrefixRemoved() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getIsPropertyPrefixRemoved();
            }

            @Override
            public void setIsPropertyPrefixRemoved(Boolean removed) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setIsPropertyPrefixRemoved(removed);
            }

            @Override
            public Boolean getIsPropertyDateFormatRemoved() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getIsPropertyDateFormatRemoved();
            }

            @Override
            public void setIsPropertyDateFormatRemoved(Boolean removed) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setIsPropertyDateFormatRemoved(removed);
            }

            @Override
            public Boolean getIsPropertyTimeZoneIdRemoved() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getIsPropertyTimeZoneIdRemoved();
            }

            @Override
            public void setIsPropertyTimeZoneIdRemoved(Boolean removed) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setIsPropertyTimeZoneIdRemoved(removed);
            }

            @Override
            public Boolean getIsPropertySequenceLengthRemoved() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getIsPropertySequenceLengthRemoved();
            }

            @Override
            public void setIsPropertySequenceLengthRemoved(Boolean removed) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setIsPropertySequenceLengthRemoved(removed);
            }

            @Override
            public Boolean getIsPropertyLastGeneratedDateRemoved() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getIsPropertyLastGeneratedDateRemoved();
            }

            @Override
            public void setIsPropertyLastGeneratedDateRemoved(Boolean removed) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setIsPropertyLastGeneratedDateRemoved(removed);
            }

            @Override
            public Boolean getIsPropertyCurrentSequenceRemoved() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getIsPropertyCurrentSequenceRemoved();
            }

            @Override
            public void setIsPropertyCurrentSequenceRemoved(Boolean removed) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setIsPropertyCurrentSequenceRemoved(removed);
            }

            @Override
            public Boolean getIsPropertyDescriptionRemoved() {
                return CreateOrMergePatchDocumentNumberGeneratorDto.this.getIsPropertyDescriptionRemoved();
            }

            @Override
            public void setIsPropertyDescriptionRemoved(Boolean removed) {
                CreateOrMergePatchDocumentNumberGeneratorDto.this.setIsPropertyDescriptionRemoved(removed);
            }


        };
    }

    public static class CreateDocumentNumberGeneratorDto extends CreateOrMergePatchDocumentNumberGeneratorDto implements DocumentNumberGeneratorCommand.CreateDocumentNumberGenerator
    {
        public CreateDocumentNumberGeneratorDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public DocumentNumberGeneratorCommand.CreateDocumentNumberGenerator toCreateDocumentNumberGenerator()
        {
            return super.toCreateDocumentNumberGenerator();
        }

    }

    public static class MergePatchDocumentNumberGeneratorDto extends CreateOrMergePatchDocumentNumberGeneratorDto implements DocumentNumberGeneratorCommand.MergePatchDocumentNumberGenerator
    {
        public MergePatchDocumentNumberGeneratorDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public DocumentNumberGeneratorCommand.MergePatchDocumentNumberGenerator toMergePatchDocumentNumberGenerator()
        {
            return super.toMergePatchDocumentNumberGenerator();
        }

    }

}

