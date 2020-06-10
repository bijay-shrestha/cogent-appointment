package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.*;
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

    List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                          Long hospitalId,
                                          Pageable pageable);

    List<DoctorDropdownDTO> fetchActiveMinDoctor(Long hospitalId);

    DoctorDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);

    List<DoctorDropdownDTO> fetchDoctorBySpecializationAndHospitalId(Long specializationId, Long hospitalId);

    List<DoctorDropdownDTO> fetchDoctorByHospitalId(Long hospitalId);

    List<DoctorDropdownDTO> fetchMinDoctorByHospitalId(Long hospitalId);

    DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id, Long hospitalId);

    List<DoctorMinResponseDTO> fetchDoctorMinInfo(Long hospitalId);

    Double fetchDoctorAppointmentFollowUpCharge(Long doctorId, Long hospitalId);

    Double fetchDoctorAppointmentCharge(Long doctorId, Long hospitalId);

    List<DoctorDropdownDTO> fetchDoctorAvatarInfo(Long hospitalId, Long doctorId);
}
