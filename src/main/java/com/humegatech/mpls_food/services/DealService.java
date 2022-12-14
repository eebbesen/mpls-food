package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Comparator;
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

    private static void removeDay(final Deal deal, final DayOfWeek day) {
        final Day dealDay = deal.hasDay(day);
        if (null != dealDay) {
            deal.getDays().remove(dealDay);
        }
    }

    private static void addDay(final Deal deal, final DayOfWeek day) {
        if (null == deal.hasDay(day)) {
            deal.getDays().add(Day.builder()
                    .deal(deal)
                    .dayOfWeek(day)
                    .lastUpdated(OffsetDateTime.now())
                    .dateCreated(OffsetDateTime.now())
                    .build());
        }
    }

    // Sorts by place name, then by the earliest day of the weak the deal is active.
    // Replacing '-' with '~' for the sorting because '~' is the last character alphabetically
    // and '-' is before all letters.
    // I prefer to display '-' so I'm taking the hit on the replace
    public List<DealDTO> findAll() {
        return dealRepository.findAll()
                .stream()
                .filter(deal -> null == deal.getStartDate() || LocalDate.now().isAfter(deal.getStartDate()) ||
                        LocalDate.now().isEqual(deal.getStartDate()))
                .filter(deal -> null == deal.getEndDate() || LocalDate.now().isBefore(deal.getEndDate()) ||
                        LocalDate.now().isEqual(deal.getEndDate()))
                .map(deal -> mapToDTO(deal, new DealDTO()))
                .sorted(Comparator.comparing((DealDTO c) -> c.getDaysDisplay().replaceAll("-", "~"))
                        .thenComparing(DealDTO::getPlaceName)
                        .thenComparing(DealDTO::getDescription))
                .collect(Collectors.toList());
    }

    public List<DealDTO> findByPlaceId(final Long placeId) {
        return dealRepository.findByPlaceId(placeId)
                .stream()
                .map(deal -> mapToDTO(deal, new DealDTO()))
                .sorted(Comparator.comparing((DealDTO c) -> c.getDaysDisplay().replaceAll("-", "~"))
                        .thenComparing(DealDTO::getDescription))
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
        dealDTO.setPlace(deal.getPlace().getId());
        dealDTO.setPlaceName(deal.getPlace().getName());
        dealDTO.setDaysDisplay(MplsFoodUtils.condensedDays(deal.getDaysOfWeek()));
        dealDTO.setDish(deal.getDish());
        dealDTO.setMinPrice(deal.getMinPrice());
        dealDTO.setMaxPrice((deal.getMaxPrice()));
        dealDTO.setMinDiscount(deal.getMinDiscount());
        dealDTO.setMaxDiscount(deal.getMaxDiscount());
        dealDTO.setMinDiscountPercent(deal.getMinDiscountPercent());
        dealDTO.setMaxDiscountPercent(deal.getMaxDiscountPercent());
        dealDTO.setPriceRange(MplsFoodUtils.getRange(deal.getMinPrice(), deal.getMaxPrice(), "$"));
        dealDTO.setDiscountRange(MplsFoodUtils.getRange(deal.getMinDiscount(), deal.getMaxDiscount(), "$"));
        dealDTO.setDiscountPercentRange(MplsFoodUtils.getRange(deal.getMinDiscountPercent(),
                deal.getMaxDiscountPercent(), "%"));
        dealDTO.setTaxIncluded(deal.isTaxIncluded());
        dealDTO.setVerified(deal.isVerified());
        dealDTO.setStartTime(deal.getStartTime());
        dealDTO.setEndTime(deal.getEndTime());
        dealDTO.setStartDate(deal.getStartDate());
        dealDTO.setEndDate(deal.getEndDate());
        applyDaysToDTO(deal, dealDTO);
        applyUploadsToDTO(deal, dealDTO);

        return dealDTO;
    }

    // use reflection to reduce repetition
    private void applyDaysToDTO(final Deal deal, final DealDTO dealDTO) {
        Arrays.stream(DayOfWeek.values()).forEach(d -> {
            try {
                if (null != deal.hasDay(d)) {
                    final String methodName = "set" + MplsFoodUtils.capitalizeFirst(d.name());
                    final Method setDay = DealDTO.class.getDeclaredMethod(methodName, boolean.class);
                    setDay.invoke(dealDTO, true);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // todo refactor to use UploadService
    private void applyUploadsToDTO(final Deal deal, final DealDTO dealDTO) {
        deal.getUploads().forEach(u -> dealDTO.getUploads().add(UploadDTO.builder()
                .dealId(u.getDeal().getId())
                .verified(u.isVerified())
                .image(u.getImage()).build()));
    }

    private Deal mapToEntity(final DealDTO dealDTO, final Deal deal) {
        final Place place = placeRepository.findById(dealDTO.getPlace())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "place not found"));

        deal.setDescription(dealDTO.getDescription());
        deal.setId(dealDTO.getId());
        deal.setPlace(place);
        deal.setDish(dealDTO.getDish());
        deal.setMinPrice(dealDTO.getMinPrice());
        deal.setMaxPrice(dealDTO.getMaxPrice());
        deal.setMinDiscount(dealDTO.getMinDiscount());
        deal.setMaxDiscount(dealDTO.getMaxDiscount());
        deal.setMinDiscountPercent(dealDTO.getMinDiscountPercent());
        deal.setMaxDiscountPercent(dealDTO.getMaxDiscountPercent());
        deal.setVerified(dealDTO.isVerified());
        deal.setTaxIncluded(dealDTO.isTaxIncluded());
        deal.setStartTime(dealDTO.getStartTime());
        deal.setEndTime(dealDTO.getEndTime());
        deal.setStartDate(dealDTO.getStartDate());
        deal.setEndDate(dealDTO.getEndDate());
        applyDaysToEntity(dealDTO, deal);

        return deal;
    }

    // use reflection to reduce repetition
    private void applyDaysToEntity(final DealDTO dealDTO, final Deal deal) {
        Arrays.stream(DayOfWeek.values()).forEach(d -> {
            final String methodName = "is" + MplsFoodUtils.capitalizeFirst(d.name());
            try {
                final Method isDay = DealDTO.class.getDeclaredMethod(methodName);
                final Boolean result = (Boolean) isDay.invoke(dealDTO);

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
