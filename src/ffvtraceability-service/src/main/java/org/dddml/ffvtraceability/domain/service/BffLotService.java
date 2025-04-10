package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffLotDto;

import java.time.OffsetDateTime;

public interface BffLotService {
    String createLot(BffLotDto lotDto, OffsetDateTime now, String operator);
}
