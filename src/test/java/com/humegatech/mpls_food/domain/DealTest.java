package com.humegatech.mpls_food.domain;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealTest {
    private Deal dealMonTues;

    @BeforeEach
    void setUp() {
        dealMonTues = TestObjects.dealMonTues();
    }

    @Test
    void testHasDay() {
        assertNotNull(dealMonTues.hasDay(DayOfWeek.MONDAY));
        assertNotNull(dealMonTues.hasDay(DayOfWeek.TUESDAY));
        assertNull(dealMonTues.hasDay(DayOfWeek.WEDNESDAY));
        assertNull(dealMonTues.hasDay(DayOfWeek.THURSDAY));
        assertNull(dealMonTues.hasDay(DayOfWeek.FRIDAY));
        assertNull(dealMonTues.hasDay(DayOfWeek.SATURDAY));
        assertNull(dealMonTues.hasDay(DayOfWeek.SUNDAY));
    }

    @Test
    void testGetDaysOfWeek() {
        List<DayOfWeek> dows = dealMonTues.getDaysOfWeek();

        assertEquals(2, dows.size());
        assertTrue(dows.contains(DayOfWeek.MONDAY));
        assertTrue(dows.contains(DayOfWeek.TUESDAY));
    }
}
