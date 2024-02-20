package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class DealServiceCacheTest extends MFServiceTest {
    private static final CacheManager cacheManager = new ConcurrentMapCacheManager("allDeals", "singleDeal", "singleDealByPlaceId");
    @Autowired
    private DealService service;

    @BeforeEach
    void setUp() {
        cacheManager.getCacheNames().forEach(cn -> Objects.requireNonNull(cacheManager.getCache(cn)).clear());
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
        deal1.setDays(Set.of(day1_1, day1_2));

        final Deal deal2 = Deal.builder()
                .place(place)
                .id(2L)
                .description("AA").build();
        final Day day2_1 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day2_2 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal2.setDays(Set.of(day2_1, day2_2));

        final Deal deal3 = Deal.builder()
                .place(place)
                .id(3L)
                .description("Z").build();
        final Day day3_1 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day3_2 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal3.setDays(Set.of(day3_1, day3_2));

        final Deal deal4 = Deal.builder()
                .place(place)
                .id(4L)
                .description("C").build();
        final Day day4_1 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.MONDAY).build();
        final Day day4_2 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal4.setDays(Set.of(day4_1, day4_2));

        final List<Deal> deals = List.of(deal1, deal2, deal3, deal4);
        when(dealRepository.findByPlaceId(place.getId())).thenReturn(deals);

        final LocalDateTime sat = LocalDateTime.of(2022, Month.SEPTEMBER, 3, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(sat);

            final List<DealDTO> dtos = service.findByPlaceId(place.getId());

            verify(dealRepository, times(1)).findByPlaceId(place.getId());
            assertEquals(deal4.getId(), dtos.get(0).getId());
            assertEquals(deal2.getId(), dtos.get(1).getId());
            assertEquals(deal1.getId(), dtos.get(2).getId());
            assertEquals(deal3.getId(), dtos.get(3).getId());
        }

        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(sat);

            final List<DealDTO> dtos = service.findByPlaceId(place.getId());

            verifyNoMoreInteractions(dealRepository);
            assertEquals(deal4.getId(), dtos.get(0).getId());
            assertEquals(deal2.getId(), dtos.get(1).getId());
            assertEquals(deal1.getId(), dtos.get(2).getId());
            assertEquals(deal3.getId(), dtos.get(3).getId());
        }
    }

    @Test
    void testFindAll() {
        final Deal active = TestObjects.tacoTuesday();
        when(dealRepository.findAll()).thenReturn(List.of(active));

        final List<DealDTO> dealDTOs = service.findAll();

        verify(dealRepository, times(1)).findAll();
        assertEquals(1, dealDTOs.size());
        assertEquals(active.getDescription(), dealDTOs.get(0).getDescription());

        final List<DealDTO> cachedDealDTOs = service.findAll();
        verifyNoMoreInteractions(dealRepository);
        assertEquals(1, cachedDealDTOs.size());
        assertEquals(active.getDescription(), cachedDealDTOs.get(0).getDescription());
    }

    @Test
    void testGet() {
        final Deal active = TestObjects.tacoTuesday();
        when(dealRepository.findById(active.getId())).thenReturn(Optional.of(active));

        final DealDTO dealDTO = service.get(active.getId());

        verify(dealRepository, times(1)).findById(active.getId());
        assertEquals(active.getDescription(), dealDTO.getDescription());

        final DealDTO cachedDealDTO = service.get(active.getId());
        verifyNoMoreInteractions(dealRepository);
        assertEquals(active.getDescription(), cachedDealDTO.getDescription());
    }


    @Test
    void testFindByPlaceIdWednesday() {
        final Place place = TestObjects.ginellis();
        final Deal deal1 = Deal.builder()
                .place(place)
                .id(1L)
                .description("BB").build();
        final Day day1_1 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day1_2 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal1.setDays(Set.of(day1_1, day1_2));

        final Deal deal2 = Deal.builder()
                .place(place)
                .id(2L)
                .description("AA").build();
        final Day day2_1 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day2_2 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal2.setDays(Set.of(day2_1, day2_2));

        final Deal deal3 = Deal.builder()
                .place(place)
                .id(3L)
                .description("Z").build();
        final Day day3_1 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day3_2 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal3.setDays(Set.of(day3_1, day3_2));

        final Deal deal4 = Deal.builder()
                .place(place)
                .id(4L)
                .description("C").build();
        final Day day4_1 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.MONDAY).build();
        final Day day4_2 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal4.setDays(Set.of(day4_1, day4_2));

        final List<Deal> deals = List.of(deal1, deal2, deal3, deal4);
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
    }

    @Test
    void testFindByPlaceIdSaturday() {
        final Place place = TestObjects.ginellis();
        final Deal deal1 = Deal.builder()
                .place(place)
                .id(1L)
                .description("BB").build();
        final Day day1_1 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day1_2 = Day.builder().deal(deal1).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal1.setDays(Set.of(day1_1, day1_2));

        final Deal deal2 = Deal.builder()
                .place(place)
                .id(2L)
                .description("AA").build();
        final Day day2_1 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day2_2 = Day.builder().deal(deal2).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal2.setDays(Set.of(day2_1, day2_2));

        final Deal deal3 = Deal.builder()
                .place(place)
                .id(3L)
                .description("Z").build();
        final Day day3_1 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.WEDNESDAY).build();
        final Day day3_2 = Day.builder().deal(deal3).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal3.setDays(Set.of(day3_1, day3_2));

        final Deal deal4 = Deal.builder()
                .place(place)
                .id(4L)
                .description("C").build();
        final Day day4_1 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.MONDAY).build();
        final Day day4_2 = Day.builder().deal(deal4).dayOfWeek(DayOfWeek.FRIDAY).build();
        deal4.setDays(Set.of(day4_1, day4_2));

        final List<Deal> deals = List.of(deal1, deal2, deal3, deal4);
        when(dealRepository.findByPlaceId(place.getId())).thenReturn(deals);

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
    void testFindAllMonday() {
        final Deal tt = TestObjects.tacoTuesday();
        final Deal mt = TestObjects.dealMonTues();
        final Deal ft = TestObjects.fridayTwofer();
        final Deal t = TestObjects.deal();
        final Deal t2 = TestObjects.deal();
        t2.setDescription("t2");
        final List<Deal> deals = List.of(tt, ft, mt, t, t2);

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
    }

    @Test
    void testFindAllFriday() {
        final Deal tt = TestObjects.tacoTuesday();
        final Deal mt = TestObjects.dealMonTues();
        final Deal ft = TestObjects.fridayTwofer();
        final Deal t = TestObjects.deal();
        final Deal t2 = TestObjects.deal();
        t2.setDescription("t2");
        final List<Deal> deals = List.of(tt, ft, mt, t, t2);

        when(dealRepository.findAll()).thenReturn(deals);

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
    void testGetNotFound() {
        assertThrows(ResponseStatusException.class, () -> service.get(-99L));
    }

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {
        @Bean
        public CacheManager cacheManager() {
            return cacheManager;
        }

        @Bean
        public DealService dealService(final DealRepository dealRepository, final PlaceRepository placeRepository) {
            return new DealService(dealRepository, placeRepository);
        }
    }

}