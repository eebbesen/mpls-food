package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Reward;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.repositories.RewardRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final RewardRepository rewardRepository;

    public PlaceService(final PlaceRepository placeRepository, RewardRepository rewardRepository) {
        this.placeRepository = placeRepository;
        this.rewardRepository = rewardRepository;
    }

    public List<PlaceDTO> findAll() {
        return placeRepository.findAll(Sort.by("name"))
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
        Long placeId = placeRepository.save(place).getId();
        if (null != placeDTO.getRewardType()) {
            final Reward reward = Reward.builder()
                    .rewardType(placeDTO.getRewardType())
                    .notes(placeDTO.getRewardNotes())
                    .place(place)
                    .build();
            rewardRepository.save(reward);
        }

        return place.getId();
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
        placeDTO.setTruncatedAddress(MplsFoodUtils.truncateAddress(place.getAddress()));
        placeDTO.setRewardNotes(null == place.getReward() ? null : place.getReward().getNotes());
        placeDTO.setRewardType(null == place.getReward() ? null : place.getReward().getRewardType());
        return placeDTO;
    }

    private Place mapToEntity(final PlaceDTO placeDTO, final Place place) {
        place.setName(placeDTO.getName());
        place.setAddress(placeDTO.getAddress());
        place.setWebsite(placeDTO.getWebsite());
        place.setApp(placeDTO.isApp());
        place.setOrderAhead(placeDTO.isOrderAhead());

        if (null != placeDTO.getRewardType()) {
            final Reward reward = Reward.builder()
                    .notes(placeDTO.getRewardNotes())
                    .rewardType(placeDTO.getRewardType())
                    .place(place)
                    .build();

            place.setReward(reward);
        }

        return place;
    }

    public boolean nameExists(final String name) {
        return placeRepository.existsByNameIgnoreCase(name);
    }

}
