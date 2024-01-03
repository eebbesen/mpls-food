package com.humegatech.mpls_food.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;

@DataJpaTest
class DayRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DayRepository dayRepository;
    private Place place;


    @BeforeEach
    void setUp() {
        place = TestObjects.place("my place");
        place.setId(null);
        place.getPlaceHours().forEach(placeHour -> placeHour.setId(null));
        entityManager.persist(place);
    }

    private void buildAndSaveDay(final Deal deal) {
        deal.setPlace(place);
        deal.setId(null);
        final Day day = Day.builder()
                .deal(deal)
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        deal.setDays(Set.of(day));

        entityManager.persist(deal);
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

    @Test
    void testFindAllActiveOneDayDealTodayOnly() {
        final LocalDate today = LocalDate.now();
        final Deal deal = TestObjects.deal();
        deal.setDescription("Today only");
        deal.setId(null);
        deal.setPlace(place);
        deal.setStartDate(today);
        deal.setEndDate(today);
        deal.setDays(new HashSet<>());
        final Day day = Day.builder()
                .dayOfWeek(today.getDayOfWeek())
                .deal(deal)
                .build();
        deal.getDays().add(day);
        entityManager.persist(deal);

        final List<Day> days = dayRepository.findAllActive();

        assertEquals(1, days.size());
        assertEquals(deal.getDescription(), days.get(0).getDeal().getDescription());
    }
}
