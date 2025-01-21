package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffBusinessContactDto;
import org.dddml.ffvtraceability.domain.Command;

public interface BffBusinessContactService {
    String createPostalAddress(BffBusinessContactDto bizContact, Command c);

    String createTelecomNumber(BffBusinessContactDto bizContact, Command c);

    String createMiscContact(BffBusinessContactDto bizContact, Command c);
}