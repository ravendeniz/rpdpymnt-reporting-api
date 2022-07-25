package com.rpdpymnt.reporting.util;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class ValidationErrorBuilderTest {

    private static final String DEFAULT_MESSAGE = "default message";
    private static final String OBJECT_NAME = "name";

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    public void givenResultWhenFromBindingErrorsThenErrorFieldsIsEmpty() {
        // Arrange
        final BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
        when(bindingResult.getAllErrors()).thenReturn(buildErrors());
        // Act
        final ErrorDto errorDto = ValidationErrorBuilder.fromBindingErrors(bindingResult);
        // Assert
        softly.assertThat(Integer.valueOf(errorDto.getCode())).isEqualTo(HttpStatus.BAD_REQUEST.value());
        softly.assertThat(errorDto.getValidationErrors().get(0).getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    public void givenResultWhenFromBindingErrorsThenErrorFieldsIsNotEmpty() {
        // Arrange
        final BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(buildFieldErrors());
        // Act
        final ErrorDto errorDto = ValidationErrorBuilder.fromBindingErrors(bindingResult);
        // Assert
        softly.assertThat(Integer.valueOf(errorDto.getCode())).isEqualTo(HttpStatus.BAD_REQUEST.value());
        softly.assertThat(errorDto.getValidationErrors().get(0).getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    public void givenErrorsWhenFromBindingErrorsThenReturnDefaultMessage() {
        // Arrange
        final Errors errors = Mockito.mock(Errors.class);
        when(errors.getFieldErrors()).thenReturn(buildFieldErrors());
        // Act
        final ErrorDto errorDto = ValidationErrorBuilder.fromBindingErrors(errors);
        // Assert
        softly.assertThat(errorDto.getMessage()).isEqualTo(ValidationErrorBuilder.REQUIRED_PARAMETERS);
        softly.assertThat(errorDto.getValidationErrors().get(0).getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    private static List<FieldError> buildFieldErrors() {
        final List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("name", "field", DEFAULT_MESSAGE));
        return errors;
    }

    private static List<ObjectError> buildErrors() {
        final List<ObjectError> errors = new ArrayList<>();
        errors.add(new ObjectError(OBJECT_NAME, DEFAULT_MESSAGE));
        return errors;
    }

}
