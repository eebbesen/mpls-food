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

import java.time.DayOfWeek;
import java.util.Optional;

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

    @BeforeEach
    void setUp() {
        dealMonTues = TestObjects.deal(TestObjects.place(), "Monday / Tuesday Deal", DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
    }

    @Test
    void testConfirmOrAddDayDayExists() {
        DealService.addDay(dealMonTues, DayOfWeek.MONDAY);
        assertEquals(2, dealMonTues.getDays().size());
    }

    @Test
    void testConfirmOrAddDayDayDoesNotExist() {
        DealService.addDay(dealMonTues, DayOfWeek.WEDNESDAY);
        assertEquals(3, dealMonTues.getDays().size());
    }

    @Test
    void testHasDay() {
        assertNotNull(DealService.hasDay(dealMonTues, DayOfWeek.MONDAY));
        assertNotNull(DealService.hasDay(dealMonTues, DayOfWeek.TUESDAY));
        assertNull(DealService.hasDay(dealMonTues, DayOfWeek.WEDNESDAY));
        assertNull(DealService.hasDay(dealMonTues, DayOfWeek.THURSDAY));
        assertNull(DealService.hasDay(dealMonTues, DayOfWeek.FRIDAY));
        assertNull(DealService.hasDay(dealMonTues, DayOfWeek.SATURDAY));
        assertNull(DealService.hasDay(dealMonTues, DayOfWeek.SUNDAY));
    }

    @Test
    void testUpdateRemoveDays() {
        Day deletedDay = null;
        for (Day day : dealMonTues.getDays()) {
            if (day.getDayOfWeek() == DayOfWeek.MONDAY) {
                deletedDay = day;
            }
        }

        DealDTO dealDTO = new DealDTO();
        dealDTO.setTuesday(true);
        dealDTO.setDescription(dealMonTues.getDescription());
        dealDTO.setPlace(dealMonTues.getPlace());
        dealDTO.setId(dealMonTues.getId());

        when(placeRepository.findById(dealMonTues.getPlace().getId())).thenReturn(Optional.of(dealMonTues.getPlace()));
        when(dealRepository.findById(dealMonTues.getId())).thenReturn(Optional.of(dealMonTues));

        service.update(dealMonTues.getId(), dealDTO);

        verify(dealRepository, times(1)).findById(dealMonTues.getId());
        assertEquals(1, dealMonTues.getDays().size());
        assertFalse(dealMonTues.getDays().contains(deletedDay));
    }
}
