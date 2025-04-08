package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.BffShipmentBoxTypeDto;
import org.dddml.ffvtraceability.domain.BffSupplierRawItemDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.mapper.BffRawItemMapper;
import org.dddml.ffvtraceability.domain.mapper.BffShipmentBoxTypeMapper;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierProductAssocIdMapper;
import org.dddml.ffvtraceability.domain.party.PartyApplicationService;
import org.dddml.ffvtraceability.domain.party.PartyState;
import org.dddml.ffvtraceability.domain.product.*;
import org.dddml.ffvtraceability.domain.repository.BffRawItemRepository;
import org.dddml.ffvtraceability.domain.repository.BffSupplierProductAssocProjection;
import org.dddml.ffvtraceability.domain.shipmentboxtype.AbstractShipmentBoxTypeCommand;
import org.dddml.ffvtraceability.domain.shipmentboxtype.ShipmentBoxTypeApplicationService;
import org.dddml.ffvtraceability.domain.supplierproduct.AbstractSupplierProductCommand;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductApplicationService;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductAssocId;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.IndicatorUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static org.dddml.ffvtraceability.domain.constants.BffProductConstants.*;
import static org.dddml.ffvtraceability.domain.constants.BffRawItemConstants.DEFAULT_CURRENCY_UOM_ID;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
public class BffRawItemApplicationServiceImpl implements BffRawItemApplicationService {

    @Autowired
    private ProductApplicationService productApplicationService;

    @Autowired
    private UomApplicationService uomApplicationService;

    @Autowired
    private SupplierProductApplicationService supplierProductApplicationService;

    @Autowired
    private PartyApplicationService partyApplicationService;

    @Autowired
    private BffRawItemRepository bffRawItemRepository;

    @Autowired
    private BffRawItemMapper bffRawItemMapper;

    @Autowired
    private BffSupplierProductAssocIdMapper bffSupplierProductAssocIdMapper;

    @Autowired
    private ShipmentBoxTypeApplicationService shipmentBoxTypeApplicationService;

    @Autowired
    private BffShipmentBoxTypeMapper bffShipmentBoxTypeMapper;

    @Autowired
    private RawItemQueryService rawItemQueryService;

