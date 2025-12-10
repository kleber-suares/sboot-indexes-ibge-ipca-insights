package com.kls.references.indexes.ibge.ipca.insights.util;

import java.math.BigDecimal;

public class NumberChecker {

    private NumberChecker() {}

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNumeric(String str) {
        if (isBlank(str)) {
            return false;
        }
        return isDouble(str);
    }

    public static boolean isInteger(String str) {
        if (isBlank(str)) {
            return false;
        }
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isDouble(String str) {
        if (isBlank(str)) {
            return false;
        }
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isBigDecimal(String str) {
        if (isBlank(str)) {
            return false;
        }
        try {
            new BigDecimal(str.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
