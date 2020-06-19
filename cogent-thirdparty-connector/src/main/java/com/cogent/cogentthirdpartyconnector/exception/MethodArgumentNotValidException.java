package com.cogent.cogentthirdpartyconnector.exception;

import lombok.Getter;

import static com.cogent.cogentthirdpartyconnector.exception.utils.ExceptionUtils.getLocalDateTime;
import static com.cogent.cogentthirdpartyconnector.exception.utils.ValidationUtils.getExceptionForMethodArgumentNotValid;
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

        String errorMessage = getExceptionForMethodArgumentNotValid(ex);

        return ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(errorMessage)
                .responseStatus(BAD_REQUEST)
                .responseCode(BAD_REQUEST.value())
                .timeStamp(getLocalDateTime())
                .build();
    }
}
