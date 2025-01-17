package org.dddml.ffvtraceability.domain.constants;

public class BffLotConstants {
    private BffLotConstants() {
        // Prevent instantiation
    }

    @Deprecated
    public static final String LOT_IDENTIFICATION_TYPE_GS1_BATCH = "GS1_BATCH";

    /**
     * Traceability lot code.
     */
    public static final String LOT_IDENTIFICATION_TYPE_TLC = "TLC";

}
