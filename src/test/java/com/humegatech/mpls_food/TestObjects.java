package com.humegatech.mpls_food;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestObjects {
    private static Long PLACE_ID = 1L;
    private static Long DEAL_ID = 1L;
    private static Long DEAL_DAY_ID = 1L;

    private static Long placeId() {
        return ++PLACE_ID;
    }

    private static Long dealId() {
        return ++DEAL_ID;
    }

    private static Long dayId() {
        return ++DEAL_DAY_ID;
    }

    public static Place tacoJohns() {
        return Place.builder()
                .name("Taco John's")
                .address("'607 Marquette Ave.\n" +
                        "        Minneapolis, MN 55402")
                .website("https://locations.tacojohns.com/mn/minneapolis/607-marquette-ave/")
                .orderAhead(false)
                .app(true)
                .id(placeId())
                .build();
    }

    public static Deal tacoTuesday() {
        Place tacoJohns = tacoJohns();
        Deal deal = Deal.builder()
                .description("$1.39 crispy taco")
                .place(tacoJohns)
                .id(dealId())
                .build();

        deal.getDays().add(Day.builder()
                .deal(deal)
                .dayOfWeek(DayOfWeek.TUESDAY)
                .id(dayId()).build());

        tacoJohns.getDeals().add(deal);

        return deal;
    }

    public static Place place() {
        return Place.builder()
                .name("Ginelli's Pizza")
                .address("121 S 8th Street #235\n" +
                        "Minneapolis, MN 55402")
                .app(false)
                .orderAhead(false)
                .website("https://www.ginellispizza.com/")
                .id(placeId())
                .build();
    }

    public static Place place(final String name) {
        Integer random = ThreadLocalRandom.current().nextInt(5, 100);
        return Place.builder()
                .name(name)
                .address(String.format("%d West Seventh\nSaint Paul, MN 55105", random))
                .website(String.format("httpx:\\%d.biz", random))
                .id(placeId())
                .build();
    }

    public static List<Place> places() {
        final Deal deal = tacoTuesday();
        final Place place = deal().getPlace();

        final List<Place> places = new ArrayList<>();
        places.add(place());
        places.add(place);

        return places;
    }

    public static Deal deal() {
        Deal deal = Deal.builder()
                .description("$5.00 for two slices from 10:30 - 11:00")
                .id(dealId())
                .place(place())
                .build();

        Day day = day(deal, DayOfWeek.THURSDAY);
        deal.getDays().add(day);

        return deal;
    }

    public static Deal dealMonTues() {
        return deal(TestObjects.place(), "Monday / Tuesday Deal", DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
    }

    public static Deal deal(final Place place, final String description, final DayOfWeek... days) {
        Deal deal = Deal.builder()
                .description(description)
                .place(place)
                .id(dealId())
                .build();

        for (DayOfWeek day : days) {
            day(deal, day);
        }

        place.getDeals().add(deal);

        return deal;
    }

    public static List<Deal> deals() {
        List<Place> places = places();
        Deal d1 = deal(places.get(0), "half-off lunch", DayOfWeek.FRIDAY, DayOfWeek.THURSDAY);
        Deal d2 = deal(places.get(1), "$5.00 for two slices", DayOfWeek.TUESDAY);

        List<Deal> deals = new ArrayList<>();
        deals.add(d1);
        deals.add(d2);

        return deals;
    }

    public static List<Day> days() {
        return Stream.concat(Stream.concat(
                        dealMonTues().getDays().stream(),
                        tacoTuesday().getDays().stream()),
                Stream.of(day(deal(), DayOfWeek.WEDNESDAY))).collect(Collectors.toList());
    }

    public static Day day(final Deal deal, final DayOfWeek dayOfWeek) {
        Day day = Day.builder()
                .dayOfWeek(dayOfWeek)
                .deal(deal)
                .id(dayId())
                .build();

        deal.getDays().add(day);

        return day;
    }
}
