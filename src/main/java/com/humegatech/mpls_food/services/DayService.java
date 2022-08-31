package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.models.DealDayDTO;
import com.humegatech.mpls_food.repositories.DayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayService {

    private final DayRepository dayRepository;

    public DayService(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    public Long create(final DealDayDTO dealDayDTO) {
        final Day day = new Day();
        mapToEntity(dealDayDTO, day);
        return dayRepository.save(day).getId();
    }


    public List<DealDayDTO> findAll() {
        return dayRepository.findAll()
                .stream()
                .map(day -> mapToDTO(day, new DealDayDTO()))
                .collect(Collectors.toList());
    }

    private DealDayDTO mapToDTO(final Day day, final DealDayDTO dealDayDTO) {
        dealDayDTO.setId(day.getId());
        dealDayDTO.setDeal(day.getDeal());
        dealDayDTO.setDayOfWeek(day.getDayOfWeek());
        dealDayDTO.setDate(day.getDate());

        return dealDayDTO;
    }

    private Day mapToEntity(final DealDayDTO dealDayDTO, final Day day) {
//        day.setDeal(dealDayDTO.getDealId());
        day.setDayOfWeek(day.getDayOfWeek());
        day.setDate(day.getDate());
        return day;
    }

    public DealDayDTO get(final Long id) {
        return dayRepository.findById(id)
                .map(deal -> mapToDTO(deal, new DealDayDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void update(final Long id, final DealDayDTO dealDayDTO) {
        final Day day = dayRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(dealDayDTO, day);
        dayRepository.save(day);
    }

    public void delete(final Long id) {
        dayRepository.deleteById(id);
    }

}
