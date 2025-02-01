package org.dddml.ffvtraceability.domain.constants;

public class BffLotConstants {
    private BffLotConstants() {
        // Prevent instantiation
    }

    public static final String LOT_TYPE_ID_PRIMARY_TLC = "PRIMARY_TLC";

    // @Deprecated public static final String LOT_IDENTIFICATION_TYPE_GS1_BATCH = "GS1_BATCH";

    /**
     * TLC (Traceability lot code) case GTIN/BATCH.
     */
    public static final String LOT_IDENTIFICATION_TYPE_TLC_CASE_GTIN_BATCH = "TLC_CASE_GTIN_BATCH";

}
