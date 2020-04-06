package com.cogent.cogentappointment.logging.exception;

import com.cogent.cogentappointment.logging.exception.utils.ExceptionUtils;
import lombok.Getter;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class UnauthorisedException extends RuntimeException {

    private ExceptionResponse exception;

    public UnauthorisedException(Class clazz, String debugMessage) {
        super(ExceptionUtils.generateMessage(clazz));
        exception = ExceptionResponse.builder()
                .errorMessage(ExceptionUtils.generateMessage(clazz))
                .debugMessage(debugMessage)
                .responseStatus(UNAUTHORIZED)
                .responseCode(UNAUTHORIZED.value())
                .timeStamp(ExceptionUtils.getLocalDateTime())
                .build();

    }

}
