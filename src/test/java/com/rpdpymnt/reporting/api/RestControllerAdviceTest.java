package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.dto.ErrorDetails;
import com.rpdpymnt.reporting.exception.InvalidParameterException;
import com.rpdpymnt.reporting.util.ErrorDto;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RunWith(MockitoJUnitRunner.class)
public class RestControllerAdviceTest {

    private static final String ENTITY_EXIST = "Entity exist!";
    private static final String ENTITY_NOT_FOUND = "Entity not found!";
    private static final String INVALID_PARAMETER = "Invalid parameter!";
    private static final String ILLEGAL_ARGUMENT_PARAMETER = "'uriVariableValues' must not be null";
    private static final String ERROR_MESSAGE = "Fatal error: ";
    private static final String FIRST_FIELD = "firstField";
    private static final String FIRST_FIELD_MESSAGE = "firstFieldMessage";
    private static final String SECOND_FIELD = "secondField";
    private static final String SECOND_FIELD_MESSAGE = "secondFieldMessage";
    private static final String MESSAGE = "message";

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @InjectMocks
    private RestControllerAdvice advice;

    @SuppressWarnings("unchecked")
    @Test
    public void givenEntityExistExceptionWhenHandleEntityAlreadyExistsExceptionInvokedThenReturnResponseEntity()
            throws Exception {
        // Arrange
        EntityExistsException exception = new EntityExistsException(ENTITY_EXIST);

        // Act
        
        ResponseEntity<ErrorDetails> responseEntity = advice.handleEntityExistsException(exception);

        // Assert
        softly.assertThat(responseEntity.getBody().getMessage()).isEqualTo(ENTITY_EXIST);
        softly.assertThat(responseEntity.getBody().getCode()).isEqualTo(CONFLICT.value());
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(CONFLICT);
    }

    @Test
    public void givenEntityNotFoundExceptionWhenHandleEntityNotFoundExceptionInvokedThenReturnResponseEntity()
            throws Exception {
        // Arrange
        EntityNotFoundException exception = new EntityNotFoundException(ENTITY_NOT_FOUND);

        // Act
        @SuppressWarnings("unchecked")
        ResponseEntity<ErrorDetails> responseEntity = advice.handleEntityNotFoundException(exception);

        // Assert
        softly.assertThat(responseEntity.getBody().getMessage()).isEqualTo(ENTITY_NOT_FOUND);
        softly.assertThat(responseEntity.getBody().getCode()).isEqualTo(NOT_FOUND.value());
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void givenInvalidParameterExceptionWhenHandleInvalidParameterExceptionInvokedThenReturnResponseEntity()
            throws Exception {
        // Arrange
        InvalidParameterException exception = new InvalidParameterException(INVALID_PARAMETER);

        // Act
        @SuppressWarnings("unchecked")
        ResponseEntity<ErrorDetails> responseEntity = advice.handleInvalidParameterException(exception);

        // Assert
        softly.assertThat(responseEntity.getBody().getMessage()).isEqualTo(INVALID_PARAMETER);
        softly.assertThat(responseEntity.getBody().getCode()).isEqualTo(BAD_REQUEST.value());
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void givenIllegalArgumentExceptionWhenHandleIllegalArgumentExceptionInvokedThenReturnResponseEntity()
            throws Exception {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException(ILLEGAL_ARGUMENT_PARAMETER);

        // Act
        @SuppressWarnings("unchecked")
        ResponseEntity<ErrorDetails> responseEntity = advice.handleIllegalArgumentException(exception);

        // Assert
        softly.assertThat(responseEntity.getBody().getMessage()).isEqualTo(ILLEGAL_ARGUMENT_PARAMETER);
        softly.assertThat(responseEntity.getBody().getCode()).isEqualTo(BAD_REQUEST.value());
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void givenExceptionWhenHandleExceptionInvokedThenReturnResponseEntity() throws Exception {
        // Act
        ResponseEntity<ErrorDetails> responseEntity = advice.handleException(new Exception(ERROR_MESSAGE));

        // Assert
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        softly.assertThat(responseEntity.getBody().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        softly.assertThat(responseEntity.getBody().getMessage()).startsWith(ERROR_MESSAGE);
    }

    @Test
    public void givenAuthenticationExceptionWhenHandleAuthenticationExceptionInvokedThenReturnResponseEntity()
            throws Exception {
        // Act
        @SuppressWarnings("unchecked")
        ResponseEntity<ErrorDetails> responseEntity = advice
                .handleAuthenticationException(new BadCredentialsException(ERROR_MESSAGE));

        // Assert
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(UNAUTHORIZED);
        softly.assertThat(responseEntity.getBody().getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        softly.assertThat(responseEntity.getBody().getMessage()).startsWith(ERROR_MESSAGE);
    }

    @Test
    public void givenExceptionWhenHandleArgumentExceptionThenReturnBadRequest() throws Exception {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult binding = mock(BindingResult.class);
        ObjectError error = mock(ObjectError.class);
        when(error.getDefaultMessage()).thenReturn("Error Message");
        when(exception.getBindingResult()).thenReturn(binding);
        when(binding.getAllErrors()).thenReturn(Collections.singletonList(error));

        // Act
        ResponseEntity<ErrorDto> response = advice.handleArgumentException(exception);

        // Assert
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        softly.assertThat(response.getBody().getMessage()).startsWith("Validation failed");
    }

    @Test
    public void givenExceptionWithFieldErrorsWhenHandleArgumentExceptionThenReturnBadRequest() throws Exception {
        // Arrange
        FieldError firstFieldError = new FieldError(FIRST_FIELD, FIRST_FIELD, FIRST_FIELD_MESSAGE);
        FieldError secondFieldError = new FieldError(SECOND_FIELD, SECOND_FIELD, SECOND_FIELD_MESSAGE);
        List<FieldError> fieldErrorList = new ArrayList<>();
        fieldErrorList.add(firstFieldError);
        fieldErrorList.add(secondFieldError);
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult binding = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(binding);
        when(binding.getFieldErrors()).thenReturn(fieldErrorList);

        // Act
        ResponseEntity<ErrorDto> response = advice.handleArgumentException(exception);

        // Assert
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        softly.assertThat(response.getBody().getMessage()).startsWith("Validation failed");
        softly.assertThat(response.getBody().getValidationErrors().size()).isEqualTo(fieldErrorList.size());
        softly.assertThat(response.getBody().getValidationErrors().get(0).getField()).isEqualTo(FIRST_FIELD);
        softly.assertThat(response.getBody().getValidationErrors().get(0).getMessage()).isEqualTo(FIRST_FIELD_MESSAGE);
        softly.assertThat(response.getBody().getValidationErrors().get(1).getField()).isEqualTo(SECOND_FIELD);
        softly.assertThat(response.getBody().getValidationErrors().get(1).getMessage()).isEqualTo(SECOND_FIELD_MESSAGE);
    }

    @Test
    public void givenExceptionWhenHandleAccessDeniedExceptionThenReturnForbidden() {
        // Act
        @SuppressWarnings("rawtypes")
        ResponseEntity responseEntity = advice.handleAccessDeniedException(new AccessDeniedException(MESSAGE));

        // Assert
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        softly.assertThat(responseEntity.getBody()).isNull();
    }


}
