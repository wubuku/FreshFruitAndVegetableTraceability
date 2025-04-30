package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffProductDto;
import org.dddml.ffvtraceability.domain.BffSimpleProductDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.mapper.BffProductMapper;
import org.dddml.ffvtraceability.domain.mapper.BffShipmentBoxTypeMapper;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierProductAssocIdMapper;
import org.dddml.ffvtraceability.domain.party.PartyApplicationService;
import org.dddml.ffvtraceability.domain.product.*;
import org.dddml.ffvtraceability.domain.repository.BffProductRepository;
import org.dddml.ffvtraceability.domain.shipmentboxtype.ShipmentBoxTypeApplicationService;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductApplicationService;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.IndicatorUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.dddml.ffvtraceability.domain.constants.BffProductConstants.*;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
public class BffProductApplicationServiceImpl implements BffProductApplicationService {

    @Autowired
    private ProductApplicationService productApplicationService;

    @Autowired
    private UomApplicationService uomApplicationService;

    @Autowired
    private SupplierProductApplicationService supplierProductApplicationService;

    @Autowired
    private PartyApplicationService partyApplicationService;

    @Autowired
    private BffProductRepository bffProductRepository;

    @Autowired
    private BffProductMapper bffProductMapper;

    @Autowired
    private BffSupplierProductAssocIdMapper bffSupplierProductAssocIdMapper;

    @Autowired
    private ShipmentBoxTypeApplicationService shipmentBoxTypeApplicationService;

    @Autowired
    private BffShipmentBoxTypeMapper bffShipmentBoxTypeMapper;

    @Autowired
    private ProductQueryService productQueryService;

    @Override
    @Transactional(readOnly = true)
    public Page<BffProductDto> when(BffProductServiceCommands.GetProducts c) {
        return PageUtils.toPage(bffProductRepository.findAllProducts(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductTypeId(), c.getSupplierId(), c.getActive()),
                bffProductMapper::toBffProductDto);
    }

    @Override
    public Page<BffSimpleProductDto> when(BffProductServiceCommands.GetProductsForAdjustInventory c) {
        return PageUtils.toPage(bffProductRepository.findAllSimpleProducts(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductTypeId(), c.getFacilityId()),
                bffProductMapper::toBffSimpleProductDto);
    }

