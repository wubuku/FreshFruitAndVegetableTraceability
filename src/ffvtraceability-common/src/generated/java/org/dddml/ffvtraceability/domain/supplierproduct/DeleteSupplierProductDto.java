// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.supplierproduct;


public class DeleteSupplierProductDto extends AbstractSupplierProductCommandDto implements SupplierProductCommand.DeleteSupplierProduct {

    public DeleteSupplierProductDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public SupplierProductCommand.DeleteSupplierProduct toDeleteSupplierProduct()
    {
        AbstractSupplierProductCommand.SimpleDeleteSupplierProduct command = new AbstractSupplierProductCommand.SimpleDeleteSupplierProduct();
        ((AbstractSupplierProductCommandDto)this).copyTo(command);
        return command;
    }
}
