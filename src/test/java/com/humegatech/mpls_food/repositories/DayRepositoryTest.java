package com.humegatech.mpls_food.repositories;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DayRepositoryTest {
    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private PlaceRepository placeRepository;

    private Place place;

    @BeforeEach
    void setUp() {
        place = TestObjects.place("my place");
        place.setId(null);
        place = placeRepository.save(place);
    }

    private void buildAndSaveDay(final Deal deal) {
        deal.setPlace(place);
        deal.setId(null);
        final Day day = Day.builder()
                .deal(deal)
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        deal.setDays(Set.of(day));

        dealRepository.save(deal);
    }

    @Test
    void testFindAllActive() {
        final Deal noDates = TestObjects.deal();
        buildAndSaveDay(noDates);
        final Deal endDatePast = TestObjects.dealMonTues();
        endDatePast.setEndDate(LocalDate.now().minusDays(1));
        buildAndSaveDay(endDatePast);
        final Deal endDateFuture = TestObjects.fridayTwofer();
        endDateFuture.setEndDate(LocalDate.now().plusDays(1));
        buildAndSaveDay(endDateFuture);
        buildAndSaveDay(Deal.builder()
                .place(noDates.getPlace())
                .description("future start date")
                .startDate(LocalDate.now().plusDays(1))
                .build());

        final List<Day> days = dayRepository.findAllActive();
        days.sort(Comparator.comparing(d -> d.getDeal().getId()));

        assertEquals(2, days.size());
        assertEquals(noDates.getId(), days.get(0).getDeal().getId());
        assertEquals(endDateFuture.getId(), days.get(1).getDeal().getId());
    }
}
