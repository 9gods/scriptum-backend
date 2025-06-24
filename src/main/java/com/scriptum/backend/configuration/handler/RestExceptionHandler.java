package com.scriptum.backend.configuration.handler;

import com.scriptum.backend.configuration.exception.BadRequestException;
import com.scriptum.backend.configuration.exception.BadRequestExceptionDetails;
import com.scriptum.backend.configuration.exception.ExceptionDetails;
import com.scriptum.backend.configuration.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException badRequestException) {
        log.error("{} - {}", badRequestException.getClass().getName(), badRequestException.getMessage());

        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Check the Documentation")
                        .details(badRequestException.getMessage() == null ? "" : badRequestException.getMessage())
                        .developerMessage(badRequestException.getClass().getName() == null ? "" : badRequestException.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        log.error("{} - {}", exception.getClass().getName(), exception.getMessage());

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));

        String fieldsMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Invalid fields")
                        .details("Check the field(s) error")
                        .developerMessage(exception.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        log.error("{} - {}", ex.getClass().getName(), ex.getMessage());

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .title(ex.getCause() == null ? "" : ex.getCause().getMessage())
                .details(ex.getMessage() == null ? "" : ex.getMessage())
                .developerMessage(ex.getClass().getName() == null ? "" : ex.getClass().getName())
                .build();

        return new ResponseEntity<>(exceptionDetails, headers, status);
    }

}
