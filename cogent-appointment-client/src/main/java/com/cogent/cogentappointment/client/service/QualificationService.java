package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationResponseDTO;
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

    List<DropDownResponseDTO> fetchMinActiveQualification();

    Qualification fetchActiveQualificationById(Long id);

    List<DropDownResponseDTO> fetchMinQualification();
}
