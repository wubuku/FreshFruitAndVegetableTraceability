package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffLotDto;
import org.dddml.ffvtraceability.domain.lot.AbstractLotCommand;
import org.dddml.ffvtraceability.domain.lot.LotApplicationService;
import org.dddml.ffvtraceability.domain.lot.LotIdentificationCommand;
import org.dddml.ffvtraceability.domain.mapper.BffLotMapper;
import org.dddml.ffvtraceability.domain.repository.BffLotRepository;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BffLotApplicationServiceImpl implements BffLotApplicationService {
    public static final String LOT_IDENTIFICATION_TYPE_GS1_BATCH = "GS1_BATCH";

    @Autowired
    private LotApplicationService lotApplicationService;

    @Autowired
    private BffLotRepository bffLotRepository;

    @Autowired
    private BffLotMapper bffLotMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffLotDto> when(BffLotServiceCommands.GetLots c) {
        return PageUtils.toPage(
                bffLotRepository.findAllLots(PageRequest.of(c.getPage(), c.getSize())),
                bffLotMapper::toBffLotDto
        );
    }

    @Override
    public BffLotDto when(BffLotServiceCommands.GetLot c) {
        return null;
    }

    @Override
    public String when(BffLotServiceCommands.CreateLot c) {
        AbstractLotCommand.SimpleCreateLot createLot = new AbstractLotCommand.SimpleCreateLot();
        createLot.setLotId(c.getLot().getLotId() != null ? c.getLot().getLotId() : IdUtils.randomId());
        createLot.setExpirationDate(c.getLot().getExpirationDate());
        createLot.setQuantity(c.getLot().getQuantity());
        createLot.setActive(true);
        createLot.setCommandId(createLot.getLotId());
        createLot.setRequesterId(c.getRequesterId());
        if (c.getLot().getGs1Batch() != null) {
            LotIdentificationCommand.CreateLotIdentification createLotIdentification = createLot.newCreateLotIdentification();
            createLotIdentification.setLotIdentificationTypeId(LOT_IDENTIFICATION_TYPE_GS1_BATCH);
            createLotIdentification.setIdValue(c.getLot().getGs1Batch());
        }
        lotApplicationService.when(createLot);
        return createLot.getLotId();
    }

    @Override
    public void when(BffLotServiceCommands.UpdateLot c) {

    }

    @Override
    public void when(BffLotServiceCommands.ActivateLot c) {

    }
}
