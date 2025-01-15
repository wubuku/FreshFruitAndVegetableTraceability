package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.Command;

public interface CteReceivingEventSynchronizationService {
    void synchronizeReceivingEvent(String shipmentId, Command command);
}
