package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti on 2019-09-25
 */
public class SpecializationUtils {
    public static Specialization parseToSpecialization(SpecializationRequestDTO requestDTO, Hospital hospital) {

        Specialization specialization = new Specialization();
        specialization.setName(toNormalCase(requestDTO.getName()));
        specialization.setCode(toUpperCase(requestDTO.getCode()));
        specialization.setStatus(requestDTO.getStatus());
        specialization.setHospital(hospital);
        return specialization;
    }

    public static void parseToUpdatedSpecialization(SpecializationUpdateRequestDTO updateRequestDTO,
                                                    Specialization specialization) {

        specialization.setName(toNormalCase(updateRequestDTO.getName()));
        specialization.setStatus(updateRequestDTO.getStatus());
        specialization.setRemarks(updateRequestDTO.getRemarks());
    }

    public static void parseToDeletedSpecialization(Specialization specialization,
                                                    DeleteRequestDTO deleteRequestDTO) {

        specialization.setStatus(deleteRequestDTO.getStatus());
        specialization.setRemarks(deleteRequestDTO.getRemarks());
    }
}
