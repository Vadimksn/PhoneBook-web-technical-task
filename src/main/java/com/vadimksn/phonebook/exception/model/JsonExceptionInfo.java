package com.vadimksn.phonebook.exception.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vadimksn.phonebook.exception.model.base.GenericException;
import com.vadimksn.phonebook.utils.DateTimeToJsonIso;
import com.vadimksn.phonebook.utils.DateUtils;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude
public class JsonExceptionInfo {
    @JsonSerialize(using = DateTimeToJsonIso.class)
    private final LocalDateTime timestamp = DateUtils.nowUtc();
    private final String error;
    private final String message;
    private final String description;
    private final Object details;

    public JsonExceptionInfo(GenericException e) {
        error = e.getErrorReason().name();
        message = e.getFullMessage();
        description = e.getErrorReason().getDescription();
        details = e.errorDetails;
    }
}
