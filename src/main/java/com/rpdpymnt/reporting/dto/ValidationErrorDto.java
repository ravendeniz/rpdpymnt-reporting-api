package com.rpdpymnt.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDto {

    private String field;

    private String message;

    public ValidationErrorDto(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return StringUtils.isEmpty(field) ? String.format("[%s]", message) : String.format("[%s]:[%s]", field, message);
    }
}
