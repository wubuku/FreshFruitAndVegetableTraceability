package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingDocumentDto;
import org.dddml.ffvtraceability.domain.BffReceivingItemDto;
import org.dddml.ffvtraceability.domain.document.AbstractDocumentCommand;
import org.dddml.ffvtraceability.domain.document.DocumentApplicationService;
import org.dddml.ffvtraceability.domain.document.DocumentState;
import org.dddml.ffvtraceability.domain.mapper.BffReceiptMapper;
import org.dddml.ffvtraceability.domain.repository.BffReceiptRepository;
import org.dddml.ffvtraceability.domain.repository.BffReceivingDocumentItemProjection;
import org.dddml.ffvtraceability.domain.shipment.AbstractShipmentCommand;
import org.dddml.ffvtraceability.domain.shipment.ShipmentApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.AbstractShipmentReceiptCommand;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shippingdocument.AbstractShippingDocumentCommand;
import org.dddml.ffvtraceability.domain.shippingdocument.ShippingDocumentApplicationService;
import org.dddml.ffvtraceability.domain.shippingdocument.ShippingDocumentState;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BffReceivingApplicationServiceImpl implements BffReceivingApplicationService {
    @Autowired
    private BffReceiptMapper bffReceiptMapper;
    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;
    @Autowired
    private ShipmentApplicationService shipmentApplicationService;
    @Autowired
    private ShippingDocumentApplicationService shippingDocumentApplicationService;
    @Autowired
    private DocumentApplicationService documentApplicationService;
    @Autowired
    private BffReceiptRepository bffReceiptRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BffReceivingDocumentDto> when(BffReceivingServiceCommands.GetReceivingDocuments c) {
        int offset = c.getPage() * c.getSize();
        long totalElements = bffReceiptRepository.countTotalShipments(c.getDocumentIdOrItem());

        List<BffReceivingDocumentItemProjection> projections =
                bffReceiptRepository.findAllReceivingDocumentsWithItems(
                        offset,
                        c.getSize(),
                        c.getDocumentIdOrItem());

        // 获取所有shipmentIds用于查询关联文档
        List<String> shipmentIds = projections.stream()
                .map(BffReceivingDocumentItemProjection::getDocumentId)
                .distinct()
                .collect(Collectors.toList());

        // 查询关联文档
        List<BffReceivingDocumentItemProjection> referenceDocuments = shipmentIds.isEmpty() ? Collections.emptyList()
                : bffReceiptRepository.findReferenceDocumentsByShipmentIds(shipmentIds);

        // 构建文档ID到引用文档列表的映射
        Map<String, List<BffDocumentDto>> documentReferenceMap = referenceDocuments.stream()
                .collect(Collectors.groupingBy(
                        BffReceivingDocumentItemProjection::getDocumentId,
                        Collectors.mapping(
                                bffReceiptMapper::toReferenceDocument,
                                Collectors.toList()
                        )
                ));

        List<BffReceivingDocumentDto> receivingDocuments = projections.stream()
                .collect(Collectors.groupingBy(
                        proj -> bffReceiptMapper.toBffReceivingDocumentDto(proj),
                        Collectors.mapping(
                                proj -> proj.getReceiptId() != null
                                        ? bffReceiptMapper.toBffReceivingItemDto(proj)
                                        : null,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList())
                                )
                        )
                ))
                .entrySet().stream()
                .map(entry -> {
                            BffReceivingDocumentDto d = entry.getKey();
                            d.setReceivingItems(entry.getValue());
                            // 设置关联文档
                            d.setReferenceDocuments(
                                    documentReferenceMap.getOrDefault(d.getDocumentId(), new ArrayList<>())
                            );
                            return d;
                        }
                ).collect(Collectors.toList());

        return Page.builder(receivingDocuments)
                .totalElements(totalElements)
                .size(c.getSize())
                .number(c.getPage())
                .build();
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
    @Transactional
    public String when(BffReceivingServiceCommands.CreateReceivingDocument c) {
        // NOTE: 将“BFF 文档 Id”映射到 Shipment Id
        AbstractShipmentCommand.SimpleCreateShipment createShipment = new AbstractShipmentCommand.SimpleCreateShipment();
        createShipment.setShipmentId(c.getReceivingDocument().getDocumentId() != null ? c.getReceivingDocument().getDocumentId() : IdUtils.randomId());
        createShipment.setPartyIdTo(c.getReceivingDocument().getPartyIdTo());
        createShipment.setPartyIdFrom(c.getReceivingDocument().getPartyIdFrom());
        createShipment.setOriginFacilityId(c.getReceivingDocument().getOriginFacilityId());
        createShipment.setDestinationFacilityId(c.getReceivingDocument().getDestinationFacilityId());
        createShipment.setPrimaryOrderId(c.getReceivingDocument().getPrimaryOrderId());
        //        PrimaryReturnId:
        //        PrimaryShipGroupSeqId:
        createShipment.setCommandId(createShipment.getShipmentId());// c.getCommandId());
        createShipment.setRequesterId(c.getRequesterId());
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
                String documentId;
                if (referenceDocument.getDocumentId() == null) {
                    AbstractDocumentCommand.SimpleCreateDocument createDocument = new AbstractDocumentCommand.SimpleCreateDocument();
                    createDocument.setDocumentId(IdUtils.randomId());
                    createDocument.setDocumentLocation(referenceDocument.getDocumentLocation());
                    createDocument.setDocumentText(referenceDocument.getDocumentText());
                    createDocument.setComments(referenceDocument.getComments());
                    createDocument.setCommandId(UUID.randomUUID().toString());
                    createDocument.setRequesterId(c.getRequesterId());
                    documentApplicationService.when(createDocument);
                    documentId = createDocument.getDocumentId();
                } else {
                    documentId = referenceDocument.getDocumentId();
                    DocumentState d = documentApplicationService.get(documentId);
                    if (d == null) {
                        //todo throw new IllegalArgumentException("Document not found: " + documentId);
                    }
                }
                // 这里每个“文档 Id”都只能与一个 Shipment 关联。判断“关联”是否已经存在，如果存在则报错。
                ShippingDocumentState sd = shippingDocumentApplicationService.get(documentId);
                if (sd != null) {
                    throw new IllegalArgumentException("Document already associated with a shipment: " + documentId);
                }
                AbstractShippingDocumentCommand.SimpleCreateShippingDocument createShippingDocument = new AbstractShippingDocumentCommand.SimpleCreateShippingDocument();
                createShippingDocument.setDocumentId(referenceDocument.getDocumentId());
                createShippingDocument.setShipmentId(createShipment.getShipmentId());
                createShippingDocument.setCommandId(UUID.randomUUID().toString());//createShipment.getShipmentId() + "-" + createShippingDocument.getDocumentId());
                shippingDocumentApplicationService.when(createShippingDocument);
            }
        }

        return createShipment.getShipmentId();
    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingPrimaryOrderId c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingReferenceDocuments c) {

    }

    @Override
    public String when(BffReceivingServiceCommands.CreateReceivingItem c) {
        return null;//todo
    }

    @Override
    public void when(BffReceivingServiceCommands.DeleteReceivingItem c) {

    }

    @Override
    public void when(BffReceivingServiceCommands.UpdateReceivingItem c) {

    }
}
