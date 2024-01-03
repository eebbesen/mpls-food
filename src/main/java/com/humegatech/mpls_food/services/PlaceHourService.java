package com.humegatech.mpls_food.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.PlaceHour;
import com.humegatech.mpls_food.models.PlaceHourDTO;
import com.humegatech.mpls_food.repositories.PlaceRepository;

@Service
public class PlaceHourService {
  private final PlaceRepository placeRepository;

    public PlaceHourService(final PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

  PlaceHour mapToEntity(final PlaceHourDTO placeHourDTO, final PlaceHour placeHour) {
    final Place place = placeRepository.findById(placeHourDTO.getPlace())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("place not found: %d", placeHourDTO.getPlace())));

    placeHour.setId(placeHourDTO.getId());
    placeHour.setPlace(place);
    placeHour.setOpenTime(placeHourDTO.getOpenTime());
    placeHour.setCloseTime(placeHourDTO.getCloseTime());
    placeHour.setDayOfWeek(placeHourDTO.getDayOfWeek());

    return placeHour;
  }

  PlaceHourDTO mapToDTO(final PlaceHour placeHour, final PlaceHourDTO placeHourDTO) {
    placeHourDTO.setId(placeHour.getId());
    placeHourDTO.setPlace(placeHour.getPlace().getId());
    placeHourDTO.setOpenTime(placeHour.getOpenTime());
    placeHourDTO.setCloseTime(placeHour.getCloseTime());
    placeHourDTO.setDayOfWeek(placeHour.getDayOfWeek());

    return placeHourDTO;
  }
}
