package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.mapper.BffRawItemMapper;
import org.dddml.ffvtraceability.domain.party.PartyApplicationService;
import org.dddml.ffvtraceability.domain.product.AbstractGoodIdentificationCommand;
import org.dddml.ffvtraceability.domain.product.AbstractProductCommand;
import org.dddml.ffvtraceability.domain.product.ProductApplicationService;
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
                bffRawItemRepository.findAllRawItems(PageRequest.of(c.getPage(), c.getSize())),
                bffRawItemMapper::toBffRawItemDto
        );
    }

    @Override
    public BffRawItemDto when(BffRawItemServiceCommands.GetRawItem c) {
        return null;
    }

    @Override
    @Transactional
    public void when(BffRawItemServiceCommands.CreateRawItem c) {
//        if (uomApplicationService.get(c.getRawItem().getQuantityUomId()) == null) {
//            throw new IllegalArgumentException("QuantityUomId is not valid.");
//        }
//        if (c.getRawItem().getSupplierId() != null && partyApplicationService.get(c.getRawItem().getSupplierId()) == null) {
//            throw new IllegalArgumentException("SupplierId is not valid.");
//        }
        AbstractProductCommand.SimpleCreateProduct createProduct = new AbstractProductCommand.SimpleCreateProduct();
        createProduct.setProductId(IdUtils.randomId());
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
            createSupplierProduct.setActive(true);
            createSupplierProduct.setCommandId(UUID.randomUUID().toString());
            supplierProductApplicationService.when(createSupplierProduct);
        }
    }

    @Override
    public void when(BffRawItemServiceCommands.UpdateRawItem c) {

    }

    @Override
    public void when(BffRawItemServiceCommands.ActivateRawItem c) {

    }

    @Override
    public void when(BffRawItemServiceCommands.BatchAddRawItems c) {

    }
}
