package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.services.PlaceService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MFController {
    /**
     * Extract user from request.
     *
     * @param request HttpServletRequest
     * @return user
     */

    protected UsernamePasswordAuthenticationToken getUser(final HttpServletRequest request) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
        if (null != user) {
            System.out.println(String.format("USER: %s\nAUTHORITY: %s", user.getName(),
                    user.getAuthorities().stream().findFirst().get().getAuthority()));
        }

        return user;
    }


    protected Map<Long, String> sortedPlaces(final PlaceService placeService) {
        return placeService.findAll().stream()
                .collect(Collectors.toMap(PlaceDTO::getId, PlaceDTO::getName))
                .entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (k, v) -> k, LinkedHashMap::new));
    }

}
