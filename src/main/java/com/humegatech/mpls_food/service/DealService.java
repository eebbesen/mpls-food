package com.humegatech.mpls_food.service;

import com.humegatech.mpls_food.domain.Deal;
import com.humegatech.mpls_food.domain.DealDay;
import com.humegatech.mpls_food.domain.Place;
import com.humegatech.mpls_food.model.DealDTO;
import com.humegatech.mpls_food.repos.DealDayRepository;
import com.humegatech.mpls_food.repos.DealRepository;
import com.humegatech.mpls_food.repos.PlaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DealService {
    private final DealDayRepository dealDayRepository;
    private final DealRepository dealRepository;
    private final PlaceRepository placeRepository;

    public DealService(final DealRepository dealRepository, final PlaceRepository placeRepository, final DealDayRepository dealDayRepository) {
        this.dealRepository = dealRepository;
        this.placeRepository = placeRepository;
        this.dealDayRepository = dealDayRepository;
    }

    protected static DealDay hasDay(final Deal deal, final DayOfWeek day) {
        for (DealDay dealDay : deal.getDealDays()) {
            if (dealDay.getDayOfWeek() == day) {
                return dealDay;
            }
        }

        return null;
    }

    protected static void addDay(final Deal deal, final DayOfWeek day) {
        if (null == hasDay(deal, day)) {
            deal.getDealDays().add(DealDay.builder()
                    .deal(deal)
                    .dayOfWeek(day)
                    .lastUpdated(OffsetDateTime.now())
                    .dateCreated(OffsetDateTime.now())
                    .build());
        }
    }

    protected static void removeDay(final Deal deal, final DayOfWeek day) {
        final DealDay dealDay = hasDay(deal, day);
        if (dealDay != null) {
            deal.getDealDays().remove(dealDay);
        }
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
        dealDTO.setSunday(null != hasDay(deal, DayOfWeek.SUNDAY));
        dealDTO.setMonday(null != hasDay(deal, DayOfWeek.MONDAY));
        dealDTO.setTuesday(null != hasDay(deal, DayOfWeek.TUESDAY));
        dealDTO.setWednesday(null != hasDay(deal, DayOfWeek.WEDNESDAY));
        dealDTO.setThursday(null != hasDay(deal, DayOfWeek.THURSDAY));
        dealDTO.setFriday(null != hasDay(deal, DayOfWeek.FRIDAY));
        dealDTO.setSaturday(null != hasDay(deal, DayOfWeek.SATURDAY));

        return dealDTO;
    }

    private Deal mapToEntity(final DealDTO dealDTO, final Deal deal) {
        deal.setDescription(dealDTO.getDescription());
        final Place place = dealDTO.getPlace() == null ? null : placeRepository.findById(dealDTO.getPlace().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "place not found"));
        deal.setPlace(place);
        if (dealDTO.isSunday()) {
            addDay(deal, DayOfWeek.SUNDAY);
        } else {
            removeDay(deal, DayOfWeek.SUNDAY);
        }

        if (dealDTO.isMonday()) {
            addDay(deal, DayOfWeek.MONDAY);
        } else {
            removeDay(deal, DayOfWeek.MONDAY);
        }

        if (dealDTO.isTuesday()) {
            addDay(deal, DayOfWeek.TUESDAY);
        } else {
            removeDay(deal, DayOfWeek.TUESDAY);
        }

        if (dealDTO.isWednesday()) {
            addDay(deal, DayOfWeek.WEDNESDAY);
        } else {
            removeDay(deal, DayOfWeek.WEDNESDAY);
        }

        if (dealDTO.isThursday()) {
            addDay(deal, DayOfWeek.THURSDAY);
        } else {
            removeDay(deal, DayOfWeek.THURSDAY);
        }

        if (dealDTO.isFriday()) {
            addDay(deal, DayOfWeek.FRIDAY);
        } else {
            removeDay(deal, DayOfWeek.FRIDAY);
        }

        if (dealDTO.isSaturday()) {
            addDay(deal, DayOfWeek.SATURDAY);
        } else {
            removeDay(deal, DayOfWeek.SATURDAY);
        }

        return deal;
    }

}
