package com.Aditya.Dispatch.Load.Balancer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private String status;
    private String message;
    private String code;
    private List<FieldErrorDetail> errors;

    public ErrorResponseDto() {
        this.status = "error";
    }

    public ErrorResponseDto(String message, String code) {
        this.status = "error";
        this.message = message;
        this.code = code;
    }

    public ErrorResponseDto(String status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public ErrorResponseDto(String message, String code, List<FieldErrorDetail> errors) {
        this.status = "error";
        this.message = message;
        this.code = code;
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FieldErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldErrorDetail> errors) {
        this.errors = errors;
    }

    public void addFieldError(String field, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new FieldErrorDetail(field, message));
    }

    public static class FieldErrorDetail {
        private String field;
        private String message;

        public FieldErrorDetail() {
        }

        public FieldErrorDetail(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
