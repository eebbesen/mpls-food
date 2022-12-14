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
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
                .id(1L)
                .description("BB").build();
        final Day day1_1 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day1_2 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal1.setDays(Stream.of(day1_1, day1_2).collect(Collectors.toSet()));

        final Deal deal2 = Deal.builder()
                .place(place)
                .id(2L)
                .description("AA").build();
        final Day day2_1 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day2_2 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal2.setDays(Stream.of(day2_1, day2_2).collect(Collectors.toSet()));

        final Deal deal3 = Deal.builder()
                .place(place)
                .id(3L)
                .description("Z").build();
        final Day day3_1 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day3_2 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal3.setDays(Stream.of(day3_1, day3_2).collect(Collectors.toSet()));

        final Deal deal4 = Deal.builder()
                .place(place)
                .id(4L)
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
    void testFindAllActiveOnly() {
        final Deal active = TestObjects.tacoTuesday();
        final Deal notStarted = TestObjects.dealMonTues();
        notStarted.setStartDate(LocalDate.now().plusDays(1));
        final Deal ended = TestObjects.fridayTwofer();
        ended.setEndDate(LocalDate.now().minusDays(1));

        when(dealRepository.findAll()).thenReturn(List.of(active, notStarted, ended));

        final List<DealDTO> dealDTOs = service.findAll();

        assertEquals(1, dealDTOs.size());
        assertEquals(active.getDescription(), dealDTOs.get(0).getDescription());
    }

    @Test
    void testFindAllActiveOnlySameDay() {
        final Deal active = TestObjects.tacoTuesday();
        final Deal startsToday = TestObjects.dealMonTues();
        startsToday.setStartDate(LocalDate.now());
        final Deal endsToday = TestObjects.fridayTwofer();
        endsToday.setEndDate(LocalDate.now());

        when(dealRepository.findAll()).thenReturn(List.of(active, startsToday, endsToday));

        final List<DealDTO> dealDTOs = service.findAll();

        assertEquals(3, dealDTOs.size());
    }

    @Test
    void testFindAll() {
        final Deal tt = TestObjects.tacoTuesday();
        final Deal mt = TestObjects.dealMonTues();
        final Deal ft = TestObjects.fridayTwofer();
        final Deal t = TestObjects.deal();
        final Deal t2 = TestObjects.deal();
        t2.setDescription("t2");
        final List<Deal> deals = Stream.of(tt, ft, mt, t, t2)
                .collect(Collectors.toList());

        when(dealRepository.findAll()).thenReturn(deals);

        final LocalDateTime mon = LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(mon);
            final List<DealDTO> dealDTOs = service.findAll();

            assertEquals(5, dealDTOs.size());
            assertEquals(mt.getPlace().getId(), dealDTOs.get(0).getPlace());
            assertEquals("MT-----", dealDTOs.get(0).getDaysDisplay());
            assertEquals(tt.getPlace().getId(), dealDTOs.get(1).getPlace());
            assertEquals("-T-----", dealDTOs.get(1).getDaysDisplay());
            assertEquals(t.getPlace().getId(), dealDTOs.get(2).getPlace());
            assertEquals("---t---", dealDTOs.get(2).getDaysDisplay());
            assertEquals(t2.getPlace().getId(), dealDTOs.get(3).getPlace());
            assertEquals("---t---", dealDTOs.get(3).getDaysDisplay());
            assertEquals(ft.getPlace().getId(), dealDTOs.get(4).getPlace());
            assertEquals("----F--", dealDTOs.get(4).getDaysDisplay());
        }

        final LocalDateTime fri = LocalDateTime.of(2022, Month.SEPTEMBER, 9, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(fri);
            final List<DealDTO> dealDTOs = service.findAll();

            assertEquals(5, dealDTOs.size());
            assertEquals(ft.getPlace().getId(), dealDTOs.get(0).getPlace());
            assertEquals("F------", dealDTOs.get(0).getDaysDisplay());
            assertEquals(mt.getPlace().getId(), dealDTOs.get(1).getPlace());
            assertEquals("---MT--", dealDTOs.get(1).getDaysDisplay());
            assertEquals(tt.getPlace().getId(), dealDTOs.get(2).getPlace());
            assertEquals("----T--", dealDTOs.get(2).getDaysDisplay());
            assertEquals(t.getPlace().getId(), dealDTOs.get(3).getPlace());
            assertEquals("------t", dealDTOs.get(3).getDaysDisplay());
            assertEquals(t2.getPlace().getId(), dealDTOs.get(4).getPlace());
            assertEquals("------t", dealDTOs.get(4).getDaysDisplay());
        }
    }

    @Test
    void testFindAllSorting() {
        final Deal dealNoStartDateNoEndDate = TestObjects.deal();
        dealNoStartDateNoEndDate.setDescription("dealNoStartDateNoEndDate");
        dealNoStartDateNoEndDate.setStartDate(null);
        dealNoStartDateNoEndDate.setEndDate(null);
        final Deal dealStartDateEndDate1 = TestObjects.deal();
        dealStartDateEndDate1.setDescription("dealStartDateEndDate1");
        dealStartDateEndDate1.setStartDate(LocalDate.now().minusDays(1));
        dealStartDateEndDate1.setEndDate(LocalDate.now().plusDays(1));
        final Deal dealStartDateEndDate2 = TestObjects.deal();
        dealStartDateEndDate2.setDescription("dealStartDateEndDate2");
        dealStartDateEndDate2.setStartDate(LocalDate.now());
        dealStartDateEndDate2.setEndDate(LocalDate.now());

        when(dealRepository.findAll()).thenReturn(List.of(dealStartDateEndDate1,
                dealStartDateEndDate2,
                dealNoStartDateNoEndDate));

        final List<DealDTO> dealDTOs = service.findAll();

        assertEquals(dealNoStartDateNoEndDate.getDescription(), dealDTOs.get(0).getDescription());
        assertEquals(dealStartDateEndDate1.getDescription(), dealDTOs.get(1).getDescription());
        assertEquals(dealStartDateEndDate2.getDescription(), dealDTOs.get(2).getDescription());
    }

    @Test
    void testupdateNotFound() {
        Exception ex = assertThrows(ResponseStatusException.class, () -> service.update(99L, new DealDTO()));
        assertEquals("404 NOT_FOUND", ex.getMessage());
    }

    @Test
    void testUpdateRemoveDays() {
        final Day deletedDay = dealMonTues.getDays().stream()
                .filter(day -> day.getDayOfWeek().equals(DayOfWeek.MONDAY))
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
        dealMonTuesDTO.setStartTime("10:30");
        dealMonTuesDTO.setEndTime("11:00");
        dealMonTuesDTO.setStartDate(LocalDate.of(2022, 10, 1));
        dealMonTuesDTO.setStartDate(LocalDate.of(2022, 10, 31));

        when(placeRepository.findById(dealMonTues.getPlace().getId())).thenReturn(Optional.of(dealMonTues.getPlace()));

        Deal deal = new Deal();
        ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealMonTuesDTO, deal);

        assertEquals(dealMonTues.getPlace(), deal.getPlace());
        assertEquals(dealMonTues.getDescription(), deal.getDescription());
        assertEquals(dealMonTues.getId(), deal.getId());
        assertEquals(dealMonTues.getDish(), deal.getDish());
        assertEquals(dealMonTues.getMaxPrice(), deal.getMaxPrice());
        assertEquals(dealMonTues.getMinPrice(), deal.getMinPrice());
        assertEquals(dealMonTues.getMaxDiscount(), deal.getMaxDiscount());
        assertEquals(dealMonTues.getMinDiscount(), deal.getMinDiscount());
        assertEquals(dealMonTues.getMaxDiscountPercent(), deal.getMaxDiscountPercent());
        assertEquals(dealMonTues.getMinDiscountPercent(), deal.getMinDiscountPercent());
        assertEquals(dealMonTues.isVerified(), deal.isVerified());
        assertEquals(dealMonTues.isTaxIncluded(), deal.isTaxIncluded());
        assertEquals(dealMonTuesDTO.getStartTime(), deal.getStartTime());
        assertEquals(dealMonTuesDTO.getEndTime(), deal.getEndTime());
        assertEquals(dealMonTuesDTO.getStartDate(), deal.getStartDate());
        assertEquals(dealMonTuesDTO.getEndDate(), deal.getEndDate());

        // confirm days
        Set<DayOfWeek> days = deal.getDays().stream().map(Day::getDayOfWeek)
                .collect(Collectors.toSet());
        assertEquals(2, days.size());
        assertTrue(days.contains(DayOfWeek.MONDAY));
        assertTrue(days.contains(DayOfWeek.TUESDAY));
    }

    @Test
    void testMapToEntityPlaceNotFound() {
        final DealDTO dealDTO = DealDTO.builder().place(99L).build();
        Exception ex = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealDTO, new Deal())
        );
        assertEquals("404 NOT_FOUND \"place not found\"", ex.getMessage());
    }

    @Test
    void testMapToEntityNullPlace() {
        final DealDTO dealDTO = DealDTO.builder().build();
        Exception ex = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealDTO, new Deal())
        );

        assertEquals("404 NOT_FOUND \"place not found\"", ex.getMessage());
    }

    @Test
    void testGet() {
        dealMonTues.setId(99L);
        when(dealRepository.findById(dealMonTues.getId())).thenReturn(Optional.of(dealMonTues));

        final DealDTO dealDTO = service.get(dealMonTues.getId());

        assertEquals(dealMonTues.getId(), dealDTO.getId());
    }

    @Test
    void testGetNotFound() {
        assertThrows(ResponseStatusException.class, () -> service.get(99L));
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
        dealMonTues.setStartTime("10:30");
        dealMonTues.setEndTime("11:00");
        dealMonTues.setStartDate(LocalDate.of(2022, 10, 1));
        dealMonTues.setStartDate(LocalDate.of(2022, 10, 31));

        final LocalDateTime mon = LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(mon);
            DealDTO dto = ReflectionTestUtils.invokeMethod(service, "mapToDTO", dealMonTues, new DealDTO());

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
            assertEquals(String.format("$%.2f", dealMonTues.getMinPrice()), dto.getPriceRange());
            assertEquals(String.format("$%.2f - $%.2f", dealMonTues.getMinDiscount(),
                    dealMonTues.getMaxDiscount()), dto.getDiscountRange());
            assertEquals(String.format("%.0f%% - %.0f%%", dealMonTues.getMinDiscountPercent(),
                    dealMonTues.getMaxDiscountPercent()), dto.getDiscountPercentRange());
            assertEquals("MT-----", dto.getDaysDisplay());
            assertEquals(dealMonTues.getDish(), dto.getDish());
            assertEquals(dealMonTues.getStartTime(), dto.getStartTime());
            assertEquals(dealMonTues.getEndTime(), dto.getEndTime());
            assertEquals(dealMonTues.getStartDate(), dto.getStartDate());
            assertEquals(dealMonTues.getEndDate(), dto.getEndDate());
            assertEquals(1, dto.getUploads().size());
            assertEquals(upload.getImage(), dto.getUploads().get(0).getImage());
            assertEquals(upload.getDeal().getId(), dto.getUploads().get(0).getDealId());
            assertEquals(upload.isVerified(), dto.getUploads().get(0).isVerified());
        }
    }

    @Test
    void testApplyUploadsToDTO() {
        final Upload upload = TestObjects.upload(dealMonTues);
        final Upload upload2 = Upload.builder()
                .deal(dealMonTues)
                .id(2L)
                .verified(true)
                .image(new byte['a']).build();

        List<Upload> uploads = Stream.of(upload, upload2).toList();
        dealMonTues.getUploads().addAll(uploads);

        final DealDTO dto = new DealDTO();
        ReflectionTestUtils.invokeMethod(service, "applyUploadsToDTO", dealMonTues, dto);

        assertEquals(2, dto.getUploads().size());
    }

    @Test
    void testDelete() {
        service.delete(99L);
        verify(dealRepository, times(1)).deleteById(99L);
    }

    @Test
    void testCreate() {
        dealMonTues.setId(99L);
        when(placeRepository.findById(dealMonTuesDTO.getPlace())).thenReturn(Optional.of(dealMonTues.getPlace()));
        when(dealRepository.save(any(Deal.class))).thenReturn(dealMonTues);

        Long id = service.create(dealMonTuesDTO);

        assertEquals(dealMonTues.getId(), id);
        verify(dealRepository, times(1)).save(any(Deal.class));
    }

}
