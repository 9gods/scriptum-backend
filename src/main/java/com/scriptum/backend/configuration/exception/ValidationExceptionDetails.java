package com.scriptum.backend.configuration.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ValidationExceptionDetails extends ExceptionDetails {

    private final String fields;

    private final String fieldsMessage;

}
