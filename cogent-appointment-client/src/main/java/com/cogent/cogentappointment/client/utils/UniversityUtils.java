package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversityUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Country;
import com.cogent.cogentappointment.persistence.model.University;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 31/01/2020
 */
public class UniversityUtils {

    public static University parseToUniversity(UniversityRequestDTO requestDTO,
                                               Country country) {

        University university = new University();
        university.setName(toNormalCase(requestDTO.getName()));
        university.setAddress(requestDTO.getAddress());
        university.setStatus(requestDTO.getStatus());
        university.setCountry(country);
        return university;
    }

    public static void parseToUpdatedUniversity(UniversityUpdateRequestDTO requestDTO,
                                                Country country,
                                                University university) {
        university.setName(toNormalCase(requestDTO.getName()));
        university.setAddress(requestDTO.getAddress());
        university.setStatus(requestDTO.getStatus());
        university.setRemarks(requestDTO.getRemarks());
        university.setCountry(country);
    }

    public static void parseToDeletedUniversity(University university,
                                                DeleteRequestDTO deleteRequestDTO) {
        university.setStatus(deleteRequestDTO.getStatus());
        university.setRemarks(deleteRequestDTO.getRemarks());
    }
}
