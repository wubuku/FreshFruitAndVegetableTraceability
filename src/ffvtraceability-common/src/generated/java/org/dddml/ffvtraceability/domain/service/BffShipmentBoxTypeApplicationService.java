// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Page;

public interface BffShipmentBoxTypeApplicationService {

    Page<BffShipmentBoxTypeDto> when(BffShipmentBoxTypeServiceCommands.GetShipmentBoxTypes c);

    BffShipmentBoxTypeDto when(BffShipmentBoxTypeServiceCommands.GetShipmentBoxType c);

    String when(BffShipmentBoxTypeServiceCommands.CreateShipmentBoxType c);

    void when(BffShipmentBoxTypeServiceCommands.UpdateShipmentBoxType c);

    void when(BffShipmentBoxTypeServiceCommands.ActivateShipmentBoxType c);

    void when(BffShipmentBoxTypeServiceCommands.BatchAddShipmentBoxTypes c);


}

