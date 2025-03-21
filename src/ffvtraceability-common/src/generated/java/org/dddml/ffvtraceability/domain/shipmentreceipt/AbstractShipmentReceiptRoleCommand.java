// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.shipmentreceipt;

import java.util.*;
import org.dddml.ffvtraceability.domain.partyrole.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.AbstractCommand;

public abstract class AbstractShipmentReceiptRoleCommand extends AbstractCommand implements ShipmentReceiptRoleCommand {

    private PartyRoleId partyRoleId;

    public PartyRoleId getPartyRoleId()
    {
        return this.partyRoleId;
    }

    public void setPartyRoleId(PartyRoleId partyRoleId)
    {
        this.partyRoleId = partyRoleId;
    }

    private String shipmentReceiptReceiptId;

    public String getShipmentReceiptReceiptId()
    {
        return this.shipmentReceiptReceiptId;
    }

    public void setShipmentReceiptReceiptId(String shipmentReceiptReceiptId)
    {
        this.shipmentReceiptReceiptId = shipmentReceiptReceiptId;
    }


    public static abstract class AbstractCreateOrMergePatchShipmentReceiptRole extends AbstractShipmentReceiptRoleCommand implements CreateOrMergePatchShipmentReceiptRole
    {
    }

    public static abstract class AbstractCreateShipmentReceiptRole extends AbstractCreateOrMergePatchShipmentReceiptRole implements CreateShipmentReceiptRole
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchShipmentReceiptRole extends AbstractCreateOrMergePatchShipmentReceiptRole implements MergePatchShipmentReceiptRole
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }


    }

    public static class SimpleCreateShipmentReceiptRole extends AbstractCreateShipmentReceiptRole
    {
    }

    
    public static class SimpleMergePatchShipmentReceiptRole extends AbstractMergePatchShipmentReceiptRole
    {
    }

    
    public static class SimpleRemoveShipmentReceiptRole extends AbstractShipmentReceiptRoleCommand implements RemoveShipmentReceiptRole
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_REMOVE;
        }
    }

    

}

