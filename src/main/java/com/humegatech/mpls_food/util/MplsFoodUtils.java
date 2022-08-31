package com.humegatech.mpls_food.util;

public class MplsFoodUtils {
    public static String capitalizeFirst(final String string) {
        if (null == string) {
            return null;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
