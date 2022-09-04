package com.humegatech.mpls_food.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DealDTO {
    private Long id;

    // using 'place' instead of 'place_id' for compatibility with thymeleaf dropdown
    // if desired fragments/forms could be modified to use 'place_id' but display 'place', it just isn't baked in by default
    @NotNull
    private Long place;
    private String placeName;

    @NotNull
    private String description;

    private boolean monday;
    private boolean sunday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private String daysDisplay;
    private String dish;
    private List<UploadDTO> uploads = new ArrayList<>();
}
