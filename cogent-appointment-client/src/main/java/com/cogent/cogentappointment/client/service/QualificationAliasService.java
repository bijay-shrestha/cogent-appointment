package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
public interface QualificationAliasService {

    List<DropDownResponseDTO> fetchActiveQualificationAlias();

    QualificationAlias fetchQualificationAliasById(Long id);
}