    @Override
    public Page<BffSimpleProductDto> when(BffProductServiceCommands.GetProductsByKeyword c) {
        return PageUtils.toPage(bffProductRepository.findSimpleProductsByKeyword(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductTypeId(),c.getFacilityId(), c.getProductKeyword()),
                bffProductMapper::toBffSimpleProductDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BffProductDto when(BffProductServiceCommands.GetProduct c) {
        return productQueryService.getProductWithoutCache(c.getProductId());
    }

    @Override
    @Transactional
    public String when(BffProductServiceCommands.CreateProduct c) {
        BffProductDto product = c.getProduct();
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        if (product.getProductTypeId() == null || product.getProductTypeId().isBlank()) {
            throw new IllegalArgumentException("Product type can't be null");
        }
        if (!PRODUCT_TYPES_NOT_RAW.contains(product.getProductTypeId())) {
            throw new IllegalArgumentException("Product type is error");
        }
        if (product.getProductId() != null) {
            product.setProductId(product.getProductId().trim());
            if (!product.getProductId().isEmpty()) {
                if (productApplicationService.get(product.getProductId()) != null) {
                    throw new IllegalArgumentException("The product already exists: " + product.getProductId());
                }
            }
        }
//        String supplierId = product.getSupplierId();
//        if (supplierId == null || supplierId.isBlank()) {
//            throw new IllegalArgumentException("Supplier can't be null");
//        }
//        if (partyApplicationService.get(supplierId) == null) {
//            throw new IllegalArgumentException("SupplierId is not valid.");
//        }
        // if (uomApplicationService.get(c.getProduct().getQuantityUomId()) == null) {
        // throw new IllegalArgumentException("QuantityUomId is not valid.");
        // }
        // if (c.getProduct().getSupplierId() != null &&
        // partyApplicationService.get(c.getProduct().getSupplierId()) == null) {
        // throw new IllegalArgumentException("SupplierId is not valid.");
        // }
        AbstractProductCommand.SimpleCreateProduct createProduct = new AbstractProductCommand.SimpleCreateProduct();
        createProduct.setProductId(product.getProductId() != null ? product.getProductId() : IdUtils.randomId());
        // (Product.getProductId()); // NOTE: ignore the productId from the client side?
        createProduct.setProductTypeId(product.getProductTypeId());
        createProduct.setProductName(product.getProductName());
        createProduct.setSmallImageUrl(product.getSmallImageUrl());
        createProduct.setMediumImageUrl(product.getMediumImageUrl());
        createProduct.setLargeImageUrl(product.getLargeImageUrl());
        createProduct.setQuantityUomId(product.getQuantityUomId());
        // If you have a six-pack of 12oz soda cans you would have quantityIncluded=12,
        // quantityUomId=oz, piecesIncluded=6.
        createProduct.setQuantityIncluded(product.getQuantityIncluded());
        createProduct.setPiecesIncluded(product.getPiecesIncluded() != null ? product.getPiecesIncluded() : 1);
        createProduct.setDescription(product.getDescription());
        createProduct.setBrandName(product.getBrandName());
        createProduct.setProduceVariety(product.getProduceVariety());
        createProduct.setOrganicCertifications(product.getOrganicCertifications());
        createProduct.setCountryOfOrigin(product.getCountryOfOrigin());
        createProduct.setShelfLifeDescription(product.getShelfLifeDescription());
        createProduct.setHandlingInstructions(product.getHandlingInstructions());
        createProduct.setStorageConditions(product.getStorageConditions());
        createProduct.setMaterialCompositionDescription(product.getMaterialCompositionDescription());
        createProduct.setCertificationCodes(product.getCertificationCodes());
        createProduct.setIndividualsPerPackage(product.getIndividualsPerPackage());
        createProduct.setCaseUomId(product.getCaseUomId());
        createProduct.setDimensionsDescription(product.getDimensionsDescription());

        // Weight related fields
        createProduct.setWeightUomId(product.getWeightUomId());
        createProduct.setShippingWeight(product.getShippingWeight());
        createProduct.setProductWeight(product.getProductWeight());
        // Height related fields
        createProduct.setHeightUomId(product.getHeightUomId());
        createProduct.setProductHeight(product.getProductHeight());
        createProduct.setShippingHeight(product.getShippingHeight());
        // Width related fields
        createProduct.setWidthUomId(product.getWidthUomId());
        createProduct.setProductWidth(product.getProductWidth());
        createProduct.setShippingWidth(product.getShippingWidth());
        // Depth related fields
        createProduct.setDepthUomId(product.getDepthUomId());
        createProduct.setProductDepth(product.getProductDepth());
        createProduct.setShippingDepth(product.getShippingDepth());
        // Diameter related fields
        createProduct.setDiameterUomId(product.getDiameterUomId());
        createProduct.setProductDiameter(product.getProductDiameter());
        // 默认情况下（不传值）就是Y
        createProduct.setActive(IndicatorUtils.asIndicatorDefaultYes(product.getActive()));

        if (product.getGtin() != null && !product.getGtin().isBlank()) {
            product.setGtin(product.getGtin().trim());
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_GTIN, product.getGtin(), createProduct);
        }
        if (product.getInternalId() != null && !product.getInternalId().isBlank()) {
            product.setInternalId(product.getInternalId().trim());
            if (bffProductRepository.countByIdentificationTypeIdAndIdValue(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, product.getInternalId()) > 0) {
                throw new IllegalArgumentException(String.format("Item Number:%s is already in use. Please try a different one.", product.getInternalId()));
            }
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, product.getInternalId(), createProduct);
        }
        if (product.getHsCode() != null && !product.getHsCode().isBlank()) {
            product.setHsCode(product.getHsCode().trim());
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_HS_CODE, product.getHsCode(), createProduct);
        }

//        if (product.getDefaultShipmentBoxTypeId() != null) {
//            createProduct.setDefaultShipmentBoxTypeId(product.getDefaultShipmentBoxTypeId());
//        } else if (product.getDefaultShipmentBoxType() != null) {
//            createProduct.setDefaultShipmentBoxTypeId(createShipmentBoxType(product, c));
//        }
        createProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : createProduct.getProductId());
        createProduct.setRequesterId(c.getRequesterId());
        productApplicationService.when(createProduct);

//        if (product.getSupplierId() != null) {
//            createSupplierProduct(createProduct.getProductId(), product.getSupplierId(), c);
//        }
        return createProduct.getProductId();
    }

    private void addGoodIdentification(String goodIdentificationTypeId, String idValue, AbstractProductCommand.SimpleCreateProduct createProduct) {
        AbstractGoodIdentificationCommand.SimpleCreateGoodIdentification createGoodIdentification = new AbstractGoodIdentificationCommand.SimpleCreateGoodIdentification();
        createGoodIdentification.setGoodIdentificationTypeId(goodIdentificationTypeId);
        createGoodIdentification.setIdValue(idValue);
        createProduct.getCreateGoodIdentificationCommands().add(createGoodIdentification);
    }

