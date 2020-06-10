package com.cogent.cogentappointment.thirdparty.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.thirdparty.exception.utils.ExceptionUtils.generateMessage;
import static com.cogent.cogentappointment.thirdparty.exception.utils.ExceptionUtils.getLocalDateTime;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class InternalServerErrorException extends RuntimeException {

    private ExceptionResponse exception;

    public InternalServerErrorException(Class clazz, String debugMessage) {
        super(generateMessage(clazz));
        exception = ExceptionResponse.builder()
                .errorMessage(generateMessage(clazz))
                .debugMessage(debugMessage)
                .responseStatus(INTERNAL_SERVER_ERROR)
                .responseCode(INTERNAL_SERVER_ERROR.value())
                .timeStamp(getLocalDateTime())
                .build();

    }

}
