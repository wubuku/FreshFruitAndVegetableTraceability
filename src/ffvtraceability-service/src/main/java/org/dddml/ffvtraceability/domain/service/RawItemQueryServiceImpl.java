package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.mapper.BffRawItemMapper;
import org.dddml.ffvtraceability.domain.mapper.BffShipmentBoxTypeMapper;
import org.dddml.ffvtraceability.domain.product.ProductApplicationService;
import org.dddml.ffvtraceability.domain.product.ProductState;
import org.dddml.ffvtraceability.domain.shipmentboxtype.ShipmentBoxTypeApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.dddml.ffvtraceability.domain.constants.BffProductConstants.*;

@Service
public class RawItemQueryServiceImpl implements RawItemQueryService {
    @Autowired
    private ProductApplicationService productApplicationService;
    @Autowired
    private BffRawItemMapper bffRawItemMapper;
    @Autowired
    private BffShipmentBoxTypeMapper bffShipmentBoxTypeMapper;
    @Autowired
    private ShipmentBoxTypeApplicationService shipmentBoxTypeApplicationService;

    @Override
    @Cacheable(value = "BffRawItems", key = "#productId", unless = "#result == null")
    @Transactional(readOnly = true)
    public BffRawItemDto findRawItem(String productId) {
        ProductState productState = productApplicationService.get(productId);
        if (productState != null) {
            BffRawItemDto dto = bffRawItemMapper.toBffRawItemDto(productState);
            productState.getGoodIdentifications().stream().forEach(x -> {
                if (x.getGoodIdentificationTypeId().equals(GOOD_IDENTIFICATION_TYPE_GTIN)) {
                    dto.setGtin(x.getIdValue());
                } else if (x.getGoodIdentificationTypeId().equals(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID)) {
                    dto.setInternalId(x.getIdValue());
                } else if (x.getGoodIdentificationTypeId().equals(GOOD_IDENTIFICATION_TYPE_HS_CODE)) {
                    dto.setHsCode(x.getIdValue());
                }
            });
            if (productState.getDefaultShipmentBoxTypeId() != null) {
                // 连带返回默认的发货箱类型信息？
                dto.setDefaultShipmentBoxType(bffShipmentBoxTypeMapper.toBffShipmentBoxTypeDto(
                        shipmentBoxTypeApplicationService.get(dto.getDefaultShipmentBoxTypeId())));
            }
            return dto;
        }
        return null;
    }
}
