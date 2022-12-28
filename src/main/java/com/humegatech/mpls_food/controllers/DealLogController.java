package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.DealLogDTO;
import com.humegatech.mpls_food.services.DealLogService;
import com.humegatech.mpls_food.services.DealService;
import com.humegatech.mpls_food.services.PlaceService;
import com.humegatech.mpls_food.util.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/deal_logs")
public class DealLogController extends MFController {
    private final DealLogService dealLogService;
    private final PlaceService placeService;
    private final DealService dealService;

    public DealLogController(DealLogService dealLogService, PlaceService placeService, DealService dealService) {
        this.dealLogService = dealLogService;
        this.placeService = placeService;
        this.dealService = dealService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("placeValues", sortedPlaces(placeService));
        model.addAttribute("dealValues", sortedDeals());
        model.addAttribute("dealPlaces", dealPlaceMap());
        model.addAttribute("dealTypeValues", dealTypeDisplay());
    }

    private Map<Long, Long> dealPlaceMap() {
        return dealService.findAll().stream()
                .collect(Collectors.toMap(DealDTO::getId, DealDTO::getPlace));
    }

    private Map<Long, String> sortedDeals() {
        return dealService.findAll().stream()
                .collect(Collectors.toMap(DealDTO::getId, DealDTO::getDescription))
                .entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("dealLogs", dealLogService.findAll());
        return "deal_logs/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable final Long id, final Model model) {
        model.addAttribute("dealLog", dealLogService.get(id));
        return "deal_logs/show";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("dealLog") final DealLogDTO dealLogDto) {
        return "deal_logs/add";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("dealLog") @Valid final DealLogDTO dealLogDto,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal_logs/add";
        }
        dealLogService.create(dealLogDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("dealLog.create.success"));
        return "redirect:/deal_logs";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("dealLog", dealLogService.get(id));
        return "deal_logs/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("dealLog") @Valid final DealLogDTO dealLogDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal_logs/edit";
        }
        dealLogService.update(id, dealLogDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.update.success"));
        return "redirect:/deal_logs";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        dealLogService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("dealLog.delete.success"));
        return "redirect:/deal_logs";
    }
}
