package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.model.QualificationAlias;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
public interface QualificationAliasService {

    List<DropDownResponseDTO> fetchActiveQualificationAlias();

    QualificationAlias fetchQualificationAliasById(Long id);
}
