package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.model.Country;
import com.cogent.cogentappointment.admin.model.Qualification;
import com.cogent.cogentappointment.admin.model.QualificationAlias;
import com.cogent.cogentappointment.admin.utils.commons.StringUtil;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;


/**
 * @author smriti on 11/11/2019
 */
public class QualificationUtils {

    public static Qualification parseToQualification(QualificationRequestDTO requestDTO,
                                                     Country country,
                                                     QualificationAlias qualificationAlias) {
        Qualification qualification = new Qualification();
        qualification.setName(StringUtil.toUpperCase(requestDTO.getName()));
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
        qualification.setName(StringUtil.toUpperCase(requestDTO.getName()));
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
