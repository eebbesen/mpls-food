package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.PlaceHour;
import com.humegatech.mpls_food.models.PlaceHourDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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
    void testGet() {
        final Place place = TestObjects.place("place");
        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));

        final Place foundPlace = service.get(place.getId());

        assertEquals(place.getName(), foundPlace.getName());
    }

    @Test
    void testGetNotFound() {
        final Exception exception = assertThrows(ResponseStatusException.class, () -> service.get(99L));
        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void testFindAll() {
        final List<Place> places = TestObjects.places();

        when(placeRepository.findAll(Sort.by("name"))).thenReturn(places);

        final List<Place> foundPlaces = service.findAll();

        assertEquals("Taco John's", foundPlaces.get(1).getName());
        assertEquals("Ginelli's Pizza", foundPlaces.get(0).getName());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreate() {
        final Place place = TestObjects.place("Taco Bell");
        place.setId(99L);

        final Place placeToCreate = Place.builder()
                .id(place.getId())
                .name(place.getName())
                .build();

        when(placeRepository.save(any(Place.class))).thenReturn(place);

        final Long id = service.create(placeToCreate);

        assertEquals(place.getId(), id);
        assertEquals(place.getId(), id);
    }

    @Test
    @WithAnonymousUser
    void testCreateUnauthenticated() {
        assertThrows(AccessDeniedException.class, () -> {
            service.create(null);
        });
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
    @WithMockUser(roles = "USER")
    void testDeleteUserRole() {
        assertThrows(AccessDeniedException.class, () -> {
            service.delete(99L);
        });
    }

    @Test
    @WithAnonymousUser
    void testDeleteUnauthenticated() {
        assertThrows(AccessDeniedException.class, () -> {
            service.delete(99L);
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate() {
        final Place place = TestObjects.place("Taco Bell");
        final Place placeToUpdate = Place.builder()
                .name(place.getName())
                .build();

        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));

        service.update(placeToUpdate);

        verify(placeRepository, times(1)).save(place);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateUserRole() {
        assertThrows(AccessDeniedException.class, () -> {
            service.update(null);
        });
    }

    @Test
    @WithAnonymousUser
    void testUpdateUnauthenticated() {
        assertThrows(AccessDeniedException.class, () -> {
            service.update(null);
        });
    }
}
