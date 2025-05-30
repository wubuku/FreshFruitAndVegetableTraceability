package org.dddml.ffvtraceability.domain.constants;

public class BffOrderConstants {
    private BffOrderConstants() {
        // Prevent instantiation
    }

    public static final String RECEIVING = "RECEIVING";

    public static final String PURCHASE_ORDER = "PURCHASE_ORDER";

    public static final String SALES_ORDER = "SALES_ORDER";

    public static final String PRODUCT_ORDER_ITEM = "PRODUCT_ORDER_ITEM";

    public static final String ROLE_TYPE_SUPPLIER = "SUPPLIER";
    public static final String ROLE_TYPE_CUSTOMER = "CUSTOMER";

    public static final String FULFILLMENT_STATUS_FULFILLED = "FULFILLED";
    public static final String FULFILLMENT_STATUS_PARTIALLY_FULFILLED = "PARTIALLY_FULFILLED";
    public static final String FULFILLMENT_STATUS_NOT_FULFILLED = "NOT_FULFILLED";

}
