package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
@Repository
@Qualifier("qualificationAliasRepositoryCustom")
public interface QualificationAliasRepositoryCustom {
    List<DropDownResponseDTO> fetchActiveQualificationAlias();
}
