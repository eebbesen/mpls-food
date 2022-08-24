package com.humegatech.mpls_food;

import com.humegatech.mpls_food.domain.Deal;
import com.humegatech.mpls_food.domain.DealDay;
import com.humegatech.mpls_food.domain.Place;
import com.humegatech.mpls_food.model.DealDTO;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestObjects {
    public static Place place() {
        return Place.builder().address("121 S 8th Street #235\n" +
                        "Minneapolis, MN 55402")
                .name("Ginelli's Pizza")
                .app(false)
                .orderAhead(false)
                .website("https://www.ginellispizza.com/").build();
    }

    public static Place place(final String name) {
        Integer random = ThreadLocalRandom.current().nextInt(1, 100);
        return Place.builder()
                .name(name)
                .address(String.format("%d West Seventh\nSaint Paul, MN 55105", random))
                .website(String.format("httpx:\\%d.biz", random))
                .build();
    }

    public static List<Place> places() {
        final Place tacoJohns = Place.builder()
                .address("'607 Marquette Ave.\n" +
                        "        Minneapolis, MN 55402")
                .name("Taco John's")
                .website("https://locations.tacojohns.com/mn/minneapolis/607-marquette-ave/")
                .orderAhead(false)
                .app(true)
                .build();

        final Deal deal = Deal.builder()
                .description("$1.39 crispy taco")
//                .tuesday(true)
                .place(tacoJohns).build();
        tacoJohns.getPlaceDeals().add(deal);

        final List<Place> places = new ArrayList<>();
        places.add(place());
        places.add(tacoJohns);

        return places;
    }

    public static Deal deal() {
        return Deal.builder()
                .description("$5.00 for two slices from 10:30 - 11:00")
                .build();
    }

    public static Deal deal(final Place place, final String description, final DayOfWeek... days) {
        Deal deal = Deal.builder()
                .description(description)
                .place(place).build();

        for (DayOfWeek day : days) {
            dealDay(deal, day);
        }

        place.getPlaceDeals().add(deal);

        return deal;
    }


    public static DealDay dealDay(final Deal deal, final DayOfWeek dayOfWeek) {
        DealDay dealDay = DealDay.builder()
                .dayOfWeek(dayOfWeek)
                .deal(deal).build();

        deal.getDealDays().add(dealDay);

        return dealDay;
    }

    public static List<DealDTO> dealsToDtos(final List<Deal> deals) {
        List<DealDTO> dtos = new ArrayList<>();

        for (Deal deal : deals) {
            DealDTO dto = new DealDTO();
            dto.setDescription(deal.getDescription());
            dto.setPlace(deal.getPlace());
            dto.setId(deal.getId());

            dtos.add(dto);
        }

        return dtos;
    }
}
