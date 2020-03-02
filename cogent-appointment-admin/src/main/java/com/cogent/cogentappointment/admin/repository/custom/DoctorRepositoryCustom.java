package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorUpdateResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Qualifier("doctorRepositoryCustom")
public interface DoctorRepositoryCustom {

    Long validateDoctorDuplicity(String name, String mobileNumber, Long hospitalId);

    Long validateDoctorDuplicityForUpdate(Long id, String name, String mobileNumber, Long hospitalId);

    List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO, Pageable pageable);

    List<DoctorDropdownDTO> fetchDoctorForDropdown();

    DoctorDetailResponseDTO fetchDetailsById(Long id);

    List<DoctorDropdownDTO> fetchDoctorBySpecializationId(Long specializationId);

    List<DoctorDropdownDTO> fetchDoctorByHospitalId(Long hospitalId);

    DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id);

    List<DoctorDropdownDTO> fetchDoctorAvatarInfo(Long hospitalId, Long doctorId);
}
