package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BffFacilityLocationApplicationServiceImpl implements BffFacilityLocationApplicationService {

    @Override
    @Transactional(readOnly = true)
    public Page<BffFacilityLocationDto> when(BffFacilityLocationServiceCommands.GetFacilityLocations c) {
        return null; //todo
    }

}
