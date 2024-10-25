package org.dddml.ffvtraceability.domain;

public class TenantContext {

    private static ThreadLocal<String> currentTenantId = new ThreadLocal<>();

    public static String getTenantId() {
        return currentTenantId.get();
    }

    public static void setTenantId(String tenantId) {
        currentTenantId.set(tenantId);
    }

    public static void clear() {
        currentTenantId.set(null);
    }

}
