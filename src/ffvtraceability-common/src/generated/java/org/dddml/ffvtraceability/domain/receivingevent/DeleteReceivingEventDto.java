// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.receivingevent;


public class DeleteReceivingEventDto extends AbstractReceivingEventCommandDto implements ReceivingEventCommand.DeleteReceivingEvent {

    public DeleteReceivingEventDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public ReceivingEventCommand.DeleteReceivingEvent toDeleteReceivingEvent()
    {
        return new ReceivingEventCommand.DeleteReceivingEvent() {
            @Override
            public String getEventId() {
                return DeleteReceivingEventDto.this.getEventId();
            }

            @Override
            public void setEventId(String p) {
                DeleteReceivingEventDto.this.setEventId(p);
            }

            @Override
            public Long getVersion() {
                return DeleteReceivingEventDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                DeleteReceivingEventDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return DeleteReceivingEventDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 DeleteReceivingEventDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return DeleteReceivingEventDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                DeleteReceivingEventDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return DeleteReceivingEventDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                DeleteReceivingEventDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return DeleteReceivingEventDto.this.getCommandContext();
            }


        };
    }
}

