package org.dddml.ffvtraceability.domain.constants;

public final class BffSupplierConstants {
    private BffSupplierConstants() {
        // Prevent instantiation
    }

    public static final String DEFAULT_PREFERRED_CURRENCY_UOM_ID = "USD";
    public static final String PARTY_ROLE_SUPPLIER = "SUPPLIER";
    
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
} 