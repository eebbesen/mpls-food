package com.humegatech.mpls_food.util;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.models.DayDTO;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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
        assertEquals("App", MplsFoodUtils.capitalizeFirst("APP"));
        assertNull(MplsFoodUtils.capitalizeFirst(null));
        assertNull(MplsFoodUtils.capitalizeFirst(""));
        assertEquals("Punch Card", MplsFoodUtils.capitalizeFirst("PUNCH_CARD"));
    }

    @Test
    void testCondensedDays() {
        final LocalDateTime mon = LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(mon);
            assertEquals("-------", MplsFoodUtils.condensedDays(new ArrayList()));
            assertEquals("MT-----", MplsFoodUtils.condensedDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)));
            assertEquals("-TWt-Ss", MplsFoodUtils.condensedDays(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));
        }

        final LocalDateTime sun = LocalDateTime.of(2022, Month.SEPTEMBER, 4, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(sun);
            assertEquals("-------", MplsFoodUtils.condensedDays(new ArrayList()));
            assertEquals("-MT----", MplsFoodUtils.condensedDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)));
            assertEquals("s-TWt-S", MplsFoodUtils.condensedDays(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));
        }
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
    void testGetSortOrderByDay() {
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

    @Test
    void testDaysSorting() {
        Deal deal = TestObjects.deal();
        DayDTO mon = DayDTO.builder()
                .deal(deal.getId())
                .dayOfWeek(DayOfWeek.MONDAY).build();
        DayDTO sun = DayDTO.builder()
                .deal(deal.getId())
                .dayOfWeek(DayOfWeek.SUNDAY).build();
        DayDTO wed = DayDTO.builder()
                .deal(deal.getId())
                .dayOfWeek(DayOfWeek.WEDNESDAY).build();
        DayDTO mon2 = DayDTO.builder()
                .deal(deal.getId())
                .dayOfWeek(DayOfWeek.MONDAY).build();

        List<DayDTO> days = Arrays.asList(mon, wed, mon2, sun).stream().collect(Collectors.toList());

        Map<DayOfWeek, Integer> wedFirst = MplsFoodUtils.getSortOrderFromDay(DayOfWeek.WEDNESDAY);
        days.sort(Comparator.comparing((DayDTO d) -> wedFirst.get(d.getDayOfWeek())));

        assertEquals(DayOfWeek.WEDNESDAY, days.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, days.get(1).getDayOfWeek());
        assertEquals(DayOfWeek.MONDAY, days.get(2).getDayOfWeek());
        assertEquals(DayOfWeek.MONDAY, days.get(2).getDayOfWeek());
    }


    @Test
    void testGetRangeMinNullMaxNull() {
        final String rangeString = MplsFoodUtils.getRange(null, null, "$");
        assertEquals(null, rangeString);
    }

    @Test
    void testGetRangeMinNullMaxPopulated() {
        final String rangeString = MplsFoodUtils.getRange(null, 1.22, "$");
        assertEquals("$1.22", rangeString);
    }

    @Test
    void testGetRangeMinPopulatedMaxNull() {
        final String rangeString = MplsFoodUtils.getRange(1.22, null, "$");
        assertEquals("$1.22", rangeString);
    }

    @Test
    void testGetRangeSameWithDollar() {
        final String rangeString = MplsFoodUtils.getRange(1.22, 1.22, "$");
        assertEquals("$1.22", rangeString);
    }

    @Test
    void testGetRangeSameWithPercent() {
        final String rangeString = MplsFoodUtils.getRange(42.0, 42.0, "%");
        assertEquals("42%", rangeString);
    }

    @Test
    void testGetRangeDifferent() {
        final String rangeString = MplsFoodUtils.getRange(42.0, 55.0, "%");
        assertEquals("42% - 55%", rangeString);
    }

    @Test
    void testDecorateValuePercent() {
        final String decorated = MplsFoodUtils.decorateValue(42.0, "%");
        assertEquals("42%", decorated);
    }

    @Test
    void testDecorateValueDollarSign() {
        final String decorated = MplsFoodUtils.decorateValue(42.0, "$");
        assertEquals("$42.00", decorated);
    }

    @Test
    void testDecorateValueNullValue() {
        assertNull(MplsFoodUtils.decorateValue(null, "$"));
    }

    @Test
    void testDecorateValueNullPunctuation() {
        assertEquals("5.0", MplsFoodUtils.decorateValue(5.0, null));
    }
}
