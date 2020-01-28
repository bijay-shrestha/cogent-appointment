package com.cogent.cogentappointment.admin.exception;

import com.cogent.cogentappointment.admin.exception.utils.ExceptionUtils;
import com.cogent.cogentappointment.admin.exception.utils.ValidationUtils;
import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author smriti on 2019-09-10
 * <p>
 * MethodArgumentNotValidException to be thrown when validation on an argument annotated with {@code @Valid}
 * (request dtos) fails on Controller class
 */
@Getter
public class MethodArgumentNotValidException extends RuntimeException {

    public static ExceptionResponse handleMethodArgumentNotValidException(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        String errorMessage = ValidationUtils.getExceptionForMethodArgumentNotValid(ex);

        return ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(errorMessage)
                .status(BAD_REQUEST)
                .timeStamp(ExceptionUtils.getLocalDateTime())
                .build();
    }
}
