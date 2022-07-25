package com.rpdpymnt.reporting.util;

import com.rpdpymnt.reporting.dto.ValidationErrorDto;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@UtilityClass
public class ValidationErrorBuilder {

    public static final String REQUIRED_PARAMETERS = "Required parameters must be provided";
    public static final String VALIDATION_ERROR = "Validation failed. %d error(s) -> %s";

    public static ErrorDto fromBindingErrors(BindingResult result) {
        ErrorDto error = new ErrorDto();
        if (result.getFieldErrors().isEmpty()) {
            for (ObjectError objectError : result.getAllErrors()) {
                error.addValidationError(new ValidationErrorDto(objectError.getDefaultMessage()));
            }
        } else {
            for (FieldError fieldError : result.getFieldErrors()) {
                error.addValidationError(new ValidationErrorDto(fieldError.getField(), fieldError.getDefaultMessage()));
            }
        }
        error.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        error.setMessage(String.format(VALIDATION_ERROR, result.getErrorCount(), error.getValidationErrors()));
        return error;
    }

    public static ErrorDto fromBindingErrors(Errors errors) {
        ErrorDto error = new ErrorDto();
        error.setMessage(REQUIRED_PARAMETERS);
        errors.getFieldErrors().forEach(fieldError -> {
            ValidationErrorDto validation = new ValidationErrorDto(fieldError.getField(),
                    fieldError.getDefaultMessage());
            error.addValidationError(validation);
        });
        return error;
    }


}
