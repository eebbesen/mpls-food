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

    @GetMapping
    public String list(final Model model, final HttpServletRequest request) {
        final DayOfWeek dayOfWeekFilter = handleDayOfWeekFilter(request.getParameter("dayOfWeek"));
        final String dishFilter = handleFilter(request.getParameter("dish"));
        final String placeFilter = handleFilter(request.getParameter("place"));
        final String cuisineFilter = handleFilter(request.getParameter("cuisine"));
        final List<DayDTO> days = dayService.findAll();

        model.addAttribute("selectedDay", dayOfWeekFilter);
        model.addAttribute("selectedDish", dishFilter);
        model.addAttribute("selectedPlace", placeFilter);
        model.addAttribute("selectedCuisine", cuisineFilter);
        model.addAttribute("dishes",
                days.stream().map(dayDTO -> dayDTO.getDish()).distinct().collect(Collectors.toList()));
        model.addAttribute("places",
                days.stream().map(dayDTO -> dayDTO.getPlaceName()).distinct().collect(Collectors.toList()));
        model.addAttribute("cuisines",
                days.stream().map(dayDTO -> dayDTO.getCuisine()).distinct().collect(Collectors.toList()));

        model.addAttribute("days", days.stream()
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
                .collect(Collectors.toList()));


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
