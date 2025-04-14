package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.lot.AbstractLotCommand;
import org.dddml.ffvtraceability.domain.lot.LotApplicationService;
import org.dddml.ffvtraceability.domain.mapper.BffLotMapper;
import org.dddml.ffvtraceability.domain.repository.BffLotProjection;
import org.dddml.ffvtraceability.domain.repository.BffLotRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
public class BffLotServiceImpl implements BffLotService {

    @Autowired
    private LotApplicationService lotApplicationService;

    @Autowired
    private BffLotRepository bffLotRepository;

    @Autowired
    private BffLotMapper bffLotMapper;

    @Override
    public String createLot(BffLotDto lotDto, OffsetDateTime now, String operator) {
        if (lotDto == null) {
            throw new IllegalArgumentException("Lot information can't be null");
        }
        if (lotDto.getSupplierId() == null || lotDto.getSupplierId().isBlank()) {
            throw new IllegalArgumentException("Vendor can't be null");
        }
        if (lotDto.getInternalId() == null || lotDto.getInternalId().isBlank()) {
            throw new IllegalArgumentException("Lot no. can't be null");
        }
        Optional<BffLotProjection> lotProjection = bffLotRepository.
                findLotBySupplierIdAndLotNo(lotDto.getInternalId(), lotDto.getSupplierId(), lotDto.getProductId());
        if (lotProjection.isPresent()) {
            return lotProjection.get().getLotId();
        }
        AbstractLotCommand.SimpleCreateLot createLot = bffLotMapper.toCreateLot(lotDto);
        createLot.setLotId(lotDto.getLotId() != null && !lotDto.getLotId().isEmpty() ? lotDto.getLotId() : IdUtils.randomId());
        createLot.setActive(INDICATOR_YES);//.asIndicatorDefaultYes(lotDto.getActive())); // 将前端传入的 active 规范化
        createLot.setCommandId(createLot.getLotId());
        createLot.setSupplierId(lotDto.getSupplierId());
        createLot.setInternalId(lotDto.getInternalId());
        createLot.setProductId(lotDto.getProductId());
        createLot.setRequesterId(operator);
        lotApplicationService.when(createLot);
        return createLot.getLotId();
    }
}
