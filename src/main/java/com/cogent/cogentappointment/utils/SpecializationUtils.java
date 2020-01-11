package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.model.Specialization;

import static com.cogent.cogentappointment.utils.commons.RandomNumberGenerator.generateRandomNumber;
import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti on 2019-09-25
 */
public class SpecializationUtils {
    public static Specialization parseToSpecialization(SpecializationRequestDTO requestDTO) {

        Specialization specialization = new Specialization();
        specialization.setName(toUpperCase(requestDTO.getName()));
        specialization.setCode(String.valueOf(generateRandomNumber(3)));
        specialization.setStatus(requestDTO.getStatus());
        return specialization;
    }

    public static Specialization parseToUpdatedSpecialization(SpecializationUpdateRequestDTO updateRequestDTO,
                                                              Specialization specialization) {

        specialization.setName(toUpperCase(updateRequestDTO.getName()));
        specialization.setStatus(updateRequestDTO.getStatus());
        specialization.setRemarks(updateRequestDTO.getRemarks());
        return specialization;
    }

    public static Specialization parseToDeletedSpecialization(Specialization specialization,
                                                              DeleteRequestDTO deleteRequestDTO) {

        specialization.setStatus(deleteRequestDTO.getStatus());
        specialization.setRemarks(deleteRequestDTO.getRemarks());
        return specialization;
    }
}
