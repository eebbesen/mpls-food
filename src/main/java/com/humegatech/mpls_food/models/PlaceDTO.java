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

    private String website;

    private boolean app;

    private boolean orderAhead;

    public String truncatedAddress() {
        if (!address.contains("\n")) {
            return address;
        }

        return address.substring(0, address.lastIndexOf("\n")).replaceAll("\n", " ");
    }

}
