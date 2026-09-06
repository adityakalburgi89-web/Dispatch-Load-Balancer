package com.Aditya.Dispatch.Load.Balancer.validation;

import com.Aditya.Dispatch.Load.Balancer.domain.valueobject.GeoLocation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CoordinatesValidator implements ConstraintValidator<ValidCoordinates, GeoLocation> {

    @Override
    public boolean isValid(GeoLocation value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Double lat = value.getLatitude();
        Double lon = value.getLongitude();

        if (lat == null || lon == null) {
            return false;
        }

        return lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0;
    }
}
