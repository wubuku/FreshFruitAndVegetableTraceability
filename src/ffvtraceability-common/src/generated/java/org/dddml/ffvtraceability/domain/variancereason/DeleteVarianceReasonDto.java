// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.variancereason;


public class DeleteVarianceReasonDto extends AbstractVarianceReasonCommandDto implements VarianceReasonCommand.DeleteVarianceReason {

    public DeleteVarianceReasonDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public VarianceReasonCommand.DeleteVarianceReason toDeleteVarianceReason()
    {
        return new VarianceReasonCommand.DeleteVarianceReason() {
            @Override
            public String getVarianceReasonId() {
                return DeleteVarianceReasonDto.this.getVarianceReasonId();
            }

            @Override
            public void setVarianceReasonId(String p) {
                DeleteVarianceReasonDto.this.setVarianceReasonId(p);
            }

            @Override
            public Long getVersion() {
                return DeleteVarianceReasonDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                DeleteVarianceReasonDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return DeleteVarianceReasonDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 DeleteVarianceReasonDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return DeleteVarianceReasonDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                DeleteVarianceReasonDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return DeleteVarianceReasonDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                DeleteVarianceReasonDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return DeleteVarianceReasonDto.this.getCommandContext();
            }


        };
    }
}

