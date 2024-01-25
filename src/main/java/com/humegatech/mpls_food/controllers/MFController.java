package com.humegatech.mpls_food.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.humegatech.mpls_food.domains.DealType;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.services.PlaceService;
import com.humegatech.mpls_food.util.MplsFoodUtils;

public class MFController {
    protected Map<Long, String> sortedPlaces(final PlaceService placeService) {
        return placeService.findAll().stream()
                .collect(Collectors.toMap(PlaceDTO::getId, PlaceDTO::getName))
                .entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
    }

    protected Map<String, String> dealTypeDisplay() {
        return Stream.of(DealType.values())
                .collect(Collectors.toMap(DealType::name, rt -> MplsFoodUtils.capitalizeFirst(rt.name())));
    }

}
