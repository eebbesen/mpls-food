package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


@Service
@Slf4j
public class DealService {
    private final DealRepository dealRepository;
    private final PlaceRepository placeRepository;
    private final Logger logger = LoggerFactory.getLogger(DealService.class);

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

    @PreAuthorize("hasRole('USER')")
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
    // I prefer to display '-' so I'm taking the hit on the replacement
    @Cacheable("allDeals")
    public List<DealDTO> findAll() {
        return dealRepository.findAll()
                .stream()
                .filter(deal -> null == deal.getStartDate() || LocalDate.now().isAfter(deal.getStartDate()) ||
                        LocalDate.now().isEqual(deal.getStartDate()))
                .filter(deal -> null == deal.getEndDate() || LocalDate.now().isBefore(deal.getEndDate()) ||
                        LocalDate.now().isEqual(deal.getEndDate()))
                .map(deal -> mapToDTO(deal, new DealDTO()))
                .sorted(Comparator.comparing((DealDTO c) -> c.getDaysDisplay().replace("-", "~"))
                        .thenComparing(DealDTO::getPlaceName)
                        .thenComparing(DealDTO::getDescription))
                .toList();
    }

    @Cacheable("singleDealByPlaceId")
    public List<DealDTO> findByPlaceId(final Long placeId) {
        return dealRepository.findByPlaceId(placeId)
                .stream()
                .map(deal -> mapToDTO(deal, new DealDTO()))
                .sorted(Comparator.comparing((DealDTO c) -> c.getDaysDisplay().replace("-", "~"))
                        .thenComparing(DealDTO::getDescription))
                .toList();
    }

    @Cacheable("singleDeal")
    public DealDTO get(final Long id) {
        return dealRepository.findById(id)
                .map(deal -> mapToDTO(deal, new DealDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @PreAuthorize("hasRole('USER')")
    public Long create(final DealDTO dealDTO) {
        final Deal deal = new Deal();
        mapToEntity(dealDTO, deal);
        return dealRepository.save(deal).getId();
    }


    @PreAuthorize("hasRole('USER')")
    public void update(final Long id, final DealDTO dealDTO) {
        final Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(dealDTO, deal);
        dealRepository.save(deal);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void delete(final Long id) {
        dealRepository.deleteById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void copy(final Long dealId, List<Long> placeIds) {
        final Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "deal not found"));
        final List<Place> places = placeRepository.findByIdIn(placeIds);
        if (places.size() < placeIds.size()) {
            List<Long> notFound = new ArrayList<>(placeIds);
            notFound.removeAll(places.stream().map(Place::getId).toList());
            final String message = "one or more places not found:%n %s".formatted(notFound);
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        final List<Deal> newDeals = new ArrayList<>();
        places.forEach(p -> {
            final DealDTO dealDTO = new DealDTO();
            mapToDTO(deal, dealDTO);
            dealDTO.setId(null);
            dealDTO.setPlace(p.getId());
            newDeals.add(mapToEntity(dealDTO, new Deal()));
        });

        log.info("Deals created: %s".formatted(newDeals.stream().map(Deal::getId)));
        dealRepository.saveAll(newDeals);
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
        dealDTO.setDealType(deal.getDealType());
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

    protected Deal mapToEntity(final DealDTO dealDTO, final Deal deal) {
        final Place place = placeRepository.findById(dealDTO.getPlace())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("place not found: %d", dealDTO.getPlace())));

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
        deal.setDealType(dealDTO.getDealType());
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
                logger.error(String.format("Error invoking method %s%n%s", methodName, e.getMessage()));
            }
        });
    }
}