package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.PlaceHour;
import com.humegatech.mpls_food.domains.RewardType;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.models.PlaceHourDTO;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlaceServiceTest extends MFServiceTest {
    @Autowired
    private PlaceService service;

    private void comparePlaceHours(final Set<PlaceHour> placeHours, final Set<PlaceHourDTO> placeHourDTOs) {
        assertEquals(placeHours.size(), placeHourDTOs.size());
        for (final PlaceHour placeHour : placeHours) {
            final PlaceHourDTO placeHourDTO = placeHourDTOs.stream()
                    .filter(ph -> ph.getDayOfWeek().equals(placeHour.getDayOfWeek()))
                    .findFirst()
                    .orElseThrow();
            assertEquals(placeHour.getPlace().getId(), placeHourDTO.getPlace());
            assertEquals(placeHour.getOpenTime(), placeHourDTO.getOpenTime());
            assertEquals(placeHour.getCloseTime(), placeHourDTO.getCloseTime());
        }
    }

    @Test
    void testMapToDTO() {
        final Place place = TestObjects.ginellis();

        final PlaceDTO placeDTO = ReflectionTestUtils.invokeMethod(service, "mapToDTO", place, new PlaceDTO());

        assertNotNull(placeDTO);
        assertEquals(place.getId(), placeDTO.getId());
        assertEquals(place.getName(), placeDTO.getName());
        assertEquals(place.getAddress(), placeDTO.getAddress());
        assertEquals(place.getWebsite(), placeDTO.getWebsite());
        assertEquals(place.isApp(), placeDTO.isApp());
        assertEquals(place.isOrderAhead(), placeDTO.isOrderAhead());
        assertEquals(MplsFoodUtils.truncateAddress(place.getAddress()), placeDTO.getTruncatedAddress());
        assertEquals(place.getReward().getNotes(), placeDTO.getRewardNotes());
        assertEquals(place.getReward().getRewardType(), placeDTO.getRewardType());
        assertEquals(place.getPlaceHours().size(), placeDTO.getPlaceHours().size());
        comparePlaceHours(place.getPlaceHours(), placeDTO.getPlaceHours());
    }

    @Test
    void testMapToDTONoReward() {
        final Place place = TestObjects.ginellis();
        place.setReward(null);

        final PlaceDTO placeDTO = ReflectionTestUtils.invokeMethod(service, "mapToDTO", place, new PlaceDTO());

        assertNotNull(placeDTO);
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
    void testMapToEntityNewPlace() {
        final PlaceDTO placeDTO = PlaceDTO.builder()
                .name("test")
                .address("123 Robert St")
                .rewardNotes("Free slice after purchase of 9 regularly priced slices")
                .rewardType(RewardType.PUNCH_CARD)
                .id(5L)
                .build();

        Place place = ReflectionTestUtils.invokeMethod(service, "mapToEntity", placeDTO, new Place());

        assertNotNull(place);
        assertEquals(placeDTO.getName(), place.getName());
        assertEquals(placeDTO.getAddress(), place.getAddress());
        assertEquals(placeDTO.getRewardType(), place.getReward().getRewardType());
        assertEquals(placeDTO.getRewardNotes(), place.getReward().getNotes());
    }

    @Test
    void testMapToEntityRewardUpdate() {
        final Place place = TestObjects.ginellis();
        final PlaceDTO placeDTO = PlaceDTO.builder()
                .name(place.getName())
                .address(place.getAddress())
                .rewardNotes("UPDATED: Free slice after purchase of 9 regularly priced slices")
                .rewardType(RewardType.PUNCH_CARD)
                .id(place.getId())
                .build();

        Place updatedPlace = ReflectionTestUtils.invokeMethod(service, "mapToEntity", placeDTO, new Place());

        assertNotNull(updatedPlace);
        assertEquals(placeDTO.getName(), updatedPlace.getName());
        assertEquals(placeDTO.getAddress(), updatedPlace.getAddress());
        assertEquals(placeDTO.getRewardType(), updatedPlace.getReward().getRewardType());
        assertEquals(placeDTO.getRewardNotes(), updatedPlace.getReward().getNotes());
        assertEquals(place.getReward().getId(), updatedPlace.getReward().getId());
    }

    @Test
    void testMapToEntityRewardOnPlace() {
        final Place place = TestObjects.ginellis();

        final PlaceDTO placeDTO = PlaceDTO.builder()
                .name(place.getName())
                .address(place.getAddress())
                .id(place.getId())
                .rewardType(RewardType.PUNCH_CARD)
                .rewardNotes("reward")
                .build();

        Place updatedPlace = ReflectionTestUtils.invokeMethod(service, "mapToEntity", placeDTO, place);

        assertNotNull(updatedPlace);
        assertEquals(placeDTO.getName(), updatedPlace.getName());
        assertEquals(placeDTO.getAddress(), updatedPlace.getAddress());
        assertEquals(placeDTO.getRewardType(), updatedPlace.getReward().getRewardType());
        assertEquals(placeDTO.getRewardNotes(), updatedPlace.getReward().getNotes());
    }

    @Test
    void testMapToEntityNoRewardOnPlace() {
        final Place place = TestObjects.ginellis();
        place.setReward(null);

        final PlaceDTO placeDTO = PlaceDTO.builder()
                .name(place.getName())
                .address(place.getAddress())
                .id(place.getId())
                .rewardType(RewardType.PUNCH_CARD)
                .rewardNotes("reward")
                .build();

        Place updatedPlace = ReflectionTestUtils.invokeMethod(service, "mapToEntity", placeDTO, place);

        assertNotNull(updatedPlace);
        assertEquals(placeDTO.getName(), updatedPlace.getName());
        assertEquals(placeDTO.getAddress(), updatedPlace.getAddress());
        assertEquals(placeDTO.getRewardType(), updatedPlace.getReward().getRewardType());
        assertEquals(placeDTO.getRewardNotes(), updatedPlace.getReward().getNotes());
    }

    @Test
    void testGet() {
        final Place place = TestObjects.place("place");
        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));

        final PlaceDTO placeDTO = service.get(place.getId());

        assertEquals(place.getName(), placeDTO.getName());
        assertEquals(place.getPlaceHours().size(), placeDTO.getPlaceHours().size());
    }

    @Test
    void testGetNotFound() {
        final Exception exception = assertThrows(ResponseStatusException.class, () -> service.get(99L));
        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateNotFound() {
        final PlaceDTO dto = new PlaceDTO();
        final Exception exception = assertThrows(ResponseStatusException.class, () -> service.update(99L, dto));
        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void testFindAll() {
        final List<Place> places = TestObjects.places();

        when(placeRepository.findAll(Sort.by("name"))).thenReturn(places);

        final List<PlaceDTO> placeDTOs = service.findAll();

        assertEquals("Taco John's", placeDTOs.get(1).getName());
        assertEquals("Ginelli's Pizza", placeDTOs.get(0).getName());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreate() {
        final Place place = TestObjects.place("Taco Bell");
        place.setId(99L);

        final PlaceDTO placeDTO = PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .build();

        when(placeRepository.save(any(Place.class))).thenReturn(place);

        final Long id = service.create(placeDTO);

        assertEquals(place.getId(), id);
    }

    @Test
    void testNameExistsNameExists() {
        when(placeRepository.existsByNameIgnoreCase("exists")).thenReturn(true);

        assertTrue(service.nameExists("exists"));
    }

    @Test
    void testNameExistsNameDoesNotExist() {
        when(placeRepository.existsByNameIgnoreCase("exists")).thenReturn(false);

        assertFalse(service.nameExists("exists"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDelete() {
        service.delete(99L);

        verify(placeRepository, times(1)).deleteById(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate() {
        final Place place = TestObjects.place("Taco Bell");
        final PlaceDTO placeDTO = PlaceDTO.builder()
                .name(place.getName())
                .build();

        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));

        service.update(place.getId(), placeDTO);

        verify(placeRepository, times(1)).findById(place.getId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateNoPlace() {
        final PlaceDTO dto = new PlaceDTO();
        assertThrows(ResponseStatusException.class, () -> service.update(99L, dto));
    }
}
