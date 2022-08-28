package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.services.PlaceService;
import com.humegatech.mpls_food.util.WebUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/rest/places")
public class PlaceRestController {
    private final PlaceService placeService;

    public PlaceRestController(final PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        placeService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("place.delete.success"));

        return String.format("Deleted Place %d", id);
    }
}
