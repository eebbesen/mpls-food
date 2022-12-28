package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PlaceRepositoryTest {
    @Autowired
    PlaceRepository repository;

    @Test
    void testFindByIdIn() {
        final Set<Place> places = TestObjects.places().stream().collect(Collectors.toSet());
        final List<Place> persistedPlaces = repository.saveAll(places);
        final List<Long> placeIds = List.of(persistedPlaces.get(0).getId(), persistedPlaces.get(1).getId());

        List<Place> retrievedPlaces = repository.findByIdIn(placeIds);

        assertEquals(placeIds.size(), retrievedPlaces.size());
        assertTrue(placeIds.contains(retrievedPlaces.get(0).getId()));
        assertTrue(placeIds.contains(retrievedPlaces.get(1).getId()));
    }
}
