package com.humegatech.mpls_food.controllers;

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

@Controller
@RequestMapping("/days")
public class DayController {
    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    private static DayOfWeek handleDayOfWeekFilter(final String dayOfWeekString) {
        if (ObjectUtils.isEmpty(dayOfWeekString)) {
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

    @GetMapping
    public String list(final Model model, final HttpServletRequest request) {
        final String dayOfWeekFilter = request.getParameter("dayOfWeek");
        final DayOfWeek dayOfWeek = handleDayOfWeekFilter(dayOfWeekFilter);

        if (null != dayOfWeek) {
            model.addAttribute("days", dayService.findByDayOfWeek(dayOfWeek));
            model.addAttribute("selectedDay", dayOfWeek);
        } else {
            model.addAttribute("days", dayService.findAll());
        }

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
