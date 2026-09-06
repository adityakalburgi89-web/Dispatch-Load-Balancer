package com.Aditya.Dispatch.Load.Balancer.exception;

import com.Aditya.Dispatch.Load.Balancer.dto.response.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                "Request validation failed for one or more fields",
                "VALIDATION_FAILED"
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleMalformedJson(HttpMessageNotReadableException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                "Malformed JSON request payload or invalid parameter type",
                "MALFORMED_JSON"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidInput(InvalidInputException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), ex.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateId(DuplicateIdException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), ex.getCode());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NoVehiclesAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleNoVehicles(NoVehiclesAvailableException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), ex.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NoOrdersAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleNoOrders(NoOrdersAvailableException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), ex.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InsufficientFleetCapacityException.class)
    public ResponseEntity<ErrorResponseDto> handleInsufficientFleetCapacity(InsufficientFleetCapacityException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), ex.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OrderUnassignableException.class)
    public ResponseEntity<ErrorResponseDto> handleOrderUnassignable(OrderUnassignableException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), ex.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), "RESOURCE_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidCoordinateException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCoordinate(InvalidCoordinateException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), "INVALID_COORDINATES");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), "BAD_REQUEST");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        log.error("Unhandled internal server error occurred", ex);
        ErrorResponseDto error = new ErrorResponseDto(
                "An unexpected internal server error occurred",
                "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
