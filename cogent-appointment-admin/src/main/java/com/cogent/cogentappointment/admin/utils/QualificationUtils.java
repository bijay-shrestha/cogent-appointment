package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.model.Country;
import com.cogent.cogentappointment.admin.model.Qualification;
import com.cogent.cogentappointment.admin.model.QualificationAlias;
import com.cogent.cogentappointment.admin.model.University;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.toUpperCase;


/**
 * @author smriti on 11/11/2019
 */
public class QualificationUtils {

    public static Qualification parseToQualification(QualificationRequestDTO requestDTO,
                                                     Country country,
                                                     QualificationAlias qualificationAlias,
                                                     University university) {
        Qualification qualification = new Qualification();
        qualification.setName(toUpperCase(requestDTO.getName()));
        qualification.setStatus(requestDTO.getStatus());
        parseToQualification(qualification, country, qualificationAlias, university);
        return qualification;
    }

    private static void parseToQualification(Qualification qualification,
                                             Country country,
                                             QualificationAlias qualificationAlias,
                                             University university) {
        qualification.setCountry(country);
        qualification.setQualificationAlias(qualificationAlias);
        qualification.setUniversity(university);
    }

    public static void parseToUpdatedQualification(QualificationUpdateRequestDTO requestDTO,
                                                   Country country,
                                                   QualificationAlias qualificationAlias,
                                                   University university,
                                                   Qualification qualification) {
        qualification.setName(toUpperCase(requestDTO.getName()));
        qualification.setStatus(requestDTO.getStatus());
        qualification.setRemarks(requestDTO.getRemarks());
        parseToQualification(qualification, country, qualificationAlias, university);
    }

    public static void parseToDeletedQualification(Qualification qualification,
                                                   DeleteRequestDTO deleteRequestDTO) {
        qualification.setStatus(deleteRequestDTO.getStatus());
        qualification.setRemarks(deleteRequestDTO.getRemarks());
    }
}
