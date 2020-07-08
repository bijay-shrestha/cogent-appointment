package com.cogent.cogentappointment.esewa.utils.commons;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author smriti on 2019-08-27
 */
public class ObjectMapperUtils {

    public static <T> T map(String source, Class<T> destination) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(source, destination);
    }

    public static <T> T convertValue(Object source, Class<T> destination) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(source, destination);
    }

}
