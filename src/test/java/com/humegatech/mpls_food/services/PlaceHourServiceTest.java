package com.humegatech.mpls_food.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.PlaceHour;
import com.humegatech.mpls_food.models.PlaceHourDTO;

@SpringBootTest
class PlaceHourServiceTest extends MFServiceTest {
    @Autowired
    private PlaceHourService service;

    @Test
    void testMapToDTO() {
        Place place = TestObjects.place("Sal's");
        PlaceHour placeHour = TestObjects.placeHour(place, DayOfWeek.THURSDAY, true);

        PlaceHourDTO placeHourDTO = service.mapToDTO(placeHour, new PlaceHourDTO());

        assertEquals(placeHour.getId(), placeHourDTO.getId());
        assertEquals(placeHour.getPlace().getId(), placeHourDTO.getPlace());
        assertEquals(placeHour.getOpenTime(), placeHourDTO.getOpenTime());
        assertEquals(placeHour.getCloseTime(), placeHourDTO.getCloseTime());
        assertEquals(placeHour.getDayOfWeek(), placeHourDTO.getDayOfWeek());
    }

    @Test
    void testMapToEntity() {
        Place place = TestObjects.place("Sal's");
        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));

        PlaceHourDTO placeHourDTO = PlaceHourDTO.builder()
                .id(5)
                .place(place.getId())
                .openTime(LocalTime.of(7, 30))
                .closeTime(LocalTime.of(18, 0))
                .dayOfWeek(DayOfWeek.THURSDAY)
                .build();

        PlaceHour placeHour = service.mapToEntity(placeHourDTO, new PlaceHour());

        assertEquals(placeHourDTO.getId(), placeHour.getId());
        assertEquals(placeHourDTO.getPlace(), placeHour.getPlace().getId());
        assertEquals(placeHourDTO.getOpenTime(), placeHour.getOpenTime());
        assertEquals(placeHourDTO.getCloseTime(), placeHour.getCloseTime());
        assertEquals(placeHourDTO.getDayOfWeek(), placeHour.getDayOfWeek());
    }
}
