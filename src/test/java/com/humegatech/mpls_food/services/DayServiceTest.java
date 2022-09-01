package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.models.DayDTO;
import com.humegatech.mpls_food.repositories.DayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DayServiceTest {
    @MockBean
    private DayRepository dayRepository;

    @Autowired
    private DayService service;

    @Test
    void testFindAll() {
        List<Day> days = TestObjects.days();

        when(dayRepository.findAll()).thenReturn(days);

        List<DayDTO> dayDTOs = service.findAll();

        assertEquals(4, dayDTOs.size());
        assertEquals("Ginelli's Pizza", dayDTOs.get(0).getDeal().getPlace().getName());
        assertEquals(DayOfWeek.MONDAY, dayDTOs.get(0).getDayOfWeek());
        assertEquals("Ginelli's Pizza", dayDTOs.get(1).getDeal().getPlace().getName());
        assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(1).getDayOfWeek());
        assertEquals("Ginelli's Pizza", dayDTOs.get(2).getDeal().getPlace().getName());
        assertEquals(DayOfWeek.WEDNESDAY, dayDTOs.get(2).getDayOfWeek());
        assertEquals("Taco John's", dayDTOs.get(3).getDeal().getPlace().getName());
        assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(3).getDayOfWeek());
    }
}
