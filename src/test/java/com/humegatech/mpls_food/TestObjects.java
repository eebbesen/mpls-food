package com.humegatech.mpls_food;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.domains.DealType;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.PlaceHour;
import com.humegatech.mpls_food.domains.Reward;
import com.humegatech.mpls_food.domains.RewardType;
import com.humegatech.mpls_food.domains.Upload;

public class TestObjects {
    private static Long PLACE_ID = 1L;
    private static Long DEAL_ID = 1L;
    private static Long DEAL_DAY_ID = 1L;
    private static Long UPLOAD_ID = 1L;
    private static Long DEAL_LOG_ID = 1L;
    private static Long PLACE_HOUR_ID = 1L;

    private static Long placeId() {
        return ++PLACE_ID;
    }

    private static Long dealId() {
        return ++DEAL_ID;
    }

    private static Long dayId() {
        return ++DEAL_DAY_ID;
    }

    private static Long dealLogId() {
        return ++DEAL_LOG_ID;
    }

    private static Long placeHourId() {
        return ++PLACE_HOUR_ID;
    }

    public static Place tacoJohns() {
        Place place = Place.builder()
                .name("Taco John's")
                .address("'607 Marquette Ave.\n" +
                        "        Minneapolis, MN 55402")
                .website("https://locations.tacojohns.com/mn/minneapolis/607-marquette-ave/")
                .orderAhead(false)
                .app(true)
                .id(placeId())
                .build();

        addPlaceHours(place);

        return place;
    }

    public static Deal tacoTuesday() {
        final Place tacoJohns = tacoJohns();
        final Deal deal = Deal.builder()
                .description("$1.39 crispy taco")
                .place(tacoJohns)
                .id(dealId())
                .dish("Taco")
                .build();

        deal.getDays().add(Day.builder()
                .deal(deal)
                .dayOfWeek(DayOfWeek.TUESDAY)
                .id(dayId()).build());

        tacoJohns.getDeals().add(deal);

        return deal;
    }

    public static Place ginellis() {
        Place place = Place.builder()
                .name("Ginelli's Pizza")
                .address("121 S 8th Street #235\n" +
                        "Minneapolis, MN 55402")
                .app(false)
                .orderAhead(false)
                .website("https://www.ginellispizza.com/")
                .id(placeId())
                .build();
        addPlaceHours(place);

        final Reward reward = reward(place);
        place.setReward(reward);

        return place;
    }

    public static Deal fridayTwofer() {
        final Place ginellis = ginellis();

        final Deal deal = Deal.builder()
                .description("$5.00 for two slices from 10:30 - 11:00")
                .id(dealId())
                .place(ginellis)
                .startTime("10:30")
                .endTime("11:00")
                .minPrice(5d)
                .maxPrice(5d)
                .minDiscount(2.4)
                .maxDiscount(3.9)
                .minDiscountPercent(32d)
                .maxDiscountPercent(44d)
                .dealType(DealType.DEAL)
                .dish("Pizza").build();

        deal.getDays().add(Day.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .deal(deal).build());

        ginellis.getDeals().add(deal);

        return deal;
    }

    /**
     * Returns place with unique ID,address and website
     *
     * @param name Name to be used for the Place
     * @return Place
     */
    public static Place place(final String name) {
        Integer random = ThreadLocalRandom.current().nextInt(5, 100);
        Place place = Place.builder()
                .name(name)
                .address(String.format("%d West Seventh\nSaint Paul, MN 55105", random))
                .website(String.format("httpx:\\%d.biz", random))
                .id(placeId())
                .build();
        addPlaceHours(place);

        return place;
    }

    public static List<Place> places() {
        return List.of(deal().getPlace(), tacoJohns());
    }

    public static Deal deal() {
        Deal deal = Deal.builder()
                .description("$5.00 for two slices from 10:30 - 11:00")
                .id(dealId())
                .place(ginellis())
                .dish("Pizza")
                .minPrice(5d)
                .minDiscount(2.4d)
                .minDiscountPercent(32d)
                .verified(true)
                .startTime("10:30")
                .endTime("11:00")
                .dealType(DealType.DEAL)
                .build();

        Day day = day(deal, DayOfWeek.THURSDAY);
        deal.getDays().add(day);

        return deal;
    }

    public static Deal dealMonTues() {
        return deal(TestObjects.ginellis(), "Monday / Tuesday Deal", DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
    }

    public static Deal deal(final Place place, final String description, final DayOfWeek... days) {
        Deal deal = Deal.builder()
                .description(description)
                .place(place)
                .id(dealId())
                .dish("Pizza")
                .minPrice(3.20D)
                .maxPrice(3.20d)
                .minDiscount(.50d)
                .maxDiscount(1.25d)
                .minDiscountPercent(15.63d)
                .maxDiscountPercent(28.08d)
                .verified(true)
                .taxIncluded(true)
                .startTime("10:30")
                .endTime("15:00")
                .dealType(DealType.DEAL)
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

    public static Upload upload(final Deal deal) {
        final Upload upload = Upload.builder()
                .deal(deal)
                .image(image())
                .verified(false)
                .id(UPLOAD_ID++)
                .build();

        deal.getUploads().add(upload);

        return upload;
    }

    public static byte[] image() {
        byte[] bytes;
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "test.png", MediaType.MULTIPART_FORM_DATA_VALUE, "abcd".getBytes());

        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytes;
    }

    public static Reward reward(final Place place) {
        return Reward.builder()
                .rewardType(RewardType.PUNCH_CARD)
                .notes("Free slice after purchase of 9 regularly priced slices")
                .place(place)
                .build();
    }

    public static DealLog dealLog() {
        final Deal deal = deal();
        return DealLog.builder()
                .deal(deal)
                .id(dealLogId())
                .description("Got two slices for $5.00")
                .place(deal.getPlace())
                .redeemed(true)
                .redemptionDate(LocalDate.now())
                .dealType(DealType.PUNCH_CARD)
                .build();
    }

    public static PlaceHour placeHour(final Place place, final DayOfWeek dayOfWeek, final boolean populateId) {
        Integer randomHoursOpen = ThreadLocalRandom.current().nextInt(6, 10);
        Integer randomHoursClose = ThreadLocalRandom.current().nextInt(13, 20);
        Integer randomMinutesOpen = ThreadLocalRandom.current().nextInt(0, 59);
        Integer randomMinutesClose = ThreadLocalRandom.current().nextInt(0, 59);

        return PlaceHour.builder()
                .id(populateId ? placeHourId() : null)
                .dayOfWeek(dayOfWeek)
                .openTime(LocalTime.of(randomHoursOpen, randomMinutesOpen, 0, 0))
                .closeTime(LocalTime.of(randomHoursClose, randomMinutesClose, 0, 0))
                .place(place)
                .build();
    }

    public static void addPlaceHours(final Place place, final boolean populateId) {
        Arrays.stream(DayOfWeek.values()).forEach(dayOfWeek -> {
            final PlaceHour placeHour = placeHour(place, dayOfWeek, populateId);

            place.getPlaceHours().add(placeHour);
        });
    }

    public static void addPlaceHours(final Place place) {
        addPlaceHours(place, true);
    }
}
