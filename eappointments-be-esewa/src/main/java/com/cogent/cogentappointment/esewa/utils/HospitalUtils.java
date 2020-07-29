package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTOWithStatus;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

public class HospitalUtils {

    public static HospitalMinResponseDTOWithStatus parseToHospitalMinResponseDTOWithStatus(
            List<HospitalMinResponseDTO> responseDTO) {

        return HospitalMinResponseDTOWithStatus.builder()
                .hospitalMinResponseDTOS(responseDTO)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }
}
