package com.humegatech.mpls_food.service;

import com.humegatech.mpls_food.domain.Deal;
import com.humegatech.mpls_food.domain.Place;
import com.humegatech.mpls_food.model.DealDTO;
import com.humegatech.mpls_food.repos.DealRepository;
import com.humegatech.mpls_food.repos.PlaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DealService {

    private final DealRepository dealRepository;
    private final PlaceRepository placeRepository;

    public DealService(final DealRepository dealRepository, final PlaceRepository placeRepository) {
        this.dealRepository = dealRepository;
        this.placeRepository = placeRepository;
    }

    public List<DealDTO> findAll() {
        return dealRepository.findAll(Sort.by("id"))
                .stream()
                .map(deal -> mapToDTO(deal, new DealDTO()))
                .collect(Collectors.toList());
    }

    public DealDTO get(final Long id) {
        return dealRepository.findById(id)
                .map(deal -> mapToDTO(deal, new DealDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final DealDTO dealDTO) {
        final Deal deal = new Deal();
        mapToEntity(dealDTO, deal);
        return dealRepository.save(deal).getId();
    }

    public void update(final Long id, final DealDTO dealDTO) {
        final Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(dealDTO, deal);
        dealRepository.save(deal);
    }

    public void delete(final Long id) {
        dealRepository.deleteById(id);
    }

    private DealDTO mapToDTO(final Deal deal, final DealDTO dealDTO) {
        dealDTO.setId(deal.getId());
        dealDTO.setDescription(deal.getDescription());
        dealDTO.setPlace(deal.getPlace() == null ? null : deal.getPlace());
        return dealDTO;
    }

    private Deal mapToEntity(final DealDTO dealDTO, final Deal deal) {
        deal.setDescription(dealDTO.getDescription());
        final Place place = dealDTO.getPlace() == null ? null : placeRepository.findById(dealDTO.getPlace().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "place not found"));
        deal.setPlace(place);
        return deal;
    }

}
