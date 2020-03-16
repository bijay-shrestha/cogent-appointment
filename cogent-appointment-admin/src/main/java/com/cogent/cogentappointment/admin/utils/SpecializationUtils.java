package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti on 2019-09-25
 */
public class SpecializationUtils {
    public static Specialization parseToSpecialization(SpecializationRequestDTO requestDTO,
                                                       Hospital hospital) {

        Specialization specialization = new Specialization();
        specialization.setName(convertToNormalCase(requestDTO.getName()));
        specialization.setCode(toUpperCase(requestDTO.getCode()));
        specialization.setStatus(requestDTO.getStatus());
        specialization.setHospital(hospital);
        return specialization;
    }

    public static void parseToUpdatedSpecialization(SpecializationUpdateRequestDTO updateRequestDTO,
                                                    Specialization specialization,
                                                    Hospital hospital) {

        specialization.setName(convertToNormalCase(updateRequestDTO.getName()));
        specialization.setStatus(updateRequestDTO.getStatus());
        specialization.setRemarks(updateRequestDTO.getRemarks());
        specialization.setCode(toUpperCase(updateRequestDTO.getCode()));
        specialization.setHospital(hospital);
    }

    public static Specialization parseToDeletedSpecialization(Specialization specialization,
                                                              DeleteRequestDTO deleteRequestDTO) {

        specialization.setStatus(deleteRequestDTO.getStatus());
        specialization.setRemarks(deleteRequestDTO.getRemarks());
        return specialization;
    }
}