    @Override
    @Transactional(readOnly = true)
    public Page<BffRawItemDto> when(BffRawItemServiceCommands.GetRawItems c) {
        return PageUtils.toPage(
                bffRawItemRepository.findAllRawItems(PageRequest.of(c.getPage(), c.getSize()), c.getSupplierId(),
                        c.getActive()),
                bffRawItemMapper::toBffRawItemDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BffRawItemDto when(BffRawItemServiceCommands.GetRawItem c) {
        return rawItemQueryService.getRawItemWithoutCache(c.getProductId());
    }

    @Override
    @Transactional
    public String when(BffRawItemServiceCommands.CreateRawItem c) {
        BffRawItemDto rawItem = c.getRawItem();
        if (rawItem == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        if (rawItem.getProductId() != null) {
            rawItem.setProductId(rawItem.getProductId().trim());
            if (!rawItem.getProductId().isEmpty()) {
                if (productApplicationService.get(rawItem.getProductId()) != null) {
                    throw new IllegalArgumentException("The product already exists: " + rawItem.getProductId());
                }
            }
        }
//        String supplierId = rawItem.getSupplierId();
//        if (supplierId == null || supplierId.isBlank()) {
//            throw new IllegalArgumentException("Supplier can't be null");
//        }
//        if (partyApplicationService.get(supplierId) == null) {
//            throw new IllegalArgumentException("SupplierId is not valid.");
//        }
        // if (uomApplicationService.get(c.getRawItem().getQuantityUomId()) == null) {
        // throw new IllegalArgumentException("QuantityUomId is not valid.");
        // }
        // if (c.getRawItem().getSupplierId() != null &&
        // partyApplicationService.get(c.getRawItem().getSupplierId()) == null) {
        // throw new IllegalArgumentException("SupplierId is not valid.");
        // }
        AbstractProductCommand.SimpleCreateProduct createProduct = new AbstractProductCommand.SimpleCreateProduct();
        createProduct.setProductId(rawItem.getProductId() != null ? rawItem.getProductId() : IdUtils.randomId());
        // (rawItem.getProductId()); // NOTE: ignore the productId from the client side?
        createProduct.setProductTypeId(PRODUCT_TYPE_RAW_MATERIAL);
        createProduct.setProductName(rawItem.getProductName());
        createProduct.setSmallImageUrl(rawItem.getSmallImageUrl());
        createProduct.setMediumImageUrl(rawItem.getMediumImageUrl());
        createProduct.setLargeImageUrl(rawItem.getLargeImageUrl());
        createProduct.setQuantityUomId(rawItem.getQuantityUomId());
        // If you have a six-pack of 12oz soda cans you would have quantityIncluded=12,
        // quantityUomId=oz, piecesIncluded=6.
        createProduct.setQuantityIncluded(rawItem.getQuantityIncluded());
        createProduct.setPiecesIncluded(rawItem.getPiecesIncluded() != null ? rawItem.getPiecesIncluded() : 1);
        createProduct.setDescription(rawItem.getDescription());
        createProduct.setBrandName(rawItem.getBrandName());
        createProduct.setProduceVariety(rawItem.getProduceVariety());
        createProduct.setOrganicCertifications(rawItem.getOrganicCertifications());
        createProduct.setCountryOfOrigin(rawItem.getCountryOfOrigin());
        createProduct.setShelfLifeDescription(rawItem.getShelfLifeDescription());
        createProduct.setHandlingInstructions(rawItem.getHandlingInstructions());
        createProduct.setStorageConditions(rawItem.getStorageConditions());
        createProduct.setMaterialCompositionDescription(rawItem.getMaterialCompositionDescription());
        createProduct.setCertificationCodes(rawItem.getCertificationCodes());
        createProduct.setIndividualsPerPackage(rawItem.getIndividualsPerPackage());
        createProduct.setCaseUomId(rawItem.getCaseUomId());
        createProduct.setDimensionsDescription(rawItem.getDimensionsDescription());

        // Weight related fields
        createProduct.setWeightUomId(rawItem.getWeightUomId());
        createProduct.setShippingWeight(rawItem.getShippingWeight());
        createProduct.setProductWeight(rawItem.getProductWeight());
        // Height related fields
        createProduct.setHeightUomId(rawItem.getHeightUomId());
        createProduct.setProductHeight(rawItem.getProductHeight());
        createProduct.setShippingHeight(rawItem.getShippingHeight());
        // Width related fields
        createProduct.setWidthUomId(rawItem.getWidthUomId());
        createProduct.setProductWidth(rawItem.getProductWidth());
        createProduct.setShippingWidth(rawItem.getShippingWidth());
        // Depth related fields
        createProduct.setDepthUomId(rawItem.getDepthUomId());
        createProduct.setProductDepth(rawItem.getProductDepth());
        createProduct.setShippingDepth(rawItem.getShippingDepth());
        // Diameter related fields
        createProduct.setDiameterUomId(rawItem.getDiameterUomId());
        createProduct.setProductDiameter(rawItem.getProductDiameter());
        // 默认情况下（不传值）就是Y
        createProduct.setActive(IndicatorUtils.asIndicatorDefaultYes(rawItem.getActive()));

        if (rawItem.getGtin() != null && !rawItem.getGtin().isBlank()) {
            rawItem.setGtin(rawItem.getGtin().trim());
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_GTIN, rawItem.getGtin(), createProduct);
        }
        if (rawItem.getInternalId() != null && !rawItem.getInternalId().isBlank()) {
            rawItem.setInternalId(rawItem.getInternalId().trim());
            if (bffRawItemRepository.countByIdentificationTypeIdAndIdValue(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID,
                    rawItem.getInternalId()) > 0) {
                throw new IllegalArgumentException(String.format(
                        "Item Number:%s is already in use. Please try a different one.", rawItem.getInternalId()));
            }
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, rawItem.getInternalId(), createProduct);
        }
        if (rawItem.getHsCode() != null && !rawItem.getHsCode().isBlank()) {
            rawItem.setHsCode(rawItem.getHsCode().trim());
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_HS_CODE, rawItem.getHsCode(), createProduct);
        }

        if (rawItem.getDefaultShipmentBoxTypeId() != null) {
            createProduct.setDefaultShipmentBoxTypeId(rawItem.getDefaultShipmentBoxTypeId());
        } else if (rawItem.getDefaultShipmentBoxType() != null) {
            createProduct.setDefaultShipmentBoxTypeId(createShipmentBoxType(rawItem, c));
        }
        createProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : createProduct.getProductId());
        createProduct.setRequesterId(c.getRequesterId());
        productApplicationService.when(createProduct);
        if (rawItem.getSuppliers() != null && !rawItem.getSuppliers().isEmpty()) {
            List<String> supplierIds = new ArrayList<>();
            rawItem.getSuppliers().forEach(supplierRawItem -> {
                if (supplierIds.contains(supplierRawItem.getSupplierId())) {
                    throw new IllegalArgumentException("Duplicate vendor: " + supplierRawItem.getSupplierId());
                }
                supplierIds.add(supplierRawItem.getSupplierId());
                createSupplierProduct(createProduct.getProductId(), supplierRawItem, c);
            });
        }
        return createProduct.getProductId();
    }

    private void addGoodIdentification(String goodIdentificationTypeId, String idValue,
                                       AbstractProductCommand.SimpleCreateProduct createProduct) {
        AbstractGoodIdentificationCommand.SimpleCreateGoodIdentification createGoodIdentification = new AbstractGoodIdentificationCommand.SimpleCreateGoodIdentification();
        createGoodIdentification.setGoodIdentificationTypeId(goodIdentificationTypeId);
        createGoodIdentification.setIdValue(idValue);
        createProduct.getCreateGoodIdentificationCommands().add(createGoodIdentification);
    }

    private String createShipmentBoxType(BffRawItemDto rawItem, Command c) {
        BffShipmentBoxTypeDto shipmentBoxTypeDto = rawItem.getDefaultShipmentBoxType();
        AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType createShipmentBoxType = new AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType();
        createShipmentBoxType.setShipmentBoxTypeId(shipmentBoxTypeDto.getShipmentBoxTypeId() != null
                ? shipmentBoxTypeDto.getShipmentBoxTypeId()
                : IdUtils.randomId());
        createShipmentBoxType.setDescription(shipmentBoxTypeDto.getDescription());
        createShipmentBoxType.setDimensionUomId(shipmentBoxTypeDto.getDimensionUomId());
        createShipmentBoxType.setBoxLength(shipmentBoxTypeDto.getBoxLength());
        createShipmentBoxType.setBoxHeight(shipmentBoxTypeDto.getBoxHeight());
        createShipmentBoxType.setBoxWidth(shipmentBoxTypeDto.getBoxWidth());
        createShipmentBoxType.setWeightUomId(shipmentBoxTypeDto.getWeightUomId());
        createShipmentBoxType.setBoxWeight(shipmentBoxTypeDto.getBoxWeight());
        createShipmentBoxType.setBoxTypeName(shipmentBoxTypeDto.getBoxTypeName());
        createShipmentBoxType.setCommandId(
                c.getCommandId() != null ? c.getCommandId() : createShipmentBoxType.getShipmentBoxTypeId());
        createShipmentBoxType.setRequesterId(c.getRequesterId());
        shipmentBoxTypeApplicationService.when(createShipmentBoxType);
        return createShipmentBoxType.getShipmentBoxTypeId();
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.UpdateRawItem c) {
        if (c.getProductId() == null) {
            throw new IllegalArgumentException("Product id can't be null");
        }
        String productId = c.getProductId();
        ProductState productState = productApplicationService.get(productId);
        if (productState == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        String supplierId = c.getRawItem().getSupplierId();
        if (supplierId == null || supplierId.isBlank()) {
            throw new IllegalArgumentException("Supplier can't be null");
        }
        if (partyApplicationService.get(supplierId) == null) {
            throw new IllegalArgumentException("SupplierId is not valid.");
        }
        BffRawItemDto rawItem = c.getRawItem();
        AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();
        mergePatchProduct.setProductId(productId);
        mergePatchProduct.setProductTypeId(PRODUCT_TYPE_RAW_MATERIAL);
        mergePatchProduct.setVersion(productState.getVersion());
        mergePatchProduct.setProductName(rawItem.getProductName());
        mergePatchProduct.setDescription(rawItem.getDescription());
        mergePatchProduct.setSmallImageUrl(rawItem.getSmallImageUrl());
        mergePatchProduct.setMediumImageUrl(rawItem.getMediumImageUrl());
        mergePatchProduct.setLargeImageUrl(rawItem.getLargeImageUrl());
        mergePatchProduct.setBrandName(rawItem.getBrandName());
        mergePatchProduct.setProduceVariety(rawItem.getProduceVariety());
        mergePatchProduct.setOrganicCertifications(rawItem.getOrganicCertifications());
        mergePatchProduct.setMaterialCompositionDescription(rawItem.getMaterialCompositionDescription());
        mergePatchProduct.setCountryOfOrigin(rawItem.getCountryOfOrigin());
        mergePatchProduct.setCertificationCodes(rawItem.getCertificationCodes());
        mergePatchProduct.setShelfLifeDescription(rawItem.getShelfLifeDescription());
        mergePatchProduct.setHandlingInstructions(rawItem.getHandlingInstructions());
        mergePatchProduct.setStorageConditions(rawItem.getStorageConditions());
        mergePatchProduct.setDimensionsDescription(rawItem.getDimensionsDescription());

        if (rawItem.getCaseUomId() != null) {
            mergePatchProduct.setCaseUomId(rawItem.getCaseUomId());
        }
        if (rawItem.getQuantityUomId() != null)
            mergePatchProduct.setQuantityUomId(rawItem.getQuantityUomId());
        if (rawItem.getQuantityIncluded() != null)
            mergePatchProduct.setQuantityIncluded(rawItem.getQuantityIncluded());
        if (rawItem.getPiecesIncluded() != null)
            mergePatchProduct.setPiecesIncluded(rawItem.getPiecesIncluded() != null ? rawItem.getPiecesIncluded() : 1);
        if (rawItem.getWeightUomId() != null)
            mergePatchProduct.setWeightUomId(rawItem.getWeightUomId());
        if (rawItem.getShippingWeight() != null)
            mergePatchProduct.setShippingWeight(rawItem.getShippingWeight());
        if (rawItem.getProductWeight() != null)
            mergePatchProduct.setProductWeight(rawItem.getProductWeight());

        if (rawItem.getHeightUomId() != null)
            mergePatchProduct.setHeightUomId(rawItem.getHeightUomId());
        if (rawItem.getProductHeight() != null)
            mergePatchProduct.setProductHeight(rawItem.getProductHeight());
        if (rawItem.getShippingHeight() != null)
            mergePatchProduct.setShippingHeight(rawItem.getShippingHeight());

        if (rawItem.getWidthUomId() != null)
            mergePatchProduct.setWidthUomId(rawItem.getWidthUomId());
        if (rawItem.getProductWidth() != null)
            mergePatchProduct.setProductWidth(rawItem.getProductWidth());
        if (rawItem.getShippingWidth() != null)
            mergePatchProduct.setShippingWidth(rawItem.getShippingWidth());

        if (rawItem.getDepthUomId() != null)
            mergePatchProduct.setDepthUomId(rawItem.getDepthUomId());
        if (rawItem.getProductDepth() != null)
            mergePatchProduct.setProductDepth(rawItem.getProductDepth());
        if (rawItem.getShippingDepth() != null)
            mergePatchProduct.setShippingDepth(rawItem.getShippingDepth());

        if (rawItem.getDiameterUomId() != null)
            mergePatchProduct.setDiameterUomId(rawItem.getDiameterUomId());
        if (rawItem.getProductDiameter() != null)
            mergePatchProduct.setProductDiameter(rawItem.getProductDiameter());
        if (rawItem.getIndividualsPerPackage() != null) {
            mergePatchProduct.setIndividualsPerPackage(rawItem.getIndividualsPerPackage());
        }
        mergePatchProduct.setActive(IndicatorUtils.asIndicatorDefaultYes(rawItem.getActive()));

        mergePatchProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchProduct.setRequesterId(c.getRequesterId());
        if (rawItem.getDefaultShipmentBoxTypeId() != null) {
            mergePatchProduct.setDefaultShipmentBoxTypeId(rawItem.getDefaultShipmentBoxTypeId());
        } else if (rawItem.getDefaultShipmentBoxType() != null) {
            mergePatchProduct.setDefaultShipmentBoxTypeId(createShipmentBoxType(rawItem, c));
        }

        if (rawItem.getInternalId() != null && !rawItem.getInternalId().isBlank()) {
            rawItem.setInternalId(rawItem.getInternalId().trim());
            String itemId = bffRawItemRepository
                    .queryProductIdByIdentificationTypeIdAndIdValue(
                            GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, rawItem.getInternalId());
            if (itemId != null && !itemId.equals(productId)) {
                throw new IllegalArgumentException(String.format(
                        "Item Number:%s is already in use. Please try a different one.", rawItem.getInternalId()));
            }
        }
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_GTIN, rawItem.getGtin());
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_INTERNAL_ID,
                rawItem.getInternalId());
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_HS_CODE,
                rawItem.getHsCode());

        productApplicationService.when(mergePatchProduct);

        // 这里不需要使用完整的 ID 全等匹配查询
        BffSupplierProductAssocProjection existingAssoc = bffRawItemRepository.findSupplierProductAssociation(
                productId,
                supplierId,
                DEFAULT_CURRENCY_UOM_ID,
                BigDecimal.ZERO,
                OffsetDateTime.now());
        SupplierProductAssocId supplierProductAssocId;
        if (existingAssoc == null) {
            //createSupplierProduct(productId, supplierId, c);
        } else {
            supplierProductAssocId = bffSupplierProductAssocIdMapper.toSupplierProductAssocId(existingAssoc);
            AbstractSupplierProductCommand.SimpleMergePatchSupplierProduct mergePatchSupplierProduct = new AbstractSupplierProductCommand.SimpleMergePatchSupplierProduct();
            mergePatchSupplierProduct.setSupplierProductAssocId(supplierProductAssocId);
            mergePatchSupplierProduct.setAvailableThruDate(OffsetDateTime.now().plusYears(100));
            mergePatchSupplierProduct.setVersion(existingAssoc.getVersion());
            mergePatchSupplierProduct.setCommandId(UUID.randomUUID().toString());
            mergePatchSupplierProduct.setRequesterId(c.getRequesterId());
            supplierProductApplicationService.when(mergePatchSupplierProduct);
        }
    }

    private void updateProductIdentification(AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct,
                                             ProductState productState, String identificationTypeId, String newValue) {
        Optional<GoodIdentificationState> existingGtin = productState.getGoodIdentifications().stream()
                .filter(x -> x.getGoodIdentificationTypeId().equals(identificationTypeId))
                .findFirst();
        if (newValue != null && !newValue.isBlank()) {
            newValue = newValue.trim();
            if (existingGtin.isPresent()) {
                if (!newValue.equals(existingGtin.get().getIdValue())) {
                    GoodIdentificationCommand.MergePatchGoodIdentification mergePatchGoodIdentification = mergePatchProduct
                            .newMergePatchGoodIdentification();
                    mergePatchGoodIdentification.setGoodIdentificationTypeId(identificationTypeId);
                    mergePatchGoodIdentification.setIdValue(newValue);
                    mergePatchProduct.getGoodIdentificationCommands().add(mergePatchGoodIdentification);
                }
            } else {
                GoodIdentificationCommand.CreateGoodIdentification createGoodIdentification = mergePatchProduct
                        .newCreateGoodIdentification();
                createGoodIdentification.setGoodIdentificationTypeId(identificationTypeId);
                createGoodIdentification.setIdValue(newValue);
                mergePatchProduct.getGoodIdentificationCommands().add(createGoodIdentification);
            }
        } else {
            if (existingGtin.isPresent()) {// 原来有，现在没有，做删除处理
                GoodIdentificationCommand.RemoveGoodIdentification removeGoodIdentification = mergePatchProduct
                        .newRemoveGoodIdentification();
                removeGoodIdentification.setGoodIdentificationTypeId(identificationTypeId);
                mergePatchProduct.getGoodIdentificationCommands().add(removeGoodIdentification);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.ActivateRawItem c) {
        updateProductActiveStatus(c.getProductId(), c.getActive() != null && c.getActive(), c);
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.BatchActivateRawItems c) {
        Arrays.stream(c.getProductIds()).forEach(productId -> updateProductActiveStatus(productId, true, c));
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.BatchDeactivateRawItems c) {
        Arrays.stream(c.getProductIds()).forEach(productId -> updateProductActiveStatus(productId, false, c));
    }

    private void updateProductActiveStatus(String productId, boolean active, Command c) {
        ProductState productState = productApplicationService.get(productId);
        if (productState == null) {
            throw new IllegalArgumentException("Raw item not found: " + productId);
        }

        AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();
        mergePatchProduct.setProductId(productId);
        mergePatchProduct.setProductTypeId(PRODUCT_TYPE_RAW_MATERIAL);
        mergePatchProduct.setVersion(productState.getVersion());
        mergePatchProduct.setActive(active ? INDICATOR_YES : INDICATOR_NO);
        mergePatchProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchProduct.setRequesterId(c.getRequesterId());

        productApplicationService.when(mergePatchProduct);
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.BatchAddRawItems c) {
        if (c.getRawItems() == null) {
            return;
        }

        for (BffRawItemDto rawItem : c.getRawItems()) {
            // 创建 CreateRawItem 命令
            BffRawItemServiceCommands.CreateRawItem createCommand = new BffRawItemServiceCommands.CreateRawItem();
            createCommand.setRawItem(rawItem);
            createCommand.setRequesterId(c.getRequesterId());
            createCommand.setCommandId(UUID.randomUUID().toString());

            // 调用已有的创建方法
            when(createCommand);
        }
    }

    private void createSupplierProduct(String productId, BffSupplierRawItemDto supplierRawItem, Command c) {
        PartyState partyState = partyApplicationService.get(supplierRawItem.getSupplierId());
        if (partyState == null) {
            throw new IllegalArgumentException(String.format("Vendor not found:%s ", supplierRawItem.getSupplierId()));
        }
        SupplierProductAssocId supplierProductAssocId = new SupplierProductAssocId();
        supplierProductAssocId.setProductId(productId);
        supplierProductAssocId.setPartyId(supplierRawItem.getSupplierId());
        supplierProductAssocId.setCurrencyUomId(DEFAULT_CURRENCY_UOM_ID);
        supplierProductAssocId.setAvailableFromDate(OffsetDateTime.now());
        supplierProductAssocId.setMinimumOrderQuantity(BigDecimal.ZERO);
        AbstractSupplierProductCommand.SimpleCreateSupplierProduct createSupplierProduct = new AbstractSupplierProductCommand.SimpleCreateSupplierProduct();
        createSupplierProduct.setActive(IndicatorUtils.INDICATOR_YES);
        createSupplierProduct.setBrandName(supplierRawItem.getBrandName());
        createSupplierProduct.setGtin(supplierRawItem.getGtin());
        createSupplierProduct.setQuantityIncluded(supplierRawItem.getQuantityIncluded());
        createSupplierProduct.setProductWeight(supplierRawItem.getProductWeight());
        createSupplierProduct.setCaseUomId(supplierRawItem.getCaseUomId());
        createSupplierProduct.setOrganicCertifications(supplierRawItem.getOrganicCertifications());
        createSupplierProduct.setMaterialCompositionDescription(supplierRawItem.getMaterialCompositionDescription());
        createSupplierProduct.setCertificationCodes(supplierRawItem.getCertificationCodes());
        createSupplierProduct.setCountryOfOrigin(supplierRawItem.getCountryOfOrigin());
        createSupplierProduct.setIndividualsPerPackage(supplierRawItem.getIndividualsPerPackage());
        createSupplierProduct.setSupplierProductAssocId(supplierProductAssocId);
        createSupplierProduct.setAvailableThruDate(OffsetDateTime.now().plusYears(100));
        createSupplierProduct.setCommandId(UUID.randomUUID().toString());
        createSupplierProduct.setRequesterId(c.getRequesterId());
        supplierProductApplicationService.when(createSupplierProduct);
    }
}
