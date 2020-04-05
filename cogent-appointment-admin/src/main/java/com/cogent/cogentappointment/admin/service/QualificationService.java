package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationResponseDTO;
import com.cogent.cogentappointment.persistence.model.Qualification;
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

    List<QualificationDropdownDTO> fetchActiveMinQualification();

    List<DropDownResponseDTO> fetchMinQualification();

    Qualification fetchQualificationById(Long id);
}
