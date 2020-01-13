package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.doctor.DoctorUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.model.Doctor;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 2019-09-29
 */
public interface DoctorService {

    String save(DoctorRequestDTO requestDTO);

    void update(DoctorUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                          Pageable pageable);

    List<DropDownResponseDTO> fetchDoctorForDropdown();

    DoctorDetailResponseDTO fetchDetailsById(Long id);

    Doctor fetchDoctorById(Long id);

    List<DropDownResponseDTO> fetchDoctorBySpecializationId(Long specializationId);

    DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id);
}
