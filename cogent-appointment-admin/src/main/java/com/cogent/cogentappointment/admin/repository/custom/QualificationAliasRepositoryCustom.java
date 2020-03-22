package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.qualificationAlias.QualificationAliasSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualificationAlias.QualificationAliasMinimalResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
@Repository
@Qualifier("qualificationAliasRepositoryCustom")
public interface QualificationAliasRepositoryCustom {
    List<DropDownResponseDTO> fetchActiveQualificationAlias();

    Long validateDuplicity(String name);

    Long validateDuplicity(Long id, String name);

    List<QualificationAliasMinimalResponseDTO> search(QualificationAliasSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable);
}
