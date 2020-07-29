package com.cogent.cogentappointment.logging.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.logging.exception.utils.ExceptionUtils.getLocalDateTime;
import static com.cogent.cogentappointment.logging.exception.utils.ExceptionUtils.toMap;
import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class DataDuplicationException extends RuntimeException {

    private ExceptionResponse exception;

    public DataDuplicationException(String errorMessage) {
        setErrorResponse(errorMessage, errorMessage);
    }

    public DataDuplicationException(String errorMessage, String... searchParamsMap) {
        String debugMessage = "Duplicate entries with " + toMap(String.class, String.class, searchParamsMap);
        setErrorResponse(errorMessage, debugMessage);
    }

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .responseStatus(CONFLICT)
                .responseCode(CONFLICT.value())
                .timeStamp(getLocalDateTime())
                .build();
    }

}
