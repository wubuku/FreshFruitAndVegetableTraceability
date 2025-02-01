package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.constants.BffLotConstants;
import org.dddml.ffvtraceability.domain.lot.LotApplicationService;
import org.dddml.ffvtraceability.domain.lot.LotIdentificationState;
import org.dddml.ffvtraceability.domain.lot.LotState;
import org.dddml.ffvtraceability.domain.mapper.BffReceivingMapper;
import org.dddml.ffvtraceability.domain.product.ProductApplicationService;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.receivingevent.AbstractReceivingEventCommand;
import org.dddml.ffvtraceability.domain.receivingevent.ReceivingEventApplicationService;
import org.dddml.ffvtraceability.domain.receivingevent.ReceivingEventCommand;
import org.dddml.ffvtraceability.domain.receivingevent.ReceivingEventState;
import org.dddml.ffvtraceability.domain.repository.*;
import org.dddml.ffvtraceability.domain.shipment.ShipmentApplicationService;
import org.dddml.ffvtraceability.domain.shipment.ShipmentState;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.tenant.TenantApplicationService;
import org.dddml.ffvtraceability.domain.tenant.TenantState;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.uom.UomState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.dddml.ffvtraceability.domain.service.BffReceivingApplicationServiceImpl.getReceivingDocument;

@Service
@Transactional
public class CteReceivingEventSynchronizationServiceImpl implements CteReceivingEventSynchronizationService {
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/M/d HH:mm:ss";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReceivingEventApplicationService receivingEventApplicationService;

    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;

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

    @Autowired
    private ShipmentApplicationService shipmentApplicationService;

    @Autowired
    private LotApplicationService lotApplicationService;

    @Autowired
    private BffLotRepository bffLotRepository;

    public static String formatKdeProductDescription(KdeProductDescription pd) {
        String sb = pd.getProductName() +
                " - " +
                pd.getPackagingSize();
        // sb.append(" ").append(pd.getPackagingStyle()); // NOTE: ignore PackagingStyle?
        return sb;
    }

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
            //ShipmentReceiptState receiptState = shipmentReceiptApplicationService.get(receiptId);
            String eventId = receiptId;
            ReceivingEventState receivingEventState = receivingEventApplicationService.get(eventId);
            if (receivingEventState == null) {
                AbstractReceivingEventCommand.SimpleCreateReceivingEvent createReceivingEvent = new AbstractReceivingEventCommand.SimpleCreateReceivingEvent();
                e = createReceivingEvent;
            } else {
                AbstractReceivingEventCommand.SimpleMergePatchReceivingEvent mergePatchReceivingEvent = new AbstractReceivingEventCommand.SimpleMergePatchReceivingEvent();
                mergePatchReceivingEvent.setVersion(receivingEventState.getVersion());
                e = mergePatchReceivingEvent;
            }
            e.setEventId(eventId);
            e.setReceiveDate(formatDateTime(receivingDocument.getCreatedAt())); // TODO: use `createdAt`?

            String productId = receivingItem.getProductId();
            String productName = receivingItem.getProductName();
            ProductState productState = productApplicationService.get(productId);

            UomState qtyUomState = null;
            if (productState.getQuantityUomId() != null) {
                qtyUomState = uomApplicationService.get(productState.getQuantityUomId());
            }
            UomState caseUomState = null;
            if (productState.getCaseUomId() != null) {
                caseUomState = uomApplicationService.get(productState.getCaseUomId());
            }

            //KdeProductDescription
            KdeProductDescription pd = getKdeProductDescription(productName, productState, qtyUomState, caseUomState);
            e.setProductDescription(pd);

            //KdeQuantityAndUom
            KdeQuantityAndUom qu = getKdeQuantityAndUom(receivingItem, caseUomState, qtyUomState);
            e.setQuantityAndUom(qu);

            //KdeTraceabilityLotCode
            KdeTraceabilityLotCode tlc = getKdeTraceabilityLotCode(receivingItem);
            e.setTraceabilityLotCode(tlc);

            //ShipFrom KdeLocationDescription
            KdeLocationDescription shipFrom = getKdeLocationDescription(receivingDocument.getOriginFacilityId());
            e.setShipFromLocation(shipFrom);
            //ShipTo KdeLocationDescription
            KdeLocationDescription shipTo = getKdeLocationDescription(receivingDocument.getDestinationFacilityId());
            e.setShipToLocation(shipTo);

            //KdeReferenceDocument
            List<KdeReferenceDocument> referenceDocuments = getKdeReferenceDocuments(receivingItem, receivingDocument);
            e.setReferenceDocuments(referenceDocuments.toArray(new KdeReferenceDocument[0]));

