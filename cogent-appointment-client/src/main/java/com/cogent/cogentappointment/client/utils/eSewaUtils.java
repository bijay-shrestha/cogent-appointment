package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.eSewa.AvailableDoctorResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author smriti on 15/03/20
 */
public class eSewaUtils {

    public static List<AvailableDoctorResponseDTO> mergeOverrideAndActualDoctorList(
            List<AvailableDoctorResponseDTO> overrideList,
            List<AvailableDoctorResponseDTO> actualList) {

        List<AvailableDoctorResponseDTO> unmatchedList = actualList.stream()
                .filter(actual -> (overrideList.stream()
                        .filter(override -> (override.getDoctorId().equals(actual.getDoctorId()))
                                && (override.getSpecializationId().equals(actual.getSpecializationId()))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        overrideList.addAll(unmatchedList);

        return overrideList;
    }
}


