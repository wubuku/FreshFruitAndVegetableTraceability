package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.mapper.BffRawItemMapper;
import org.dddml.ffvtraceability.domain.party.PartyApplicationService;
import org.dddml.ffvtraceability.domain.product.*;
import org.dddml.ffvtraceability.domain.repository.BffRawItemRepository;
import org.dddml.ffvtraceability.domain.supplierproduct.AbstractSupplierProductCommand;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductApplicationService;
import org.dddml.ffvtraceability.domain.supplierproduct.SupplierProductAssocId;
import org.dddml.ffvtraceability.domain.uom.UomApplicationService;
import org.dddml.ffvtraceability.domain.util.IdUtils;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_NO;
import static org.dddml.ffvtraceability.domain.util.IndicatorUtils.INDICATOR_YES;

@Service
public class BffRawItemApplicationServiceImpl implements BffRawItemApplicationService {
    public static final String GOOD_IDENTIFICATION_TYPE_GTIN = "GTIN";
    public static final String DEFAULT_CURRENCY_UOM_ID = "USD";

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

    @Override
    @Transactional(readOnly = true)
    public Page<BffRawItemDto> when(BffRawItemServiceCommands.GetRawItems c) {
        return PageUtils.toPage(
                bffRawItemRepository.findAllRawItems(PageRequest.of(c.getPage(), c.getSize()), c.getActive()),
                bffRawItemMapper::toBffRawItemDto
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BffRawItemDto when(BffRawItemServiceCommands.GetRawItem c) {
        ProductState productState = productApplicationService.get(c.getProductId());
        if (productState != null) {
            BffRawItemDto dto = bffRawItemMapper.toBffRawItemDto(productState);
            productState.getGoodIdentifications().stream().forEach(x -> {
                if (x.getGoodIdentificationTypeId().equals(GOOD_IDENTIFICATION_TYPE_GTIN)) {
                    dto.setGtin(x.getIdValue());
                }
            });
            return dto;
        }
        return null;
    }

    @Override
    @Transactional
    public String when(BffRawItemServiceCommands.CreateRawItem c) {
//        if (uomApplicationService.get(c.getRawItem().getQuantityUomId()) == null) {
//            throw new IllegalArgumentException("QuantityUomId is not valid.");
//        }
//        if (c.getRawItem().getSupplierId() != null && partyApplicationService.get(c.getRawItem().getSupplierId()) == null) {
//            throw new IllegalArgumentException("SupplierId is not valid.");
//        }
        AbstractProductCommand.SimpleCreateProduct createProduct = new AbstractProductCommand.SimpleCreateProduct();
        createProduct.setProductId(c.getRawItem().getProductId() != null ? c.getRawItem().getProductId() : IdUtils.randomId());
        //(c.getRawItem().getProductId()); // NOTE: ignore the productId from the client side?
        createProduct.setProductName(c.getRawItem().getProductName());
        createProduct.setSmallImageUrl(c.getRawItem().getSmallImageUrl());
        createProduct.setMediumImageUrl(c.getRawItem().getMediumImageUrl());
        createProduct.setLargeImageUrl(c.getRawItem().getLargeImageUrl());
        createProduct.setQuantityUomId(c.getRawItem().getQuantityUomId());
        // If you have a six-pack of 12oz soda cans you would have quantityIncluded=12, quantityUomId=oz, piecesIncluded=6.
        createProduct.setQuantityIncluded(c.getRawItem().getQuantityIncluded());
        createProduct.setPiecesIncluded(c.getRawItem().getPiecesIncluded());
        createProduct.setCommandId(createProduct.getProductId()); //c.getCommandId());
        createProduct.setRequesterId(c.getRequesterId());
        if (c.getRawItem().getGtin() != null) {
            AbstractGoodIdentificationCommand.SimpleCreateGoodIdentification createGoodIdentification
                    = new AbstractGoodIdentificationCommand.SimpleCreateGoodIdentification();
            createGoodIdentification.setGoodIdentificationTypeId(GOOD_IDENTIFICATION_TYPE_GTIN);
            createGoodIdentification.setIdValue(c.getRawItem().getGtin());
            createProduct.getCreateGoodIdentificationCommands().add(createGoodIdentification);
        }
        productApplicationService.when(createProduct);

        if (c.getRawItem().getSupplierId() != null) {
            AbstractSupplierProductCommand.SimpleCreateSupplierProduct createSupplierProduct
                    = new AbstractSupplierProductCommand.SimpleCreateSupplierProduct();
            SupplierProductAssocId supplierProductAssocId = new SupplierProductAssocId();
            supplierProductAssocId.setProductId(createProduct.getProductId());
            supplierProductAssocId.setPartyId(c.getRawItem().getSupplierId());
            supplierProductAssocId.setCurrencyUomId(DEFAULT_CURRENCY_UOM_ID); //todo ?
            supplierProductAssocId.setAvailableFromDate(OffsetDateTime.now());
            supplierProductAssocId.setMinimumOrderQuantity(BigDecimal.ZERO);
            createSupplierProduct.setSupplierProductAssocId(supplierProductAssocId);
            createSupplierProduct.setAvailableThruDate(OffsetDateTime.now().plusYears(100));
            createSupplierProduct.setCommandId(UUID.randomUUID().toString());
            supplierProductApplicationService.when(createSupplierProduct);
        }
        return createProduct.getProductId();
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.UpdateRawItem c) {
        String productId = c.getProductId();
        ProductState productState = productApplicationService.get(productId);
        if (productState == null) {
            throw new IllegalArgumentException("Raw item not found: " + productId);
        }
        AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();
        mergePatchProduct.setProductId(productId);
        mergePatchProduct.setVersion(productState.getVersion());
        mergePatchProduct.setProductName(c.getRawItem().getProductName());
        mergePatchProduct.setDescription(c.getRawItem().getDescription());
        mergePatchProduct.setSmallImageUrl(c.getRawItem().getSmallImageUrl());
        mergePatchProduct.setMediumImageUrl(c.getRawItem().getMediumImageUrl());
        mergePatchProduct.setLargeImageUrl(c.getRawItem().getLargeImageUrl());
        mergePatchProduct.setQuantityUomId(c.getRawItem().getQuantityUomId());
        mergePatchProduct.setQuantityIncluded(c.getRawItem().getQuantityIncluded());
        mergePatchProduct.setPiecesIncluded(c.getRawItem().getPiecesIncluded());
        mergePatchProduct.setCommandId(c.getCommandId() != null ? c.getCommandId() : UUID.randomUUID().toString());
        mergePatchProduct.setRequesterId(c.getRequesterId());
        productApplicationService.when(mergePatchProduct);

        if (c.getRawItem().getGtin() != null) {
            var existingGtin = productState.getGoodIdentifications().stream()
                    .filter(x -> x.getGoodIdentificationTypeId().equals(GOOD_IDENTIFICATION_TYPE_GTIN))
                    .findFirst();

            if (existingGtin.isPresent()) {
                if (!existingGtin.get().getIdValue().equals(c.getRawItem().getGtin())) {
                    GoodIdentificationCommand.MergePatchGoodIdentification mergePatchGoodIdentification
                            = mergePatchProduct.newMergePatchGoodIdentification();
                    mergePatchGoodIdentification.setGoodIdentificationTypeId(GOOD_IDENTIFICATION_TYPE_GTIN);
                    mergePatchGoodIdentification.setIdValue(c.getRawItem().getGtin());
                    mergePatchProduct.getGoodIdentificationCommands().add(mergePatchGoodIdentification);
                }
            } else {
                GoodIdentificationCommand.CreateGoodIdentification createGoodIdentification
                        = mergePatchProduct.newCreateGoodIdentification();
                createGoodIdentification.setGoodIdentificationTypeId(GOOD_IDENTIFICATION_TYPE_GTIN);
                createGoodIdentification.setIdValue(c.getRawItem().getGtin());
                mergePatchProduct.getGoodIdentificationCommands().add(createGoodIdentification);
            }
        }
        if (c.getRawItem().getSupplierId() != null) {
            SupplierProductAssocId supplierProductAssocId = new SupplierProductAssocId();
            supplierProductAssocId.setProductId(productId);
            supplierProductAssocId.setPartyId(c.getRawItem().getSupplierId());
            supplierProductAssocId.setCurrencyUomId(DEFAULT_CURRENCY_UOM_ID);
            supplierProductAssocId.setAvailableFromDate(OffsetDateTime.now());
            supplierProductAssocId.setMinimumOrderQuantity(BigDecimal.ZERO);
            //todo 不需要使用完整的 ID 进行查询，使用部分 Id 查询即可
            if (supplierProductApplicationService.get(supplierProductAssocId) == null) {
                AbstractSupplierProductCommand.SimpleCreateSupplierProduct createSupplierProduct
                        = new AbstractSupplierProductCommand.SimpleCreateSupplierProduct();
                createSupplierProduct.setSupplierProductAssocId(supplierProductAssocId);
                createSupplierProduct.setAvailableThruDate(OffsetDateTime.now().plusYears(100));
                createSupplierProduct.setCommandId(UUID.randomUUID().toString());
                createSupplierProduct.setRequesterId(c.getRequesterId());
                supplierProductApplicationService.when(createSupplierProduct);
            }
        }
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.ActivateRawItem c) {
        String productId = c.getProductId();
        ProductState productState = productApplicationService.get(productId);
        if (productState == null) {
            throw new IllegalArgumentException("Raw item not found: " + productId);
        }
        AbstractProductCommand.SimpleMergePatchProduct mergePatchProduct = new AbstractProductCommand.SimpleMergePatchProduct();
        mergePatchProduct.setProductId(productId);
        mergePatchProduct.setVersion(productState.getVersion());
        mergePatchProduct.setActive(c.getActive() ? INDICATOR_YES : INDICATOR_NO);
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
}
