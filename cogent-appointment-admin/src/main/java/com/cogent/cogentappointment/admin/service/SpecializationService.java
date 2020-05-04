package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.persistence.model.Specialization;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 2019-08-11
 */
public interface SpecializationService {
    void save(SpecializationRequestDTO requestDTO);

    void update(SpecializationUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable);

    List<DropDownResponseDTO> fetchActiveSpecializationForDropDown();

    List<DropDownResponseDTO> fetchSpecializationForDropDown();

    SpecializationResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long DoctorId);

    List<DropDownResponseDTO> fetchActiveSpecializationByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> fetchSpecializationByHospitalId(Long hospitalId);

    Specialization fetchActiveSpecializationById(Long specializationId);
}
