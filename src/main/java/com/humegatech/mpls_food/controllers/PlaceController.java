package com.humegatech.mpls_food.controllers;

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


@Controller
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final DealService dealService;

    public PlaceController(final PlaceService placeService, DealService dealService) {
        this.placeService = placeService;
        this.dealService = dealService;
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable final Long id, final Model model) {
        model.addAttribute("place", placeService.get(id));
        model.addAttribute("deals", dealService.findByPlaceId(id));
        return "place/show";
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("places", placeService.findAll());
        return "place/list";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("place") final PlaceDTO placeDTO) {
        return "place/add";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("place") @Valid final PlaceDTO placeDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") &&
                placeService.nameExists(placeDTO.getName())) {
            bindingResult.rejectValue("name", "exists.place.name");
        }
        if (bindingResult.hasErrors()) {
            return "place/add";
        }
        placeService.create(placeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("place.create.success"));
        return "redirect:/places";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("place", placeService.get(id));
        return "place/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("place") @Valid final PlaceDTO placeDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") &&
                !placeService.get(id).getName().equalsIgnoreCase(placeDTO.getName()) &&
                placeService.nameExists(placeDTO.getName())) {
            bindingResult.rejectValue("name", "exists.place.name");
        }
        if (bindingResult.hasErrors()) {
            return "place/edit";
        }
        placeService.update(id, placeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("place.update.success"));
        return "redirect:/places";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        placeService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("place.delete.success"));

        return "redirect:/places";
    }

}
