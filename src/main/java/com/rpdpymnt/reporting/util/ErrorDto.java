package com.rpdpymnt.reporting.util;

import com.rpdpymnt.reporting.dto.ValidationErrorDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "validationErrors")
public class ErrorDto {

    private String code;

    private String message;

    @Setter(AccessLevel.NONE)
    private List<ValidationErrorDto> validationErrors = new ArrayList<>();

    public List<ValidationErrorDto> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public void addValidationError(ValidationErrorDto dto) {
        validationErrors.add(dto);
    }
}
