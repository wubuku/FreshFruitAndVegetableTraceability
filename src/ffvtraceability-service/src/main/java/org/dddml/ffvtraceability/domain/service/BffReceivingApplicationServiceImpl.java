package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingItemDto;
import org.dddml.ffvtraceability.domain.document.DocumentApplicationService;
import org.dddml.ffvtraceability.domain.shipment.AbstractShipmentCommand;
import org.dddml.ffvtraceability.domain.shipment.ShipmentApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.AbstractShipmentReceiptCommand;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shippingdocument.AbstractShippingDocumentCommand;
import org.dddml.ffvtraceability.domain.shippingdocument.ShippingDocumentApplicationService;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class BffReceivingApplicationServiceImpl implements BffReceivingApplicationService {
    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;

    @Autowired
    private ShipmentApplicationService shipmentApplicationService;

    @Autowired
    private ShippingDocumentApplicationService shippingDocumentApplicationService;

    @Autowired
    private DocumentApplicationService documentApplicationService;

    @Override
    public Page<BffReceivingDocumentDto> when(BffReceivingServiceCommands.GetReceivingDocuments c) {
        return null;
    }

    @Override
    public BffReceivingDocumentDto when(BffReceivingServiceCommands.GetReceivingDocument c) {
        return null;
    }

    @Override
    public BffReceivingItemDto when(BffReceivingServiceCommands.GetReceivingItem c) {
        return null;
    }

    @Override
    public void when(BffReceivingServiceCommands.CreateReceivingDocument c) {
        // NOTE: 将“BFF 文档 Id”映射到 Shipment Id
        AbstractShipmentCommand.SimpleCreateShipment createShipment = new AbstractShipmentCommand.SimpleCreateShipment();
        createShipment.setShipmentId(IdUtils.randomId());//Ignore??? (c.getReceivingDocument().getDocumentId());
        createShipment.setPartyIdTo(c.getReceivingDocument().getPartyIdTo());
        createShipment.setPartyIdFrom(c.getReceivingDocument().getPartyIdFrom());
        createShipment.setOriginFacilityId(c.getReceivingDocument().getOriginFacilityId());
        createShipment.setDestinationFacilityId(c.getReceivingDocument().getDestinationFacilityId());
        createShipment.setPrimaryOrderId(c.getReceivingDocument().getPrimaryOrderId());
        //        PrimaryReturnId:
        //        PrimaryShipGroupSeqId:
        createShipment.setCommandId(c.getCommandId());
        shipmentApplicationService.when(createShipment);

        if (c.getReceivingDocument().getReceivingItems() != null) {
            int itemSeq = 1;
            for (BffReceivingItemDto receivingItem : c.getReceivingDocument().getReceivingItems()) {
                AbstractShipmentReceiptCommand.SimpleCreateShipmentReceipt createShipmentReceipt = new AbstractShipmentReceiptCommand.SimpleCreateShipmentReceipt();
                createShipmentReceipt.setReceiptId(createShipment.getShipmentId() + "-" + itemSeq);
                createShipmentReceipt.setShipmentId(createShipment.getShipmentId());
                createShipmentReceipt.setProductId(receivingItem.getProductId());
                createShipmentReceipt.setLotId(receivingItem.getLotId());
                createShipmentReceipt.setLocationSeqId(receivingItem.getLocationSeqId());
                createShipmentReceipt.setQuantityAccepted(receivingItem.getQuantityAccepted());
                createShipmentReceipt.setQuantityRejected(receivingItem.getQuantityRejected());
                createShipmentReceipt.setCasesAccepted(receivingItem.getCasesAccepted());
                createShipmentReceipt.setCasesRejected(receivingItem.getCasesRejected());
                createShipmentReceipt.setItemDescription(receivingItem.getItemDescription());
                createShipmentReceipt.setDatetimeReceived(OffsetDateTime.now());
                //createShipmentReceipt.setOrderId();
                //createShipmentReceipt.setRejectionId();
                //todo createShipmentReceipt.setReceivedBy();
                createShipmentReceipt.setCommandId(createShipmentReceipt.getReceiptId());
                shipmentReceiptApplicationService.when(createShipmentReceipt);
                itemSeq++;
            }
        }

        if (c.getReceivingDocument() != null) {
            for (BffDocumentDto referenceDocument : c.getReceivingDocument().getReferenceDocuments()) {
                AbstractShippingDocumentCommand.SimpleCreateShippingDocument createShippingDocument = new AbstractShippingDocumentCommand.SimpleCreateShippingDocument();
                // TODO 判断文档 Id 是否存在
                createShippingDocument.setDocumentId(referenceDocument.getDocumentId());
                createShippingDocument.setShipmentId(createShipment.getShipmentId());
                createShippingDocument.setCommandId(UUID.randomUUID().toString());//createShipment.getShipmentId() + "-" + createShippingDocument.getDocumentId());
                shippingDocumentApplicationService.when(createShippingDocument);
            }
        }
    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingPrimaryOrderId c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingReferenceDocuments c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.CreateReceivingItem c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.DeleteReceivingItem c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingItem c) {

    }
}
