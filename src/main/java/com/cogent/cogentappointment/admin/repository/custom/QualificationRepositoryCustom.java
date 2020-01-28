package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationResponseDTO;
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

    List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable);

    QualificationResponseDTO fetchDetailsById(Long id);

    List<QualificationDropdownDTO> fetchActiveQualificationForDropDown();

}
