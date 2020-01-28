package com.cogent.cogentappointment.admin.exception;

import com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils;
import lombok.Getter;
import org.springframework.util.StringUtils;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class NoContentFoundException extends RuntimeException {

    private ExceptionResponse exception;

    public NoContentFoundException(Class clazz) {
        super(ExceptionUtils.generateMessage(clazz));
        setErrorResponse(ExceptionUtils.generateMessage(clazz), ExceptionUtils.generateDebugMessage(clazz));
    }

    public NoContentFoundException(String errorMessage) {
        setErrorResponse(errorMessage, errorMessage);
    }

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .status(NOT_FOUND)
                .timeStamp(ExceptionUtils.getLocalDateTime())
                .build();
    }

    public NoContentFoundException(Class clazz, String... searchParamsMap) {
        super(ExceptionUtils.generateMessage(clazz.getSimpleName(), ExceptionUtils.toMap(String.class, String.class, searchParamsMap)));
        setErrorResponse(
                ExceptionUtils.generateMessage(clazz),
                StringUtils.capitalize("Object returned empty or null for ")
                        + ExceptionUtils.toMap(String.class, String.class, searchParamsMap));
    }

    public NoContentFoundException(String errorMessage, String... searchParamsMap) {
        super(ExceptionUtils.generateMessage(errorMessage, ExceptionUtils.toMap(String.class, String.class, searchParamsMap)));
        setErrorResponse(errorMessage,
                StringUtils.capitalize("Object returned empty or null for ")
                        + ExceptionUtils.toMap(String.class, String.class, searchParamsMap));
    }
}
