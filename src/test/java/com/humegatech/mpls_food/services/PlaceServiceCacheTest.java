package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlaceServiceCacheTest extends MFServiceTest {
    private static final CacheManager cacheManager = new ConcurrentMapCacheManager("allPlaces", "singlePlace");
    @Autowired
    private PlaceService service;

    @BeforeEach
    void setUp() {
        cacheManager.getCacheNames().forEach(cn -> Objects.requireNonNull(cacheManager.getCache(cn)).clear());
    }

    @Test
    void testGetCaching() {
        final Place place = TestObjects.place("place");
        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));

        final Place foundPlace = service.get(place.getId());

        verify(placeRepository, times(1)).findById(place.getId());
        assertEquals(place.getName(), foundPlace.getName());

        final Place cachedPlace = service.get(place.getId());
        assertEquals(cachedPlace.getName(), foundPlace.getName());
        verifyNoMoreInteractions(placeRepository);
    }

    @Test
    void testFindAllCaching() {
        final List<Place> places = TestObjects.places();
        when(placeRepository.findAll(Sort.by("name"))).thenReturn(places);

        final List<Place> foundPlaces = service.findAll();
        verify(placeRepository, times(1)).findAll(Sort.by("name"));

        assertEquals("Taco John's", foundPlaces.get(1).getName());
        assertEquals("Ginelli's Pizza", foundPlaces.get(0).getName());

        final List<Place> cachedPlaces = service.findAll();
        assertEquals(cachedPlaces.size(), 2);
        assertTrue(cachedPlaces.containsAll(foundPlaces));
        verifyNoMoreInteractions(placeRepository);
    }

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {
        @Bean
        public CacheManager cacheManager() {
            return cacheManager;
        }

        @Bean
        public PlaceService placeService(final PlaceRepository placeRepository) {
            return new PlaceService(placeRepository);
        }
    }

}