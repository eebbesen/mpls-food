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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public List<DayDTO> findByDayOfWeek(final DayOfWeek dayOfWeek) {
        return dayRepository.findByDayOfWeek(dayOfWeek)
                .stream()
                .map(day -> mapToDTO(day, new DayDTO()))
                .sorted(Comparator.comparing((DayDTO c) -> c.getPlaceName()))
                .collect(Collectors.toList());
    }

    private List<DayDTO> sortDays(final List<Day> days) {
        final Map<DayOfWeek, Integer> order = MplsFoodUtils.getSortOrderFromDay(LocalDateTime.now().getDayOfWeek());
        return days.stream()
                .map(day -> mapToDTO(day, new DayDTO()))
                .sorted(Comparator.comparing((DayDTO d) -> order.get(d.getDayOfWeek()))
                        .thenComparing((DayDTO d) -> null == d.getStartTime() ? "zzz" : d.getStartTime())
                        .thenComparing((DayDTO d) -> null == d.getEndTime() ? "zzz" : d.getEndTime())
                        .thenComparing((DayDTO d) -> d.getPlaceName()))
                .collect(Collectors.toList());
    }

    public List<DayDTO> findAll() {
        return sortDays(dayRepository.findAll());
    }

    public List<DayDTO> findAllActive() {
        return sortDays(dayRepository.findAllActive());
    }

    private DayDTO mapToDTO(final Day day, final DayDTO dayDTO) {
        dayDTO.setId(day.getId());
        dayDTO.setDeal(day.getDeal().getId());
        dayDTO.setDayOfWeek(day.getDayOfWeek());
        dayDTO.setDate(day.getDate());
        dayDTO.setDealDescription(day.getDeal().getDescription());
        dayDTO.setPlaceName(day.getDeal().getPlace().getName());
        dayDTO.setDayOfWeekDisplay(MplsFoodUtils.capitalizeFirst(day.getDayOfWeek().name()));
        dayDTO.setDish(day.getDeal().getDish());
        dayDTO.setPriceRange(MplsFoodUtils.getRange(day.getDeal().getMinPrice(),
                day.getDeal().getMaxPrice(), "$"));
        dayDTO.setDiscountRange(MplsFoodUtils.getRange(day.getDeal().getMinDiscount(),
                day.getDeal().getMaxDiscount(), "$"));
        dayDTO.setDiscountPercentRange(MplsFoodUtils.getRange(day.getDeal().getMinDiscountPercent(),
                day.getDeal().getMaxDiscountPercent(), "%"));
        dayDTO.setMinPrice(day.getDeal().getMinPrice());
        dayDTO.setMinDiscount(day.getDeal().getMinDiscount());
        dayDTO.setMinDiscountPercent(day.getDeal().getMinDiscountPercent());
        dayDTO.setStartTime(day.getDeal().getStartTime());
        dayDTO.setEndTime(day.getDeal().getEndTime());

        dayDTO.setVerified(day.getDeal().isVerified());

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
                .map(day -> mapToDTO(day, new DayDTO()))
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
