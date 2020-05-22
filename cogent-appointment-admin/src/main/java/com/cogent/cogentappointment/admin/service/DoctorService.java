package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorShiftRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.*;
import com.cogent.cogentappointment.persistence.model.Doctor;
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

    Doctor fetchActiveDoctorById(Long id);

    List<DoctorDropdownDTO> fetchDoctorBySpecializationId(Long specializationId);

    List<DoctorDropdownDTO> fetchDoctorByHospitalId(Long hospitalId);

    List<DoctorDropdownDTO> fetchMinDoctorByHospitalId(Long hospitalId);

    DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id);

    List<DoctorShiftMinResponseDTO> fetchAssignedDoctorShifts(Long doctorId);

    void assignShiftsToDoctor(DoctorShiftRequestDTO requestDTO);

    Long validateDoctorShiftCount(List<Long> shiftIds, Long doctorId);
}
