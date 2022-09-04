package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.services.DealService;
import com.humegatech.mpls_food.services.PlaceService;
import com.humegatech.mpls_food.services.UploadService;
import com.humegatech.mpls_food.util.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;


@RequestMapping("/deals")
@Controller
public class DealController {
    private final DealService dealService;
    private final PlaceService placeService;
    private final UploadService uploadService;

    public DealController(final DealService dealService, final PlaceService placeService, final UploadService uploadService) {
        this.placeService = placeService;
        this.dealService = dealService;
        this.uploadService = uploadService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("placeValues", placeService.findAll().stream().collect(
                Collectors.toMap(PlaceDTO::getId, PlaceDTO::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("deals", dealService.findAll());
        return "deal/list";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("deal") final DealDTO dealDTO) {
        return "deal/add";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("deal") @Valid final DealDTO DealDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal/add";
        }
        dealService.create(DealDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.create.success"));
        return "redirect:/deals";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("deal", dealService.get(id));
        return "deal/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("deal") @Valid final DealDTO DealDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal/edit";
        }
        dealService.update(id, DealDTO);
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
