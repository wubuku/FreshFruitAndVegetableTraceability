package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffProductAssociationDto;
import org.dddml.ffvtraceability.domain.CreateBomVo;
import org.dddml.ffvtraceability.domain.ProductToVo;
import org.dddml.ffvtraceability.domain.mapper.BffProductAssocMapper;
import org.dddml.ffvtraceability.domain.product.ProductApplicationService;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.productassoc.AbstractProductAssocCommand;
import org.dddml.ffvtraceability.domain.productassoc.ProductAssocApplicationService;
import org.dddml.ffvtraceability.domain.productassoc.ProductAssocId;
import org.dddml.ffvtraceability.domain.repository.BffProductAssocRepository;
import org.dddml.ffvtraceability.domain.repository.BffProductAssociationDtoProjection;
import org.dddml.ffvtraceability.domain.util.PageUtils;
import org.dddml.ffvtraceability.specialization.DomainError;
import org.dddml.ffvtraceability.specialization.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.dddml.ffvtraceability.domain.constants.BffBomConstants.PRODUCT_ASSOC_TYPE_MANUF_COMPONENT;
import static org.dddml.ffvtraceability.domain.constants.BffProductConstants.*;

@Service
public class BffBomApplicationServiceImpl implements BffBomApplicationService {
    @Autowired
    private ProductApplicationService productApplicationService;
    @Autowired
    private ProductAssocApplicationService productAssocApplicationService;
    @Autowired
    private BffProductAssocRepository bffProductAssocRepository;
    @Autowired
    private BffProductAssocMapper bffProductAssocMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BffProductAssociationDto> when(BffBomServiceCommands.GetBoms c) {
        return PageUtils.toPage(
                bffProductAssocRepository.findAllBoms(PageRequest.of(c.getPage(), c.getSize()),
                        c.getProductTypeId(), c.getProductId(), c.getInternalId()),
                bffProductAssocMapper::toBffProductAssociationDto);
    }

