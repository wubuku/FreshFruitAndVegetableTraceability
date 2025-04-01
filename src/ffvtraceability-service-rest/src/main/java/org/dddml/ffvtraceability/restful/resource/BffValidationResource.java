package org.dddml.ffvtraceability.restful.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dddml.ffvtraceability.domain.repository.BffFacilityLocationRepository;
import org.dddml.ffvtraceability.domain.repository.BffFacilityRepository;
import org.dddml.ffvtraceability.domain.repository.BffRawItemRepository;
import org.dddml.ffvtraceability.domain.repository.BffSupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.dddml.ffvtraceability.domain.constants.BffFacilityConstants.FACILITY_IDENTIFICATION_TYPE_INTERNAL_ID;
import static org.dddml.ffvtraceability.domain.constants.BffPartyConstants.PARTY_IDENTIFICATION_TYPE_INTERNAL_ID;
import static org.dddml.ffvtraceability.domain.constants.BffProductConstants.GOOD_IDENTIFICATION_TYPE_INTERNAL_ID;

@RequestMapping(path = "BffValidations", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Tag(name = "字段验证", description = "对重要数据的单个字段进行验证")
public class BffValidationResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffFacilityLocationRepository bffFacilityLocationRepository;
    @Autowired
    private BffFacilityRepository bffFacilityRepository;
    @Autowired
    private BffRawItemRepository bffRawItemRepository;
    @Autowired
    private BffSupplierRepository bffSupplierRepository;

    @GetMapping(path = "VendorNumberWhenCreate")
    @Transactional
    @Operation(summary = "创建Supplier(Vendor)的时候验证其Number不能重复", description = "创建Vendor(Supplier)的时候验证其Number不能重复，【注意只有其填写的时候才会验证，用户不填写该字段不要调用该接口】", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateSupplierNumberWhenCreate(
            @Parameter(name = "internalId", description = "Supplier Number(Vendor Number)", required = true)
            @RequestParam(value = "internalId") String internalId) {
        if (internalId == null || internalId.isBlank()) {
            return;
        }
        internalId = internalId.trim();
        if (bffSupplierRepository.countByPartyIdentificationTypeIdAndIdValue(PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, internalId) > 0) {
            throw new IllegalArgumentException(String.format("Vendor number:%s is already in use. Please try a different one.", internalId));
        }
    }

    @GetMapping(path = "VendorNumberWhenUpdate")
    @Transactional
    @Operation(summary = "更新Supplier(Vendor)的时候验证其Number不能重复", description = "更新Vendor(Supplier)的时候验证其Number不能重复，【注意只有其填写的时候才会验证，用户不填写该字段不要调用该接口】", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateSupplierNumberWhenUpdate(
            @Parameter(name = "supplierId", description = "要更新的Supplier(Vendor)的id", required = true)
            @RequestParam(value = "supplierId") String supplierId,
            @Parameter(name = "internalId", description = "Supplier Number(Vendor Number)", required = true)
            @RequestParam(value = "internalId") String internalId) {
        if (internalId == null || internalId.isBlank()) {
            return;
        }
        internalId = internalId.trim();
        String partyId = bffSupplierRepository.queryByPartyIdentificationTypeIdAndIdValue(PARTY_IDENTIFICATION_TYPE_INTERNAL_ID, internalId);
        if (partyId != null && !partyId.equals(supplierId)) {
            throw new IllegalArgumentException(String.format("Vendor number:%s is already in use. Please try a different one.", internalId));
        }
    }

    @GetMapping(path = "ItemNumberWhenCreate")
    @Transactional
    @Operation(summary = "创建Item的时候验证其Number不能重复", description = "创建Item的时候验证其Number不能重复，【注意只有其填写的时候才会验证，用户不填写该字段不要调用该接口】", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateItemNumberWhenCreate(
            @Parameter(name = "internalId", description = "Item Number", required = true)
            @RequestParam(value = "internalId") String internalId) {
        if (internalId == null || internalId.isBlank()) {
            return;
        }
        internalId = internalId.trim();
        if (bffRawItemRepository.countByIdentificationTypeIdAndIdValue(GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, internalId) > 0) {
            throw new IllegalArgumentException(String.format("Item number:%s is already in use. Please try a different one.", internalId));
        }
    }

    @GetMapping(path = "ItemNumberWhenUpdate")
    @Transactional
    @Operation(summary = "更新Item的时候验证其Number不能重复", description = "更新Item的时候验证其Number不能重复，【注意只有其填写的时候才会验证，用户不填写该字段不要调用该接口】", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateItemNumberWhenUpdate(
            @Parameter(name = "productId", description = "要更新的Item(Product)的id", required = true)
            @RequestParam(value = "productId") String productId,
            @Parameter(name = "internalId", description = "Item Number", required = true)
            @RequestParam(value = "internalId") String internalId) {
        if (internalId == null || internalId.isBlank()) {
            return;
        }
        internalId = internalId.trim();
        String itemId = bffRawItemRepository.queryProductIdByIdentificationTypeIdAndIdValue(
                GOOD_IDENTIFICATION_TYPE_INTERNAL_ID, internalId);
        if (itemId != null && !itemId.equals(productId)) {
            throw new IllegalArgumentException(String.format("Item number:%s is already in use. Please try a different one.", internalId));
        }
    }

    @GetMapping(path = "FacilityNameWhenCreate")
    @Transactional
    @Operation(summary = "创建Facility的时候验证其name不能重复", description = "创建Facility的时候验证其name不能重复", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateFacilityNameWhenCreate(
            @Parameter(name = "facilityName", description = "Facility的name", required = true)
            @RequestParam(value = "facilityName") String facilityName) {
        if (facilityName == null || facilityName.isBlank()) {
            throw new IllegalArgumentException("Location name is required");
        }
        facilityName = facilityName.trim();
        if (bffFacilityRepository.countByFacilityName(facilityName) > 0) {
            throw new IllegalArgumentException(String.format("Facility name already exists: %s", facilityName));
        }
    }


    @GetMapping(path = "FacilityNameWhenUpdate")
    @Transactional
    @Operation(summary = "更新Facility的时候验证其name不能重复", description = "更新Facility的时候验证其name不能重复", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateFacilityNameWhenUpdate(
            @Parameter(name = "facilityId", description = "要更新的Facility的id", required = true)
            @RequestParam(value = "facilityId") String facilityId,
            @Parameter(name = "facilityName", description = "更新Facility时指定的name", required = true)
            @RequestParam(value = "facilityName") String facilityName) {
        if (facilityName == null || facilityName.isBlank()) {
            throw new IllegalArgumentException("Location name is required");
        }
        facilityName = facilityName.trim();
        String facilityIdByName = bffFacilityRepository.queryByFacilityName(facilityName);
        if (facilityIdByName != null && !facilityIdByName.equals(facilityId)) {
            throw new IllegalArgumentException("Facility name already exists: " + facilityIdByName);
        }
    }

    @GetMapping(path = "FacilityNumberWhenCreate")
    @Transactional
    @Operation(summary = "创建Facility的时候验证其Number不能重复", description = "创建Facility的时候验证其Number不能重复，【注意只有其填写的时候才会验证，用户不填写该字段不要调用该接口】",
            responses = {
                    @ApiResponse(responseCode = "200", description = "验证成功"),
                    @ApiResponse(responseCode = "400", description = "验证失败")
            })
    public void validateFacilityNumberWhenCreate(
            @Parameter(name = "internalId", description = "Facility的Number(Warehouse Number)", required = true)
            @RequestParam(value = "internalId") String internalId) {
        if (internalId == null || internalId.isBlank()) {
            //throw new IllegalArgumentException("Location name is required");
            return;
        }
        internalId = internalId.trim();
        if (bffFacilityRepository.countByIdentificationTypeIdAndIdValue(FACILITY_IDENTIFICATION_TYPE_INTERNAL_ID, internalId) > 0) {
            throw new IllegalArgumentException(String.format("Facility number:%s is already in use. Please try a different one.", internalId));
        }
    }


    @GetMapping(path = "FacilityNumberWhenUpdate")
    @Transactional
    @Operation(summary = "创建Facility的时候验证其Number不能重复", description = "更新Facility的时候验证其Number不能重复，【注意只有其填写的时候才会验证，用户不填写该字段不要调用该接口】",
            responses = {
                    @ApiResponse(responseCode = "200", description = "验证成功"),
                    @ApiResponse(responseCode = "400", description = "验证失败")
            })
    public void validateFacilityNumberWhenUpdate(
            @Parameter(name = "facilityId", description = "要更新的Facility的id", required = true)
            @RequestParam(value = "facilityId") String facilityId,
            @Parameter(name = "internalId", description = "更新Facility时指定的Facility Number(Warehouse Number)", required = true)
            @RequestParam(value = "internalId") String internalId) {
        if (internalId == null || internalId.isBlank()) {
            //throw new IllegalArgumentException("Location name is required");
            return;
        }
        internalId = internalId.trim();
        String existingId = bffFacilityRepository.queryByIdentificationTypeIdAndIdValue(FACILITY_IDENTIFICATION_TYPE_INTERNAL_ID, internalId);
        if (existingId != null && !existingId.equals(facilityId)) {
            throw new IllegalArgumentException("Facility number:" + internalId + " is already in use. Please try a different one.");
        }
    }

    @GetMapping(path = "FacilityLocationNameWhenCreate")
    @Transactional
    @Operation(summary = "创建Location的时候验证其name不能重复", description = "创建Location的时候验证其name不能重复", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateFacilityLocationNameWhenCreate(
            @Parameter(name = "locationName", description = "Location的name", required = true)
            @RequestParam(value = "locationName") String locationName) {
        if (locationName == null || locationName.isBlank()) {
            throw new IllegalArgumentException("Location name is required");
        }
        locationName = locationName.trim();
        if (bffFacilityLocationRepository.countyByLocationName(locationName) > 0) {
            throw new IllegalArgumentException(String.format("Location name already exists: %s", locationName));
        }
    }

    @GetMapping(path = "FacilityLocationCodeWhenCreate")
    @Transactional
    @Operation(summary = "创建Location的时候验证其Code不能重复", description = "创建Location的时候验证其Code不能重复", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateFacilityLocationCodeWhenCreate(
            @Parameter(name = "locationCode", description = "Location的code", required = true)
            @RequestParam(value = "locationCode") String locationCode) {
        if (locationCode == null || locationCode.isBlank()) {
            throw new IllegalArgumentException("Location code is required");
        }
        locationCode = locationCode.trim();
        if (bffFacilityLocationRepository.countyByLocationCode(locationCode) > 0) {
            throw new IllegalArgumentException(String.format("Location code already exists: %s", locationCode));
        }
    }

    @GetMapping(path = "FacilityLocationNameWhenUpdate")
    @Transactional
    @Operation(summary = "更新Location的时候验证其name不能重复", description = "更新Location的时候验证其name不能重复", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateFacilityLocationNameWhenUpdate(
            @Parameter(name = "facilityId", description = "要修改的Location所属Facility的id", required = true)
            @RequestParam(value = "facilityId") String facilityId,
            @Parameter(name = "locationSeqId", description = "要修改的Location的SeqId", required = true)
            @RequestParam(value = "locationSeqId") String locationSeqId,
            @Parameter(name = "locationName", description = "更新Location时所指定的LocationName", required = true)
            @RequestParam(value = "locationName") String locationName) {
        if (locationName == null || locationName.isBlank()) {
            throw new IllegalArgumentException("Location name is required");
        }
        locationName = locationName.trim();
        BffFacilityLocationRepository.BffFacilityLocationIdProjection bffFacilityLocationIdProjection
                = bffFacilityLocationRepository.findFacilityLocationIdByLocationName(locationName);
        if (bffFacilityLocationIdProjection != null &&
                !(bffFacilityLocationIdProjection.getLocationSeqId().equals(locationSeqId)
                        && bffFacilityLocationIdProjection.getFacilityId().equals(facilityId))) {
            throw new IllegalArgumentException(String.format("Location name already exists: %s", locationName));
        }
    }


    @GetMapping(path = "FacilityLocationCodeWhenUpdate")
    @Transactional
    @Operation(summary = "更新Location的时候验证其code不能重复", description = "更新Location的时候验证其code不能重复", responses = {
            @ApiResponse(responseCode = "200", description = "验证成功"),
            @ApiResponse(responseCode = "400", description = "验证失败")
    })
    public void validateFacilityLocationCodeWhenUpdate(
            @Parameter(name = "facilityId", description = "要修改的Location所属Facility的id", required = true)
            @RequestParam(value = "facilityId") String facilityId,
            @Parameter(name = "locationSeqId", description = "要修改的Location的SeqId", required = true)
            @RequestParam(value = "locationSeqId") String locationSeqId,
            @Parameter(name = "locationCode", description = "更新Location时所指定的LocationCode", required = true)
            @RequestParam(value = "locationCode") String locationCode) {
        if (locationCode == null || locationCode.isBlank()) {
            throw new IllegalArgumentException("Location code is required");
        }
        locationCode = locationCode.trim();
        BffFacilityLocationRepository.BffFacilityLocationIdProjection bffFacilityLocationIdProjection
                = bffFacilityLocationRepository.findFacilityLocationIdByLocationCode(locationCode);
        if (bffFacilityLocationIdProjection != null &&
                !(bffFacilityLocationIdProjection.getLocationSeqId().equals(locationSeqId)
                        && bffFacilityLocationIdProjection.getFacilityId().equals(facilityId))) {
            throw new IllegalArgumentException(String.format("Location code already exists: %s", locationCode));
        }
    }
}
