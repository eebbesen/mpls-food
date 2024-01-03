package com.humegatech.mpls_food.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DayDTO;
import com.humegatech.mpls_food.util.MplsFoodUtils;

@SpringBootTest
class DayServiceTest extends MFServiceTest {
    @Autowired
    private DayService service;

    @Test
    void testCreate() {
        final Deal deal = TestObjects.deal();
        final DayDTO dto = DayDTO.builder()
                .dayOfWeek(DayOfWeek.SATURDAY)
                .deal(deal.getId())
                .placeName(deal.getPlace().getName())
                .build();

        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));
        when(dayRepository.save(any(Day.class))).thenReturn(Day.builder().id(1L).build());

        assertEquals(1L, service.create(dto));
    }

    @Test
    void testFindByDayOfWeek() {
        final List<Day> days = TestObjects.days().stream()
                .filter(d -> d.getDayOfWeek().equals(DayOfWeek.TUESDAY))
                .toList();
        when(dayRepository.findByDayOfWeek(DayOfWeek.TUESDAY)).thenReturn(days);
        final List<DayDTO> dtos = service.findByDayOfWeek(DayOfWeek.TUESDAY);

        assertEquals(2, dtos.size());
        assertEquals("Ginelli's Pizza", dtos.get(0).getPlaceName());
        assertEquals("Taco John's", dtos.get(1).getPlaceName());
    }

    @Test
    void testFindAllSortsByDayBasedOnCurrentDay() {
        final List<Day> days = TestObjects.days();

        when(dayRepository.findAll()).thenReturn(days);

        final LocalDateTime mon = LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(mon);
            final List<DayDTO> dayDTOs = service.findAll();

            assertEquals(4, dayDTOs.size());
            assertEquals("Ginelli's Pizza", dayDTOs.get(0).getPlaceName());
            assertEquals(DayOfWeek.MONDAY, dayDTOs.get(0).getDayOfWeek());
            assertEquals("Taco John's", dayDTOs.get(1).getPlaceName());
            assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(1).getDayOfWeek());
            assertEquals("Ginelli's Pizza", dayDTOs.get(2).getPlaceName());
            assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(2).getDayOfWeek());
            assertEquals("Ginelli's Pizza", dayDTOs.get(3).getPlaceName());
            assertEquals(DayOfWeek.WEDNESDAY, dayDTOs.get(3).getDayOfWeek());
        }

        final LocalDateTime wed = LocalDateTime.of(2022, Month.AUGUST, 31, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(wed);
            final List<DayDTO> dayDTOs = service.findAll();

            assertEquals(4, dayDTOs.size());
            assertEquals("Ginelli's Pizza", dayDTOs.get(0).getPlaceName());
            assertEquals(DayOfWeek.WEDNESDAY, dayDTOs.get(0).getDayOfWeek());
            assertEquals("Ginelli's Pizza", dayDTOs.get(1).getPlaceName());
            assertEquals(DayOfWeek.MONDAY, dayDTOs.get(1).getDayOfWeek());
            assertEquals("Taco John's", dayDTOs.get(2).getPlaceName());
            assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(2).getDayOfWeek());
            assertEquals("Ginelli's Pizza", dayDTOs.get(3).getPlaceName());
            assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(3).getDayOfWeek());
        }
    }

    @Test
    void testFindAllSortsByDealStartTimeEndTimePlaceName() {
        final Place place1 = TestObjects.place("first");
        final Deal deal1 = TestObjects.deal(place1, "z10:30 - 15:00 deal: 1", DayOfWeek.MONDAY);
        final Deal deal2 = TestObjects.deal(place1, "a11:00 - 11:00 deal: 2", DayOfWeek.MONDAY);
        deal2.setStartTime(LocalTime.of(11, 00));
        deal2.setEndTime(LocalTime.of(11, 00));
        final Place place2 = TestObjects.place("asecond");
        final Deal deal3 = TestObjects.deal(place2, "aa 11:00 - 12:30 deal: 3", DayOfWeek.MONDAY);
        deal3.setStartTime(LocalTime.of(11, 00));
        deal3.setEndTime(LocalTime.of(12, 30));
        final Deal deal4 = TestObjects.deal(place2, "aaa 10:30 - 12:30 deal: 4", DayOfWeek.MONDAY);
        deal4.setStartTime(LocalTime.of(10, 30));
        deal4.setEndTime(LocalTime.of(12, 30));
        final Deal deal5 = TestObjects.deal(place2, "aaa 10:30 - 11:00 deal: 5", DayOfWeek.MONDAY);
        deal5.setStartTime(LocalTime.of(10, 30));
        deal5.setEndTime(LocalTime.of(11, 00));
        final Deal deal6 = TestObjects.deal(place2, "null start and end time: 6", DayOfWeek.MONDAY);
        deal6.setStartTime(null);
        deal6.setEndTime(null);
        final Deal deal7 = TestObjects.deal(place2, "null start - 12:30: 7", DayOfWeek.MONDAY);
        deal7.setStartTime(null);
        deal7.setEndTime(LocalTime.of(12, 30));

        final List<Day> days = new ArrayList<>();
        for (Deal d : List.of(deal1, deal2, deal3, deal4, deal5, deal6, deal7)) {
            days.addAll(d.getDays());
        }
        when(dayRepository.findAll()).thenReturn(days);

        final List<DayDTO> dayDTOs = service.findAll();

        assertEquals(deal7.getDescription(), dayDTOs.get(0).getDealDescription());
        assertEquals(deal6.getDescription(), dayDTOs.get(1).getDealDescription());
        assertEquals(deal5.getDescription(), dayDTOs.get(2).getDealDescription());
        assertEquals(deal4.getDescription(), dayDTOs.get(3).getDealDescription());
        assertEquals(deal1.getDescription(), dayDTOs.get(4).getDealDescription());
        assertEquals(deal2.getDescription(), dayDTOs.get(5).getDealDescription());
        assertEquals(deal3.getDescription(), dayDTOs.get(6).getDealDescription());
    }

    @Test
    void testFindAllActiveSortsByDealStartTimeEndTimePlaceName() {
        final Place place1 = TestObjects.place("first");
        final Deal deal1 = TestObjects.deal(place1, "z10:30 - 15:00 deal: 1", DayOfWeek.MONDAY);
        final Deal deal2 = TestObjects.deal(place1, "a11:00 - 11:00 deal: 2", DayOfWeek.MONDAY);
        deal2.setStartTime(LocalTime.of(11, 00));
        deal2.setEndTime(LocalTime.of(11, 00));
        final Place place2 = TestObjects.place("asecond");
        final Deal deal3 = TestObjects.deal(place2, "aa 11:00 - 12:30 deal: 3", DayOfWeek.MONDAY);
        deal3.setStartTime(LocalTime.of(11, 00));
        deal3.setEndTime(LocalTime.of(12, 30));
        final Deal deal4 = TestObjects.deal(place2, "aaa 10:30 - 12:30 deal: 4", DayOfWeek.MONDAY);
        deal4.setStartTime(LocalTime.of(10, 30));
        deal4.setEndTime(LocalTime.of(12, 30));
        final Deal deal5 = TestObjects.deal(place2, "aaa 10:30 - 11:00 deal: 5", DayOfWeek.MONDAY);
        deal5.setStartTime(LocalTime.of(10, 30));
        deal5.setEndTime(LocalTime.of(11, 00));
        final Deal deal6 = TestObjects.deal(place2, "null start and end time: 6", DayOfWeek.MONDAY);
        deal6.setStartTime(null);
        deal6.setEndTime(null);
        final Deal deal7 = TestObjects.deal(place2, "null start - 12:30: 7", DayOfWeek.MONDAY);
        deal7.setStartTime(null);
        deal7.setEndTime(LocalTime.of(12, 30));

        final List<Day> days = new ArrayList<>();
        for (Deal d : List.of(deal1, deal2, deal3, deal4, deal5, deal6, deal7)) {
            days.addAll(d.getDays());
        }
        when(dayRepository.findAllActive()).thenReturn(days);

        final List<DayDTO> dayDTOs = service.findAllActive();

        assertEquals(deal7.getDescription(), dayDTOs.get(0).getDealDescription());
        assertEquals(deal6.getDescription(), dayDTOs.get(1).getDealDescription());
        assertEquals(deal5.getDescription(), dayDTOs.get(2).getDealDescription());
        assertEquals(deal4.getDescription(), dayDTOs.get(3).getDealDescription());
        assertEquals(deal1.getDescription(), dayDTOs.get(4).getDealDescription());
        assertEquals(deal2.getDescription(), dayDTOs.get(5).getDealDescription());
        assertEquals(deal3.getDescription(), dayDTOs.get(6).getDealDescription());
    }

    @Test
    void testFindAllSortByEndTime() {
        final Deal deal1 = TestObjects.deal();
        final Deal deal2 = TestObjects.deal(deal1.getPlace(), "new deal", deal1.getDaysOfWeek().get(0));
        deal2.setStartTime(deal1.getStartTime());
        deal2.setEndTime(deal1.getStartTime());
        final List<Day> days = Stream.of(deal1.getDays(), deal2.getDays())
                .flatMap(Collection::stream)
                .toList();

        when(dayRepository.findAll()).thenReturn(days);

        final List<DayDTO> dtos = service.findAll();

        assertEquals(2, dtos.size());
        assertEquals("new deal", dtos.get(0).getDealDescription());
        assertEquals("$5.00 for two slices from 10:30 - 11:00", dtos.get(1).getDealDescription());
    }

    @Test
    void testMapToDto() {
        final Deal deal = TestObjects.deal();
        final DayDTO dto = ReflectionTestUtils
                .invokeMethod(service, "mapToDTO", TestObjects.day(deal, DayOfWeek.WEDNESDAY), new DayDTO());

        assertNotNull(dto);
        assertEquals(deal.getId(), dto.getDeal());
        assertEquals(deal.getDescription(), dto.getDealDescription());
        assertEquals(deal.getPlace().getName(), dto.getPlaceName());
        assertEquals(DayOfWeek.WEDNESDAY, dto.getDayOfWeek());
        assertEquals(deal.getDish(), dto.getDish());
        assertEquals(MplsFoodUtils.capitalizeFirst(DayOfWeek.WEDNESDAY.name()), dto.getDayOfWeekDisplay());
        assertEquals(MplsFoodUtils.getRange(deal.getMinPrice(), deal.getMaxPrice(), "$"), dto.getPriceRange());
        assertEquals(MplsFoodUtils.getRange(deal.getMinDiscount(), deal.getMaxDiscount(), "$"), dto.getDiscountRange());
        assertEquals(MplsFoodUtils.getRange(deal.getMinDiscountPercent(), deal.getMaxDiscountPercent(), "%"), dto.getDiscountPercentRange());
        assertFalse(ObjectUtils.isEmpty(dto.getMinPrice()));
        assertEquals(deal.getMinPrice(), dto.getMinPrice());
        assertFalse(ObjectUtils.isEmpty(dto.getMinDiscount() > 0d));
        assertEquals(deal.getMinDiscount(), dto.getMinDiscount());
        assertEquals(deal.getStartTime(), dto.getStartTime());
        assertEquals(deal.getEndTime(), dto.getEndTime());
        assertTrue(dto.isVerified());
        assertEquals(deal.isVerified(), dto.isVerified());
    }

    @Test
    void testMapToEntityTimeBoxedOnOpenTime() {
        Deal deal = TestObjects.deal();
        deal.getPlace().getPlaceHours().stream().forEach(ph -> {
            if (ph.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
                ph.setOpenTime(deal.getStartTime().minusHours(1));
            }
        });
        final DayDTO dto = ReflectionTestUtils
                .invokeMethod(service, "mapToDTO", TestObjects.day(deal, DayOfWeek.WEDNESDAY), new DayDTO());

        assertNotNull(dto);
        assertTrue(dto.isTimeBoxed());
    }

    @Test
    void testMapToEntityTimeBoxedOnCloseTime() {
        Deal deal = TestObjects.deal();
        deal.getPlace().getPlaceHours().stream().forEach(ph -> {
            if (ph.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
                ph.setCloseTime(deal.getEndTime().plusHours(1));
            }
        });
        final DayDTO dto = ReflectionTestUtils
                .invokeMethod(service, "mapToDTO", TestObjects.day(deal, DayOfWeek.WEDNESDAY), new DayDTO());

        assertNotNull(dto);
        assertTrue(dto.isTimeBoxed());
    }

    @Test
    void testMapToEntityNotTimeBoxed() {
        Deal deal = TestObjects.deal();
        deal.getPlace().getPlaceHours().stream().forEach(ph -> {
            if (ph.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
                ph.setOpenTime(deal.getStartTime());
                ph.setCloseTime(deal.getEndTime());
            }
        });
        final DayDTO dto = ReflectionTestUtils
                .invokeMethod(service, "mapToDTO", TestObjects.day(deal, DayOfWeek.WEDNESDAY), new DayDTO());

        assertNotNull(dto);
        assertFalse(dto.isTimeBoxed());
    }

    @Test
    void testMapToEntity() {
        final Deal deal = TestObjects.deal();
        final DayDTO dto = DayDTO.builder()
                .dayOfWeek(DayOfWeek.SATURDAY)
                .deal(deal.getId())
                .dealDescription(deal.getDescription())
                .placeName(deal.getPlace().getName())
                .id(88L).build();

        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));

        Day day = ReflectionTestUtils
                .invokeMethod(service, "mapToEntity", dto, new Day());

        assertNotNull(day);
        assertEquals(88, day.getId());
        assertEquals(DayOfWeek.SATURDAY, day.getDayOfWeek());
        assertEquals(deal.getId(), day.getDeal().getId());
        assertNotNull(day.getDeal().getPlace().getId());
    }

    @Test
    void testMapToEntityDealNotFound() {
        final DayDTO dto = DayDTO.builder()
                .dayOfWeek(DayOfWeek.SATURDAY)
                .deal(99L)
                .id(88L).build();

        when(dealRepository.findById(dto.getDeal())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ReflectionTestUtils.invokeMethod(service, "mapToEntity", dto, new Day()));
    }

    @Test
    void testGet() {
        final Day day = TestObjects.day(TestObjects.deal(), DayOfWeek.MONDAY);
        day.setId(99L);

        when(dayRepository.findById(day.getId())).thenReturn(Optional.of(day));

        DayDTO dayDTO = service.get(day.getId());

        assertEquals(day.getDayOfWeek(), dayDTO.getDayOfWeek());
        assertEquals(day.getId(), dayDTO.getId());
    }

    @Test
    void testGetNoDay() {
        final Day day = TestObjects.day(TestObjects.deal(), DayOfWeek.MONDAY);
        day.setId(99L);

        when(dayRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.get(99L));
    }

    @Test
    void testUpdate() {
        final Deal deal = TestObjects.deal();
        final Day day = TestObjects.day(deal, DayOfWeek.MONDAY);
        day.setId(99L);
        final DayDTO dayDTO = DayDTO.builder()
                .id(day.getId())
                .deal(deal.getId())
                .build();

        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));
        when(dayRepository.findById(day.getId())).thenReturn(Optional.of(day));

        service.update(day.getId(), dayDTO);

        verify(dayRepository, times(1)).save(day);
    }

    @Test
    void testUpdateNoDay() {
        when(dayRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.update(99L, new DayDTO()));
    }

    @Test
    void testDelete() {
        service.delete(99L);

        verify(dayRepository, times(1)).deleteById(99L);
    }
}
