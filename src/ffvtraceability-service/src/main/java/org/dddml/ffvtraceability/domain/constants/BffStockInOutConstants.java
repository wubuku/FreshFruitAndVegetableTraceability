package org.dddml.ffvtraceability.domain.constants;

public final class BffStockInOutConstants {
    private BffStockInOutConstants() {
        // Prevent instantiation
    }

    public static final String INVENTORY_TRANSFER_IN = "TRANSFER_IN";

    public static final String INVENTORY_TRANSFER_OUT = "TRANSFER_OUT";
    /**
     * Quantity adjustment
     */
    public static final String VARIANCE_REASON_TYPE_QUANTITY_ADJUSTMENT = "QUANTITY_ADJUSTMENT";
    /**
     * Scrap
     */
    public static final String VARIANCE_REASON_TYPE_QUANTITY_SCRAP = "SCRAP";
    /**
     * Return to vendor
     */
    public static final String VARIANCE_REASON_TYPE_RETURN_TO_VENDOR = "RETURN_TO_VENDOR";

    /**
     * Receiving
     */
    public static final String INOUT_TYPE_RECEIVING = "RECEIVING";

}
