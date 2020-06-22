package com.cogent.cogentappointment.commons.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.cogent.cogentappointment.commons.exception.utils.ExceptionUtils.generateMessage;
import static com.cogent.cogentappointment.commons.exception.utils.ExceptionUtils.getLocalDateTime;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author smriti on 7/2/19
 */
@Getter
@ResponseStatus(value= UNAUTHORIZED)
public class UnauthorisedException extends RuntimeException {

    private ExceptionResponse exception;

    public UnauthorisedException(Class clazz, String debugMessage) {
        super(generateMessage(clazz));
        exception = ExceptionResponse.builder()
                .errorMessage(generateMessage(clazz))
                .debugMessage(debugMessage)
                .responseStatus(UNAUTHORIZED)
                .responseCode(UNAUTHORIZED.value())
                .timeStamp(getLocalDateTime())
                .build();

    }


}
