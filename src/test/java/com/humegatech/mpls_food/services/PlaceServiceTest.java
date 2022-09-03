package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PlaceServiceTest {
    @MockBean
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceService service;

    @Test
    void testMapToDTO() {
        Place place = TestObjects.ginellis();

        PlaceDTO placeDTO = ReflectionTestUtils.invokeMethod(service, "mapToDTO", place, new PlaceDTO());

        assertEquals(place.getId(), placeDTO.getId());
        assertEquals(place.getName(), placeDTO.getName());
        assertEquals(place.getAddress(), placeDTO.getAddress());
        assertEquals(place.getWebsite(), placeDTO.getWebsite());
        assertEquals(place.isApp(), placeDTO.isApp());
        assertEquals(place.isOrderAhead(), placeDTO.isOrderAhead());
        assertEquals(MplsFoodUtils.truncateAddress(place.getAddress()), placeDTO.getTruncatedAddress());
    }

}
