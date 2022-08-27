package com.humegatech.mpls_food.service;

import com.humegatech.mpls_food.domain.Place;
import com.humegatech.mpls_food.model.PlaceDTO;
import com.humegatech.mpls_food.repos.PlaceRepository;
import com.humegatech.mpls_food.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(final PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<PlaceDTO> findAll() {
        return placeRepository.findAll(Sort.by("id"))
                .stream()
                .map(place -> mapToDTO(place, new PlaceDTO()))
                .collect(Collectors.toList());
    }

    public PlaceDTO get(final Long id) {
        return placeRepository.findById(id)
                .map(place -> mapToDTO(place, new PlaceDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final PlaceDTO placeDTO) {
        final Place place = new Place();
        mapToEntity(placeDTO, place);
        return placeRepository.save(place).getId();
    }

    public void update(final Long id, final PlaceDTO placeDTO) {
        final Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(placeDTO, place);
        placeRepository.save(place);
    }

    public void delete(final Long id) {
        placeRepository.deleteById(id);
    }

    private PlaceDTO mapToDTO(final Place place, final PlaceDTO placeDTO) {
        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        placeDTO.setAddress(place.getAddress());
        placeDTO.setWebsite(place.getWebsite());
        placeDTO.setApp(place.isApp());
        placeDTO.setOrderAhead(place.isOrderAhead());
        return placeDTO;
    }

    private Place mapToEntity(final PlaceDTO placeDTO, final Place place) {
        place.setName(placeDTO.getName());
        place.setAddress(placeDTO.getAddress());
        place.setWebsite(placeDTO.getWebsite());
        place.setApp(placeDTO.isApp());
        place.setOrderAhead(placeDTO.isOrderAhead());
        return place;
    }

    public boolean nameExists(final String name) {
        return placeRepository.existsByNameIgnoreCase(name);
    }

    @Transactional
    public String getReferencedWarning(final Long id) {
        final Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!place.getDeals().isEmpty()) {
            return WebUtils.getMessage("place.deal.manyToOne.referenced", place.getDeals().iterator().next().getId());
        }
        return null;
    }

}
