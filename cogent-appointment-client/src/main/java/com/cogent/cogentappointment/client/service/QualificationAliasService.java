package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.qualificationAlias.QualificationAliasMinimalResponseDTO;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
public interface QualificationAliasService {

    void save(QualificationAliasRequestDTO requestDTO);

    void update(QualificationAliasUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DropDownResponseDTO> fetchActiveQualificationAlias();

    QualificationAlias fetchQualificationAliasById(Long id);

    List<QualificationAliasMinimalResponseDTO> search(QualificationAliasSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable);
}
