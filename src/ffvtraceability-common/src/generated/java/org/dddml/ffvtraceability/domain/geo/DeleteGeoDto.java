// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.geo;


public class DeleteGeoDto extends AbstractGeoCommandDto implements GeoCommand.DeleteGeo {

    public DeleteGeoDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public GeoCommand.DeleteGeo toDeleteGeo()
    {
        return new GeoCommand.DeleteGeo() {
            @Override
            public String getGeoId() {
                return DeleteGeoDto.this.getGeoId();
            }

            @Override
            public void setGeoId(String p) {
                DeleteGeoDto.this.setGeoId(p);
            }

            @Override
            public Long getVersion() {
                return DeleteGeoDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                DeleteGeoDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return DeleteGeoDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 DeleteGeoDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return DeleteGeoDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                DeleteGeoDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return DeleteGeoDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                DeleteGeoDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return DeleteGeoDto.this.getCommandContext();
            }


        };
    }
}
