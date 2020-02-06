package com.cogent.cogentappointment.admin.exception;

import com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils;
import lombok.Getter;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class InternalServerErrorException extends RuntimeException {

    private ExceptionResponse exception;

    public InternalServerErrorException(Class clazz, String debugMessage) {
        super(ExceptionUtils.generateMessage(clazz));
        exception = ExceptionResponse.builder()
                .errorMessage(ExceptionUtils.generateMessage(clazz))
                .debugMessage(debugMessage)
                .status(INTERNAL_SERVER_ERROR)
                .timeStamp(ExceptionUtils.getLocalDateTime())
                .build();

    }

}
