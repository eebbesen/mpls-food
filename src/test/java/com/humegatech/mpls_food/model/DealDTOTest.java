package com.humegatech.mpls_food.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DealDTOTest {
    @Test
    public void testDaysActive() {
        DealDTO deal = new DealDTO();
        deal.setDescription("test deal");
        deal.setPlace(10000l);
        deal.setSunday(true);
        deal.setThursday(true);

        assertEquals("Sunday, Thursday", deal.daysActive());
    }

    @Test
    public void testDaysActiveNoDays() {
        DealDTO deal = new DealDTO();
        deal.setDescription("test deal");
        deal.setPlace(10000l);

        assertEquals("", deal.daysActive());
    }
}
