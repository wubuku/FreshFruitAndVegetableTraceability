package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffProductDto;
import org.dddml.ffvtraceability.domain.BffShipmentBoxTypeDto;
import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.mapper.BffProductMapper;
import org.dddml.ffvtraceability.domain.mapper.BffShipmentBoxTypeMapper;
import org.dddml.ffvtraceability.domain.mapper.BffSupplierProductAssocIdMapper;
import org.dddml.ffvtraceability.domain.party.PartyApplicationService;
import org.dddml.ffvtraceability.domain.product.*;
import org.dddml.ffvtraceability.domain.repository.BffProductRepository;
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
    private ProductQueryService ProductQueryService;

    @Override
    @Transactional(readOnly = true)
    public Page<BffProductDto> when(BffProductServiceCommands.GetProducts c) {
        return PageUtils.toPage(
                bffProductRepository.findAllRawItems(PageRequest.of(c.getPage(), c.getSize()), c.getSupplierId(),
                        c.getActive()),
                bffProductMapper::toBffProductDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BffProductDto when(BffProductServiceCommands.GetProduct c) {
        //return ProductQueryService.getProductWithoutCache(c.getProductId());
        return null;
    }

    @Override
    @Transactional
    public String when(BffProductServiceCommands.CreateProduct c) {
        BffProductDto Product = c.getProduct();
        if (Product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        if (Product.getProductId() != null) {
            Product.setProductId(Product.getProductId().trim());
            if (!Product.getProductId().isEmpty()) {
                if (productApplicationService.get(Product.getProductId()) != null) {
                    throw new IllegalArgumentException("The product already exists: " + Product.getProductId());
                }
            }
        }
        String supplierId = Product.getSupplierId();
        if (supplierId == null || supplierId.isBlank()) {
            throw new IllegalArgumentException("Supplier can't be null");
        }
        if (partyApplicationService.get(supplierId) == null) {
            throw new IllegalArgumentException("SupplierId is not valid.");
        }
        // if (uomApplicationService.get(c.getProduct().getQuantityUomId()) == null) {
        // throw new IllegalArgumentException("QuantityUomId is not valid.");
        // }
        // if (c.getProduct().getSupplierId() != null &&
        // partyApplicationService.get(c.getProduct().getSupplierId()) == null) {
        // throw new IllegalArgumentException("SupplierId is not valid.");
        // }
        AbstractProductCommand.SimpleCreateProduct createProduct = new AbstractProductCommand.SimpleCreateProduct();
        createProduct.setProductId(Product.getProductId() != null ? Product.getProductId() : IdUtils.randomId());
        // (Product.getProductId()); // NOTE: ignore the productId from the client side?
        createProduct.setProductTypeId(PRODUCT_TYPE_RAW_MATERIAL);
        createProduct.setProductName(Product.getProductName());
        createProduct.setSmallImageUrl(Product.getSmallImageUrl());
        createProduct.setMediumImageUrl(Product.getMediumImageUrl());
        createProduct.setLargeImageUrl(Product.getLargeImageUrl());
        createProduct.setQuantityUomId(Product.getQuantityUomId());
        // If you have a six-pack of 12oz soda cans you would have quantityIncluded=12,
        // quantityUomId=oz, piecesIncluded=6.
        createProduct.setQuantityIncluded(Product.getQuantityIncluded());
        createProduct.setPiecesIncluded(Product.getPiecesIncluded() != null ? Product.getPiecesIncluded() : 1);
        createProduct.setDescription(Product.getDescription());
        createProduct.setBrandName(Product.getBrandName());
        createProduct.setProduceVariety(Product.getProduceVariety());
        createProduct.setOrganicCertifications(Product.getOrganicCertifications());
        createProduct.setCountryOfOrigin(Product.getCountryOfOrigin());
        createProduct.setShelfLifeDescription(Product.getShelfLifeDescription());
        createProduct.setHandlingInstructions(Product.getHandlingInstructions());
        createProduct.setStorageConditions(Product.getStorageConditions());
        createProduct.setMaterialCompositionDescription(Product.getMaterialCompositionDescription());
        createProduct.setCertificationCodes(Product.getCertificationCodes());
        createProduct.setIndividualsPerPackage(Product.getIndividualsPerPackage());
        createProduct.setCaseUomId(Product.getCaseUomId());
        createProduct.setDimensionsDescription(Product.getDimensionsDescription());

        // Weight related fields
        createProduct.setWeightUomId(Product.getWeightUomId());
        createProduct.setShippingWeight(Product.getShippingWeight());
        createProduct.setProductWeight(Product.getProductWeight());
        // Height related fields
        createProduct.setHeightUomId(Product.getHeightUomId());
        createProduct.setProductHeight(Product.getProductHeight());
        createProduct.setShippingHeight(Product.getShippingHeight());
        // Width related fields
        createProduct.setWidthUomId(Product.getWidthUomId());
        createProduct.setProductWidth(Product.getProductWidth());
        createProduct.setShippingWidth(Product.getShippingWidth());
        // Depth related fields
        createProduct.setDepthUomId(Product.getDepthUomId());
        createProduct.setProductDepth(Product.getProductDepth());
        createProduct.setShippingDepth(Product.getShippingDepth());
        // Diameter related fields
        createProduct.setDiameterUomId(Product.getDiameterUomId());
        createProduct.setProductDiameter(Product.getProductDiameter());
        // 默认情况下（不传值）就是Y
        createProduct.setActive(IndicatorUtils.asIndicatorDefaultYes(Product.getActive()));

        if (Product.getGtin() != null && !Product.getGtin().isBlank()) {
            Product.setGtin(Product.getGtin().trim());
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_GTIN, Product.getGtin(), createProduct);
        }
        if (Product.getInternalId() != null && !Product.getInternalId().isBlank()) {
            Product.setInternalId(Product.getInternalId().trim());
            if (bffProductRepository.countByIdentificationTypeIdAndIdValue(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID,
                    Product.getInternalId()) > 0) {
                throw new IllegalArgumentException(String.format(
                        "Item Number:%s is already in use. Please try a different one.", Product.getInternalId()));
            }
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, Product.getInternalId(), createProduct);
        }
        if (Product.getHsCode() != null && !Product.getHsCode().isBlank()) {
            Product.setHsCode(Product.getHsCode().trim());
            addGoodIdentification(GOOD_IDENTIFICATION_TYPE_HS_CODE, Product.getHsCode(), createProduct);
        }

        if (Product.getDefaultShipmentBoxTypeId() != null) {
            createProduct.setDefaultShipmentBoxTypeId(Product.getDefaultShipmentBoxTypeId());
        } else if (Product.getDefaultShipmentBoxType() != null) {
            createProduct.setDefaultShipmentBoxTypeId(createShipmentBoxType(Product, c));
        }
        createProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : createProduct.getProductId());
        createProduct.setRequesterId(c.getRequesterId());
        productApplicationService.when(createProduct);

        if (Product.getSupplierId() != null) {
            createSupplierProduct(createProduct.getProductId(), Product.getSupplierId(), c);
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

    private String createShipmentBoxType(BffProductDto Product, Command c) {
        BffShipmentBoxTypeDto shipmentBoxTypeDto = Product.getDefaultShipmentBoxType();
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
    public void when(BffProductServiceCommands.UpdateProduct c) {
        if (c.getProductId() == null) {
            throw new IllegalArgumentException("Product id can't be null");
        }
        String productId = c.getProductId();
        ProductState productState = productApplicationService.get(productId);
        if (productState == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        String supplierId = c.getProduct().getSupplierId();
        if (supplierId == null || supplierId.isBlank()) {
            throw new IllegalArgumentException("Supplier can't be null");
        }
        if (partyApplicationService.get(supplierId) == null) {
            throw new IllegalArgumentException("SupplierId is not valid.");
        }
        BffProductDto Product = c.getProduct();
        AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();
        mergePatchProduct.setProductId(productId);
        mergePatchProduct.setProductTypeId(PRODUCT_TYPE_RAW_MATERIAL);
        mergePatchProduct.setVersion(productState.getVersion());
        mergePatchProduct.setProductName(Product.getProductName());
        mergePatchProduct.setDescription(Product.getDescription());
        mergePatchProduct.setSmallImageUrl(Product.getSmallImageUrl());
        mergePatchProduct.setMediumImageUrl(Product.getMediumImageUrl());
        mergePatchProduct.setLargeImageUrl(Product.getLargeImageUrl());
        mergePatchProduct.setBrandName(Product.getBrandName());
        mergePatchProduct.setProduceVariety(Product.getProduceVariety());
        mergePatchProduct.setOrganicCertifications(Product.getOrganicCertifications());
        mergePatchProduct.setMaterialCompositionDescription(Product.getMaterialCompositionDescription());
        mergePatchProduct.setCountryOfOrigin(Product.getCountryOfOrigin());
        mergePatchProduct.setCertificationCodes(Product.getCertificationCodes());
        mergePatchProduct.setShelfLifeDescription(Product.getShelfLifeDescription());
        mergePatchProduct.setHandlingInstructions(Product.getHandlingInstructions());
        mergePatchProduct.setStorageConditions(Product.getStorageConditions());
        mergePatchProduct.setDimensionsDescription(Product.getDimensionsDescription());

        if (Product.getCaseUomId() != null) {
            mergePatchProduct.setCaseUomId(Product.getCaseUomId());
        }
        if (Product.getQuantityUomId() != null)
            mergePatchProduct.setQuantityUomId(Product.getQuantityUomId());
        if (Product.getQuantityIncluded() != null)
            mergePatchProduct.setQuantityIncluded(Product.getQuantityIncluded());
        if (Product.getPiecesIncluded() != null)
            mergePatchProduct.setPiecesIncluded(Product.getPiecesIncluded() != null ? Product.getPiecesIncluded() : 1);
        if (Product.getWeightUomId() != null)
            mergePatchProduct.setWeightUomId(Product.getWeightUomId());
        if (Product.getShippingWeight() != null)
            mergePatchProduct.setShippingWeight(Product.getShippingWeight());
        if (Product.getProductWeight() != null)
            mergePatchProduct.setProductWeight(Product.getProductWeight());

        if (Product.getHeightUomId() != null)
            mergePatchProduct.setHeightUomId(Product.getHeightUomId());
        if (Product.getProductHeight() != null)
            mergePatchProduct.setProductHeight(Product.getProductHeight());
        if (Product.getShippingHeight() != null)
            mergePatchProduct.setShippingHeight(Product.getShippingHeight());

        if (Product.getWidthUomId() != null)
            mergePatchProduct.setWidthUomId(Product.getWidthUomId());
        if (Product.getProductWidth() != null)
            mergePatchProduct.setProductWidth(Product.getProductWidth());
        if (Product.getShippingWidth() != null)
            mergePatchProduct.setShippingWidth(Product.getShippingWidth());

        if (Product.getDepthUomId() != null)
            mergePatchProduct.setDepthUomId(Product.getDepthUomId());
        if (Product.getProductDepth() != null)
            mergePatchProduct.setProductDepth(Product.getProductDepth());
        if (Product.getShippingDepth() != null)
            mergePatchProduct.setShippingDepth(Product.getShippingDepth());

        if (Product.getDiameterUomId() != null)
            mergePatchProduct.setDiameterUomId(Product.getDiameterUomId());
        if (Product.getProductDiameter() != null)
            mergePatchProduct.setProductDiameter(Product.getProductDiameter());
        if (Product.getIndividualsPerPackage() != null) {
            mergePatchProduct.setIndividualsPerPackage(Product.getIndividualsPerPackage());
        }
        mergePatchProduct.setActive(IndicatorUtils.asIndicatorDefaultYes(Product.getActive()));

        mergePatchProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchProduct.setRequesterId(c.getRequesterId());
        if (Product.getDefaultShipmentBoxTypeId() != null) {
            mergePatchProduct.setDefaultShipmentBoxTypeId(Product.getDefaultShipmentBoxTypeId());
        } else if (Product.getDefaultShipmentBoxType() != null) {
            mergePatchProduct.setDefaultShipmentBoxTypeId(createShipmentBoxType(Product, c));
        }

        if (Product.getInternalId() != null && !Product.getInternalId().isBlank()) {
            Product.setInternalId(Product.getInternalId().trim());
            String itemId = bffProductRepository
                    .queryProductIdByIdentificationTypeIdAndIdValue(
                            GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, Product.getInternalId());
            if (itemId != null && !itemId.equals(productId)) {
                throw new IllegalArgumentException(String.format(
                        "Item Number:%s is already in use. Please try a different one.", Product.getInternalId()));
            }
        }
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_GTIN, Product.getGtin());
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_INTERNAL_ID,
                Product.getInternalId());
        updateProductIdentification(mergePatchProduct, productState, GOOD_IDENTIFICATION_TYPE_HS_CODE,
                Product.getHsCode());

        productApplicationService.when(mergePatchProduct);

        // 这里不需要使用完整的 ID 全等匹配查询
        BffSupplierProductAssocProjection existingAssoc = bffProductRepository.findSupplierProductAssociation(
                productId,
                supplierId,
                DEFAULT_CURRENCY_UOM_ID,
                BigDecimal.ZERO,
                OffsetDateTime.now());
        SupplierProductAssocId supplierProductAssocId;
        if (existingAssoc == null) {
            createSupplierProduct(productId, supplierId, c);
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
        mergePatchProduct.setProductTypeId(PRODUCT_TYPE_RAW_MATERIAL);
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

    private void createSupplierProduct(String productId, String supplierId, Command c) {
        SupplierProductAssocId supplierProductAssocId = new SupplierProductAssocId();
        supplierProductAssocId.setProductId(productId);
        supplierProductAssocId.setPartyId(supplierId);
        supplierProductAssocId.setCurrencyUomId(DEFAULT_CURRENCY_UOM_ID);
        supplierProductAssocId.setAvailableFromDate(OffsetDateTime.now());
        supplierProductAssocId.setMinimumOrderQuantity(BigDecimal.ZERO);
        AbstractSupplierProductCommand.SimpleCreateSupplierProduct createSupplierProduct = new AbstractSupplierProductCommand.SimpleCreateSupplierProduct();
        createSupplierProduct.setSupplierProductAssocId(supplierProductAssocId);
        createSupplierProduct.setAvailableThruDate(OffsetDateTime.now().plusYears(100));
        createSupplierProduct.setCommandId(UUID.randomUUID().toString());
        createSupplierProduct.setRequesterId(c.getRequesterId());
        supplierProductApplicationService.when(createSupplierProduct);
    }
}
