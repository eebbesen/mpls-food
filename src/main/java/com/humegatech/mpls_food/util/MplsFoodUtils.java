package com.humegatech.mpls_food.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MplsFoodUtils {
    private static Map<DayOfWeek, String> dowAbbreviationMap = Map.of(
            DayOfWeek.MONDAY, "M",
            DayOfWeek.TUESDAY, "T",
            DayOfWeek.WEDNESDAY, "W",
            DayOfWeek.THURSDAY, "t",
            DayOfWeek.FRIDAY, "F",
            DayOfWeek.SATURDAY, "S",
            DayOfWeek.SUNDAY, "s"
    );

    private static String dowSeparator = "-";

    public static String capitalizeFirst(final String string) {
        if (null == string) {
            return null;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    /**
     * This will need some i18n treatment
     *
     * @return String representing abbreviations for the days of the week.
     */
    public static String dowAbbreviation(final DayOfWeek dayOfWeek) {
        return dowAbbreviationMap.get(dayOfWeek);
    }

    /**
     * @param days
     * @return String of abbreviations for the days of the week if they exist in the input, or a "-" if they do not
     */
    public static String condensedDays(final List<DayOfWeek> days) {
        return condensedDaysFromDay(days, LocalDateTime.now().getDayOfWeek());
    }

    public static String condensedDaysFromDay(final List<DayOfWeek> days, final DayOfWeek fromDay) {
        Map<DayOfWeek, Integer> order = MplsFoodUtils.getSortOrderFromDay(fromDay);
        return Arrays.stream(DayOfWeek.values()).sorted(Comparator.comparing((DayOfWeek d) -> order.get(d)))
                .map(day -> days.contains(day) ? dowAbbreviation(day) : dowSeparator)
                .collect(Collectors.joining(""));
    }

    public static String truncateAddress(final String address) {
        if (!address.contains("\n")) {
            return address;
        }

        return address.substring(0, address.lastIndexOf("\n")).replaceAll("\n", " ");
    }

    /**
     * @return sorted DayOfWeek map with the passed-in day at position 0
     */
    public static Map<DayOfWeek, Integer> getSortOrderFromDay(final DayOfWeek t) {
        return Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toMap(
                        v -> v,
                        v -> v.getValue() >= t.getValue() ? (v.getValue() - t.getValue()) : (v.getValue() + 7 - t.getValue())));
    }

}
