package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffGeoDto;
import org.dddml.ffvtraceability.domain.mapper.BffGeoMapper;
import org.dddml.ffvtraceability.domain.repository.BffGeoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class BffGeoApplicationServiceImpl implements BffGeoApplicationService {

    @Autowired
    private BffGeoRepository bffGeoRepository;

    @Autowired
    private BffGeoMapper bffGeoMapper;

    @Override
    @Transactional(readOnly = true)
    public Iterable<BffGeoDto> when(BffGeoServiceCommands.GetAllNorthAmericanStatesAndProvinces c) {
        return bffGeoRepository.findAllNorthAmericanStatesAndProvinces().stream().map(
                bffGeoMapper::toBffGeoDto
        ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<BffGeoDto> when(BffGeoServiceCommands.GetCountries c) {
        return bffGeoRepository.findAllCountries().stream().map(
                bffGeoMapper::toBffGeoDto
        ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<BffGeoDto> when(BffGeoServiceCommands.GetStatesAndProvinces c) {
        return bffGeoRepository.findStatesAndProvincesByCountryId(c.getCountryId()).stream().map(
                bffGeoMapper::toBffGeoDto
        ).collect(Collectors.toUnmodifiableList());
    }

}
