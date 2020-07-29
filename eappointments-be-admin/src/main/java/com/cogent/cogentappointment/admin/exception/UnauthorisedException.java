package com.cogent.cogentappointment.admin.exception;

import com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

import static com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils.generateDebugMessage;
import static com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils.generateMessage;
import static com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils.getLocalDateTime;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author smriti on 7/2/19
 */
@Getter
@ResponseStatus(value= UNAUTHORIZED)
public class UnauthorisedException extends RuntimeException {


    private ExceptionResponse exception;

    public UnauthorisedException(Class clazz) {
        super(generateMessage(clazz));
        setErrorResponse(generateMessage(clazz), generateDebugMessage(clazz));
    }

    public UnauthorisedException(String errorMessage) {
        setErrorResponse(errorMessage, errorMessage);
    }

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

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .responseStatus(UNAUTHORIZED)
                .responseCode(UNAUTHORIZED.value())
                .timeStamp(getLocalDateTime())
                .build();
    }

}
