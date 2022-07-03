package com.humegatech.mpls_food.controller;

import com.humegatech.mpls_food.domain.Place;
import com.humegatech.mpls_food.model.DealDTO;
import com.humegatech.mpls_food.repos.PlaceRepository;
import com.humegatech.mpls_food.service.DealService;
import com.humegatech.mpls_food.util.WebUtils;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
@RequestMapping("/deals")
public class DealController {

    private final DealService dealService;
    private final PlaceRepository placeRepository;

    public DealController(final DealService dealService, final PlaceRepository placeRepository) {
        this.dealService = dealService;
        this.placeRepository = placeRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("placeValues", placeRepository.findAll().stream().collect(
                Collectors.toMap(Place::getId, Place::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("deals", dealService.findAll());
        return "deal/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("deal") final DealDTO dealDTO) {
        return "deal/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("deal") @Valid final DealDTO dealDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "deal/add";
        }
        dealService.create(dealDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("deal.create.success"));
        return "redirect:/deals";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("deal", dealService.get(id));
        return "deal/edit";
    }

    @PostMapping("/edit/{id}")
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

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        dealService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("deal.delete.success"));
        return "redirect:/deals";
    }

}
