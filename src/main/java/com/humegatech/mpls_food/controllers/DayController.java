package com.humegatech.mpls_food.controllers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.humegatech.mpls_food.models.DayDTO;
import com.humegatech.mpls_food.services.DayService;
import com.humegatech.mpls_food.util.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/days")
public class DayController {
    public static final LocalTime HH_CUTOFF = LocalTime.of(13, 00);
    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    private static DayOfWeek handleDayOfWeekFilter(final String dayOfWeekString) {
        if (null == handleFilter(dayOfWeekString)) {
            return null;
        }

        DayOfWeek dayOfWeek;

        try {
            dayOfWeek = DayOfWeek.valueOf(dayOfWeekString);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid day of the week received: %s".formatted(dayOfWeekString));
            return null;
        }

        return dayOfWeek;
    }

    private static String handleFilter(final String filter) {
        if (ObjectUtils.isEmpty(filter)) {
            return null;
        }

        return filter;
    }

    private static Comparator<DayDTO> priceComparator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinPrice() ? 99999d : day.getMinPrice());
    }

    private static Comparator<DayDTO> priceComparatorReversed() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinPrice() ? 0d : day.getMinPrice()).reversed();
    }

    private static Comparator<DayDTO> discountComparator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscount() ? 99999d : day.getMinDiscount());
    }

    private static Comparator<DayDTO> discountComparatorReversed() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscount() ? 0d
                : day.getMinDiscount()).reversed();
    }

    private static Comparator<DayDTO> discountPercentComparator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscountPercent() ? 99999d
                : day.getMinDiscountPercent());
    }

    private static Comparator<DayDTO> discountPercentComparatorReversed() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscountPercent() ? 0d
                : day.getMinDiscountPercent()).reversed();
    }

    private static void handleSort(final List<DayDTO> days, final String sortBy) {
        if (ObjectUtils.isEmpty(sortBy)) return;

        if (sortBy.equals("price")) {
            days.sort(priceComparator());
        }

        if (sortBy.equals("priceDesc")) {
            days.sort(priceComparatorReversed());
        }

        if (sortBy.equals("discount")) {
            days.sort(discountComparator());
        }

        if (sortBy.equals("discountDesc")) {
            days.sort(discountComparatorReversed());
        }

        if (sortBy.equals("discountPercent")) {
            days.sort(discountPercentComparator());
        }

        if (sortBy.equals("discountPercentDesc")) {
            days.sort(discountPercentComparatorReversed());
        }
    }

    private static String calculateNextSort(final String sortBy, final String match) {
        if (ObjectUtils.isEmpty(sortBy)) return match;
        if (!sortBy.startsWith(match)) return match;

        if (!sortBy.replaceFirst("Desc", "").equals(match)) return match;

        return sortBy.endsWith("Desc") ? sortBy.replaceFirst("Desc", "")
                : String.format("%sDesc", sortBy);
    }

    @GetMapping
    public String list(final Model model, final HttpServletRequest request) {
        final DayOfWeek dayOfWeekFilter = handleDayOfWeekFilter(request.getParameter("dayOfWeek"));
        final String dishFilter = handleFilter(request.getParameter("dish"));
        final String placeFilter = handleFilter(request.getParameter("place"));
        final String happyHourFilter = handleFilter(request.getParameter("happyHour"));
        final String sortBy = handleFilter(request.getParameter("sortBy"));
        final List<DayDTO> days = new ArrayList<>(dayService.findAllActive());

        model.addAttribute("selectedDay", dayOfWeekFilter);
        model.addAttribute("selectedDish", dishFilter);
        model.addAttribute("selectedPlace", placeFilter);
        model.addAttribute("selectedSortBy", sortBy);
        model.addAttribute("nextPriceSort", calculateNextSort(sortBy, "price"));
        model.addAttribute("nextDiscountSort", calculateNextSort(sortBy, "discount"));
        model.addAttribute("nextDiscountPercentSort", calculateNextSort(sortBy, "discountPercent"));
        model.addAttribute("happyHour", happyHourFilter);
        model.addAttribute("dishes",
                days.stream().map(DayDTO::getDish).filter(Objects::nonNull).distinct().sorted()
                        .toList());
        model.addAttribute("places",
                days.stream().map(DayDTO::getPlaceName).distinct().sorted().toList());
        model.addAttribute("requestURI", request.getRequestURI());

        handleSort(days, sortBy);

        final List<DayDTO> dayDTOs = days.stream()
                .filter(d -> null == dayOfWeekFilter || d.getDayOfWeek().equals(dayOfWeekFilter))
                .filter(d -> null == dishFilter || (null != d.getDish() && d.getDish().equals(dishFilter)))
                .filter(d -> null == placeFilter || d.getPlaceName().equals(placeFilter))
                .filter(d -> (null != happyHourFilter && happyHourFilter.equals("on"))
                        || (null == d.getStartTime() || 0 > d.getStartTime().compareTo(HH_CUTOFF)))
                .toList();

        model.addAttribute("days", dayDTOs);

        return "days/list";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        dayService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("day.delete.success"));
        return "redirect:/days";
    }

}
