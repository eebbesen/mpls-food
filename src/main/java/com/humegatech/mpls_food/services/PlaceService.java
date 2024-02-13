package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Reward;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.models.PlaceHourDTO;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceHourService placeHourService;

    public PlaceService(final PlaceRepository placeRepository, PlaceHourService placeHourService) {
        this.placeRepository = placeRepository;
        this.placeHourService = placeHourService;
    }

    public List<PlaceDTO> findAll() {
        return placeRepository.findAll(Sort.by("name"))
                .stream()
                .map(place -> mapToDTO(place, new PlaceDTO()))
                .toList();
    }

    public PlaceDTO get(final Long id) {
        return placeRepository.findById(id)
                .map(place -> mapToDTO(place, new PlaceDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @PreAuthorize("hasRole('USER')")
    public Long create(final PlaceDTO placeDTO) {
        final Place place = new Place();
        mapToEntity(placeDTO, place);

        return placeRepository.save(place).getId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void update(final Long id, final PlaceDTO placeDTO) {
        final Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(placeDTO, place);
        placeRepository.save(place);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(final Long id) {
        placeRepository.deleteById(id);
    }

    protected PlaceDTO mapToDTO(final Place place, final PlaceDTO placeDTO) {
        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        placeDTO.setAddress(place.getAddress());
        placeDTO.setWebsite(place.getWebsite());
        placeDTO.setApp(place.isApp());
        placeDTO.setOrderAhead(place.isOrderAhead());
        placeDTO.setTruncatedAddress(MplsFoodUtils.truncateAddress(place.getAddress()));
        placeDTO.setRewardNotes(null == place.getReward() ? null : place.getReward().getNotes());
        placeDTO.setRewardType(null == place.getReward() ? null : place.getReward().getRewardType());
        placeDTO.setPlaceHours(place.getPlaceHours().stream()
                .map(placeHour -> placeHourService.mapToDTO(placeHour, new PlaceHourDTO()))
                .collect(Collectors.toSet()));

        return placeDTO;
    }

    private Place mapToEntity(final PlaceDTO placeDTO, final Place place) {
        Reward reward = null;
        if (null != placeDTO.getRewardType()) {
            reward = place.getReward();
            if (null == reward) {
                reward = new Reward();
                reward.setPlace(place);
            }

            reward.setRewardType(placeDTO.getRewardType());
            reward.setNotes(placeDTO.getRewardNotes());
        }

        place.setName(placeDTO.getName());
        place.setAddress(placeDTO.getAddress());
        place.setWebsite(placeDTO.getWebsite());
        place.setApp(placeDTO.isApp());
        place.setOrderAhead(placeDTO.isOrderAhead());
        place.setReward(reward);

        return place;
    }

    public boolean nameExists(final String name) {
        return placeRepository.existsByNameIgnoreCase(name);
    }

}
