package com.humegatech.mpls_food.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PlaceDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String address;

    private String truncatedAddress;
    private String website;
    private boolean app;
    private boolean orderAhead;

}
