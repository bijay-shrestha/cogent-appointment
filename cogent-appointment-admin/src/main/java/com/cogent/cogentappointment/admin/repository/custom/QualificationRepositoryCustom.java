package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
@Repository
@Qualifier("qualificationRepositoryCustom")
public interface QualificationRepositoryCustom {

    Long validateDuplicity(String name);

    Long validateDuplicity(Long id, String name);

    List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable);

    QualificationResponseDTO fetchDetailsById(Long id);

    List<QualificationDropdownDTO> fetchActiveQualificationForDropDown();

    List<DropDownResponseDTO> fetchMinQualification();

}
