package com.cogent.cogentappointment.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.exception.utils.ExceptionUtils.getLocalDateTime;
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

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .status(CONFLICT)
                .timeStamp(getLocalDateTime())
                .build();
    }

}
