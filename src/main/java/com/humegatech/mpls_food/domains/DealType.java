package com.humegatech.mpls_food.domains;

import lombok.Getter;

@Getter
public enum DealType {
    APP(0),
    EMAIL(1),
    PUNCH_CARD(2),
    DEAL(3),
    TOAST(4);

    private final int value;

    DealType(int value) {
        this.value = value;
    }

}
