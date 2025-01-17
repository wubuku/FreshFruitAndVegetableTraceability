package org.dddml.ffvtraceability.domain.constants;

public final class BffRawItemConstants {
    private BffRawItemConstants() {
        // Prevent instantiation
    }

    /**
     * Default currency for raw items
     */
    public static final String DEFAULT_CURRENCY_UOM_ID = "USD";
    //todo 供应商有 `PreferredCurrencyUomId` 属性。
    //    如果供应商的 `PreferredCurrencyUomId` 不为空，那么应该使用供应商的 `PreferredCurrencyUomId`。
    //    搜索使用了 DEFAULT_CURRENCY_UOM_ID 的地方，看看是否需要修改。
} 