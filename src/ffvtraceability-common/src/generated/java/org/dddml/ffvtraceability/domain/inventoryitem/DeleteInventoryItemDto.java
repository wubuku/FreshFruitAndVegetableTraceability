// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventoryitem;


public class DeleteInventoryItemDto extends AbstractInventoryItemCommandDto implements InventoryItemCommand.DeleteInventoryItem {

    public DeleteInventoryItemDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public InventoryItemCommand.DeleteInventoryItem toDeleteInventoryItem()
    {
        AbstractInventoryItemCommand.SimpleDeleteInventoryItem command = new AbstractInventoryItemCommand.SimpleDeleteInventoryItem();
        ((AbstractInventoryItemCommandDto)this).copyTo(command);
        return command;
    }
}

