package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.model.Patient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Sauravi Thapa 9/22/19
 */

@Repository
@Qualifier("patientRepositoryCustom")
public interface PatientRepositoryCustom {

    Long fetchPatientForValidation(String name, String mobileNumber, Date dateOfBirth);

    List<PatientMinimalResponseDTO> searchPatient(
            PatientSearchRequestDTO searchRequestDTO, Pageable pageable);

    PatientResponseDTO fetchPatientDetailsById(Long id);

    Optional<List<DropDownResponseDTO>> fetchActiveDropDownList();

    Optional<List<DropDownResponseDTO>> fetchDropDownList();

    List<Object[]> getPatient();

    String fetchLatestPatientHisNumber();

    Patient fetchPatient(Long id);
}
