package org.dddml.ffvtraceability.domain.constants;

import java.util.Arrays;
import java.util.List;

public final class BffProductConstants {
    /**
     * RAW_MATERIAL: 原材料
     */
    public static final String PRODUCT_TYPE_RAW_MATERIAL = "RAW_MATERIAL";
    /**
     * WIP: Work In Process(在制品)
     */
    public static final String PRODUCT_TYPE_RAC_WIP = "RAC_WIP";
    public static final String PRODUCT_TYPE_RTE_WIP = "RTE_WIP";
    public static final String PRODUCT_TYPE_PACKED_WIP = "PACKED_WIP";
    /**
     * FINISHED_GOOD: 成品
     */
    public static final String PRODUCT_TYPE_FINISHED_GOOD = "FINISHED_GOOD";

    /**
     * WIP产品类型列表
     */
    public static final List<String> PRODUCT_TYPES_NOT_RAW = Arrays.asList(
        PRODUCT_TYPE_RAC_WIP,
        PRODUCT_TYPE_RTE_WIP,
        PRODUCT_TYPE_PACKED_WIP,
        PRODUCT_TYPE_FINISHED_GOOD
    );
    /**
     * GTIN (Global Trade Item Number)
     */
    public static final String GOOD_IDENTIFICATION_TYPE_GTIN = "GTIN";
    /**
     * INTERNAL_ID (Internal ID)
     */
    public static final String GOOD_IDENTIFICATION_TYPE_INTERNAL_ID = "INTERNAL_ID";
    /**
     * HS_CODE (Harmonized System Code).
     */
    public static final String GOOD_IDENTIFICATION_TYPE_HS_CODE = "HS_CODE";

    public static final String DEFAULT_CURRENCY_UOM_ID = "USD";
    private BffProductConstants() {
        // Prevent instantiation
    }
} 