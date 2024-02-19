package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.services.DealService;
import com.humegatech.mpls_food.services.PlaceServiceDTO;
import com.humegatech.mpls_food.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;


@RequestMapping("/deals")
@Controller
public class DealController extends MFController {
    private final DealService dealService;
    private final PlaceServiceDTO placeService;

    public DealController(final DealService dealService, final PlaceServiceDTO placeService) {
        this.placeService = placeService;
        this.dealService = dealService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("placeValues", sortedPlaces(placeService));
        model.addAttribute("dealTypeValues", dealTypeDisplay());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("deals", dealService.findAll());
        return "deal/list";
    }

    private static final String REQUEST_URI = "requestURI";

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@ModelAttribute("deal") final DealDTO dealDto,
                      final Model model,
                      final HttpServletRequest request) {
        model.addAttribute(REQUEST_URI, request.getRequestURI());
        return "deal/add";
    }

    private static final String REDIRECT_DEALS = "redirect:/deals";

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String add(@ModelAttribute("deal") @Valid final DealDTO dealDto,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal/add";
        }
        dealService.create(dealDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.create.success"));
        return REDIRECT_DEALS;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable final Long id, final Model model, final HttpServletRequest request) {
        model.addAttribute("deal", dealService.get(id));
        model.addAttribute(REQUEST_URI, request.getRequestURI());
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
        return REDIRECT_DEALS;
    }

    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String copy(@PathVariable final Long id, final Model model, final HttpServletRequest request) {
        model.addAttribute("deal", dealService.get(id));
        model.addAttribute(REQUEST_URI, request.getRequestURI());
        return "deal/copy";
    }

    @PostMapping("/copy/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String copy(@PathVariable final Long id, HttpServletRequest request,
                       final RedirectAttributes redirectAttributes) {
        // multiple place params so need to manually extract them (or create a DTO to hold them)
        final String[] places = request.getParameterMap().get("places");
        if (null != places) {
            String[] placeIds = Arrays.stream(places).filter(place -> !place.isEmpty()).toArray(String[]::new);
            if (placeIds.length > 0) {
                dealService.copy(id, Arrays.stream(placeIds).map(Long::parseLong).toList());
                redirectAttributes
                        .addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.update.success"));
                return REDIRECT_DEALS;
            }
        }

        return String.format("redirect:/deals/copy/%d", id);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        dealService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("deal.delete.success"));
        return REDIRECT_DEALS;
    }

}
