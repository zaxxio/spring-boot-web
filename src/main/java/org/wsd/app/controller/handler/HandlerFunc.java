package org.wsd.app.controller.handler;

import org.apache.kafka.common.KafkaException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wsd.app.payload.Payload;
import org.wsd.app.security.auth.impl.TwoFactorFailedException;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@RestControllerAdvice
public class HandlerFunc {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Payload<Map<String, String>>> handleValidationException(MethodArgumentNotValidException exception) {
        final Map<String, String> errors = new HashMap<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }

        final Payload<Map<String, String>> payload = new Payload.Builder<Map<String, String>>()
                .message("Request validation failed.")
                .payload(errors)
                .build();

        return new ResponseEntity<>(payload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataAccessException.class, CannotCreateTransactionException.class, KafkaException.class})
    public ResponseEntity<Payload<?>> handleDatabaseException(Exception e) {
        log.error("Database access error: ", e);
        final Payload<?> responsePayload = new Payload.Builder<>()
                .message("Service temporarily unavailable due to a database issue. Please try again later." + e.getMessage())
                .build();
        return new ResponseEntity<>(responsePayload, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Payload<?>> userNotFound(UsernameNotFoundException e) {
        log.error(e.getMessage());
        final Payload<?> responsePayload = new Payload.Builder<>()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(responsePayload, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Payload<?>> userNotFound(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        final Payload<?> responsePayload = new Payload.Builder<>()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({TwoFactorFailedException.class})
    public ResponseEntity<Payload<?>> twoFactorFailed(Exception e) {
        log.error(e.getMessage());
        final Payload<?> responsePayload = new Payload.Builder<>()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(responsePayload, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
