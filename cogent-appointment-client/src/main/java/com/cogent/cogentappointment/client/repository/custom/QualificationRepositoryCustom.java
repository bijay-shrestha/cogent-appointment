package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationResponseDTO;
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

    Long validateDuplicity(String name, Long hospitalId);

    Long validateDuplicity(Long id, String name, Long hospitalId);

    List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                 Long hospitalId,
                                                 Pageable pageable);

    QualificationResponseDTO fetchDetailsById(Long id, Long hospitalId);

    List<QualificationDropdownDTO> fetchActiveQualificationForDropDown(Long hospitalId);

    List<DropDownResponseDTO> fetchMinQualification(Long hospitalId);

}
