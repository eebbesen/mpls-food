package com.humegatech.mpls_food.models;

import lombok.Getter;

import java.util.List;


@Getter
public class ErrorResponse {

    private Integer httpStatus;
    private String exception;
    private String message;
    private List<FieldError> fieldErrors;

    public void setHttpStatus(final Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setException(final String exception) {
        this.exception = exception;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setFieldErrors(final List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}
