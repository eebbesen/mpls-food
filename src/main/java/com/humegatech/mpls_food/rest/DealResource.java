package com.humegatech.mpls_food.rest;

import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.services.DealService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/deals", produces = MediaType.APPLICATION_JSON_VALUE)
public class DealResource {

    private final DealService dealService;

    public DealResource(final DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping
    public ResponseEntity<List<DealDTO>> getAllDeals() {
        return ResponseEntity.ok(dealService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealDTO> getDeal(@PathVariable final Long id) {
        return ResponseEntity.ok(dealService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDeal(@RequestBody @Valid final DealDTO dealDTO) {
        return new ResponseEntity<>(dealService.create(dealDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDeal(@PathVariable final Long id,
                                           @RequestBody @Valid final DealDTO dealDTO) {
        dealService.update(id, dealDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDeal(@PathVariable final Long id) {
        dealService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
