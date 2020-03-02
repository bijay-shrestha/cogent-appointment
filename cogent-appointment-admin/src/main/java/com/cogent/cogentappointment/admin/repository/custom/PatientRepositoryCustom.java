package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientMinDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.persistence.model.Patient;
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

    PatientDetailResponseDTO fetchDetailsById(Long hospitalPatientInfoId);

    List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable);

    String fetchLatestRegistrationNumber(Long hospitalId);

    Long countOverallRegisteredPatients(Long HospitalId);

    Patient getPatientByHospitalPatientInfoId(Long hospitalPatientInfoId);

    PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId);
}
