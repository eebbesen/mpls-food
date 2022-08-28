package com.humegatech.mpls_food.controllers;


import com.humegatech.mpls_food.services.DealService;
import com.humegatech.mpls_food.util.WebUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/rest/deals")
public class DealRestController {
    private final DealService dealService;

    public DealRestController(final DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        dealService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("deal.delete.success"));

        return String.format("Deleted Deal %d", id);
    }

}