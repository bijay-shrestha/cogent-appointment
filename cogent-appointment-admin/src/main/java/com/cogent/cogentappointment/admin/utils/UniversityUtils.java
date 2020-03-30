package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Country;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.University;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;

/**
 * @author smriti ON 31/01/2020
 */
public class UniversityUtils {

    public static University parseToUniversity(UniversityRequestDTO requestDTO,
                                               Country country,
                                               Hospital hospital) {

        University university = new University();
        university.setName(convertToNormalCase(requestDTO.getName()));
        university.setAddress(requestDTO.getAddress());
        university.setStatus(requestDTO.getStatus());
        university.setCountry(country);
        university.setHospital(hospital);
        return university;
    }

    public static void parseToUpdatedUniversity(UniversityUpdateRequestDTO requestDTO,
                                                Country country,
                                                Hospital hospital,
                                                University university) {

        university.setName(convertToNormalCase(requestDTO.getName()));
        university.setAddress(requestDTO.getAddress());
        university.setCountry(country);
        university.setHospital(hospital);
        university.setStatus(requestDTO.getStatus());
        university.setRemarks(requestDTO.getRemarks());
    }

    public static void parseToDeletedUniversity(University university, DeleteRequestDTO deleteRequestDTO) {
        university.setStatus(deleteRequestDTO.getStatus());
        university.setRemarks(deleteRequestDTO.getRemarks());
    }
}
