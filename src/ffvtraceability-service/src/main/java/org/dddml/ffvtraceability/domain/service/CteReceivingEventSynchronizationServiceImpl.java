package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.mapper.BffReceivingMapper;
import org.dddml.ffvtraceability.domain.product.ProductApplicationService;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.receivingevent.AbstractReceivingEventCommand;
import org.dddml.ffvtraceability.domain.repository.BffFacilityContactMechRepository;
import org.dddml.ffvtraceability.domain.repository.BffFacilityRepository;
import org.dddml.ffvtraceability.domain.repository.BffReceivingRepository;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptState;
import org.dddml.ffvtraceability.domain.tenant.TenantApplicationService;
import org.dddml.ffvtraceability.domain.tenant.TenantState;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.uom.UomState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.dddml.ffvtraceability.domain.service.BffReceivingApplicationServiceImpl.getReceivingDocument;

@Service
@Transactional
public class CteReceivingEventSynchronizationServiceImpl implements CteReceivingEventSynchronizationService {
    @Autowired
    ShipmentReceiptApplicationService shipmentReceiptApplicationService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BffFacilityRepository bffFacilityRepository;
    @Autowired
    private BffFacilityContactMechRepository bffFacilityContactMechRepository;
    @Autowired
    private BffReceivingMapper bffReceivingMapper;
    @Autowired
    private BffReceivingRepository bffReceivingRepository;
    @Autowired
    private TenantApplicationService tenantApplicationService;
    @Autowired
    private ProductApplicationService productApplicationService;
    @Autowired
    private UomApplicationService uomApplicationService;

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

            // todo 使用 getCurrentTenantTimeZoneId 来格式化得到一个“当地时间”字符串？
            //e.setReceiveDate(receivingDocument.getCreatedAt());// todo format DateTime?

            String productId = receivingItem.getProductId();

            //KdeProductDescription
            KdeProductDescription pd = new KdeProductDescription();
            ProductState productState = productApplicationService.get(productId);
            UomState qtyUomState = uomApplicationService.get(productState.getQuantityUomId());
            UomState caseUomState = uomApplicationService.get(productState.getCaseUomId());
            productState.getPiecesIncluded();//todo
            productState.getQuantityIncluded();//todo

            pd.setProductName(receivingItem.getProductName());
            //pd.setPackagingSize(); // todo
            //pd.setPackagingStyle(); // todo
            e.setProductDescription(pd);

            //KdeQuantityAndUom
            KdeQuantityAndUom qu = new KdeQuantityAndUom();
            //qu.setUom(); // todo 设置单位（Case 单位优先？）
            qu.setQuantity(receivingItem.getQuantityAccepted()); // Or use `receivingItem.getCasesAccepted()`?
            e.setQuantityAndUom(qu);

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

            //KdeLocationDescription
            KdeLocationDescription shipFrom = getKdeLocationDescription(receivingDocument.getOriginFacilityId());
            e.setShipFromLocation(shipFrom);
            KdeLocationDescription shipTo = getKdeLocationDescription(receivingDocument.getDestinationFacilityId());
            e.setShipToLocation(shipTo);

            //KdeReferenceDocument // todo 引用的相关单据

            //KdeTlcSourceOrTlcSourceReference // TODO ...
//            KdeTlcSourceOrTlcSourceReference tlcSrcOrSrcRef = new KdeTlcSourceOrTlcSourceReference();
//            e.setTlcSourceOrTlcSourceReference(tlcSrcOrSrcRef);

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

    private String getCurrentTenantTimeZoneId() {
        if (TenantContext.getTenantId() == null) {
            logger.error("TenantId is not set.");
            throw new IllegalStateException("TenantId is not set.");
        }
        TenantState tenantState = tenantApplicationService.get(TenantContext.getTenantId());
        if (tenantState == null) {
            String message = "Tenant not found: " + TenantContext.getTenantId();
            logger.error(message);
            throw new IllegalStateException(message);
        }
        if (tenantState.getTimeZoneId() == null && tenantState.getTimeZoneId().isEmpty()) {
            throw new IllegalStateException("TenantId timezone Id is not set.");
        }
        return tenantState.getTimeZoneId();
    }

}
