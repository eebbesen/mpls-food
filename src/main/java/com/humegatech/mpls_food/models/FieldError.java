package com.humegatech.mpls_food.models;


import lombok.Getter;

@Getter
public class FieldError {

    private String field;
    private String errorCode;

    public void setField(final String field) {
        this.field = field;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

}
