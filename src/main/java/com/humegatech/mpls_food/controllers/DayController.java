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
//            todo log error
//            result.addError(new ObjectError("globalError", String.format("Invalid day of the week: %s", dayOfWeekString)));
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

    private static Comparator discountComperator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscount() ? 99999d : day.getMinDiscount());
    }

    private static Comparator discountComperatorReversed() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscount() ? 0d : day.getMinDiscount()).reversed();
    }

    private static Comparator discountPercentComperator() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscountPercent() ? 99999d : day.getMinDiscountPercent());
    }

    private static Comparator discountPercentComperatorReversed() {
        return Comparator.comparing((DayDTO day) -> null == day.getMinDiscountPercent() ? 0d : day.getMinDiscountPercent()).reversed();
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
            days.sort(discountComperator());
        }

        if (sortBy.equals("discountDesc")) {
            days.sort(discountComperatorReversed());
        }

        if (sortBy.equals("discountPercent")) {
            days.sort(discountPercentComperator());
        }

        if (sortBy.equals("discountPercentDesc")) {
            days.sort(discountPercentComperatorReversed());
        }
    }

    private static String calculateNextSort(final String sortBy, final String match) {
        if (ObjectUtils.isEmpty(sortBy)) return match;
        if (!sortBy.startsWith(match)) return match;

        final String cleaned = sortBy.replaceFirst("Desc", "");
        if (sortBy.replaceFirst("Desc", "") != match) return match;

        return sortBy.endsWith("Desc") ? sortBy.replaceFirst("Desc", "") : String.format("%sDesc", sortBy);
    }

    @GetMapping
    public String list(final Model model, final HttpServletRequest request) {
        final DayOfWeek dayOfWeekFilter = handleDayOfWeekFilter(request.getParameter("dayOfWeek"));
        final String dishFilter = handleFilter(request.getParameter("dish"));
        final String placeFilter = handleFilter(request.getParameter("place"));
        final String cuisineFilter = handleFilter(request.getParameter("cuisine"));
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
        model.addAttribute("dishes",
                days.stream().map(dayDTO -> dayDTO.getDish()).distinct().collect(Collectors.toList()));
        model.addAttribute("places",
                days.stream().map(dayDTO -> dayDTO.getPlaceName()).distinct().collect(Collectors.toList()));
        model.addAttribute("cuisines",
                days.stream().map(dayDTO -> dayDTO.getCuisine()).distinct().collect(Collectors.toList()));

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
