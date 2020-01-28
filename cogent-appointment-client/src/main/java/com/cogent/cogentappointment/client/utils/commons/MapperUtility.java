package com.cogent.cogentappointment.client.utils.commons;

import org.modelmapper.ModelMapper;

/**
 * @author smriti on 7/5/19
 */
public class MapperUtility {
    public static <T> T map(Object source, Class<T> destination) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destination);
    }
}


