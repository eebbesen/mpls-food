package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.models.DayDTO;
import com.humegatech.mpls_food.repositories.DayRepository;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DayServiceTest {
    @MockBean
    private DayRepository dayRepository;

    @MockBean
    private DealRepository dealRepository;

    @Autowired
    private DayService service;

    // ensure ordering is correct
    @Test
    void testFindAll() {
        final List<Day> days = TestObjects.days();

        when(dayRepository.findAll()).thenReturn(days);

        final List<DayDTO> dayDTOs = service.findAll();

        assertEquals(4, dayDTOs.size());
        assertEquals("Ginelli's Pizza", dayDTOs.get(0).getPlaceName());
        assertEquals(DayOfWeek.MONDAY, dayDTOs.get(0).getDayOfWeek());
        assertEquals("Ginelli's Pizza", dayDTOs.get(1).getPlaceName());
        assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(1).getDayOfWeek());
        assertEquals("Ginelli's Pizza", dayDTOs.get(2).getPlaceName());
        assertEquals(DayOfWeek.WEDNESDAY, dayDTOs.get(2).getDayOfWeek());
        assertEquals("Taco John's", dayDTOs.get(3).getPlaceName());
        assertEquals(DayOfWeek.TUESDAY, dayDTOs.get(3).getDayOfWeek());
    }

    @Test
    void testMapToDto() {
        final Deal deal = TestObjects.deal();
        final DayDTO dto = (DayDTO) ReflectionTestUtils
                .invokeMethod(service, "mapToDTO", TestObjects.day(deal, DayOfWeek.WEDNESDAY), new DayDTO());

        assertEquals(deal.getId(), dto.getDeal());
        assertEquals(deal.getDescription(), dto.getDealDescription());
        assertEquals(deal.getPlace().getName(), dto.getPlaceName());
        assertEquals(DayOfWeek.WEDNESDAY, dto.getDayOfWeek());
        assertEquals(deal.getDish(), dto.getDish());
        assertEquals(MplsFoodUtils.capitalizeFirst(DayOfWeek.WEDNESDAY.name()), dto.getDayOfWeekDisplay());
    }

    @Test
    void testMapToEntity() {
        final Deal deal = TestObjects.deal();
        final DayDTO dto = DayDTO.builder()
                .dayOfWeek(DayOfWeek.SATURDAY)
                .deal(deal.getId())
                .dealDescription(deal.getDescription())
                .placeName(deal.getPlace().getName())
                .id(88l).build();

        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));

        Day day = (Day) ReflectionTestUtils
                .invokeMethod(service, "mapToEntity", dto, new Day());

        assertEquals(88, day.getId());
        assertEquals(DayOfWeek.SATURDAY, day.getDayOfWeek());
        assertEquals(deal.getId(), day.getDeal().getId());
        assertNotNull(day.getDeal().getPlace().getId());
    }
}
