package org.dddml.ffvtraceability.domain.constants;

public final class BffPartyConstants {
    public static final String DEFAULT_PREFERRED_CURRENCY_UOM_ID = "USD";
    public static final String PARTY_ROLE_SUPPLIER = "SUPPLIER";
    public static final String PARTY_ROLE_CUSTOMER = "CUSTOMER";
    public static final String PARTY_STATUS_ACTIVE = "ACTIVE";
    public static final String PARTY_STATUS_INACTIVE = "INACTIVE";
    /**
     * GGN (GLOBALG.A.P. Number)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_GGN = "GGN";
    /**
     * GLN (Global Location Number)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_GLN = "GLN";
    /**
     * GCP (GS1 Company Prefix)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_GS1_COMPANY_PREFIX = "GCP";
    /**
     * TAX_ID (Tax ID / VAT Number)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_TAX_ID = "TAX_ID";
    /**
     * INTERNAL_ID (Internal ID)
     */
    public static final String PARTY_IDENTIFICATION_TYPE_INTERNAL_ID = "INTERNAL_ID";

    private BffPartyConstants() {
        // Prevent instantiation
    }
} 