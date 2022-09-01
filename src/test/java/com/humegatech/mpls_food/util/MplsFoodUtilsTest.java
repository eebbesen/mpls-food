package com.humegatech.mpls_food.util;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MplsFoodUtilsTest {

    @Test
    void testDowAbbreviation() {
        assertEquals("M", MplsFoodUtils.dowAbbreviation(DayOfWeek.MONDAY));
        assertEquals("T", MplsFoodUtils.dowAbbreviation(DayOfWeek.TUESDAY));
        assertEquals("W", MplsFoodUtils.dowAbbreviation(DayOfWeek.WEDNESDAY));
        assertEquals("t", MplsFoodUtils.dowAbbreviation(DayOfWeek.THURSDAY));
        assertEquals("F", MplsFoodUtils.dowAbbreviation(DayOfWeek.FRIDAY));
        assertEquals("S", MplsFoodUtils.dowAbbreviation(DayOfWeek.SATURDAY));
        assertEquals("s", MplsFoodUtils.dowAbbreviation(DayOfWeek.SUNDAY));
    }

    @Test
    void testCapitalizeFirst() {
        assertEquals("Dealdto", MplsFoodUtils.capitalizeFirst("dealDTO"));
        assertNull(MplsFoodUtils.capitalizeFirst(null));
    }

    @Test
    void testCondensedDays() {
        assertEquals("-------", MplsFoodUtils.condensedDays(new ArrayList()));
        assertEquals("MT-----", MplsFoodUtils.condensedDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)));
        assertEquals("-TWt-Ss", MplsFoodUtils.condensedDays(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));
    }

    @Test
    public void truncateAddress() {
        assertEquals("123 Marquette Ave", MplsFoodUtils.truncateAddress("123 Marquette Ave\nMinneapolis, MN 55402"));
    }

    @Test
    public void truncateAddressMultipleCarriageReturns() {
        assertEquals("123 Marquette Ave Minneapolis, MN", MplsFoodUtils.truncateAddress("123 Marquette Ave\nMinneapolis, MN\n55402"));
    }

    @Test
    public void truncateAddressNoCarriageReturns() {
        assertEquals("123 Marquette Ave Minneapolis, MN 55402", MplsFoodUtils.truncateAddress("123 Marquette Ave Minneapolis, MN 55402"));
    }
}
