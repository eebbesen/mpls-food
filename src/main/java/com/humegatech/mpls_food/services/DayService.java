package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.models.DayDTO;
import com.humegatech.mpls_food.repositories.DayRepository;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayService {

    private final DayRepository dayRepository;
    private final DealRepository dealRepository;

    public DayService(DayRepository dayRepository, DealRepository dealRepository) {
        this.dayRepository = dayRepository;
        this.dealRepository = dealRepository;
    }

    public Long create(final DayDTO dayDTO) {
        final Day day = new Day();
        mapToEntity(dayDTO, day);
        return dayRepository.save(day).getId();
    }


    public List<DayDTO> findAll() {
        return dayRepository.findAll()
                .stream()
                .map(day -> mapToDTO(day, new DayDTO()))
                .sorted(Comparator.comparing((DayDTO c) -> c.getPlaceName())
                        .thenComparing((DayDTO d) -> d.getDayOfWeek()))
                .collect(Collectors.toList());
    }

    private DayDTO mapToDTO(final Day day, final DayDTO dayDTO) {
        dayDTO.setId(day.getId());
        dayDTO.setDeal(day.getDeal().getId());
        dayDTO.setDayOfWeek(day.getDayOfWeek());
        dayDTO.setDate(day.getDate());
        dayDTO.setDealDescription(day.getDeal().getDescription());
        dayDTO.setPlaceName(day.getDeal().getPlace().getName());
        dayDTO.setDayOfWeekDisplay(MplsFoodUtils.capitalizeFirst(day.getDayOfWeek().name()));

        return dayDTO;
    }

    private Day mapToEntity(final DayDTO dayDTO, final Day day) {
        final Deal deal = dayDTO.getDeal() == null ? null : dealRepository.findById(dayDTO.getDeal())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "deal not found"));

        return Day.builder()
                .deal(deal)
                .dayOfWeek(dayDTO.getDayOfWeek())
                .id(dayDTO.getId())
                .date(dayDTO.getDate()).build();
    }

    public DayDTO get(final Long id) {
        return dayRepository.findById(id)
                .map(deal -> mapToDTO(deal, new DayDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void update(final Long id, final DayDTO dayDTO) {
        final Day day = dayRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(dayDTO, day);
        dayRepository.save(day);
    }

    public void delete(final Long id) {
        dayRepository.deleteById(id);
    }

}