//    private String createShipmentBoxType(BffProductDto Product, Command c) {
//        BffShipmentBoxTypeDto shipmentBoxTypeDto = Product.getDefaultShipmentBoxType();
//        AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType createShipmentBoxType = new AbstractShipmentBoxTypeCommand.SimpleCreateShipmentBoxType();
//        createShipmentBoxType.setShipmentBoxTypeId(shipmentBoxTypeDto.getShipmentBoxTypeId() != null ? shipmentBoxTypeDto.getShipmentBoxTypeId() : IdUtils.randomId());
//        createShipmentBoxType.setDescription(shipmentBoxTypeDto.getDescription());
//        createShipmentBoxType.setDimensionUomId(shipmentBoxTypeDto.getDimensionUomId());
//        createShipmentBoxType.setBoxLength(shipmentBoxTypeDto.getBoxLength());
//        createShipmentBoxType.setBoxHeight(shipmentBoxTypeDto.getBoxHeight());
//        createShipmentBoxType.setBoxWidth(shipmentBoxTypeDto.getBoxWidth());
//        createShipmentBoxType.setWeightUomId(shipmentBoxTypeDto.getWeightUomId());
//        createShipmentBoxType.setBoxWeight(shipmentBoxTypeDto.getBoxWeight());
//        createShipmentBoxType.setBoxTypeName(shipmentBoxTypeDto.getBoxTypeName());
//        createShipmentBoxType.setCommandId(c.getCommandId() != null ? c.getCommandId() : createShipmentBoxType.getShipmentBoxTypeId());
//        createShipmentBoxType.setRequesterId(c.getRequesterId());
//        shipmentBoxTypeApplicationService.when(createShipmentBoxType);
//        return createShipmentBoxType.getShipmentBoxTypeId();
//    }

    @Override
    @Transactional
    public void when(BffProductServiceCommands.UpdateProduct c) {
        if (c.getProductId() == null) {
            throw new IllegalArgumentException("Product id can't be null");
        }
        BffProductDto product = c.getProduct();
        if (product.getProductTypeId() == null || product.getProductTypeId().isBlank()) {
            throw new IllegalArgumentException("Product type can't be null");
        }
        if (!PRODUCT_TYPES_NOT_RAW.contains(product.getProductTypeId())) {
            throw new IllegalArgumentException("Product type is error");
        }
        String productId = c.getProductId();
        ProductState productState = productApplicationService.get(productId);
        if (productState == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
//        String supplierId = c.getProduct().getSupplierId();
//        if (supplierId == null || supplierId.isBlank()) {
//            throw new IllegalArgumentException("Supplier can't be null");
//        }
//        if (partyApplicationService.get(supplierId) == null) {
//            throw new IllegalArgumentException("SupplierId is not valid.");
//        }
        AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();
        mergePatchProduct.setProductId(productId);
        mergePatchProduct.setProductTypeId(product.getProductTypeId());
        mergePatchProduct.setVersion(productState.getVersion());
        mergePatchProduct.setProductName(product.getProductName());
        mergePatchProduct.setDescription(product.getDescription());
        mergePatchProduct.setSmallImageUrl(product.getSmallImageUrl());
        mergePatchProduct.setMediumImageUrl(product.getMediumImageUrl());
        mergePatchProduct.setLargeImageUrl(product.getLargeImageUrl());
        mergePatchProduct.setBrandName(product.getBrandName());
        mergePatchProduct.setProduceVariety(product.getProduceVariety());
        mergePatchProduct.setOrganicCertifications(product.getOrganicCertifications());
        mergePatchProduct.setMaterialCompositionDescription(product.getMaterialCompositionDescription());
        mergePatchProduct.setCountryOfOrigin(product.getCountryOfOrigin());
        mergePatchProduct.setCertificationCodes(product.getCertificationCodes());
        mergePatchProduct.setShelfLifeDescription(product.getShelfLifeDescription());
        mergePatchProduct.setHandlingInstructions(product.getHandlingInstructions());
        mergePatchProduct.setStorageConditions(product.getStorageConditions());
        mergePatchProduct.setDimensionsDescription(product.getDimensionsDescription());

        if (product.getCaseUomId() != null) {
            mergePatchProduct.setCaseUomId(product.getCaseUomId());
        }
        if (product.getQuantityUomId() != null) mergePatchProduct.setQuantityUomId(product.getQuantityUomId());
        if (product.getQuantityIncluded() != null) mergePatchProduct.setQuantityIncluded(product.getQuantityIncluded());
        if (product.getPiecesIncluded() != null)
            mergePatchProduct.setPiecesIncluded(product.getPiecesIncluded() != null ? product.getPiecesIncluded() : 1);
        if (product.getWeightUomId() != null) mergePatchProduct.setWeightUomId(product.getWeightUomId());
        if (product.getShippingWeight() != null) mergePatchProduct.setShippingWeight(product.getShippingWeight());
        if (product.getProductWeight() != null) mergePatchProduct.setProductWeight(product.getProductWeight());

        if (product.getHeightUomId() != null) mergePatchProduct.setHeightUomId(product.getHeightUomId());
        if (product.getProductHeight() != null) mergePatchProduct.setProductHeight(product.getProductHeight());
        if (product.getShippingHeight() != null) mergePatchProduct.setShippingHeight(product.getShippingHeight());

        if (product.getWidthUomId() != null) mergePatchProduct.setWidthUomId(product.getWidthUomId());
        if (product.getProductWidth() != null) mergePatchProduct.setProductWidth(product.getProductWidth());
        if (product.getShippingWidth() != null) mergePatchProduct.setShippingWidth(product.getShippingWidth());

        if (product.getDepthUomId() != null) mergePatchProduct.setDepthUomId(product.getDepthUomId());
        if (product.getProductDepth() != null) mergePatchProduct.setProductDepth(product.getProductDepth());
        if (product.getShippingDepth() != null) mergePatchProduct.setShippingDepth(product.getShippingDepth());

        if (product.getDiameterUomId() != null) mergePatchProduct.setDiameterUomId(product.getDiameterUomId());
        if (product.getProductDiameter() != null) mergePatchProduct.setProductDiameter(product.getProductDiameter());
        if (product.getIndividualsPerPackage() != null) {
            mergePatchProduct.setIndividualsPerPackage(product.getIndividualsPerPackage());
        }
        mergePatchProduct.setActive(IndicatorUtils.asIndicatorDefaultYes(product.getActive()));

        mergePatchProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchProduct.setRequesterId(c.getRequesterId());
//        if (product.getDefaultShipmentBoxTypeId() != null) {
//            mergePatchProduct.setDefaultShipmentBoxTypeId(product.getDefaultShipmentBoxTypeId());
//        } else if (product.getDefaultShipmentBoxType() != null) {
//            mergePatchProduct.setDefaultShipmentBoxTypeId(createShipmentBoxType(product, c));
//        }

        if (product.getInternalId() != null && !product.getInternalId().isBlank()) {
            product.setInternalId(product.getInternalId().trim());
            String itemId = bffProductRepository.queryProductIdByIdentificationTypeIdAndIdValue(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, product.getInternalId());
            if (itemId != null && !itemId.equals(productId)) {
                throw new IllegalArgumentException(String.format("Item Number:%s is already in use. Please try a different one.", product.getInternalId()));
            }
        }
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_GTIN, product.getGtin());
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, product.getInternalId());
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_HS_CODE, product.getHsCode());

        productApplicationService.when(mergePatchProduct);

        // 这里不需要在与供应商有关联关关系