            //KdeTlcSourceOrTlcSourceReference
            if (tlc.getCaseGtin() != null && tlc.getCaseBatch() != null) {
                Optional<BffLotProjection> lotProjection = bffLotRepository.findPrimaryTlcByCaseGtinAndBatch(tlc.getCaseGtin(), tlc.getCaseBatch());
                if (lotProjection.isPresent() && lotProjection.get().getSourceFacilityId() != null) {
                    String sourceFacilityId = lotProjection.get().getSourceFacilityId();
                    KdeLocationDescription tlcLocationDescription = getKdeLocationDescription(sourceFacilityId);
                    KdeTlcSourceOrTlcSourceReference tlcSrcOrSrcRef = new KdeTlcSourceOrTlcSourceReference();
                    tlcSrcOrSrcRef.setTlcSource(tlcLocationDescription);
                    e.setTlcSourceOrTlcSourceReference(tlcSrcOrSrcRef);
                }
            }

            if (e instanceof ReceivingEventCommand.CreateReceivingEvent) {
                receivingEventApplicationService.when((ReceivingEventCommand.CreateReceivingEvent) e);
            } else { //if (e instanceof ReceivingEventCommand.MergePatchReceivingEvent)
                receivingEventApplicationService.when((ReceivingEventCommand.MergePatchReceivingEvent) e);
            }
        }
    }

    private KdeTraceabilityLotCode getKdeTraceabilityLotCode(BffReceivingItemDto receivingItem) {
        KdeTraceabilityLotCode tlc = new KdeTraceabilityLotCode();
        String lotId = receivingItem.getLotId();
        String caseGtin = null;
        String caseBatch = null; // = lotId??? NOTE: Might the `lotId` not be a real Case BATCH?

        LotState lotState = lotApplicationService.get(lotId);
        if (lotState != null) {
            Optional<LotIdentificationState> tlcLotIdentification = lotState.getLotIdentifications().stream().filter(
                    x -> BffLotConstants.LOT_IDENTIFICATION_TYPE_TLC_CASE_GTIN_BATCH.equals(x.getLotIdentificationTypeId())
            ).findAny();
            if (tlcLotIdentification.isPresent()) {
                // User Case GTIN of LotIdentification
                LotIdentificationState l = tlcLotIdentification.get();
                caseGtin = l.getGtin();
                caseBatch = l.getGs1Batch();
            } else {
                if (lotState.getGtin() != null && !lotState.getGtin().trim().isEmpty()) {
                    // Use GTIN of Lot
                    caseGtin = lotState.getGtin().trim(); // NOTE: Might this not be a real Case GTIN?
                }
                if (lotState.getGs1Batch() != null && !lotState.getGs1Batch().trim().isEmpty()) {
                    caseBatch = lotState.getGs1Batch().trim(); // NOTE: Might this not be a real Case Batch?
                }
            }
            tlc.setPalletSscc(lotState.getPalletSscc());
            tlc.setSerialNumber(lotState.getSerialNumber());
            if (lotState.getPackDate() != null) {
                tlc.setPackDate(formatDateTime(lotState.getPackDate()));
            }
            if (lotState.getHarvestDate() != null) {
                tlc.setHarvestDate(formatDateTime(lotState.getHarvestDate()));
            }
            if (lotState.getExpirationDate() != null) {
                tlc.setBestIfUsedByDate(formatDateTime(lotState.getExpirationDate()));
            }
        }
        if ((caseGtin == null || caseGtin.trim().isEmpty()) && receivingItem.getGtin() != null) {
            // Use GTIN of Product
            caseGtin = receivingItem.getGtin(); // NOTE: Might this not be a real Case GTIN?
        }
        tlc.setCaseGtin(caseGtin);
        tlc.setCaseBatch(caseBatch);
        return tlc;
    }

    private List<KdeReferenceDocument> getKdeReferenceDocuments(
            BffReceivingItemDto receivingItem,
            BffReceivingDocumentDto receivingDocument
    ) {
        String shipmentId = receivingDocument.getDocumentId();
        List<KdeReferenceDocument> referenceDocuments = new ArrayList<>();
        String poId = null;
        if (receivingItem.getOrderId() != null) {
            poId = receivingItem.getOrderId();
        } else if (receivingDocument.getPrimaryOrderId() != null) {
            poId = receivingDocument.getPrimaryOrderId();
        }
        if (poId != null) {
            KdeReferenceDocument poDoc = new KdeReferenceDocument();
            poDoc.setDocumentType("PO");
            poDoc.setDocumentNumber(poId);
            referenceDocuments.add(poDoc);
        }

        KdeReferenceDocument shipDoc = new KdeReferenceDocument();
        ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
        if (shipmentState != null) {
            shipDoc.setDocumentType(shipmentState.getShipmentTypeId());
        } else {
            shipDoc.setDocumentType("SHIPMENT");
        }
        shipDoc.setDocumentNumber(shipmentId);
        return referenceDocuments;
    }

    private KdeQuantityAndUom getKdeQuantityAndUom(BffReceivingItemDto receivingItem, UomState caseUomState, UomState qtyUomState) {
        KdeQuantityAndUom qu = new KdeQuantityAndUom();
        if (receivingItem.getCasesAccepted() != null) {
            qu.setQuantity(BigDecimal.valueOf(receivingItem.getCasesAccepted()));
            qu.setUom(formatUomName(caseUomState, "Case"));
        } else {
            qu.setQuantity(receivingItem.getQuantityAccepted()); // Or use ``?
            qu.setUom(formatUomName(qtyUomState, qtyUomState != null ? qtyUomState.getUomId() : null));
        }
        return qu;
    }

    private String formatUomName(UomState uomState, String defaultName) {
        if (uomState == null) {
            return defaultName;
        }
        return uomState.getUomName() != null ? uomState.getUomName()
                : (uomState.getAbbreviation() != null ? uomState.getAbbreviation()
                : (uomState.getDescription() != null ? uomState.getDescription()
                : defaultName
        ));
    }

    private KdeProductDescription getKdeProductDescription(
            String productName,
            ProductState productState,
            UomState qtyUomState,
            UomState caseUomState
    ) {
        String prdName = productName;
        if (prdName == null || prdName.isEmpty()) {
            prdName = productState.getProductName();
        }
        if (prdName == null || prdName.isEmpty()) {
            prdName = productState.getBrandName();
        }

        KdeProductDescription pd = new KdeProductDescription();
        pd.setProductName(prdName);
        StringBuilder sb = new StringBuilder();
        if (productState.getQuantityIncluded() != null) {
            sb.append(productState.getQuantityIncluded().toBigInteger()); // Only int?
            if (qtyUomState != null) {
                if (qtyUomState.getAbbreviation() != null) {
                    sb.append(qtyUomState.getAbbreviation());
                } else if (qtyUomState.getUomName() != null) {
                    sb.append(" ").append(qtyUomState.getUomName());
                } else if (qtyUomState.getDescription() != null) {
                    sb.append(" ").append(qtyUomState.getDescription());
                } else {
                    sb.append(" ").append(qtyUomState.getUomId());
                }
            }
        }
        if (productState.getPiecesIncluded() != null && productState.getPiecesIncluded() != 1) {
            sb.append(" - ")
                    .append(productState.getQuantityIncluded())
                    .append(" Pack"); // NOTE: hard coded here?
        }

        pd.setPackagingSize(sb.toString());

        pd.setPackagingStyle(caseUomState == null ? "Case"
                : (caseUomState.getUomName() != null ? caseUomState.getUomName()
                : (caseUomState.getDescription() != null ? caseUomState.getDescription()
                : (caseUomState.getAbbreviation() != null ? caseUomState.getAbbreviation()
                : "Case"//caseUomState.getUomId()
        ))));
        return pd;
    }

    private KdeLocationDescription getKdeLocationDescription(String facilityId) {
        BffBusinessContactDto facilityContact = BffFacilityApplicationServiceImpl.getBusinessContact(
                bffFacilityContactMechRepository, facilityId
        );
        KdeLocationDescription ld = new KdeLocationDescription();
        if (facilityContact == null) {
            return ld; // NOTE: return an empty object?
        }
        ld.setBusinessName(facilityContact.getBusinessName());
        ld.setState(facilityContact.getState());
        ld.setCity(facilityContact.getCity());
        ld.setPhysicalLocationAddress(facilityContact.getPhysicalLocationAddress());
        ld.setZipCode(facilityContact.getZipCode());
        ld.setPhoneNumber(facilityContact.getPhoneNumber());
        return ld;
    }

    private String formatDateTime(OffsetDateTime dt) {
        TenantState tenantState = null;
        if (TenantContext.getTenantId() == null) {
            logger.warn("TenantId is not set.");
            //throw new IllegalStateException("TenantId is not set.");
        } else {
            tenantState = tenantApplicationService.get(TenantContext.getTenantId());
            if (tenantState == null) {
                String message = "Tenant not found: " + TenantContext.getTenantId();
                logger.warn(message);
                //throw new IllegalStateException(message);
            }
        }
        String dateTimeFormat = (tenantState != null && tenantState.getDateTimeFormat() != null)
                ? tenantState.getDateTimeFormat() : DEFAULT_DATE_TIME_FORMAT;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        if (tenantState != null && tenantState.getTimeZoneId() != null && !tenantState.getTimeZoneId().isEmpty()) {
            dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.of(tenantState.getTimeZoneId()));
        }
        return dateTimeFormatter.format(dt);
    }

}
