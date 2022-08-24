package com.humegatech.mpls_food.service;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domain.Deal;
import com.humegatech.mpls_food.domain.DealDay;
import com.humegatech.mpls_food.model.DealDayDTO;
import com.humegatech.mpls_food.repos.DealDayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DealDayServiceTest {
    @Autowired
    DealDayService service;

    @MockBean
    DealDayRepository dealDayRepository;

    @Test
    void testFindAll() {
        Deal dealMonTues = TestObjects.deal(TestObjects.place(), "Monday / Tuesday Deal", DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
        Deal dealMon = TestObjects.deal(TestObjects.place(), "Monday Deal", DayOfWeek.MONDAY);
        List<DealDay> dealDays = new ArrayList<>();
        dealDays.addAll(dealMonTues.getDealDays());
        dealDays.addAll(dealMon.getDealDays());

        when(dealDayRepository.findAll()).thenReturn(dealDays);

        List<DealDayDTO> found = service.findAll();

        assertEquals(3, found.size());
    }
}
