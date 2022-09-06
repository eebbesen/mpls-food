package com.humegatech.mpls_food.util;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
    public void testTruncateAddress() {
        assertEquals("123 Marquette Ave", MplsFoodUtils.truncateAddress("123 Marquette Ave\nMinneapolis, MN 55402"));
    }

    @Test
    public void testTruncateAddressMultipleCarriageReturns() {
        assertEquals("123 Marquette Ave Minneapolis, MN", MplsFoodUtils.truncateAddress("123 Marquette Ave\nMinneapolis, MN\n55402"));
    }

    @Test
    public void testTruncateAddressNoCarriageReturns() {
        assertEquals("123 Marquette Ave Minneapolis, MN 55402", MplsFoodUtils.truncateAddress("123 Marquette Ave Minneapolis, MN 55402"));
    }

    @Test
    public void testGetSortOrderByDay() {
        final Map<DayOfWeek, Integer> wed = MplsFoodUtils.getSortOrderFromDay(DayOfWeek.WEDNESDAY);

        assertEquals(5, wed.get(DayOfWeek.MONDAY));
        assertEquals(6, wed.get(DayOfWeek.TUESDAY));
        assertEquals(0, wed.get(DayOfWeek.WEDNESDAY));
        assertEquals(1, wed.get(DayOfWeek.THURSDAY));
        assertEquals(2, wed.get(DayOfWeek.FRIDAY));
        assertEquals(3, wed.get(DayOfWeek.SATURDAY));
        assertEquals(4, wed.get(DayOfWeek.SUNDAY));

        final Map<DayOfWeek, Integer> sun = MplsFoodUtils.getSortOrderFromDay(DayOfWeek.SUNDAY);

        assertEquals(1, sun.get(DayOfWeek.MONDAY));
        assertEquals(2, sun.get(DayOfWeek.TUESDAY));
        assertEquals(3, sun.get(DayOfWeek.WEDNESDAY));
        assertEquals(4, sun.get(DayOfWeek.THURSDAY));
        assertEquals(5, sun.get(DayOfWeek.FRIDAY));
        assertEquals(6, sun.get(DayOfWeek.SATURDAY));
        assertEquals(0, sun.get(DayOfWeek.SUNDAY));
    }
}
