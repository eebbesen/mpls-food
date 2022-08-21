package com.humegatech.mpls_food.service;

import com.humegatech.mpls_food.domain.DealDay;
import com.humegatech.mpls_food.model.DealDayDTO;
import com.humegatech.mpls_food.repos.DealDayRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealDayService {

    private final DealDayRepository dealDayRepository;


    public DealDayService(DealDayRepository dealDayRepository) {
        this.dealDayRepository = dealDayRepository;
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
}
