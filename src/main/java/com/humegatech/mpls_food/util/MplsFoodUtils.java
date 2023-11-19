package com.humegatech.mpls_food.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MplsFoodUtils {
    private static final String DOW_SEPARATOR = "-";
    private static final Map<DayOfWeek, String> DOW_ABBREVIATION_MAP = Map.of(
            DayOfWeek.MONDAY, "M",
            DayOfWeek.TUESDAY, "T",
            DayOfWeek.WEDNESDAY, "W",
            DayOfWeek.THURSDAY, "t",
            DayOfWeek.FRIDAY, "F",
            DayOfWeek.SATURDAY, "S",
            DayOfWeek.SUNDAY, "s"
    );

    private MplsFoodUtils() {
    }

    public static String decorateValue(final Double value, final String punctuation) {
        if (null == value) {
            return null;
        }

        if (null == punctuation) {
            return String.valueOf(value);
        }

        return punctuation.equals("$") ? String.format("%s%.2f", punctuation, value)
                : String.format("%.0f%s", value, punctuation);
    }

    public static String getRange(final Double min, final Double max, final String type) {
        if (null == min && null == max) {
            return null;
        }

        if (null == min || null == max || min.equals(max)) {
            final Double val = null == min ? max : min;
            return decorateValue(val, type);
        }

        return String.format("%s - %s", decorateValue(min, type), decorateValue(max, type));
    }

    public static String capitalizeFirst(final String string) {
        if (null == string || string.isEmpty()) {
            return null;
        }

        final String[] workingString = string.split("[_\\s]");
        return Arrays.stream(workingString)
                .map(s -> s.substring(0, 1).toUpperCase().concat(s.substring(1).toLowerCase()))
                .collect(Collectors.joining(" "));
    }

    /**
     * This will need some i18n treatment.
     *
     * @return String representing abbreviations for the days of the week.
     */
    public static String dowAbbreviation(final DayOfWeek dayOfWeek) {
        return DOW_ABBREVIATION_MAP.get(dayOfWeek);
    }

    /**
     * Returns short display of days of the week a deal is valid for.
     *
     * @param days Active deal days
     * @return String of abbreviations for the days of the week if they exist in the input, or a "-" if they do not
     */
    public static String condensedDays(final List<DayOfWeek> days) {
        return condensedDaysFromDay(days, LocalDateTime.now().getDayOfWeek());
    }

    public static String condensedDaysFromDay(final List<DayOfWeek> days, final DayOfWeek fromDay) {
        final Map<DayOfWeek, Integer> order = MplsFoodUtils.getSortOrderFromDay(fromDay);
        return Arrays.stream(DayOfWeek.values()).sorted(Comparator.comparing(order::get))
                .map(day -> days.contains(day) ? dowAbbreviation(day) : DOW_SEPARATOR)
                .collect(Collectors.joining(""));
    }

    public static String truncateAddress(final String address) {
        if (!address.contains("\n")) {
            return address;
        }

        return address.substring(0, address.lastIndexOf("\n")).replace("\n", " ");
    }

    public static String truncateDealDescription(final String description) {
        if (null == description || description.isEmpty()) {
            return "";
        }

        String truncated = description.substring(0, description.length() > 49 ? 50 : description.length());
        if (truncated.length() == 50) {
            truncated = truncated.lastIndexOf(" ") > 9 ? truncated.substring(0, truncated.lastIndexOf(" ")) :
                    truncated;
        }

        return truncated;
    }

    /**
     * Returns DayOfWeek map with the passed-in day at position 0.
     *
     * @return sorted DayOfWeek map with the passed-in day at position 0
     */
    public static Map<DayOfWeek, Integer> getSortOrderFromDay(final DayOfWeek t) {
        return Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toMap(
                        v -> v,
                        v -> v.getValue() >= t.getValue() ? (v.getValue() - t.getValue())
                                : (v.getValue() + 7 - t.getValue())));
    }

}
