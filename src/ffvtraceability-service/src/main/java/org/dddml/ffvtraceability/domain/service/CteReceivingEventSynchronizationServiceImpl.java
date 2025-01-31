package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.mapper.BffReceivingMapper;
import org.dddml.ffvtraceability.domain.receivingevent.AbstractReceivingEventCommand;
import org.dddml.ffvtraceability.domain.repository.BffFacilityContactMechRepository;
import org.dddml.ffvtraceability.domain.repository.BffFacilityRepository;
import org.dddml.ffvtraceability.domain.repository.BffReceivingRepository;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.dddml.ffvtraceability.domain.service.BffReceivingApplicationServiceImpl.getReceivingDocument;

@Service
@Transactional
public class CteReceivingEventSynchronizationServiceImpl implements CteReceivingEventSynchronizationService {
    @Autowired
    ShipmentReceiptApplicationService shipmentReceiptApplicationService;
    @Autowired
    private BffFacilityRepository bffFacilityRepository;
    @Autowired
    private BffFacilityContactMechRepository bffFacilityContactMechRepository;
    @Autowired
    private BffReceivingMapper bffReceivingMapper;
    @Autowired
    private BffReceivingRepository bffReceivingRepository;

    @Override
    public void synchronizeReceivingEvent(String shipmentId, Command command) {
        BffReceivingDocumentDto receivingDocument = getReceivingDocument(bffReceivingRepository, bffReceivingMapper,
                shipmentId
        );
        if (receivingDocument == null) {
            return;
        }
        for (BffReceivingItemDto receivingItem : receivingDocument.getReceivingItems()) {
            AbstractReceivingEventCommand.AbstractCreateOrMergePatchReceivingEvent e;
            String receiptId = receivingItem.getReceiptId();
            ShipmentReceiptState receiptState = shipmentReceiptApplicationService.get(receiptId);

            if (receiptState == null) {
                AbstractReceivingEventCommand.SimpleCreateReceivingEvent createReceivingEvent = new AbstractReceivingEventCommand.SimpleCreateReceivingEvent();
                e = createReceivingEvent;
            } else {
                AbstractReceivingEventCommand.SimpleMergePatchReceivingEvent mergePatchReceivingEvent = new AbstractReceivingEventCommand.SimpleMergePatchReceivingEvent();
                mergePatchReceivingEvent.setVersion(receiptState.getVersion());
                e = mergePatchReceivingEvent;
            }

            e.setEventId(receivingItem.getReceiptId());

            //e.setReceiveDate(receivingDocument.getCreatedAt());// todo format DateTime?

            //KdeTraceabilityLotCode
            KdeTraceabilityLotCode tlc = new KdeTraceabilityLotCode();
            tlc.setCaseGtin(receivingItem.getGtin());
            tlc.setCaseBatch(receivingItem.getLotId());// Get Batch from LotId?
            //tlc.setPalletSscc();
            //tlc.setSerialNumber();
            //tlc.setPackDate();
            //tlc.setHarvestDate();
            //tlc.setBestIfUsedByDate();
            e.setTraceabilityLotCode(tlc);

            //KdeProductDescription
            KdeProductDescription pd = new KdeProductDescription();

            //KdeQuantityAndUom
            KdeQuantityAndUom qu = new KdeQuantityAndUom();
            //qu.setUom(); // todo
            //receivingItem.getProductId();//todo get Uom from ProductId?
            qu.setQuantity(receivingItem.getQuantityAccepted()); // Or use `receivingItem.getCasesAccepted()`?

            pd.setProductName(receivingItem.getProductName());
            //pd.setPackagingSize(); // todo
            //pd.setPackagingStyle(); // todo

            e.setProductDescription(pd);
            e.setQuantityAndUom(qu);

            //KdeLocationDescription
            KdeLocationDescription shipFrom = getKdeLocationDescription(receivingDocument.getOriginFacilityId());
            e.setShipFromLocation(shipFrom);
            KdeLocationDescription shipTo = getKdeLocationDescription(receivingDocument.getDestinationFacilityId());
            e.setShipToLocation(shipTo);

            //KdeReferenceDocument

            KdeTlcSourceOrTlcSourceReference tlcSrcOrSrcRef = new KdeTlcSourceOrTlcSourceReference();
            e.setTlcSourceOrTlcSourceReference(tlcSrcOrSrcRef); // TODO


        }
    }

    private KdeLocationDescription getKdeLocationDescription(String facilityId) {
        BffBusinessContactDto facilityContact = BffFacilityApplicationServiceImpl.getBusinessContact(
                bffFacilityContactMechRepository, facilityId
        );
        KdeLocationDescription ld = new KdeLocationDescription();
        ld.setBusinessName(facilityContact.getBusinessName());
        ld.setState(facilityContact.getState());
        ld.setCity(facilityContact.getCity());
        ld.setPhysicalLocationAddress(facilityContact.getPhysicalLocationAddress());
        ld.setZipCode(facilityContact.getZipCode());
        ld.setPhoneNumber(facilityContact.getPhoneNumber());
        return ld;
    }
}