    @Override
    @Transactional
    public void when(BffBomServiceCommands.CreateBom c) {
        CreateBomVo vo = c.getBom();
        if (vo == null) {
            throw new IllegalArgumentException("Bom is null");
        }
        if (vo.getProductId() == null || vo.getProductId().isBlank()) {
            throw new IllegalArgumentException("Product Id can't be null");
        }
        ProductState productState = productApplicationService.get(vo.getProductId());
        if (productState == null) {
            throw new IllegalArgumentException("Product not found: " + vo.getProductId());
        }
        if (productState.getProductTypeId() == null) {
            throw new DomainError("Unable to identify product type");
        }
        if (PRODUCT_TYPE_RAW_MATERIAL.equals(productState.getProductTypeId())) {
            throw new DomainError("Can't create BOM for raw material");
        }
        List<BffProductAssocRepository.ProductAssocProjection> productAssocProjections =
                bffProductAssocRepository.findBomByProductId(vo.getProductId());
        if (!productAssocProjections.isEmpty()) {
            throw new DomainError("BOM has been created for the product");
        }

        OffsetDateTime now = OffsetDateTime.now();
        List<String> productToIds = new ArrayList<>();
        Long sequenceNum = 1L;
        for (ProductToVo bom : vo.getComponents()) {
            if (productToIds.contains(bom.getProductId())) {
                throw new IllegalArgumentException("Can't choose the same product");
            }
            ProductState childProductState = productApplicationService.get(bom.getProductId());
            if (childProductState == null) {
                throw new IllegalArgumentException("Product not found: " + bom.getProductId());
            }
            if (childProductState.getProductTypeId() == null) {
                throw new DomainError("Unable to identify product type");
            } else if (PRODUCT_TYPE_RAC_WIP.equals(productState.getProductTypeId())) {
                if (!PRODUCT_TYPE_RAW_MATERIAL.equals(childProductState.getProductTypeId())) {
                    throw new DomainError("Only raw materials can be used to create RAC WIP");
                }
            } else if (PRODUCT_TYPE_RTE_WIP.equals(productState.getProductTypeId())) {
                if (!PRODUCT_TYPE_RAW_MATERIAL.equals(childProductState.getProductTypeId())
                        && !PRODUCT_TYPE_RAC_WIP.equals(childProductState.getProductTypeId())) {
                    throw new DomainError("Only raw materials or RAC WIP can be used to create RTE WIP");
                }
            } else if (PRODUCT_TYPE_PACKED_WIP.equals(productState.getProductTypeId())) {
                if (PRODUCT_TYPE_PACKED_WIP.equals(childProductState.getProductTypeId())
                        || !PRODUCT_TYPE_FINISHED_GOOD.equals(childProductState.getProductTypeId())) {
                    throw new DomainError("Only raw materials, RAC WIP, RTE WIP can be used to create PACK WIP");
                }
            } else if (PRODUCT_TYPE_FINISHED_GOOD.equals(productState.getProductTypeId())) {
                if (PRODUCT_TYPE_FINISHED_GOOD.equals(childProductState.getProductTypeId())) {
                    throw new DomainError("Can't use Finished Good to create Finished Good");
                }
            }
            productToIds.add(bom.getProductId());
            if (bom.getQuantity() == null || bom.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantity must greater than 0");
            }
            if (bom.getScrapFactor() == null) {
                bom.setScrapFactor(BigDecimal.ZERO);
            }
            if (bom.getScrapFactor().compareTo(BigDecimal.ZERO) < 0 || bom.getScrapFactor().compareTo(BigDecimal.valueOf(100)) >= 0) {
                throw new IllegalArgumentException("Scrap rate must between 0 and 100");
            }
            AbstractProductAssocCommand.SimpleCreateProductAssoc createProductAssoc = new AbstractProductAssocCommand.SimpleCreateProductAssoc();
            ProductAssocId productAssocId = new ProductAssocId();
            productAssocId.setProductId(vo.getProductId());
            productAssocId.setProductIdTo(bom.getProductId());
            productAssocId.setProductAssocTypeId(PRODUCT_ASSOC_TYPE_MANUF_COMPONENT);
            productAssocId.setFromDate(now);
            createProductAssoc.setProductAssocId(productAssocId);
            createProductAssoc.setSequenceNum(sequenceNum);
            createProductAssoc.setQuantity(bom.getQuantity());
            createProductAssoc.setScrapFactor(bom.getScrapFactor());
            createProductAssoc.setCommandId(c.getCommandId() == null ? UUID.randomUUID().toString() : c.getCommandId());
            createProductAssoc.setRequesterId(c.getRequesterId());
            productAssocApplicationService.when(createProductAssoc);
            sequenceNum++;
        }
    }


