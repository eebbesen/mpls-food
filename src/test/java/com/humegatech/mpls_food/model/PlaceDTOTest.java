package com.humegatech.mpls_food.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaceDTOTest {

    @Test
    public void truncatedAddress() {
        PlaceDTO place = new PlaceDTO();
        place.setAddress("123 Marquette Ave\nMinneapolis, MN 55402");

        assertEquals("123 Marquette Ave", place.truncatedAddress());
    }

    @Test
    public void truncatedAddressMultipleCarriageReturns() {
        PlaceDTO place = new PlaceDTO();
        place.setAddress("123 Marquette Ave\nMinneapolis, MN\n55402");

        assertEquals("123 Marquette Ave Minneapolis, MN", place.truncatedAddress());
    }

    @Test
    public void truncatedAddressNoCarriageReturns() {
        PlaceDTO place = new PlaceDTO();
        place.setAddress("123 Marquette Ave Minneapolis, MN 55402");

        assertEquals("123 Marquette Ave Minneapolis, MN 55402", place.truncatedAddress());
    }
}
