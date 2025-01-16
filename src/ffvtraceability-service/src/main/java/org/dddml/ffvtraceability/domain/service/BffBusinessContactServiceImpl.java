package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.contactmech.AbstractContactMechCommand;
import org.dddml.ffvtraceability.domain.contactmech.ContactMechApplicationService;
import org.dddml.ffvtraceability.domain.partycontactmech.PartyContactMechApplicationService;
import org.dddml.ffvtraceability.domain.repository.BffBusinessContactRepository;
import org.dddml.ffvtraceability.domain.repository.BffGeoRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.TelecomNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BffBusinessContactServiceImpl implements BffBusinessContactService {
    private static final String ERROR_STATE_NOT_FOUND = "State not found: %s";

    @Autowired
    private ContactMechApplicationService contactMechApplicationService;
    @Autowired
    private PartyContactMechApplicationService partyContactMechApplicationService;
    @Autowired
    private BffGeoRepository bffGeoRepository;
    @Autowired
    private BffBusinessContactRepository bffBusinessContactRepository;

    @Override
    public String createPostalAddress(BffBusinessContactDto bizContact, Command c) {
        Optional<BffGeoRepository.StateProvinceProjection> stateProvince
                = bffGeoRepository.findStateOrProvinceInfoById(bizContact.getStateProvinceGeoId());
        if (!stateProvince.isPresent()) {
            throw new IllegalArgumentException(String.format(ERROR_STATE_NOT_FOUND, bizContact.getState()));
        }
        AbstractContactMechCommand.SimpleCreatePostalAddress createPostalAddress = newCreatePostalAddress(bizContact, stateProvince.get());
        createPostalAddress.setCommandId(c.getCommandId() != null ? c.getCommandId() + "-P" : UUID.randomUUID().toString());
        createPostalAddress.setRequesterId(c.getRequesterId());
        contactMechApplicationService.when(createPostalAddress);
        return createPostalAddress.getContactMechId();
    }

    @Override
    public String createTelecomNumber(BffBusinessContactDto bizContact, Command c) {
        AbstractContactMechCommand.SimpleCreateTelecomNumber createTelecomNumber = newCreateTelecomNumber(bizContact);
        createTelecomNumber.setCommandId(c.getCommandId() != null ? c.getCommandId() + "-T" : UUID.randomUUID().toString());
        createTelecomNumber.setRequesterId(c.getRequesterId());
        contactMechApplicationService.when(createTelecomNumber);
        return createTelecomNumber.getContactMechId();
    }

    private AbstractContactMechCommand.SimpleCreateTelecomNumber newCreateTelecomNumber(BffBusinessContactDto bizContact) {
        TelecomNumberUtil.TelecomNumberDto telecomNumberDto = TelecomNumberUtil.parse(bizContact.getPhoneNumber());
        AbstractContactMechCommand.SimpleCreateTelecomNumber createTelecomNumber = new AbstractContactMechCommand.SimpleCreateTelecomNumber();
        createTelecomNumber.setContactMechId(IdUtils.randomId());
        createTelecomNumber.setCountryCode(telecomNumberDto.getCountryCode());
        createTelecomNumber.setAreaCode(telecomNumberDto.getAreaCode());
        createTelecomNumber.setContactNumber(telecomNumberDto.getContactNumber());
        return createTelecomNumber;
    }

    private AbstractContactMechCommand.SimpleCreatePostalAddress newCreatePostalAddress(
            BffBusinessContactDto bizContact, BffGeoRepository.StateProvinceProjection stateProvince
    ) {
        AbstractContactMechCommand.SimpleCreatePostalAddress createPostalAddress = new AbstractContactMechCommand.SimpleCreatePostalAddress();
        createPostalAddress.setContactMechId(IdUtils.randomId());
        createPostalAddress.setToName(bizContact.getBusinessName());
        createPostalAddress.setPostalCode(bizContact.getZipCode());
        createPostalAddress.setCountryGeoId(stateProvince.getParentGeoId());
        createPostalAddress.setStateProvinceGeoId(stateProvince.getGeoId());
        createPostalAddress.setCity(bizContact.getCity());
        createPostalAddress.setAddress1(bizContact.getPhysicalLocationAddress());
        return createPostalAddress;
    }

}