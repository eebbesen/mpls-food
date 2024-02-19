package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.services.PlaceService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PlaceControllerQL {

    private final PlaceService placeService;

    public PlaceControllerQL(final PlaceService placeService) {
        this.placeService = placeService;
    }

    @QueryMapping(name = "allPlaces")
    public List<Place> findAll() {
        return placeService.findAll();
    }

    @QueryMapping(name = "findPlaceById")
    public Place get(@Argument final Long id) {
        return placeService.get(id);
    }
}
