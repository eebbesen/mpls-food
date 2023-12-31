package com.humegatech.mpls_food.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.DealLogDTO;
import com.humegatech.mpls_food.services.DealLogService;
import com.humegatech.mpls_food.services.DealService;
import com.humegatech.mpls_food.services.PlaceService;
import com.humegatech.mpls_food.util.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

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

    private static final String REQUEST_URI = "requestURI";

    @GetMapping("/show/{id}")
    public String show(@PathVariable final Long id, final Model model, final HttpServletRequest request) {
        model.addAttribute("dealLog", dealLogService.get(id));
        model.addAttribute(REQUEST_URI, request.getRequestURI());
        return "deal_logs/show";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("dealLog") final DealLogDTO dealLogDto, final Model model, final HttpServletRequest request) {
        model.addAttribute(REQUEST_URI, request.getRequestURI());
        return "deal_logs/add";
    }

    private static final String REDIRECT_DEAL_LOGS = "redirect:/deal_logs";

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("dealLog") @Valid final DealLogDTO dealLogDto,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal_logs/add";
        }
        dealLogService.create(dealLogDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("dealLog.create.success"));
        return REDIRECT_DEAL_LOGS;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String edit(@PathVariable final Long id, final Model model, final HttpServletRequest request) {
        model.addAttribute("dealLog", dealLogService.get(id));
        model.addAttribute(REQUEST_URI, request.getRequestURI());
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
        return REDIRECT_DEAL_LOGS;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        dealLogService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("dealLog.delete.success"));
        return REDIRECT_DEAL_LOGS;
    }
}
