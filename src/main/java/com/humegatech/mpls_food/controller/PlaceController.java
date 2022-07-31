package com.humegatech.mpls_food.controller;

import com.humegatech.mpls_food.model.PlaceDTO;
import com.humegatech.mpls_food.service.PlaceService;
import com.humegatech.mpls_food.util.WebUtils;
import javax.validation.Valid;

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


@Controller
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(final PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("places", placeService.findAll());
        return "place/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add(@ModelAttribute("place") final PlaceDTO placeDTO) {
        return "place/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add(@ModelAttribute("place") @Valid final PlaceDTO placeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") &&
                placeService.nameExists(placeDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.place.name");
        }
        if (bindingResult.hasErrors()) {
            return "place/add";
        }
        placeService.create(placeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("place.create.success"));
        return "redirect:/places";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("place", placeService.get(id));
        return "place/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("place") @Valid final PlaceDTO placeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") &&
                !placeService.get(id).getName().equalsIgnoreCase(placeDTO.getName()) &&
                placeService.nameExists(placeDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.place.name");
        }
        if (bindingResult.hasErrors()) {
            return "place/edit";
        }
        placeService.update(id, placeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("place.update.success"));
        return "redirect:/places";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = placeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            placeService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("place.delete.success"));
        }
        return "redirect:/places";
    }

}
