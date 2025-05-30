// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.inventorytransfer;

import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractInventoryTransferCommandDto extends AbstractCommand {

    /**
     * Inventory Transfer Id
     */
    private String inventoryTransferId;

    public String getInventoryTransferId()
    {
        return this.inventoryTransferId;
    }

    public void setInventoryTransferId(String inventoryTransferId)
    {
        this.inventoryTransferId = inventoryTransferId;
    }

    /**
     * Version
     */
    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


    public void copyTo(InventoryTransferCommand command) {
        command.setInventoryTransferId(this.getInventoryTransferId());
        command.setVersion(this.getVersion());
        
        command.setRequesterId(this.getRequesterId());
        command.setCommandId(this.getCommandId());
    }

}
