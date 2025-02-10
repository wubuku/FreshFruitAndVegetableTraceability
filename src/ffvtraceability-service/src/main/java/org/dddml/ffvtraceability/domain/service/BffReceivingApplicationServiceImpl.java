package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.domain.document.AbstractDocumentCommand;
import org.dddml.ffvtraceability.domain.document.DocumentApplicationService;
import org.dddml.ffvtraceability.domain.document.DocumentState;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationApplicationService;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationId;
import org.dddml.ffvtraceability.domain.facilitylocation.FacilityLocationState;
import org.dddml.ffvtraceability.domain.mapper.BffFacilityMapper;
import org.dddml.ffvtraceability.domain.mapper.BffReceivingMapper;
import org.dddml.ffvtraceability.domain.repository.*;
import org.dddml.ffvtraceability.domain.shipment.AbstractShipmentCommand;
import org.dddml.ffvtraceability.domain.shipment.ShipmentApplicationService;
import org.dddml.ffvtraceability.domain.shipment.ShipmentCommands;
import org.dddml.ffvtraceability.domain.shipment.ShipmentState;
import org.dddml.ffvtraceability.domain.shipmentreceipt.AbstractShipmentReceiptCommand;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptApplicationService;
import org.dddml.ffvtraceability.domain.shipmentreceipt.ShipmentReceiptState;
import org.dddml.ffvtraceability.domain.shippingdocument.AbstractShippingDocumentCommand;
import org.dddml.ffvtraceability.domain.shippingdocument.ShippingDocumentApplicationService;
import org.dddml.ffvtraceability.domain.shippingdocument.ShippingDocumentState;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class BffReceivingApplicationServiceImpl implements BffReceivingApplicationService {
    @Autowired
    private ShipmentReceiptApplicationService shipmentReceiptApplicationService;
    @Autowired
    private ShipmentApplicationService shipmentApplicationService;
    @Autowired
    private ShippingDocumentApplicationService shippingDocumentApplicationService;
    @Autowired
    private DocumentApplicationService documentApplicationService;
    @Autowired
    private BffReceivingMapper bffReceivingMapper;
    @Autowired
    private BffReceivingRepository bffReceivingRepository;
    @Autowired
    private CteReceivingEventSynchronizationService cteReceivingEventSynchronizationService;
    @Autowired
    private BffOrderRepository bffOrderRepository;

    @Autowired
    private BffFacilityRepository bffFacilityRepository;
    @Autowired
    private BffFacilityMapper bffFacilityMapper;
    @Autowired
    private BffFacilityContactMechRepository bffFacilityContactMechRepository;
    @Autowired
    private FacilityLocationApplicationService facilityLocationApplicationService;

    static BffReceivingDocumentDto getReceivingDocument(
            BffReceivingRepository bffReceivingRepository,
            BffReceivingMapper bffReceivingMapper,
            String shipmentId
    ) {
        List<BffReceivingDocumentItemProjection> projections = bffReceivingRepository
                .findReceivingDocumentWithItems(shipmentId);
        if (projections.isEmpty()) {
            return null;
        }

        // 查询关联文档
        List<BffReceivingDocumentItemProjection> referenceDocuments =
                bffReceivingRepository.findReferenceDocumentsByShipmentIds(Collections.singletonList(shipmentId));
        // 构建收货单DTO
        BffReceivingDocumentDto document = bffReceivingMapper.toBffReceivingDocumentDto(projections.get(0));
        // 添加行项
        document.setReceivingItems(
                projections.stream()
                        .filter(p -> p.getReceiptId() != null)
                        .map(bffReceivingMapper::toBffReceivingItemDto)
                        .collect(Collectors.toList())
        );
        // 添加关联文档
        document.setReferenceDocuments(
                referenceDocuments.stream()
                        .map(bffReceivingMapper::toReferenceDocument)
                        .collect(Collectors.toList())
        );
        return document;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<BffReceivingDocumentDto> when(BffReceivingServiceCommands.GetReceivingDocuments c) {
        int offset = c.getPage() * c.getSize();
        long totalElements = bffReceivingRepository.countTotalShipments(
                c.getDocumentIdOrItem(), c.getFacilityId(), c.getSupplierId(),
                c.getReceivedAtFrom(), //c.getReceivedAtFrom() != null ? c.getReceivedAtFrom().toInstant() : null,
                c.getReceivedAtTo() //c.getReceivedAtTo() != null ? c.getReceivedAtTo().toInstant() : null
        );

        List<BffReceivingDocumentItemProjection> projections =
                bffReceivingRepository.findAllReceivingDocumentsWithItems(
                        offset, c.getSize(),
                        c.getDocumentIdOrItem(), c.getFacilityId(), c.getSupplierId(),
                        c.getReceivedAtFrom(), //c.getReceivedAtFrom() != null ? c.getReceivedAtFrom().toInstant() : null,
                        c.getReceivedAtTo() //c.getReceivedAtTo() != null ? c.getReceivedAtTo().toInstant() : null
                );

        // 获取所有 Shipment IDs 以查询关联文档
        List<String> shipmentIds = projections.stream()
                .map(BffReceivingDocumentItemProjection::getDocumentId)
                .distinct()
                .collect(Collectors.toList());

        // 查询关联文档
        List<BffReceivingDocumentItemProjection> referenceDocuments = shipmentIds.isEmpty() ? Collections.emptyList()
                : bffReceivingRepository.findReferenceDocumentsByShipmentIds(shipmentIds);

        // 构建 ID 到引用文档列表的映射
        Map<String, List<BffDocumentDto>> documentReferenceMap = referenceDocuments.stream()
                .collect(Collectors.groupingBy(
                        BffReceivingDocumentItemProjection::getDocumentId,
                        Collectors.mapping(
                                bffReceivingMapper::toReferenceDocument,
                                Collectors.toList()
                        )
                ));

        List<BffReceivingDocumentDto> receivingDocuments = projections.stream()
                .collect(Collectors.groupingBy(
                        proj -> bffReceivingMapper.toBffReceivingDocumentDto(proj),
                        Collectors.mapping(
                                proj -> proj.getReceiptId() != null
                                        ? bffReceivingMapper.toBffReceivingItemDto(proj)
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

        if (c.getDerivesQaInspectionStatus() != null && c.getDerivesQaInspectionStatus()) {
            // 查询 QA 检验状态
            Map<String, String> qaInspectionStatusMap =
                    bffReceivingRepository.findQaInspectionStatusByShipmentIds(shipmentIds)
                            .stream()
                            .map(p -> new AbstractMap.SimpleEntry<>(p.getDocumentId(), p.getQaInspectionStatusId()))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            receivingDocuments.forEach(d -> d.setQaInspectionStatusId(qaInspectionStatusMap.get(d.getDocumentId())));
        }

        return Page.builder(receivingDocuments)
                .totalElements(totalElements)
                .size(c.getSize())
                .number(c.getPage())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BffReceivingDocumentDto when(BffReceivingServiceCommands.GetReceivingDocument c) {
        BffReceivingDocumentDto receivingDocument = getReceivingDocument(bffReceivingRepository, bffReceivingMapper,
                c.getDocumentId()
        );
        if (receivingDocument == null) {
            return null;
        }
        if (c.getIncludesOutstandingOrderQuantity() != null && c.getIncludesOutstandingOrderQuantity()
                && receivingDocument.getReceivingItems() != null
        ) {
            for (BffReceivingItemDto item : receivingDocument.getReceivingItems()) {
                Optional<BigDecimal> oq = bffOrderRepository
                        .findReceiptAssociatedOrderItemOutstandingQuantity(item.getReceiptId());
                oq.ifPresent(item::setOutstandingOrderQuantity);
            }
        }
        if (c.getDerivesQaInspectionStatus() != null && c.getDerivesQaInspectionStatus()) {
            bffReceivingRepository.findQaInspectionStatusByShipmentId(c.getDocumentId())
                    .ifPresent(receivingDocument::setQaInspectionStatusId);
            if (receivingDocument.getReceivingItems() != null) {
                List<BffReceivingItemProjection> itemProjections =
                        bffReceivingRepository.findQaInspectionStatusByShipmentReceiptIds(
                                receivingDocument.getReceivingItems().stream()
                                        .map(BffReceivingItemDto::getReceiptId)
                                        .collect(Collectors.toList())
                        );
                Map<String, String> m = new HashMap<>();
                itemProjections.forEach(bffReceivingItemProjection -> {
                    String receiptId = bffReceivingItemProjection.getReceiptId();
                    String qaInspectionStatusId = bffReceivingItemProjection.getQaInspectionStatusId();
                    m.put(receiptId, qaInspectionStatusId);
                });
//              以下这种写法当 bffReceivingItemProjection.getQaInspectionStatusId();返回null时，报错。
//              Map<String, String> m = itemProjections.stream()
//                        .collect(Collectors.toMap(BffReceivingItemProjection::getReceiptId,
//                                BffReceivingItemProjection::getQaInspectionStatusId));
                receivingDocument.getReceivingItems().forEach(i -> i.setQaInspectionStatusId(m.get(i.getReceiptId())));
            }
        }
        if (c.getIncludesOriginFacility() != null && c.getIncludesOriginFacility()
                && receivingDocument.getOriginFacilityId() != null
        ) {
            receivingDocument.setOriginFacility(getBffFacilityDto(receivingDocument.getOriginFacilityId()));
        }
        return receivingDocument;
    }

    private BffFacilityDto getBffFacilityDto(String facilityId) {
        Optional<BffFacilityProjection> facilityProjection = bffFacilityRepository.findFacilityByFacilityId(facilityId);
        if (facilityProjection.isEmpty()) {
            return null;
        }
        BffFacilityDto bffFacilityDto = bffFacilityMapper.toBffFacilityDto(facilityProjection.get());
        enrichFacilityBusinessContactDetails(bffFacilityDto, bffFacilityDto.getFacilityId());
        return bffFacilityDto;
    }

    private void enrichFacilityBusinessContactDetails(BffFacilityDto dto, String facilityId) {
        BffBusinessContactDto facilityContact = BffFacilityApplicationServiceImpl.getBusinessContact(
                bffFacilityContactMechRepository, facilityId
        );
        if (facilityContact != null) {
            dto.setBusinessContacts(Collections.singletonList(facilityContact));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BffReceivingItemDto when(BffReceivingServiceCommands.GetReceivingItem c) {
        BffReceivingDocumentItemProjection projection =
                bffReceivingRepository.findReceivingItem(c.getDocumentId(), c.getReceiptId());
        if (projection == null) {
            return null;
        }
        return bffReceivingMapper.toBffReceivingItemDto(projection);
    }

    @Override
    @Transactional
    public String when(BffReceivingServiceCommands.CreateReceivingDocument c) {
        if (c.getReceivingDocument() == null) {
            throw new IllegalArgumentException("Receiving information can't be null");
        }
        // NOTE: 将"BFF 文档 Id"映射到 Shipment Id
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
                createShipmentReceipt.setReceivedBy(c.getRequesterId());
                createShipmentReceipt.setCommandId(createShipmentReceipt.getReceiptId());
                shipmentReceiptApplicationService.when(createShipmentReceipt);
                itemSeq++;
            }
        }

        if (c.getReceivingDocument().getReferenceDocuments() != null) {
            for (BffDocumentDto referenceDocument : c.getReceivingDocument().getReferenceDocuments()) {
                String refDocumentId;
                if (referenceDocument.getDocumentId() == null) {
                    refDocumentId = createReferenceDocument(referenceDocument, c);
                } else {
                    refDocumentId = referenceDocument.getDocumentId();
                    DocumentState d = documentApplicationService.get(refDocumentId);
                    if (d == null) {
                        throw new IllegalArgumentException("Document not found: " + refDocumentId);
                    }
                    // 这里每个"文档 Id"都只能与一个 Shipment 关联。判断"关联"是否已经存在，如果存在则报错。
                    ShippingDocumentState sd = shippingDocumentApplicationService.get(refDocumentId);
                    if (sd != null) {
                        throw new IllegalArgumentException("Document already associated with a shipment: " + refDocumentId);
                    }
                }
                createShippingDocument(createShipment.getShipmentId(), refDocumentId, c);
            }
        }

        return createShipment.getShipmentId();
    }

    @Override
    @Transactional
    public void when(BffReceivingServiceCommands.UpdateReceivingDocument c) {
        BffReceivingDocumentDto receivingDocumentDto = c.getReceivingDocument();
        if (receivingDocumentDto == null) {
            throw new IllegalArgumentException("Receiving information can't be null");
        }
        String shipmentId = receivingDocumentDto.getDocumentId();
        ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
        if (shipmentState == null) {
            throw new IllegalArgumentException("Shipment (receiving document) not found: " + shipmentId);
        }

        AbstractShipmentCommand.SimpleMergePatchShipment mergePatchShipment =
                new AbstractShipmentCommand.SimpleMergePatchShipment();
        mergePatchShipment.setShipmentId(shipmentId);
        mergePatchShipment.setVersion(shipmentState.getVersion());
        mergePatchShipment.setPrimaryOrderId(receivingDocumentDto.getPrimaryOrderId());
        // todo: More fields to update?
        mergePatchShipment.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchShipment.setRequesterId(c.getRequesterId());
        shipmentApplicationService.when(mergePatchShipment);
        //当不指定receivingItems的时候，我们认为不修改它的行项。
        //当指定receivingItems，即便是空数组（注意不是null），那么就按你最新指定的行项来重新确定行项。
        if (receivingDocumentDto.getReceivingItems() != null) {
            String originFacilityId = receivingDocumentDto.getOriginFacilityId();
            mergePatchShipment.setOriginFacilityId(originFacilityId);
            if (originFacilityId == null || originFacilityId.isEmpty()) {
                //如果它是null，后面的new FacilityLocationId就得出问题
                throw new IllegalArgumentException("Original Facility can't be null");
            }
            for (BffReceivingItemDto itemDto : receivingDocumentDto.getReceivingItems()) {
                // validate facility location
                FacilityLocationState fl = facilityLocationApplicationService.get(new FacilityLocationId(originFacilityId, itemDto.getLocationSeqId()));
                if (fl == null) {
                    throw new IllegalArgumentException("Location not found: " + originFacilityId + "/" + itemDto.getLocationSeqId());
                }
                //shipmentState.getShipmentItems()
                boolean isNewItem = true;
                boolean isDeleted = false;
                ShipmentReceiptState itemState = null;
                String receiptId = itemDto.getReceiptId();
                if (itemDto.getReceiptId() != null) {
                    itemState = shipmentReceiptApplicationService.get(receiptId);
                    if (itemState != null) {
                        isNewItem = false;
                        if (itemDto.getDeleted() != null && itemDto.getDeleted()) {
                            isDeleted = true;
                        }
                    }
                }
                if (isNewItem) {
                    createReceivingItem(itemDto, shipmentId, c);
                } else {
                    if (isDeleted) {
                        if (itemState == null) {
                            // throw new IllegalArgumentException("Receipt not found: " + receiptId);
                            // should not happen?
                        } else {
                            deleteReceivingItem(receiptId, itemState.getVersion(), c, true);
                        }
                    } else {
                        BffReceivingServiceCommands.UpdateReceivingItem u = toUpdateReceivingItem(c, itemDto, shipmentId);
                        updateReceivingItem(receiptId, itemState, u, true);
                    }
                }
            }
        }
    }

    private BffReceivingServiceCommands.UpdateReceivingItem toUpdateReceivingItem(
            BffReceivingServiceCommands.UpdateReceivingDocument c,
            BffReceivingItemDto itemDto,
            String shipmentId
    ) {
        BffReceivingServiceCommands.UpdateReceivingItem u = new BffReceivingServiceCommands.UpdateReceivingItem();
        // Set properties from itemDto to update command
        u.setDocumentId(shipmentId);
        u.setReceiptId(itemDto.getReceiptId());
        //u.setProductId(itemDto.getProductId());
        u.setLotId(itemDto.getLotId());
        u.setLocationSeqId(itemDto.getLocationSeqId());
        u.setQuantityAccepted(itemDto.getQuantityAccepted());
        u.setQuantityRejected(itemDto.getQuantityRejected());
        u.setCasesAccepted(itemDto.getCasesAccepted());
        u.setCasesRejected(itemDto.getCasesRejected());
        u.setItemDescription(itemDto.getItemDescription());
        u.setRequesterId(c.getRequesterId());
        return u;
    }

    private void createShippingDocument(String shipmentId, String referenceDocumentId, Command c) {
        AbstractShippingDocumentCommand.SimpleCreateShippingDocument createShippingDocument = new AbstractShippingDocumentCommand.SimpleCreateShippingDocument();
        createShippingDocument.setDocumentId(referenceDocumentId);
        createShippingDocument.setShipmentId(shipmentId);
        createShippingDocument.setCommandId(UUID.randomUUID().toString());//createShipment.getShipmentId() + "-" + createShippingDocument.getDocumentId());
        createShippingDocument.setRequesterId(c.getRequesterId());
        shippingDocumentApplicationService.when(createShippingDocument);
    }

    private String createReferenceDocument(BffDocumentDto referenceDocument, Command c) {
        AbstractDocumentCommand.SimpleCreateDocument createDocument = new AbstractDocumentCommand.SimpleCreateDocument();
        createDocument.setDocumentId(IdUtils.randomId());
        createDocument.setDocumentLocation(referenceDocument.getDocumentLocation());
        createDocument.setDocumentText(referenceDocument.getDocumentText());
        createDocument.setComments(referenceDocument.getComments());
        createDocument.setCommandId(UUID.randomUUID().toString());
        createDocument.setRequesterId(c.getRequesterId());
        documentApplicationService.when(createDocument);
        return createDocument.getDocumentId();
    }

    @Override
    @Transactional
    public void when(BffReceivingServiceCommands.UpdateReceivingPrimaryOrderId c) {
        String shipmentId = c.getDocumentId();
        ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
        if (shipmentState == null) {
            throw new IllegalArgumentException("Shipment (receiving document) not found: " + shipmentId);
        }

        AbstractShipmentCommand.SimpleMergePatchShipment mergePatchShipment =
                new AbstractShipmentCommand.SimpleMergePatchShipment();
        mergePatchShipment.setShipmentId(shipmentId);
        mergePatchShipment.setVersion(shipmentState.getVersion());
        mergePatchShipment.setPrimaryOrderId(c.getPrimaryOrderId());
        mergePatchShipment.setCommandId(c.getCommandId() != null ?
                c.getCommandId() : UUID.randomUUID().toString());
        mergePatchShipment.setRequesterId(c.getRequesterId());

        shipmentApplicationService.when(mergePatchShipment);
    }

//    // Helper method to validate state
//    private boolean isValidStateForQaConfirmation(ShipmentState state) {
//        // Add your state validation logic here
//        return state != null && state.getStatus() != null;
//    }

    @Override
    @Transactional
    public void when(BffReceivingServiceCommands.SubmitReceivingDocument c) {
        String shipmentId = c.getDocumentId();
        ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
        if (shipmentState == null) {
            throw new IllegalArgumentException("Shipment (receiving document) not found: " + shipmentId);
        }

        ShipmentCommands.ShipmentAction shipmentAction = new ShipmentCommands.ShipmentAction();
        shipmentAction.setShipmentId(shipmentId);
        shipmentAction.setValue(ShipmentAction.SUBMIT);
        // Add version check
        shipmentAction.setVersion(shipmentState.getVersion());
        // Add command tracking
        shipmentAction.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        shipmentAction.setRequesterId(c.getRequesterId());

        try {
            shipmentApplicationService.when(shipmentAction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit receiving document: " + shipmentId, e);
        }
    }

    @Override
    @Transactional
    public void when(BffReceivingServiceCommands.ConfirmQaInspections c) {
        String shipmentId = c.getDocumentId();
        ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
        if (shipmentState == null) {
            throw new IllegalArgumentException("Shipment (receiving document) not found: " + shipmentId);
        }

//        // Validate state before confirmation
//        if (!isValidStateForQaConfirmation(shipmentState)) {
//            throw new IllegalStateException("Invalid state for QA confirmation: " + shipmentId);
//        }

        ShipmentCommands.ShipmentQaAction qaAction = new ShipmentCommands.ShipmentQaAction();
        qaAction.setShipmentId(shipmentId);
        qaAction.setValue(ShipmentQaAction.CONFIRM);
        qaAction.setVersion(shipmentState.getVersion());
        qaAction.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        qaAction.setRequesterId(c.getRequesterId());

        try {
            shipmentApplicationService.when(qaAction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to confirm QA inspections for shipment: " + shipmentId, e);
        }
    }

    @Override
    @Transactional
    public void when(BffReceivingServiceCommands.UpdateReceivingReferenceDocuments c) {
        String shipmentId = c.getDocumentId();
        ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
        if (shipmentState == null) {
            throw new IllegalArgumentException("Shipment (receiving document) not found: " + shipmentId);
        }

        // 获取当前已关联的文档 ID 列表
        Set<String> existingDocumentIds =
                StreamSupport.stream(
                                shippingDocumentApplicationService.getByProperty("shipmentId",
                                        shipmentId, Collections.emptyList(), 0, Integer.MAX_VALUE
                                ).spliterator(), false)
                        .map(ShippingDocumentState::getDocumentId)
                        .collect(Collectors.toSet());

        // 获取新传入的文档 ID 列表
        Set<String> newDocumentIds = Arrays.stream(c.getReferenceDocuments())
                .map(BffDocumentDto::getDocumentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 需要移除的关联关系
        existingDocumentIds.stream()
                .filter(id -> !newDocumentIds.contains(id))
                .forEach(documentId -> {
                    AbstractShippingDocumentCommand.SimpleDeleteShippingDocument deleteCommand =
                            new AbstractShippingDocumentCommand.SimpleDeleteShippingDocument();
                    deleteCommand.setDocumentId(documentId);
                    shippingDocumentApplicationService.when(deleteCommand);
                });

        // 处理新的文档列表
        for (BffDocumentDto d : c.getReferenceDocuments()) {
            String documentId = d.getDocumentId();
            if (documentId != null) {
                // 如果是已存在的文档，且之前未关联，则创建关联关系
                if (!existingDocumentIds.contains(documentId)) {
                    createShippingDocument(shipmentId, documentId, c);
                }
            } else {
                // 如果是新文档，则先创建文档再建立关联关系
                documentId = createReferenceDocument(d, c);
                createShippingDocument(shipmentId, documentId, c);
            }
        }
    }

    @Override
    @Transactional
    public String when(BffReceivingServiceCommands.CreateReceivingItem c) {
        // 验证收货单是否存在
        String shipmentId = c.getDocumentId();
        ShipmentState shipmentState = shipmentApplicationService.get(shipmentId);
        if (shipmentState == null) {
            throw new IllegalArgumentException("Shipment (receiving document) not found: " + shipmentId);
        }
        BffReceivingItemDto receivingItemDto = c.getReceivingItem();

        String receiptId = createReceivingItem(receivingItemDto, shipmentId, c);
        return receiptId;
    }

    private String createReceivingItem(
            BffReceivingItemDto receivingItemDto,
            String shipmentId,
            Command c
    ) {
        // 创建收货单行项
        AbstractShipmentReceiptCommand.SimpleCreateShipmentReceipt createShipmentReceipt =
                new AbstractShipmentReceiptCommand.SimpleCreateShipmentReceipt();

        // 生成行项ID
        String receiptId = receivingItemDto.getReceiptId() != null ? (
                receivingItemDto.getReceiptId().startsWith(shipmentId)
                        ? receivingItemDto.getReceiptId()
                        : shipmentId + "-" + receivingItemDto.getReceiptId()
        ) : shipmentId + "-" + IdUtils.randomId();
        createShipmentReceipt.setReceiptId(receiptId);
        createShipmentReceipt.setShipmentId(shipmentId);

        // 设置行项数据
        createShipmentReceipt.setProductId(receivingItemDto.getProductId());
        createShipmentReceipt.setLotId(receivingItemDto.getLotId());
        createShipmentReceipt.setLocationSeqId(receivingItemDto.getLocationSeqId());
        createShipmentReceipt.setQuantityAccepted(receivingItemDto.getQuantityAccepted());
        createShipmentReceipt.setQuantityRejected(receivingItemDto.getQuantityRejected());
        createShipmentReceipt.setCasesAccepted(receivingItemDto.getCasesAccepted());
        createShipmentReceipt.setCasesRejected(receivingItemDto.getCasesRejected());
        createShipmentReceipt.setItemDescription(receivingItemDto.getItemDescription());
        createShipmentReceipt.setDatetimeReceived(OffsetDateTime.now());
        createShipmentReceipt.setReceivedBy(c.getRequesterId());
        createShipmentReceipt.setCommandId(receiptId); // NOTE: 与 receiptId 保持一致
        createShipmentReceipt.setRequesterId(c.getRequesterId());

        shipmentReceiptApplicationService.when(createShipmentReceipt);
        return receiptId;
    }

    @Override
    @Transactional
    public void when(BffReceivingServiceCommands.DeleteReceivingItem c) {
        String receiptId = c.getReceiptId();
        ShipmentReceiptState receiptState = shipmentReceiptApplicationService.get(receiptId);
        if (receiptState == null) {
            throw new IllegalArgumentException("Receipt not found: " + receiptId);
        }
        if (!c.getDocumentId().equals(receiptState.getShipmentId())) {
            throw new IllegalArgumentException("Shipment (receiving document) Id mismatch: " + c.getDocumentId());
        }

        deleteReceivingItem(receiptId, receiptState.getVersion(), c, false);
    }

    private void deleteReceivingItem(String receiptId, Long version, Command c, boolean randomCmdId) {
        AbstractShipmentReceiptCommand.SimpleDeleteShipmentReceipt deleteShipmentReceipt =
                new AbstractShipmentReceiptCommand.SimpleDeleteShipmentReceipt();
        deleteShipmentReceipt.setReceiptId(receiptId);
        deleteShipmentReceipt.setVersion(version);
        deleteShipmentReceipt.setCommandId(randomCmdId ? UUID.randomUUID().toString()
                : (c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString()));
        deleteShipmentReceipt.setRequesterId(c.getRequesterId());
        shipmentReceiptApplicationService.when(deleteShipmentReceipt);
    }

    @Override
    @Transactional
    public void when(BffReceivingServiceCommands.UpdateReceivingItem c) {
        if (c.getDocumentId() == null || c.getReceiptId() == null) {
            throw new IllegalArgumentException("DocumentId and ReceiptId are required.");
        }
        String receiptId = c.getReceiptId();
        ShipmentReceiptState shipmentReceiptState = shipmentReceiptApplicationService.get(receiptId);
        if (shipmentReceiptState == null) {
            throw new IllegalArgumentException("Receipt not found: " + receiptId);
        }
        if (!c.getDocumentId().equals(shipmentReceiptState.getShipmentId())) {
            throw new IllegalArgumentException("Shipment (receiving document) Id mismatch: " + c.getDocumentId());
        }

        updateReceivingItem(receiptId, shipmentReceiptState, c, false);
    }

    private void updateReceivingItem(String receiptId,
                                     ShipmentReceiptState shipmentReceiptState,
                                     BffReceivingServiceCommands.UpdateReceivingItem c,
                                     boolean randomCmdId
    ) {
        AbstractShipmentReceiptCommand.SimpleMergePatchShipmentReceipt mergePatchShipmentReceipt =
                new AbstractShipmentReceiptCommand.SimpleMergePatchShipmentReceipt();
        mergePatchShipmentReceipt.setReceiptId(receiptId);
        mergePatchShipmentReceipt.setVersion(shipmentReceiptState.getVersion());

        // 设置更新的字段
        BffReceivingServiceCommands.UpdateReceivingItem item = c;
        //mergePatchShipmentReceipt.setProductId(item.getProductId()); // 不能更新 product Id？
        mergePatchShipmentReceipt.setLotId(item.getLotId());
        mergePatchShipmentReceipt.setLocationSeqId(item.getLocationSeqId());
        mergePatchShipmentReceipt.setQuantityAccepted(item.getQuantityAccepted());
        mergePatchShipmentReceipt.setQuantityRejected(item.getQuantityRejected());
        mergePatchShipmentReceipt.setCasesAccepted(item.getCasesAccepted());
        mergePatchShipmentReceipt.setCasesRejected(item.getCasesRejected());
        mergePatchShipmentReceipt.setItemDescription(item.getItemDescription());

        mergePatchShipmentReceipt.setCommandId(randomCmdId ? UUID.randomUUID().toString() :
                (c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString()));
        mergePatchShipmentReceipt.setRequesterId(c.getRequesterId());

        shipmentReceiptApplicationService.when(mergePatchShipmentReceipt);
    }

    @Override
    public void when(BffReceivingServiceCommands.SynchronizeCteReceivingEvents c) {
        cteReceivingEventSynchronizationService.synchronizeReceivingEvent(c.getDocumentId(), c);
    }

}
