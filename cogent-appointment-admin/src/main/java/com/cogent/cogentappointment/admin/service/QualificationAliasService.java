package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
public interface QualificationAliasService {

    List<DropDownResponseDTO> fetchActiveQualificationAlias();

    QualificationAlias fetchQualificationAliasById(Long id);
}
