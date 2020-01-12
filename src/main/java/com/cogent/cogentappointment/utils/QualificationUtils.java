package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.model.Country;
import com.cogent.cogentappointment.model.Qualification;
import com.cogent.cogentappointment.model.QualificationAlias;

import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;


/**
 * @author smriti on 11/11/2019
 */
public class QualificationUtils {

    public static Qualification parseToQualification(QualificationRequestDTO requestDTO,
                                                     Country country,
                                                     QualificationAlias qualificationAlias) {
        Qualification qualification = new Qualification();
        qualification.setName(toUpperCase(requestDTO.getName()));
        qualification.setUniversity(requestDTO.getUniversity());
        qualification.setStatus(requestDTO.getStatus());
        parseToQualification(qualification, country, qualificationAlias);
        return qualification;
    }

    private static void parseToQualification(Qualification qualification,
                                             Country country,
                                             QualificationAlias qualificationAlias) {
        qualification.setCountry(country);
        qualification.setQualificationAlias(qualificationAlias);
    }

    public static void parseToUpdatedQualification(QualificationUpdateRequestDTO requestDTO,
                                                   Country country,
                                                   QualificationAlias qualificationAlias,
                                                   Qualification qualification) {
        qualification.setName(toUpperCase(requestDTO.getName()));
        qualification.setUniversity(requestDTO.getUniversity());
        qualification.setStatus(requestDTO.getStatus());
        qualification.setRemarks(requestDTO.getRemarks());
        parseToQualification(qualification, country, qualificationAlias);
    }

    public static void parseToDeletedQualification(Qualification qualification,
                                                   DeleteRequestDTO deleteRequestDTO) {
        qualification.setStatus(deleteRequestDTO.getStatus());
        qualification.setRemarks(deleteRequestDTO.getRemarks());
    }
}
