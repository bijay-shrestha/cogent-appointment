package com.cogent.cogentappointment.admin.exception;

import com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils;
import lombok.Getter;

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
                .status(EXPECTATION_FAILED)
                .timeStamp(ExceptionUtils.getLocalDateTime())
                .build();
    }
}
