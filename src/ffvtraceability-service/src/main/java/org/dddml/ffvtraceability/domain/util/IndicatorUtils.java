package org.dddml.ffvtraceability.domain.util;

public class IndicatorUtils {
    public static final String INDICATOR_YES = "Y";
    public static final String INDICATOR_NO = "N";

    private IndicatorUtils() {
    }

    public static Boolean toBoolean(String indicator) {
        if (indicator == null) {
            return null;
        }
        String normalizedIndicator = indicator.trim().toUpperCase();
        switch (normalizedIndicator) {
            case "Y":
            case "YES":
            case "T":
            case "TRUE":
            case "1":
                return true;
            case "N":
            case "NO":
            case "F":
            case "FALSE":
            case "0":
                return false;
            default:
                return INDICATOR_YES.equalsIgnoreCase(indicator) || "true".equalsIgnoreCase(indicator);
        }
    }

    public static String asIndicatorDefaultYes(String indicator) {
        if (indicator == null) {
            return INDICATOR_YES;
        }
        return toBoolean(indicator) ? INDICATOR_YES : INDICATOR_NO;
    }

    public static String asIndicatorDefaultNo(String indicator) {
        if (indicator == null) {
            return INDICATOR_NO;
        }
        return toBoolean(indicator) ? INDICATOR_YES : INDICATOR_NO;
    }

    public static String toIndicator(Boolean b) {
        if (b == null) {
            return null;
        }
        return b ? INDICATOR_YES : INDICATOR_NO;
    }
}
