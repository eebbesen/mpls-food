package com.humegatech.mpls_food.rest;

import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.services.PlaceService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/places", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlaceResource {

    private final PlaceService placeService;

    public PlaceResource(final PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getAllPlaces() {
        return ResponseEntity.ok(placeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> getPlace(@PathVariable final Long id) {
        return ResponseEntity.ok(placeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPlace(@RequestBody @Valid final PlaceDTO placeDTO) {
        return new ResponseEntity<>(placeService.create(placeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlace(@PathVariable final Long id,
                                            @RequestBody @Valid final PlaceDTO placeDTO) {
        placeService.update(id, placeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePlace(@PathVariable final Long id) {
        placeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
