package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.client.model.Specialization;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 2019-08-11
 */
public interface SpecializationService {
    String save(SpecializationRequestDTO requestDTO);

    void update(SpecializationUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable);

    List<DropDownResponseDTO> fetchActiveSpecializationForDropDown();

    SpecializationResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long DoctorId);

    Specialization fetchActiveSpecializationById(Long specializationId);
}
