package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.qualification.QualificationResponseDTO;
import com.cogent.cogentappointment.model.Qualification;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
public interface QualificationService {

    void save(QualificationRequestDTO requestDTO);

    void update(QualificationUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable);

    QualificationResponseDTO fetchDetailsById(Long id);

    List<QualificationDropdownDTO> fetchActiveQualificationForDropDown();

    Qualification fetchQualificationById(Long id);
}
