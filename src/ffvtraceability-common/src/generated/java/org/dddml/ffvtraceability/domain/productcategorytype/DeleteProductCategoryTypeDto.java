// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productcategorytype;


public class DeleteProductCategoryTypeDto extends AbstractProductCategoryTypeCommandDto implements ProductCategoryTypeCommand.DeleteProductCategoryType {

    public DeleteProductCategoryTypeDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public ProductCategoryTypeCommand.DeleteProductCategoryType toDeleteProductCategoryType()
    {
        return new ProductCategoryTypeCommand.DeleteProductCategoryType() {
            @Override
            public String getProductCategoryTypeId() {
                return DeleteProductCategoryTypeDto.this.getProductCategoryTypeId();
            }

            @Override
            public void setProductCategoryTypeId(String p) {
                DeleteProductCategoryTypeDto.this.setProductCategoryTypeId(p);
            }

            @Override
            public Long getVersion() {
                return DeleteProductCategoryTypeDto.this.getVersion();
            }

            @Override
            public void setVersion(Long p) {
                DeleteProductCategoryTypeDto.this.setVersion(p);
            }

            @Override
            public String getCommandType() {
                return DeleteProductCategoryTypeDto.this.getCommandType();
            }

            @Override
            public void setCommandType(String commandType) {
                 DeleteProductCategoryTypeDto.this.setCommandType(commandType);
            }

            @Override
            public String getCommandId() {
                return DeleteProductCategoryTypeDto.this.getCommandId();
            }

            @Override
            public void setCommandId(String commandId) {
                DeleteProductCategoryTypeDto.this.setCommandId(commandId);
            }

            @Override
            public String getRequesterId() {
                return DeleteProductCategoryTypeDto.this.getRequesterId();
            }

            @Override
            public void setRequesterId(String requesterId) {
                DeleteProductCategoryTypeDto.this.setRequesterId(requesterId);
            }

            @Override
            public java.util.Map<String, Object> getCommandContext() {
                return DeleteProductCategoryTypeDto.this.getCommandContext();
            }


        };
    }
}
