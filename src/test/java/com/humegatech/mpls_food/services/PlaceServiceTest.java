package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.RewardType;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        assertEquals(place.getReward().getNotes(), placeDTO.getRewardNotes());
        assertEquals(place.getReward().getRewardType(), placeDTO.getRewardType());
    }

    @Test
    void testMapToDTONoReward() {
        Place place = TestObjects.ginellis();
        place.setReward(null);

        PlaceDTO placeDTO = ReflectionTestUtils.invokeMethod(service, "mapToDTO", place, new PlaceDTO());

        assertEquals(place.getId(), placeDTO.getId());
        assertEquals(place.getName(), placeDTO.getName());
        assertEquals(place.getAddress(), placeDTO.getAddress());
        assertEquals(place.getWebsite(), placeDTO.getWebsite());
        assertEquals(place.isApp(), placeDTO.isApp());
        assertEquals(place.isOrderAhead(), placeDTO.isOrderAhead());
        assertEquals(MplsFoodUtils.truncateAddress(place.getAddress()), placeDTO.getTruncatedAddress());
        assertNull(placeDTO.getRewardNotes());
        assertNull(placeDTO.getRewardType());
    }

    @Test
    void testMapToEntity() {
        PlaceDTO placeDTO = PlaceDTO.builder()
                .name("test")
                .address("123 Robert St")
                .rewardNotes("Free slice after purchase of 9 regularly priced slices")
                .rewardType(RewardType.PUNCH_CARD)
                .id(5l)
                .build();

        Place place = ReflectionTestUtils.invokeMethod(service, "mapToEntity", placeDTO, new Place());

        assertEquals(placeDTO.getName(), place.getName());
        assertEquals(placeDTO.getAddress(), place.getAddress());
        assertEquals(placeDTO.getRewardType(), place.getReward().getRewardType());
        assertEquals(placeDTO.getRewardNotes(), place.getReward().getNotes());
    }

}
