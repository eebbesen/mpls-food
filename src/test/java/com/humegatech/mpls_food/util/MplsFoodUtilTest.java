package com.humegatech.mpls_food.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MplsFoodUtilTest {


    @Test
    void testCapitalizeFirst() {
        assertEquals("Dealdto", MplsFoodUtils.capitalizeFirst("dealDTO"));
        assertNull(MplsFoodUtils.capitalizeFirst(null));
    }
}
