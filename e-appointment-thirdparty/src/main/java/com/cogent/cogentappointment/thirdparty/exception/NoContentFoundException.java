package com.cogent.cogentappointment.thirdparty.exception;

import lombok.Getter;
import org.springframework.util.StringUtils;

import static com.cogent.cogentappointment.thirdparty.exception.utils.ExceptionUtils.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class NoContentFoundException extends RuntimeException {

    private ExceptionResponse exception;

    public NoContentFoundException(Class clazz) {
        super(generateMessage(clazz));
        setErrorResponse(generateMessage(clazz), generateDebugMessage(clazz));
    }

    public NoContentFoundException(String errorMessage) {
        setErrorResponse(errorMessage, errorMessage);
    }

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .responseStatus(NOT_FOUND)
                .responseCode(NOT_FOUND.value())
                .timeStamp(getLocalDateTime())
                .build();
    }

    public NoContentFoundException(Class clazz, String... searchParamsMap) {
        super(generateMessage(clazz.getSimpleName(), toMap(String.class, String.class, searchParamsMap)));
        setErrorResponse(
                generateMessage(clazz),
                StringUtils.capitalize("Object returned empty or null for ")
                        + toMap(String.class, String.class, searchParamsMap));
    }

    public NoContentFoundException(String errorMessage, String... searchParamsMap) {
        super(generateMessage(errorMessage, toMap(String.class, String.class, searchParamsMap)));
        setErrorResponse(errorMessage,
                StringUtils.capitalize("Object returned empty or null for ")
                        + toMap(String.class, String.class, searchParamsMap));
    }
}
