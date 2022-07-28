package com.humegatech.mpls_food.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getWebsite() { return website; }

    public void setWebsite(final String website) { this.website = website; }

    public boolean isApp() { return app; }

    public void setApp(final boolean app) { this.app = app; }

    public boolean isOrderAhead() { return orderAhead; };

    public void setOrderAhead(final boolean orderAhead) { this.orderAhead = orderAhead; }

    public String truncatedAddress() {
        if ( address.indexOf("\n") < 0 ) {
            return address;
        }

        return address.substring(0, address.lastIndexOf("\n")).replaceAll("\n", " ");
    }

}
