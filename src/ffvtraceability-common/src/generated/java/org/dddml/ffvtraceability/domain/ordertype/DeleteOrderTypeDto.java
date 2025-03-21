// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.ordertype;


public class DeleteOrderTypeDto extends AbstractOrderTypeCommandDto implements OrderTypeCommand.DeleteOrderType {

    public DeleteOrderTypeDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public OrderTypeCommand.DeleteOrderType toDeleteOrderType()
    {
        return new OrderTypeCommand.DeleteOrderType() {
            @Override
            public String getOrderTypeId() {
                return DeleteOrderTypeDto.this.getOrderTypeId();
            }

            @Override
            public void setOrderTypeId(String p) {
                DeleteOrderTypeDto.this.setOrderTypeId(p);
            }

            @Override
            public Long getVersion() {
                return DeleteOrderTypeDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                DeleteOrderTypeDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return DeleteOrderTypeDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 DeleteOrderTypeDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return DeleteOrderTypeDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                DeleteOrderTypeDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return DeleteOrderTypeDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                DeleteOrderTypeDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return DeleteOrderTypeDto.this.getCommandContext();
            }


        };
    }
}

