package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityUpdateRequestDTO;
import com.cogent.cogentappointment.admin.model.Country;
import com.cogent.cogentappointment.admin.model.University;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 31/01/2020
 */
public class UniversityUtils {

    public static University parseToUniversity(UniversityRequestDTO requestDTO,
                                               Country country) {

        University university = new University();
        university.setName(toUpperCase(requestDTO.getName()));
        university.setAddress(requestDTO.getAddress());
        university.setStatus(requestDTO.getStatus());
        university.setCountry(country);
        return university;
    }

    public static void parseToUpdatedUniversity(UniversityUpdateRequestDTO requestDTO,
                                                Country country,
                                                University university) {
        university.setName(toUpperCase(requestDTO.getName()));
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
