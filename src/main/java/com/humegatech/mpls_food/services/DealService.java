package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.repositories.DayRepository;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DealService {
    private final DayRepository dayRepository;
    private final DealRepository dealRepository;
    private final PlaceRepository placeRepository;

    public DealService(final DealRepository dealRepository, final PlaceRepository placeRepository, final DayRepository dayRepository) {
        this.dealRepository = dealRepository;
        this.placeRepository = placeRepository;
        this.dayRepository = dayRepository;
    }

    private static Day hasDay(final Deal deal, final DayOfWeek dayOfWeek) {
        for (Day day : deal.getDays()) {
            if (day.getDayOfWeek() == dayOfWeek) {
                return day;
            }
        }

        return null;
    }

    private static void addDay(final Deal deal, final DayOfWeek day) {
        if (null == hasDay(deal, day)) {
            deal.getDays().add(Day.builder()
                    .deal(deal)
                    .dayOfWeek(day)
                    .lastUpdated(OffsetDateTime.now())
                    .dateCreated(OffsetDateTime.now())
                    .build());
        }
    }

    private static void removeDay(final Deal deal, final DayOfWeek day) {
        final Day dealDay = hasDay(deal, day);
        if (dealDay != null) {
            deal.getDays().remove(dealDay);
        }
    }

    private static String capitalizeFirst(final String string) {
        if (null == string) {
            return null;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
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
        final Place place = dealDTO.getPlace() == null ? null : placeRepository.findById(dealDTO.getPlace().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "place not found"));

        deal.setDescription(dealDTO.getDescription());
        deal.setId(dealDTO.getId());
        deal.setPlace(place);
        handleDaysToEntity(dealDTO, deal);

        return deal;
    }

    // use reflection to reduce repetition
    private void handleDaysToEntity(final DealDTO dealDTO, final Deal deal) {
        Arrays.stream(DayOfWeek.values()).forEach(d -> {
            String methodName = "is" + capitalizeFirst(d.name());
            try {
                Method isDay = DealDTO.class.getDeclaredMethod(methodName);
                Boolean result = (Boolean) isDay.invoke(dealDTO);

                if (result) {
                    addDay(deal, d);
                } else {
                    removeDay(deal, d);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
