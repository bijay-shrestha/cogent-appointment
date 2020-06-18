package com.cogent.cogentappointment.commons.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.commons.exception.utils.ExceptionUtils.getLocalDateTime;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class OperationUnsuccessfulException extends RuntimeException {
    private ExceptionResponse exception;

    public OperationUnsuccessfulException(String errorMessage) {
        super(errorMessage);
        setErrorResponse(errorMessage, errorMessage);
    }

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .responseStatus(EXPECTATION_FAILED)
                .responseCode(EXPECTATION_FAILED.value())
                .timeStamp(getLocalDateTime())
                .build();
    }
}
