package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.domains.RewardType;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.services.DealService;
import com.humegatech.mpls_food.services.PlaceServiceDTO;
import com.humegatech.mpls_food.util.MplsFoodUtils;
import com.humegatech.mpls_food.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequestMapping("/places")
public class PlaceController {

    private final PlaceServiceDTO placeServiceDTO;
    private final DealService dealService;

    public PlaceController(final PlaceServiceDTO placeServiceDTO, DealService dealService) {
        this.placeServiceDTO = placeServiceDTO;
        this.dealService = dealService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        Map<String, String> rewardTypes = Stream.of(RewardType.values())
                .collect(Collectors.toMap(RewardType::name, rt -> MplsFoodUtils.capitalizeFirst(rt.name())));
        model.addAttribute("rewardTypeValues", rewardTypes);
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable final Long id, final Model model) {
        PlaceDTO place = placeServiceDTO.get(id);
        model.addAttribute("place", place);
        model.addAttribute("deals", dealService.findByPlaceId(id));
        model.addAttribute("placeHours", place.getPlaceHours()
                .stream()
                .sorted((h1, h2) -> Integer.compare(h1.getDayOfWeek().getValue(), h2.getDayOfWeek().getValue())));
        return "place/show";
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("places", placeServiceDTO.findAll());
        return "place/list";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("place") final PlaceDTO placeDTO, Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "place/add";
    }

    private static final String REDIRECT_PLACES = "redirect:/places";

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String add(@ModelAttribute("place") @Valid final PlaceDTO placeDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") &&
                placeServiceDTO.nameExists(placeDTO.getName())) {
            bindingResult.rejectValue("name", "exists.place.name");
        }
        if (bindingResult.hasErrors()) {
            return "place/add";
        }
        placeServiceDTO.create(placeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("place.create.success"));
        return REDIRECT_PLACES;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model, HttpServletRequest request) {
        model.addAttribute("place", placeServiceDTO.get(id));
        model.addAttribute("requestURI", request.getRequestURI());
        return "place/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("place") @Valid final PlaceDTO placeDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") &&
                !placeServiceDTO.get(id).getName().equalsIgnoreCase(placeDTO.getName()) &&
                placeServiceDTO.nameExists(placeDTO.getName())) {
            bindingResult.rejectValue("name", "exists.place.name");
        }
        if (bindingResult.hasErrors()) {
            return "place/edit";
        }
        placeServiceDTO.update(id, placeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("place.update.success"));
        return REDIRECT_PLACES;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        placeServiceDTO.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("place.delete.success"));

        return REDIRECT_PLACES;
    }

}
