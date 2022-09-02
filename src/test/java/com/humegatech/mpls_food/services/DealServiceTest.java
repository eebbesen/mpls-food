package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        dealMonTuesDTO.setId(dealMonTues.getId());
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
        assertEquals("MT-----", dto.getDaysDisplay());
    }

}
