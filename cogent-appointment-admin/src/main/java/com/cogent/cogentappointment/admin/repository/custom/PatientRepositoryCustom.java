package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Qualifier("patientRepositoryCustom")
public interface PatientRepositoryCustom {
    Long validatePatientDuplicity(PatientUpdateRequestDTO patientUpdateRequestDTO);

    PatientDetailResponseDTO fetchDetailsById(Long id);

    String fetchLatestRegistrationNumber();

    List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable);

    Long countOverallRegisteredPatients(Long HospitalId);
}
