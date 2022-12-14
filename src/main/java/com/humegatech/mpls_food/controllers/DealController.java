package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.PlaceDTO;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RequestMapping("/deals")
@Controller
public class DealController {
    private final DealService dealService;
    private final PlaceService placeService;

    public DealController(final DealService dealService, final PlaceService placeService) {
        this.placeService = placeService;
        this.dealService = dealService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("placeValues", sortedPlaces());
    }

    private Map<Long, String> sortedPlaces() {
        return placeService.findAll().stream()
                .collect(Collectors.toMap(PlaceDTO::getId, PlaceDTO::getName))
                .entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("deals", dealService.findAll());
        return "deal/list";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("deal") final DealDTO dealDto) {
        return "deal/add";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("deal") @Valid final DealDTO dealDto,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal/add";
        }
        dealService.create(dealDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.create.success"));
        return "redirect:/deals";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("deal", dealService.get(id));
        return "deal/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("deal") @Valid final DealDTO dealDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal/edit";
        }
        dealService.update(id, dealDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.update.success"));
        return "redirect:/deals";
    }

    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String copy(@PathVariable final Long id, final Model model) {
        model.addAttribute("deal", dealService.get(id));
        return "deal/copy";
    }

    @PostMapping("/copy/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String copy(@PathVariable final Long dealIdToCopy, @RequestParam final String[] placeIds,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal/copy";
        }
        dealService.copy(dealIdToCopy, Arrays.stream(placeIds).map(Long::getLong).toList());
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.update.success"));
        return "redirect:/deals";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        dealService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("deal.delete.success"));
        return "redirect:/deals";
    }

}
