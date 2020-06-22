package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;

/**
 * @author Rupak
 */
public class QualificationAliasUtils {

    public static QualificationAlias parseToQualificationAlias(QualificationAliasRequestDTO requestDTO) {
        QualificationAlias qualificationAlias = new QualificationAlias();
        qualificationAlias.setName(requestDTO.getName());
        qualificationAlias.setStatus(requestDTO.getStatus());
        return qualificationAlias;
    }

    public static QualificationAlias parseToUpdatedQualificationAlias(QualificationAlias qualificationAlias,
                                                                      QualificationAliasUpdateRequestDTO requestDTO) {

        qualificationAlias.setName(requestDTO.getName());
        qualificationAlias.setStatus(requestDTO.getStatus());
        qualificationAlias.setRemarks(requestDTO.getRemarks());
        return qualificationAlias;
    }

    public static void parseToDeletedQualification(QualificationAlias qualificationAlias,
                                                   DeleteRequestDTO deleteRequestDTO) {

        qualificationAlias.setStatus(deleteRequestDTO.getStatus());
        qualificationAlias.setRemarks(deleteRequestDTO.getRemarks());

    }
}
