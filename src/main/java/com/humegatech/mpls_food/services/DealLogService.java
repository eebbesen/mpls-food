package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealLogDTO;
import com.humegatech.mpls_food.repositories.DealLogRepository;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealLogService {
    private final DealLogRepository dealLogRepository;
    private final DealRepository dealRepository;
    private final PlaceRepository placeRepository;

    public DealLogService(final DealLogRepository dealLogRepository, final DealRepository dealRepository,
                          final PlaceRepository placeRepository) {
        this.dealLogRepository = dealLogRepository;
        this.dealRepository = dealRepository;
        this.placeRepository = placeRepository;
    }

    public Long create(final DealLogDTO dealLogDTO) {
        final DealLog dealLog = new DealLog();
        mapToEntity(dealLogDTO, dealLog);
        return dealLogRepository.save(dealLog).getId();
    }

    public void update(final Long id, final DealLogDTO dealLogDTO) {
        final DealLog dealLog = dealLogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(dealLogDTO, dealLog);
        dealLogRepository.save(dealLog);
    }

    public List<DealLogDTO> findAll() {
        return dealLogRepository.findAll()
                .stream()
                .map(dealLog -> mapToDTO(dealLog, new DealLogDTO()))
                .sorted(Comparator.comparing(DealLogDTO::getRedemptionDate,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(DealLogDTO::getDescription))
                .collect(Collectors.toList());
    }

    public void delete(final Long id) {
        dealLogRepository.deleteById(id);
    }

    public DealLogDTO get(final Long id) {
        return dealLogRepository.findById(id)
                .map(dealLog -> mapToDTO(dealLog, new DealLogDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private DealLogDTO mapToDTO(final DealLog dealLog, final DealLogDTO dealLogDTO) {
        if (null != dealLog.getDeal()) {
            dealLogDTO.setDeal(dealLog.getDeal().getId());
            dealLogDTO.setDealDescription(dealLog.getDeal().getDescription());
        }
        dealLogDTO.setPlace(dealLog.getPlace().getId());
        dealLogDTO.setPlaceName(dealLog.getPlace().getName());
        dealLogDTO.setDealType(dealLog.getDealType());
        dealLogDTO.setDescription(dealLog.getDescription());
        dealLogDTO.setRedeemed(dealLog.getRedeemed());
        dealLogDTO.setRedemptionDate(dealLog.getRedemptionDate());
        dealLogDTO.setId(dealLog.getId());

        return dealLogDTO;
    }

    private DealLog mapToEntity(final DealLogDTO dealLogDTO, final DealLog dealLog) {
        final Place place = dealLogDTO.getPlace() == null ? null : placeRepository.findById(dealLogDTO.getPlace())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "place not found"));
        if (null == place) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "place not found");
        }

        final Deal deal = dealLogDTO.getDeal() == null ? null : dealRepository.findById(dealLogDTO.getDeal())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "deal not found"));

        dealLog.setId(dealLogDTO.getId());
        dealLog.setDescription(dealLogDTO.getDescription());
        dealLog.setDeal(deal);
        dealLog.setPlace(place);
        dealLog.setDealType(dealLogDTO.getDealType());
        dealLog.setRedeemed(dealLogDTO.getRedeemed());
        dealLog.setRedemptionDate(dealLogDTO.getRedemptionDate());

        return dealLog;
    }
}
