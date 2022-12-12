package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.models.DayDTO;
import com.humegatech.mpls_food.services.DayService;
import com.humegatech.mpls_food.util.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/days")
public class DayController {
    private static final String HH_CUTOFF = "13:00";
    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    private static DayOfWeek handleDayOfWeekFilter(final String dayOfWeekString) {
        if (null == handleFilter(dayOfWeekString)) {
            return null;
        }

        DayOfWeek dayOfWeek = null;

        try {
            dayOfWeek = DayOfWeek.valueOf(dayOfWeekString);
        } catch (IllegalArgumentException e) {
            // todo log error
            // result.addError(new ObjectError("globalError",
            // String.format("Invalid day of the week: %s", dayOfWeekString)));
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

    private static Comparator priceComparator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinPrice() ? 99999d : day.getMinPrice());
    }

    private static Comparator priceComparatorReversed() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinPrice() ? 0d : day.getMinPrice()).reversed();
    }

    private static Comparator discountComparator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscount() ? 99999d : day.getMinDiscount());
    }

    private static Comparator discountComparatorReversed() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscount() ? 0d
                : day.getMinDiscount()).reversed();
    }

    private static Comparator discountPercentComparator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscountPercent() ? 99999d
                : day.getMinDiscountPercent());
    }

    private static Comparator discountPercentComparatorReversed() {
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
        final String cuisineFilter = handleFilter(request.getParameter("cuisine"));
        final String happyHourFilter = handleFilter(request.getParameter("happyHour"));
        final String sortBy = handleFilter(request.getParameter("sortBy"));
        final List<DayDTO> days = dayService.findAll();

        model.addAttribute("selectedDay", dayOfWeekFilter);
        model.addAttribute("selectedDish", dishFilter);
        model.addAttribute("selectedPlace", placeFilter);
        model.addAttribute("selectedCuisine", cuisineFilter);
        model.addAttribute("selectedSortBy", sortBy);
        model.addAttribute("nextPriceSort", calculateNextSort(sortBy, "price"));
        model.addAttribute("nextDiscountSort", calculateNextSort(sortBy, "discount"));
        model.addAttribute("nextDiscountPercentSort", calculateNextSort(sortBy, "discountPercent"));
        model.addAttribute("happyHour", happyHourFilter);
        model.addAttribute("dishes",
                days.stream().map(dayDTO -> dayDTO.getDish()).distinct().sorted().collect(Collectors.toList()));
        model.addAttribute("places",
                days.stream().map(dayDTO -> dayDTO.getPlaceName()).distinct().sorted().collect(Collectors.toList()));
        model.addAttribute("cuisines",
                days.stream().map(dayDTO -> dayDTO.getCuisine()).distinct().sorted().collect(Collectors.toList()));

        handleSort(days, sortBy);

        final List<DayDTO> dayDTOs = days.stream()
                .filter(d -> {
                    return null == dayOfWeekFilter || d.getDayOfWeek().equals(dayOfWeekFilter);
                })
                .filter(d -> {
                    return null == dishFilter || d.getDish().equals(dishFilter);
                })
                .filter(d -> {
                    return null == placeFilter || d.getPlaceName().equals(placeFilter);
                })
                .filter(d -> {
                    return null == cuisineFilter || d.getCuisine().equals(cuisineFilter);
                })
                .filter(d -> {
                    return (null != happyHourFilter && happyHourFilter.equals("on"))
                            || (null == d.getStartTime() || 0 > d.getStartTime().compareTo(HH_CUTOFF));
                })
                .collect(Collectors.toList());

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
