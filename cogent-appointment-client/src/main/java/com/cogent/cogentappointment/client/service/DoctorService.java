package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.*;
import com.cogent.cogentappointment.client.model.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author smriti on 2019-09-29
 */
public interface DoctorService {

    String save(DoctorRequestDTO requestDTO, MultipartFile avatar);

    void update(DoctorUpdateRequestDTO requestDTO, MultipartFile avatar);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                          Pageable pageable);

    List<DoctorDropdownDTO> fetchDoctorForDropdown();

    DoctorDetailResponseDTO fetchDetailsById(Long id);

    Doctor fetchDoctorById(Long id);

    List<DoctorDropdownDTO> fetchDoctorBySpecializationId(Long specializationId);

    List<DoctorDropdownDTO> fetchDoctorByHospitalId(Long hospitalId);

    DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id);

    List<DoctorMinResponseDTO> fetchDoctorMinInfo(DoctorMinSearchRequestDTO requestDTO);

    DoctorMinDetailResponseDTO fetchMinDoctorDetails(DoctorMinSearchRequestDTO requestDTO);
}