    @Override
    @Transactional
    public void when(BffBomServiceCommands.UpdateBom c) {
        List<BffProductAssocRepository.ProductAssocProjection> productAssocProjections =
                bffProductAssocRepository.findBomByProductId(c.getProductId());
        if (productAssocProjections.isEmpty()) {
            throw new IllegalArgumentException("BOM not found: " + c.getProductId());
        }
        List<String> existingProductToIds = new ArrayList<>();
        productAssocProjections.forEach(productAssocProjection -> {
            existingProductToIds.add(productAssocProjection.getProductIdTo());
        });
        //这次指定的关联产品的Id列表
        List<String> inputProductToIds = new ArrayList<>();
        //本次要移除的关联产品的Id列表
        List<String> toBeDeletedProductToIds = new ArrayList<>();
        List<String> toBeUpdateProductToIds = new ArrayList<>();
        //本次要添加的关联产品的Id列表
        List<String> toAddProductToIds = new ArrayList<>();
        Map<String, ProductToVo> toBeAddedProductTo = new HashMap<>();
        Map<String, ProductToVo> inputProductTo = new HashMap<>();
        Arrays.stream(c.getComponents()).forEach(productTo -> {
            if (inputProductToIds.contains(productTo.getProductId())) {
                throw new IllegalArgumentException("Can't choose the same product: " + productTo.getProductId());
            }
            inputProductToIds.add(productTo.getProductId());
            inputProductTo.put(productTo.getProductId(), productTo);
            if (!existingProductToIds.contains(productTo.getProductId())) {
                toAddProductToIds.add(productTo.getProductId());
                toBeAddedProductTo.put(productTo.getProductId(), productTo);
            }
        });
        existingProductToIds.forEach(productToId -> {
            if (!inputProductToIds.contains(productToId)) {
                toBeDeletedProductToIds.add(productToId);
            }
            toBeUpdateProductToIds.add(productToId);
        });
        OffsetDateTime now = OffsetDateTime.now();
        for (int i = 0; i < c.getComponents().length; i++) {
            Long sequenceNum = (long) i + 1;
            ProductToVo productTo = c.getComponents()[i];
            if (toAddProductToIds.contains(productTo.getProductId())) {//添加
                AbstractProductAssocCommand.SimpleCreateProductAssoc createProductAssoc = new AbstractProductAssocCommand.SimpleCreateProductAssoc();
                ProductAssocId productAssocId = new ProductAssocId();
                productAssocId.setProductId(c.getProductId());
                productAssocId.setProductIdTo(productTo.getProductId());
                productAssocId.setProductAssocTypeId(PRODUCT_ASSOC_TYPE_MANUF_COMPONENT);
                productAssocId.setFromDate(now);
                createProductAssoc.setProductAssocId(productAssocId);
                createProductAssoc.setSequenceNum(sequenceNum);
                createProductAssoc.setQuantity(productTo.getQuantity());
                createProductAssoc.setScrapFactor(productTo.getScrapFactor());
                createProductAssoc.setCommandId(c.getCommandId() == null ? UUID.randomUUID().toString() : c.getCommandId());
                createProductAssoc.setRequesterId(c.getRequesterId());
                productAssocApplicationService.when(createProductAssoc);
            } else if (toBeUpdateProductToIds.contains(productTo.getProductId())) {//更新
                productAssocProjections.forEach(productAssocProjection -> {
                    if (productTo.getProductId().equals(productAssocProjection.getProductIdTo())) {
                        //需要更新
                        AbstractProductAssocCommand.SimpleMergePatchProductAssoc updateProductAssoc = new AbstractProductAssocCommand.SimpleMergePatchProductAssoc();
                        ProductAssocId productAssocId = new ProductAssocId();
                        productAssocId.setProductId(productAssocProjection.getProductId());
                        productAssocId.setProductIdTo(productAssocProjection.getProductIdTo());
                        productAssocId.setProductAssocTypeId(productAssocProjection.getProductAssocTypeId());
                        productAssocId.setFromDate(productAssocProjection.getFromDate().atOffset(ZoneOffset.UTC));
                        updateProductAssoc.setProductAssocId(productAssocId);
                        updateProductAssoc.setVersion(productAssocProjection.getVersion());
                        updateProductAssoc.setQuantity(inputProductTo.get(productAssocProjection.getProductIdTo()).getQuantity());
                        updateProductAssoc.setScrapFactor(inputProductTo.get(productAssocProjection.getProductIdTo()).getScrapFactor());
                        updateProductAssoc.setSequenceNum(sequenceNum);
                        updateProductAssoc.setCommandId(c.getCommandId() == null ? UUID.randomUUID().toString() : c.getCommandId());
                        updateProductAssoc.setRequesterId(c.getRequesterId());
                        productAssocApplicationService.when(updateProductAssoc);
                        //FIXME 这里应该再把这一行的FromDate改成now
                    }
                });
            }
        }
        //删除
        productAssocProjections.forEach(productAssocProjection -> {
            if (toBeDeletedProductToIds.contains(productAssocProjection.getProductIdTo())) {
                AbstractProductAssocCommand.SimpleDeleteProductAssoc deleteProductAssoc = new AbstractProductAssocCommand.SimpleDeleteProductAssoc();
                ProductAssocId productAssocId = new ProductAssocId();
                productAssocId.setProductId(productAssocProjection.getProductId());
                productAssocId.setProductIdTo(productAssocProjection.getProductIdTo());
                productAssocId.setProductAssocTypeId(productAssocProjection.getProductAssocTypeId());
                productAssocId.setFromDate(productAssocProjection.getFromDate().atOffset(ZoneOffset.UTC));
                deleteProductAssoc.setProductAssocId(productAssocId);
                deleteProductAssoc.setVersion(productAssocProjection.getVersion());
                deleteProductAssoc.setCommandId(c.getCommandId() == null ? UUID.randomUUID().toString() : c.getCommandId());
                deleteProductAssoc.setRequesterId(c.getRequesterId());
                productAssocApplicationService.when(deleteProductAssoc);
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public BffProductAssociationDto when(BffBomServiceCommands.GetBOM c) {
        Optional<BffProductAssociationDtoProjection> productAssociationDtoProjection =
                bffProductAssocRepository.findProductAssocByProductId(c.getProductId());
        if (productAssociationDtoProjection.isEmpty()) {
            return null;
        }
        BffProductAssociationDto dto = bffProductAssocMapper.toBffProductAssociationDto(productAssociationDtoProjection.get());
        List<BffProductAssocRepository.ProductAssocProjection> productAssocProjections =
                bffProductAssocRepository.findBomByProductId(c.getProductId());
        productAssocProjections.forEach(productAssocProjection -> {
            getProductAssociationByProductId(dto, productAssocProjection.getProductIdTo());
        });
        return dto;
    }

    private void getProductAssociationByProductId(BffProductAssociationDto dto, String productId) {
        if (dto.getComponents() == null) {
            dto.setComponents(new ArrayList<>());
        }
        //首先看看自己是不是作为BOM存在
        Optional<BffProductAssociationDtoProjection> productAssociationDtoProjection =
                bffProductAssocRepository.findProductAssocByProductId(productId);
        if (productAssociationDtoProjection.isEmpty()) {
            //如果子节点不是作为BOM存在那么获取其作为BOM组成部分的BOM信息
            Optional<BffProductAssociationDtoProjection> productAssocProjection = bffProductAssocRepository.findProductAssocInfoByProductIdTo(dto.getProductId(), productId);
            productAssocProjection.ifPresent(
                    bffProductAssociationDtoProjection ->
                            dto.getComponents().add(bffProductAssocMapper.toBffProductAssociationDto(bffProductAssociationDtoProjection)));
            return;
        }
        BffProductAssociationDto component = bffProductAssocMapper.toBffProductAssociationDto(productAssociationDtoProjection.get());
        bffProductAssocRepository.findBomInfoByWhenProductAsBomAndComponent(dto.getProductId(), productId).ifPresent(
                bffProductAssocProjection -> {
                    component.setQuantity(bffProductAssocProjection.getQuantity());
                    component.setScrapFactor(bffProductAssocProjection.getScrapFactor());
                    component.setSequenceNum(bffProductAssocProjection.getSequenceNum());
                    //component.setCreatedBy(bffProductAssocProjection.getCreatedBy());
                    //component.setCreateAt(bffProductAssocProjection.getCreatedAt().atOffset(ZoneOffset.UTC));
                });
        dto.getComponents().add(component);
        List<BffProductAssocRepository.ProductAssocProjection> productAssocProjections =
                bffProductAssocRepository.findBomByProductId(productId);
        productAssocProjections.forEach(productAssocProjection -> {
            getProductAssociationByProductId(component, productAssocProjection.getProductIdTo());
        });
    }

    @Override
    @Transactional
    public void when(BffBomServiceCommands.DeleteBom c) {
        List<BffProductAssocRepository.ProductAssocProjection> productAssocProjections =
                bffProductAssocRepository.findBomByProductId(c.getProductId());
        productAssocProjections.forEach(productAssocProjection -> {
            AbstractProductAssocCommand.SimpleDeleteProductAssoc deleteProductAssoc = new AbstractProductAssocCommand.SimpleDeleteProductAssoc();
            ProductAssocId productAssocId = new ProductAssocId();
            productAssocId.setProductId(productAssocProjection.getProductId());
            productAssocId.setProductIdTo(productAssocProjection.getProductIdTo());
            productAssocId.setProductAssocTypeId(productAssocProjection.getProductAssocTypeId());
            productAssocId.setFromDate(productAssocProjection.getFromDate().atOffset(ZoneOffset.UTC));
            deleteProductAssoc.setProductAssocId(productAssocId);
            deleteProductAssoc.setVersion(productAssocProjection.getVersion());
            deleteProductAssoc.setCommandId(c.getCommandId() == null ? UUID.randomUUID().toString() : c.getCommandId());
            deleteProductAssoc.setRequesterId(c.getRequesterId());
            productAssocApplicationService.when(deleteProductAssoc);
        });
    }
}
