package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.*;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Qualifier("patientRepositoryCustom")
public interface PatientRepositoryCustom {

    /*eSewa*/
    PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    List<Long> fetchChildPatientIds(PatientMinSearchRequestDTO searchRequestDTO);

    List<PatientMinimalResponseDTO> fetchMinPatientInfoForOthers(List<Long> childPatientIds,
                                                                 Pageable pageable);

    PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientId);

    /*admin*/
    Long validatePatientDuplicity(PatientUpdateRequestDTO updateRequestDTO, Long hospitalId);

    PatientResponseDTO fetchPatientDetailsById(Long id, Long hospitalId);

    List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                          Pageable pageable, Long hospitalId);

    Long countOverallRegisteredPatients(Long HospitalId);

    String fetchLatestRegistrationNumber(Long hospitalId);

    Patient fetchPatient(String name, String mobileNumber, Date dateOfBirth);

    PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId);
}
