package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DealServiceTest {
    @MockBean
    PlaceRepository placeRepository;
    @Autowired
    private DealService service;

    @MockBean
    private DealRepository dealRepository;
    private Deal dealMonTues;
    private DealDTO dealMonTuesDTO;

    @BeforeEach
    void setUp() {
        dealMonTues = TestObjects.dealMonTues();

        dealMonTuesDTO = new DealDTO();
        dealMonTuesDTO.setMonday(true);
        dealMonTuesDTO.setTuesday(true);
        dealMonTuesDTO.setDescription(dealMonTues.getDescription());
        dealMonTuesDTO.setPlace(dealMonTues.getPlace().getId());
        dealMonTuesDTO.setPlaceName(dealMonTues.getPlace().getName());
        dealMonTuesDTO.setDish(dealMonTues.getDish());
        dealMonTuesDTO.setCuisine(dealMonTues.getCuisine());
        dealMonTuesDTO.setId(dealMonTues.getId());
        dealMonTuesDTO.setMinPrice(dealMonTues.getMinPrice());
        dealMonTuesDTO.setMaxPrice(dealMonTues.getMaxPrice());
        dealMonTuesDTO.setMinDiscount(dealMonTues.getMinDiscount());
        dealMonTuesDTO.setMaxDiscount(dealMonTues.getMaxDiscount());
        dealMonTuesDTO.setMinDiscountPercent(dealMonTues.getMinDiscountPercent());
        dealMonTuesDTO.setMaxDiscountPercent(dealMonTues.getMaxDiscountPercent());
        dealMonTuesDTO.setVerified(dealMonTues.isVerified());
        dealMonTuesDTO.setTaxIncluded(dealMonTues.isTaxIncluded());
    }

    @Test
    void testGetRangeMinNullMaxNull() {
        final String rangeString = ReflectionTestUtils.invokeMethod(service.getClass(), "getRange", null, null, "$");
        assertEquals(null, rangeString);
    }

    @Test
    void testGetRangeMinNullMaxPopulated() {
        final String rangeString = ReflectionTestUtils.invokeMethod(service.getClass(), "getRange", null, 1.22, "$");
        assertEquals("$1.22", rangeString);
    }

    @Test
    void testGetRangeMinPopulatedMaxNull() {
        final String rangeString = ReflectionTestUtils.invokeMethod(service.getClass(), "getRange", 1.22, null, "$");
        assertEquals("$1.22", rangeString);
    }

    @Test
    void testGetRangeSameWithDollar() {
        final String rangeString = ReflectionTestUtils.invokeMethod(service.getClass(), "getRange", 1.22, 1.22, "$");
        assertEquals("$1.22", rangeString);
    }

    @Test
    void testGetRangeSameWithPercent() {
        final String rangeString = ReflectionTestUtils.invokeMethod(service.getClass(), "getRange", 42.0, 42.0, "%");
        assertEquals("42%", rangeString);
    }

    @Test
    void testGetRangeDifferent() {
        final String rangeString = ReflectionTestUtils.invokeMethod(service.getClass(), "getRange", 42.0, 55.0, "%");
        assertEquals("42% - 55%", rangeString);
    }

    @Test
    void testDecorateValuePercent() {
        final String decorated = ReflectionTestUtils.invokeMethod(service.getClass(), "decorateValue", 42.0, "%");
        assertEquals("42%", decorated);
    }

    @Test
    void testDecorateValueDollarSign() {
        final String decorated = ReflectionTestUtils.invokeMethod(service.getClass(), "decorateValue", 42.0, "$");
        assertEquals("$42.00", decorated);
    }

    @Test
    void testConfirmOrAddDayDayExists() {
        ReflectionTestUtils.invokeMethod(service.getClass(), "addDay", dealMonTues, DayOfWeek.MONDAY);
        assertEquals(2, dealMonTues.getDays().size());
    }

    @Test
    void testConfirmOrAddDayDayDoesNotExist() {
        ReflectionTestUtils.invokeMethod(service.getClass(), "addDay", dealMonTues, DayOfWeek.WEDNESDAY);
        assertEquals(3, dealMonTues.getDays().size());
    }

    @Test
    void testFindByPlaceId() {
        final Place place = TestObjects.ginellis();
        final Deal deal1 = Deal.builder()
                .place(place)
                .id(1l)
                .description("BB").build();
        final Day day1_1 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day1_2 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal1.setDays(Stream.of(day1_1, day1_2).collect(Collectors.toSet()));

        final Deal deal2 = Deal.builder()
                .place(place)
                .id(2l)
                .description("AA").build();
        final Day day2_1 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day2_2 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal2.setDays(Stream.of(day2_1, day2_2).collect(Collectors.toSet()));

        final Deal deal3 = Deal.builder()
                .place(place)
                .id(3l)
                .description("Z").build();
        final Day day3_1 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day3_2 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal3.setDays(Stream.of(day3_1, day3_2).collect(Collectors.toSet()));

        final Deal deal4 = Deal.builder()
                .place(place)
                .id(4l)
                .description("C").build();
        final Day day4_1 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.MONDAY).build();
        final Day day4_2 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal4.setDays(Stream.of(day4_1, day4_2).collect(Collectors.toSet()));

        final List<Deal> deals = Stream.of(deal1, deal2, deal3, deal4).collect(Collectors.toList());
        when(dealRepository.findByPlaceId(place.getId())).thenReturn(deals);

        final LocalDateTime wed = LocalDateTime.of(2022, Month.AUGUST, 31, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(wed);

            final List<DealDTO> dtos = service.findByPlaceId(place.getId());

            assertEquals(deal2.getId(), dtos.get(0).getId());
            assertEquals(deal1.getId(), dtos.get(1).getId());
            assertEquals(deal3.getId(), dtos.get(2).getId());
            assertEquals(deal4.getId(), dtos.get(3).getId());
        }

        final LocalDateTime sat = LocalDateTime.of(2022, Month.SEPTEMBER, 3, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(sat);

            final List<DealDTO> dtos = service.findByPlaceId(place.getId());

            assertEquals(deal4.getId(), dtos.get(0).getId());
            assertEquals(deal2.getId(), dtos.get(1).getId());
            assertEquals(deal1.getId(), dtos.get(2).getId());
            assertEquals(deal3.getId(), dtos.get(3).getId());
        }
    }

    @Test
    void testFindAll() {
        final Deal tt = TestObjects.tacoTuesday();
        final Deal mt = TestObjects.dealMonTues();
        final Deal ft = TestObjects.fridayTwofer();
        final List<Deal> deals = Stream.of(tt, ft, mt)
                .collect(Collectors.toList());

        when(dealRepository.findAll()).thenReturn(deals);

        final LocalDateTime mon = LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(mon);
            final List<DealDTO> dealDTOs = service.findAll();

            assertEquals(3, dealDTOs.size());
            assertEquals(mt.getPlace().getId(), dealDTOs.get(0).getPlace());
            assertEquals("MT-----", dealDTOs.get(0).getDaysDisplay());
            assertEquals(tt.getPlace().getId(), dealDTOs.get(1).getPlace());
            assertEquals("-T-----", dealDTOs.get(1).getDaysDisplay());
            assertEquals(ft.getPlace().getId(), dealDTOs.get(2).getPlace());
            assertEquals("----F--", dealDTOs.get(2).getDaysDisplay());
        }

        final LocalDateTime fri = LocalDateTime.of(2022, Month.SEPTEMBER, 9, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(fri);
            final List<DealDTO> dealDTOs = service.findAll();

            assertEquals(3, dealDTOs.size());
            assertEquals(ft.getPlace().getId(), dealDTOs.get(0).getPlace());
            assertEquals("F------", dealDTOs.get(0).getDaysDisplay());
            assertEquals(mt.getPlace().getId(), dealDTOs.get(1).getPlace());
            assertEquals("---MT--", dealDTOs.get(1).getDaysDisplay());
            assertEquals(tt.getPlace().getId(), dealDTOs.get(2).getPlace());
            assertEquals("----T--", dealDTOs.get(2).getDaysDisplay());
        }
    }

    @Test
    void testUpdateRemoveDays() {
        final Day deletedDay = dealMonTues.getDays().stream()
                .filter(day -> day.getDayOfWeek() == DayOfWeek.MONDAY)
                .findFirst().orElse(null);
        dealMonTuesDTO.setMonday(false);

        when(placeRepository.findById(dealMonTues.getPlace().getId())).thenReturn(Optional.of(dealMonTues.getPlace()));
        when(dealRepository.findById(dealMonTues.getId())).thenReturn(Optional.of(dealMonTues));

        service.update(dealMonTues.getId(), dealMonTuesDTO);

        verify(dealRepository, times(1)).findById(dealMonTues.getId());
        assertEquals(1, dealMonTues.getDays().size());
        assertFalse(dealMonTues.getDays().contains(deletedDay));
    }

    @Test
    void testMapToEntity() {
        when(placeRepository.findById(dealMonTues.getPlace().getId())).thenReturn(Optional.of(dealMonTues.getPlace()));

        Deal deal = new Deal();
        ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealMonTuesDTO, deal);

        assertEquals(dealMonTues.getPlace(), deal.getPlace());
        assertEquals(dealMonTues.getDescription(), deal.getDescription());
        assertEquals(dealMonTues.getId(), deal.getId());
        assertEquals(dealMonTues.getDish(), deal.getDish());
        assertEquals(dealMonTues.getCuisine(), deal.getCuisine());
        assertEquals(dealMonTues.getMaxPrice(), deal.getMaxPrice());
        assertEquals(dealMonTues.getMinPrice(), deal.getMinPrice());
        assertEquals(dealMonTues.getMaxDiscount(), deal.getMaxDiscount());
        assertEquals(dealMonTues.getMinDiscount(), deal.getMinDiscount());
        assertEquals(dealMonTues.getMaxDiscountPercent(), deal.getMaxDiscountPercent());
        assertEquals(dealMonTues.getMinDiscountPercent(), deal.getMinDiscountPercent());
        assertEquals(dealMonTues.isVerified(), deal.isVerified());
        assertEquals(dealMonTues.isTaxIncluded(), deal.isTaxIncluded());

        // confirm days
        Set<DayOfWeek> days = deal.getDays().stream().map(Day::getDayOfWeek)
                .collect(Collectors.toSet());
        assertEquals(2, days.size());
        assertTrue(days.contains(DayOfWeek.MONDAY));
        assertTrue(days.contains(DayOfWeek.TUESDAY));
    }

    @Test
    void testApplyDaysToEntity() {
        Deal deal = new Deal();
        ReflectionTestUtils.invokeMethod(service, "applyDaysToEntity", dealMonTuesDTO, deal);

        // confirm days
        Set<DayOfWeek> days = deal.getDays().stream().map(Day::getDayOfWeek)
                .collect(Collectors.toSet());
        assertEquals(2, days.size());
        assertTrue(days.contains(DayOfWeek.MONDAY));
        assertTrue(days.contains(DayOfWeek.TUESDAY));
    }

    @Test
    void testApplyDaysToEntityRemoveDays() {
        Deal deal = new Deal();
        deal.getDays().add(Day.builder().deal(dealMonTues).dayOfWeek(DayOfWeek.SUNDAY).build());
        ReflectionTestUtils.invokeMethod(service, "applyDaysToEntity", dealMonTuesDTO, deal);

        // confirm days
        Set<DayOfWeek> days = deal.getDays().stream().map(Day::getDayOfWeek)
                .collect(Collectors.toSet());
        assertEquals(2, days.size());
        assertTrue(days.contains(DayOfWeek.MONDAY));
        assertTrue(days.contains(DayOfWeek.TUESDAY));
    }

    @Test
    void testApplyDaysToDTO() {
        DealDTO dealDTO = new DealDTO();
        ReflectionTestUtils.invokeMethod(service, "applyDaysToDTO", dealMonTues, dealDTO);

        assertFalse(dealDTO.isSunday());
        assertTrue(dealDTO.isMonday());
        assertTrue(dealDTO.isTuesday());
        assertFalse(dealDTO.isWednesday());
        assertFalse(dealDTO.isThursday());
        assertFalse(dealDTO.isFriday());
        assertFalse(dealDTO.isSaturday());
    }

    @Test
    void testMapToDTO() {
        final Upload upload = TestObjects.upload(dealMonTues);

        final LocalDateTime mon = LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(mon);
            DealDTO dto = (DealDTO) ReflectionTestUtils.invokeMethod(service, "mapToDTO", dealMonTues, new DealDTO());

            assertEquals(dealMonTues.getId(), dto.getId());
            assertTrue(dto.isMonday());
            assertTrue(dto.isTuesday());
            assertFalse(dto.isWednesday());
            assertFalse(dto.isThursday());
            assertFalse(dto.isFriday());
            assertFalse(dto.isSaturday());
            assertFalse(dto.isSunday());
            assertEquals(dealMonTues.getDescription(), dto.getDescription());
            assertEquals(dealMonTues.getPlace().getId(), dto.getPlace());
            assertEquals(dealMonTues.getPlace().getName(), dto.getPlaceName());
            assertEquals(dealMonTues.getMinPrice(), dto.getMinPrice());
            assertEquals(dealMonTues.getMaxPrice(), dto.getMaxPrice());
            assertEquals(dealMonTues.getMinDiscount(), dto.getMinDiscount());
            assertEquals(dealMonTues.getMaxDiscount(), dto.getMaxDiscount());
            assertEquals(dealMonTues.isTaxIncluded(), dto.isTaxIncluded());
            assertEquals(dealMonTues.isVerified(), dto.isVerified());
            assertEquals(dealMonTues.isTaxIncluded(), dto.isTaxIncluded());
            assertEquals(String.format("$%.2f", dealMonTues.getMinPrice(), dealMonTues.getMaxPrice()), dto.getPriceRange());
            assertEquals(String.format("$%.2f - $%.2f", dealMonTues.getMinDiscount(), dealMonTues.getMaxDiscount()), dto.getDiscountRange());
            assertEquals(String.format("%.0f%% - %.0f%%", dealMonTues.getMinDiscountPercent(), dealMonTues.getMaxDiscountPercent()), dto.getDiscountPercentRange());
            assertEquals("MT-----", dto.getDaysDisplay());
            assertEquals(dealMonTues.getDish(), dto.getDish());
            assertEquals(dealMonTues.getCuisine(), dto.getCuisine());
            assertEquals(1, dto.getUploads().size());
            assertEquals(upload.getImage(), dto.getUploads().get(0).getImage());
            assertEquals(upload.getDeal().getId(), dto.getUploads().get(0).getDealId());
            assertEquals(upload.getId(), dto.getUploads().get(0).getId());
            assertEquals(upload.isVerified(), dto.getUploads().get(0).isVerified());
        }
    }

    @Test
    void testApplyUploadsToDTO() {
        final Upload upload = TestObjects.upload(dealMonTues);
        final Upload upload2 = Upload.builder()
                .deal(dealMonTues)
                .id(2l)
                .verified(true)
                .image(new byte['a']).build();

        List<Upload> uploads = Stream.of(upload, upload2).collect(Collectors.toList());
        dealMonTues.getUploads().addAll(uploads);

        final DealDTO dto = new DealDTO();
        ReflectionTestUtils.invokeMethod(service, "applyUploadsToDTO", dealMonTues, dto);

        assertEquals(2, dto.getUploads().size());
    }

}
