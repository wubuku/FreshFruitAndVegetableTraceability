// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.productcategory;


public class DeleteProductCategoryDto extends AbstractProductCategoryCommandDto implements ProductCategoryCommand.DeleteProductCategory {

    public DeleteProductCategoryDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public ProductCategoryCommand.DeleteProductCategory toDeleteProductCategory()
    {
        AbstractProductCategoryCommand.SimpleDeleteProductCategory command = new AbstractProductCategoryCommand.SimpleDeleteProductCategory();
        ((AbstractProductCategoryCommandDto)this).copyTo(command);
        return command;
    }
}
