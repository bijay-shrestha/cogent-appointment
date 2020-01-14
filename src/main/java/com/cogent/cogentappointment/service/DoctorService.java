package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.doctor.DoctorUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.model.Doctor;
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

    DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id);
}