//        BffSupplierProductAssocProjection existingAssoc = bffProductRepository.findSupplierProductAssociation(productId, supplierId, DEFAULT_CURRENCY_UOM_ID, BigDecimal.ZERO, OffsetDateTime.now());
//        SupplierProductAssocId supplierProductAssocId;
//        if (existingAssoc == null) {
//            createSupplierProduct(productId, supplierId, c);
//        } else {
//            supplierProductAssocId = bffSupplierProductAssocIdMapper.toSupplierProductAssocId(existingAssoc);
//            AbstractSupplierProductCommand.SimpleMergePatchSupplierProduct mergePatchSupplierProduct = new AbstractSupplierProductCommand.SimpleMergePatchSupplierProduct();
//            mergePatchSupplierProduct.setSupplierProductAssocId(supplierProductAssocId);
//            mergePatchSupplierProduct.setAvailableThruDate(OffsetDateTime.now().plusYears(100));
//            mergePatchSupplierProduct.setVersion(existingAssoc.getVersion());
//            mergePatchSupplierProduct.setCommandId(UUID.randomUUID().toString());
//            mergePatchSupplierProduct.setRequesterId(c.getRequesterId());
//            supplierProductApplicationService.when(mergePatchSupplierProduct);
//        }
    }

    private void updateProductIdentification(AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct, ProductState productState, String identificationTypeId, String newValue) {
        Optional<GoodIdentificationState> existingGtin = productState.getGoodIdentifications().stream().filter(x -> x.getGoodIdentificationTypeId().equals(identificationTypeId)).findFirst();
        if (newValue != null && !newValue.isBlank()) {
            newValue = newValue.trim();
            if (existingGtin.isPresent()) {
                if (!newValue.equals(existingGtin.get().getIdValue())) {
                    GoodIdentificationCommand.MergePatchGoodIdentification mergePatchGoodIdentification = mergePatchProduct.newMergePatchGoodIdentification();
                    mergePatchGoodIdentification.setGoodIdentificationTypeId(identificationTypeId);
                    mergePatchGoodIdentification.setIdValue(newValue);
                    mergePatchProduct.getGoodIdentificationCommands().add(mergePatchGoodIdentification);
                }
            } else {
                GoodIdentificationCommand.CreateGoodIdentification createGoodIdentification = mergePatchProduct.newCreateGoodIdentification();
                createGoodIdentification.setGoodIdentificationTypeId(identificationTypeId);
                createGoodIdentification.setIdValue(newValue);
                mergePatchProduct.getGoodIdentificationCommands().add(createGoodIdentification);
            }
        } else {
            if (existingGtin.isPresent()) {// 原来有，现在没有，做删除处理
                GoodIdentificationCommand.RemoveGoodIdentification removeGoodIdentification = mergePatchProduct.newRemoveGoodIdentification();
                removeGoodIdentification.setGoodIdentificationTypeId(identificationTypeId);
                mergePatchProduct.getGoodIdentificationCommands().add(removeGoodIdentification);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffProductServiceCommands.ActivateProduct c) {
        updateProductActiveStatus(c.getProductId(), c.getActive() != null && c.getActive(), c);
    }

    @Override
    @Transactional
    public void when(BffProductServiceCommands.BatchActivateProducts c) {
        Arrays.stream(c.getProductIds()).forEach(productId -> updateProductActiveStatus(productId, true, c));
    }

    @Override
    @Transactional
    public void when(BffProductServiceCommands.BatchDeactivateProducts c) {
        Arrays.stream(c.getProductIds()).forEach(productId -> updateProductActiveStatus(productId, false, c));
    }

    private void updateProductActiveStatus(String productId, boolean active, Command c) {
        ProductState productState = productApplicationService.get(productId);
        if (productState == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }

        AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();
        mergePatchProduct.setProductId(productId);
        //mergePatchProduct.setProductTypeId(PRODUCT_TYPE_RAW_MATERIAL);
        mergePatchProduct.setVersion(productState.getVersion());
        mergePatchProduct.setActive(active ? INDICATOR_YES : INDICATOR_NO);
        mergePatchProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchProduct.setRequesterId(c.getRequesterId());

        productApplicationService.when(mergePatchProduct);
    }

    @Override
    @Transactional
    public void when(BffProductServiceCommands.BatchAddProducts c) {
        if (c.getProducts() == null) {
            return;
        }

        for (BffProductDto Product : c.getProducts()) {
            // 创建 CreateProduct 命令
            BffProductServiceCommands.CreateProduct createCommand = new BffProductServiceCommands.CreateProduct();
            createCommand.setProduct(Product);
            createCommand.setRequesterId(c.getRequesterId());
            createCommand.setCommandId(UUID.randomUUID().toString());

            // 调用已有的创建方法
            when(createCommand);
        }
    }

//    private void createSupplierProduct(String productId, String supplierId, Command c) {
//        SupplierProductAssocId supplierProductAssocId = new SupplierProductAssocId();
//        supplierProductAssocId.setProductId(productId);
//        supplierProductAssocId.setPartyId(supplierId);
//        supplierProductAssocId.setCurrencyUomId(DEFAULT_CURRENCY_UOM_ID);
//        supplierProductAssocId.setAvailableFromDate(OffsetDateTime.now());
//        supplierProductAssocId.setMinimumOrderQuantity(BigDecimal.ZERO);
//        AbstractSupplierProductCommand.SimpleCreateSupplierProduct createSupplierProduct = new AbstractSupplierProductCommand.SimpleCreateSupplierProduct();
//        createSupplierProduct.setSupplierProductAssocId(supplierProductAssocId);
//        createSupplierProduct.setAvailableThruDate(OffsetDateTime.now().plusYears(100));
//        createSupplierProduct.setCommandId(UUID.randomUUID().toString());
//        createSupplierProduct.setRequesterId(c.getRequesterId());
//        supplierProductApplicationService.when(createSupplierProduct);
//    }
}
