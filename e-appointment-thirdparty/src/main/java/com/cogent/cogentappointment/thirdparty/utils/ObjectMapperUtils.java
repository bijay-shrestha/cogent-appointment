package com.cogent.cogentappointment.thirdparty.utils;

import com.cogent.cogentappointment.persistence.model.ThirdPartyInfo;
import com.cogent.cogentappointment.thirdparty.dto.request.CheckInRequestDTO;
import org.modelmapper.ModelMapper;

public class ObjectMapperUtils {

    public static ThirdPartyInfo convertToEntity(ModelMapper modalMapper,
                                                 CheckInRequestDTO checkInRequestDTO) {
        return modalMapper.map(checkInRequestDTO, ThirdPartyInfo.class);
    }
}
