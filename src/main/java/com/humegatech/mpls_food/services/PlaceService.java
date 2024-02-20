package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(final PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Cacheable("allPlaces")
    public List<Place> findAll() {
        return placeRepository.findAll(Sort.by("name"));
    }

    @Cacheable("singlePlace")
    public Place get(final Long id) {
        return placeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('USER')")
    @CacheEvict(value = "allPlaces", allEntries = true)
    public Long create(final Place place) {
        return placeRepository.save(place).getId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = {"allPlaces", "singlePlace"}, allEntries = true)
    public void update(final Place place) {
        placeRepository.save(place);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = {"allPlaces", "singlePlace"}, allEntries = true)
    public void delete(final Long id) {
        placeRepository.deleteById(id);
    }

    public boolean nameExists(final String name) {
        return placeRepository.existsByNameIgnoreCase(name);
    }

}
