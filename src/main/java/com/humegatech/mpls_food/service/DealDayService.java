package com.humegatech.mpls_food.service;

import com.humegatech.mpls_food.domain.DealDay;
import com.humegatech.mpls_food.model.DealDayDTO;
import com.humegatech.mpls_food.repos.DealDayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealDayService {

    private final DealDayRepository dealDayRepository;

    public DealDayService(DealDayRepository dealDayRepository) {
        this.dealDayRepository = dealDayRepository;
    }

    public Long create(final DealDayDTO dealDayDTO) {
        final DealDay dealDay = new DealDay();
        mapToEntity(dealDayDTO, dealDay);
        return dealDayRepository.save(dealDay).getId();
    }


    public List<DealDayDTO> findAll() {
        return dealDayRepository.findAll()
                .stream()
                .map(dealDay -> mapToDTO(dealDay, new DealDayDTO()))
                .collect(Collectors.toList());
    }

    private DealDayDTO mapToDTO(final DealDay dealDay, final DealDayDTO dealDayDTO) {
        dealDayDTO.setId(dealDay.getId());
        dealDayDTO.setDeal(dealDay.getDeal());

        return dealDayDTO;
    }

    private DealDay mapToEntity(final DealDayDTO dealDayDTO, final DealDay dealDay) {
        dealDay.setDeal(dealDayDTO.getDeal());
        dealDay.setDayOfWeek(dealDay.getDayOfWeek());
        dealDay.setDate(dealDay.getDate());
        return dealDay;
    }

    public DealDayDTO get(final Long id) {
        return dealDayRepository.findById(id)
                .map(deal -> mapToDTO(deal, new DealDayDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void update(final Long id, final DealDayDTO dealDayDTO) {
        final DealDay dealDay = dealDayRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(dealDayDTO, dealDay);
        dealDayRepository.save(dealDay);
    }

    public void delete(final Long id) {
        dealDayRepository.deleteById(id);
    }

}
