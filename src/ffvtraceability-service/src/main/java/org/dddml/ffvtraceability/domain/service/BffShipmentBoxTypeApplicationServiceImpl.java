package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffShipmentBoxTypeDto;
import org.dddml.ffvtraceability.domain.mapper.BffShipmentBoxTypeMapper;
import org.dddml.ffvtraceability.domain.shipmentboxtype.AbstractShipmentBoxTypeCommand;
import org.dddml.ffvtraceability.domain.shipmentboxtype.ShipmentBoxTypeApplicationService;
import org.dddml.ffvtraceability.domain.shipmentboxtype.ShipmentBoxTypeState;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.IndicatorUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class BffShipmentBoxTypeApplicationServiceImpl implements BffShipmentBoxTypeApplicationService {

    @Autowired
    private ShipmentBoxTypeApplicationService shipmentBoxTypeApplicationService;

    @Autowired
    private BffShipmentBoxTypeMapper bffShipmentBoxTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffShipmentBoxTypeDto> when(BffShipmentBoxTypeServiceCommands.GetShipmentBoxTypes c) {
        return null; //todo
    }

    @Override
    @Transactional(readOnly = true)
    public BffShipmentBoxTypeDto when(BffShipmentBoxTypeServiceCommands.GetShipmentBoxType c) {
        return null; //todo
    }

    @Override
    public String when(BffShipmentBoxTypeServiceCommands.CreateShipmentBoxType c) {
        BffShipmentBoxTypeDto shipmentBoxTypeDto = c.getShipmentBoxType();
        AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType createShipmentBoxType
                = bffShipmentBoxTypeMapper.toCreateShipmentBoxType(shipmentBoxTypeDto);

        createShipmentBoxType.setShipmentBoxTypeId(shipmentBoxTypeDto.getShipmentBoxTypeId() != null ?
                shipmentBoxTypeDto.getShipmentBoxTypeId() : IdUtils.randomId());
        createShipmentBoxType.setActive(IndicatorUtils.asIndicatorDefaultYes(shipmentBoxTypeDto.getActive()));

        createShipmentBoxType.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        createShipmentBoxType.setRequesterId(c.getRequesterId());
        shipmentBoxTypeApplicationService.when(createShipmentBoxType);
        return createShipmentBoxType.getShipmentBoxTypeId();
    }

    @Override
    public void when(BffShipmentBoxTypeServiceCommands.UpdateShipmentBoxType c) {
        String shipmentBoxTypeId = c.getShipmentBoxTypeId();
        BffShipmentBoxTypeDto shipmentBoxTypeDto = c.getShipmentBoxType();
        shipmentBoxTypeDto.setShipmentBoxTypeId(shipmentBoxTypeId);

        ShipmentBoxTypeState shipmentBoxTypeState = shipmentBoxTypeApplicationService.get(shipmentBoxTypeId);
        if (shipmentBoxTypeState == null) {
            throw new IllegalArgumentException("ShipmentBoxType not found: " + shipmentBoxTypeId);
        }

        AbstractShipmentBoxTypeCommand.SimpleMergePatchShipmentBoxType toMergePatchShipmentBoxType
                = bffShipmentBoxTypeMapper.toMergePatchShipmentBoxType(shipmentBoxTypeDto);
        toMergePatchShipmentBoxType.setActive(IndicatorUtils.asIndicatorDefaultYes(shipmentBoxTypeDto.getActive()));
        toMergePatchShipmentBoxType.setVersion(shipmentBoxTypeState.getVersion());
        toMergePatchShipmentBoxType.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        toMergePatchShipmentBoxType.setRequesterId(c.getRequesterId());
        shipmentBoxTypeApplicationService.when(toMergePatchShipmentBoxType);
    }

    @Override
    public void when(BffShipmentBoxTypeServiceCommands.ActivateShipmentBoxType c) {
        //todo
    }

    @Override
    public void when(BffShipmentBoxTypeServiceCommands.BatchAddShipmentBoxTypes c) {
        //todo
    }
}
