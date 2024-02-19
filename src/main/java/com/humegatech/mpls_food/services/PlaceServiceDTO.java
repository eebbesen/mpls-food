package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Reward;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.models.PlaceHourDTO;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaceServiceDTO {
    private final PlaceHourService placeHourService;
    private final PlaceService placeService;

    public PlaceServiceDTO(final PlaceHourService placeHourService, final PlaceService placeService) {
        this.placeHourService = placeHourService;
        this.placeService = placeService;
    }

    public List<PlaceDTO> findAll() {
        return placeService.findAll()
                .stream()
                .map(place -> mapToDTO(place, new PlaceDTO()))
                .toList();
    }

    public PlaceDTO get(final Long id) {
        return mapToDTO(placeService.get(id), new PlaceDTO());
    }

    @PreAuthorize("hasRole('USER')")
    public Long create(final PlaceDTO placeDTO) {
        final Place place = new Place();
        mapToEntity(placeDTO, place);

        return placeService.create(place);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void update(final Long id, final PlaceDTO placeDTO) {
        final Place place = placeService.get(id);
        mapToEntity(placeDTO, place);
        placeService.update(place);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(final Long id) {
        placeService.delete(id);
    }

    public boolean nameExists(final String name) {
        return placeService.nameExists(name);
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
}
