// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.geotype;


public class DeleteGeoTypeDto extends AbstractGeoTypeCommandDto implements GeoTypeCommand.DeleteGeoType {

    public DeleteGeoTypeDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public GeoTypeCommand.DeleteGeoType toDeleteGeoType()
    {
        return new GeoTypeCommand.DeleteGeoType() {
            @Override
            public String getGeoTypeId() {
                return DeleteGeoTypeDto.this.getGeoTypeId();
            }

            @Override
            public void setGeoTypeId(String p) {
                DeleteGeoTypeDto.this.setGeoTypeId(p);
            }

            @Override
            public Long getVersion() {
                return DeleteGeoTypeDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                DeleteGeoTypeDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return DeleteGeoTypeDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 DeleteGeoTypeDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return DeleteGeoTypeDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                DeleteGeoTypeDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return DeleteGeoTypeDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                DeleteGeoTypeDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return DeleteGeoTypeDto.this.getCommandContext();
            }


        };
    }
}

