package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffFacilityDto;
import org.dddml.ffvtraceability.domain.BffFacilityLocationDto;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.stereotype.Service;

@Service
public class BffFacilityApplicationServiceImpl implements BffFacilityApplicationService {
    @Override
    public Page<BffFacilityDto> when(BffFacilityServiceCommands.GetFacilities c) {
        return null;
    }

    @Override
    public BffFacilityDto when(BffFacilityServiceCommands.GetFacility c) {
        return null;
    }

    @Override
    public void when(BffFacilityServiceCommands.CreateFacility c) {

    }

    @Override
    public void when(BffFacilityServiceCommands.UpdateFacility c) {

    }

    @Override
    public void when(BffFacilityServiceCommands.ActivateFacility c) {

    }

    @Override
    public BffFacilityLocationDto when(BffFacilityServiceCommands.GetFacilityLocation c) {
        return null;
    }

    @Override
    public void when(BffFacilityServiceCommands.CreateFacilityLocation c) {

    }

    @Override
    public void when(BffFacilityServiceCommands.UpdateFacilityLocation c) {

    }

    @Override
    public void when(BffFacilityServiceCommands.ActivateFacilityLocation c) {

    }
}
