package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.dto.BadRequestDetails;
import com.rpdpymnt.reporting.dto.ErrorDetails;
import com.rpdpymnt.reporting.exception.ApiException;
import com.rpdpymnt.reporting.exception.InvalidParameterException;
import com.rpdpymnt.reporting.exception.InvalidPreconditionException;
import com.rpdpymnt.reporting.util.ErrorDto;
import com.rpdpymnt.reporting.util.ValidationErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@ControllerAdvice
public class RestControllerAdvice {

    private static final String COLON = " : ";
    private static final String ERROR_MESSAGE = " - Fatal error: ";
    private static final int SB_INITIAL_CAPACITY = 128;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(INTERNAL_SERVER_ERROR.value(), getMessage(exception) + ERROR_MESSAGE + uuid));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDetails> handleIOException(IOException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(INTERNAL_SERVER_ERROR.value(), ERROR_MESSAGE + uuid));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity handleEntityExistsException(EntityExistsException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(CONFLICT.value(), getMessage(exception)));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(NOT_FOUND.value(), getMessage(exception)));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity handleInvalidParameterException(InvalidParameterException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(BAD_REQUEST.value(), getMessage(exception)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(BAD_REQUEST.value(), getMessage(exception)));
    }
    
    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(BAD_REQUEST.value(), getMessage(exception)));
    }

    @ExceptionHandler(InvalidPreconditionException.class)
    public ResponseEntity handleInvalidPreconditionException(InvalidPreconditionException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(BAD_REQUEST.value(), getMessage(exception)));
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleAuthenticationException(AuthenticationException exception) {
        String uuid = UUID.randomUUID().toString();
        log.error(uuid + COLON + getMessage(exception), exception);
        return errorResponse(new ErrorDetails(UNAUTHORIZED.value(), getMessage(exception)));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        log.error(getMessage(exception), exception);
        return ResponseEntity.status(FORBIDDEN).build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BadRequestDetails> handleConstraintException(ConstraintViolationException exception) {
        log.error(getMessage(exception), exception);
        StringBuilder message = new StringBuilder(SB_INITIAL_CAPACITY);

        exception.getConstraintViolations().forEach(env -> {
            message.append("Constraint Violation: Property[").append(env.getPropertyPath()).append("] Value[")
                    .append(env.getInvalidValue()).append("] Description[").append(env.getMessage()).append("]\n");
        });
        message.setLength(Math.max(message.length() - 1, 0));
        return new ResponseEntity<>(new BadRequestDetails(message.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleArgumentException(MethodArgumentNotValidException exception) {
        log.error(getMessage(exception), exception);
        return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(exception.getBindingResult()));
    }

    private String getMessage(Exception exception) {
        return StringUtils.defaultString(exception.getMessage());
    }

    private ResponseEntity<ErrorDetails> errorResponse(ErrorDetails error) {
        log.error(error.getMessage());
        return ResponseEntity.status(error.getCode()).contentType(APPLICATION_JSON).body(error);
    }
}
