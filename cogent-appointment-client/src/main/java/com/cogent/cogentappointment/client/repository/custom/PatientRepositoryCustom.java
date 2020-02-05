package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
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

    Long fetchPatientForValidation(String name, String mobileNumber,
                                   Date dateOfBirth, Long hospitalId);

    PatientDetailResponseDTO searchForSelf(PatientSearchRequestDTO searchRequestDTO);

    List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientSearchRequestDTO searchRequestDTO,
                                                            Pageable pageable);

    PatientDetailResponseDTO fetchDetailsById(Long id);

//    List<PatientMinimalResponseDTO> searchPatient(
//            PatientSearchRequestDTO searchRequestDTO, Pageable pageable);
//
//    PatientDetailResponseDTO fetchPatientDetailsById(Long id);
//
//    Optional<List<DropDownResponseDTO>> fetchActiveDropDownList();
//
//    Optional<List<DropDownResponseDTO>> fetchDropDownList();
//
//    List<Object[]> getPatient();
//
//    String fetchLatestPatientHisNumber();
}
