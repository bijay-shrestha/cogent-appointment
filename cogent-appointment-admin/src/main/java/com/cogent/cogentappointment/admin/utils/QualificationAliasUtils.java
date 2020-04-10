package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualificationAlias.QualificationAliasRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualificationAlias.QualificationAliasUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;

/**
 * @author Rupak
 */
public class QualificationAliasUtils {

    public static QualificationAlias parseToQualificationAlias(QualificationAliasRequestDTO requestDTO) {
        QualificationAlias qualificationAlias = new QualificationAlias();
        qualificationAlias.setName(convertToNormalCase(requestDTO.getName()));
        qualificationAlias.setStatus(requestDTO.getStatus());
        return qualificationAlias;
    }

    public static void parseToUpdatedQualificationAlias(QualificationAlias qualificationAlias,
                                                        QualificationAliasUpdateRequestDTO requestDTO) {

        qualificationAlias.setName(convertToNormalCase(requestDTO.getName()));
        qualificationAlias.setStatus(requestDTO.getStatus());
        qualificationAlias.setRemarks(requestDTO.getRemarks());
    }

    public static void parseToDeletedQualification(QualificationAlias qualificationAlias,
                                                   DeleteRequestDTO deleteRequestDTO) {

        qualificationAlias.setStatus(deleteRequestDTO.getStatus());
        qualificationAlias.setRemarks(deleteRequestDTO.getRemarks());

    }
}
