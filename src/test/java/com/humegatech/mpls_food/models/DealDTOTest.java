package com.humegatech.mpls_food.models;

import com.humegatech.mpls_food.domains.Place;
import org.junit.jupiter.api.Test;

public class DealDTOTest {

    @Test
    public void testDaysActive() {
        Place place = new Place();
        place.setAddress("123");
        place.setName("Taco Town");

        DealDTO deal = new DealDTO();
        deal.setDescription("test deal");
        deal.setPlace(place);
    }

    @Test
    public void testDaysActiveNoDays() {
        Place place = new Place();
        place.setAddress("123");
        place.setName("Taco Town");

        DealDTO deal = new DealDTO();
        deal.setDescription("test deal");
        deal.setPlace(place);

    }
}
