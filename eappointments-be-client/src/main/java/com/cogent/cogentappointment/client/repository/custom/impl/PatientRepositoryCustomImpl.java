package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.jasper.PatientDetailsJasperResponse;
import com.cogent.cogentappointment.client.dto.jasper.PatientDetailsJasperResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientSearchResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentConstants.APPOINTMENT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.HOSPITAL_PATIENT_INFO_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS;
import static com.cogent.cogentappointment.client.query.PatientQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validatePatientDuplicity(Long patientId,
                                         String name,
                                         String mobileNumber,
                                         Date dateOfBirth) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_PATIENT_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(dateOfBirth))
                .setParameter(ID, patientId);

        return (Long) query.getSingleResult();
    }

    @Override
    public PatientResponseDTO fetchPatientDetailsById(Long hospitalPatientInfoId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(HOSPITAL_PATIENT_INFO_ID, hospitalPatientInfoId)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return transformQueryToSingleResult(query, PatientResponseDTO.class);
        } catch (NoResultException e) {
            log.error(PATIENT_NOT_FOUND_BY_HOSPITAL_PATIENT_INFO_ID, PATIENT, hospitalPatientInfoId);
            throw new NoContentFoundException(Patient.class, "id", hospitalPatientInfoId.toString());
        }
    }

    @Override
    public List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable,
                                                 Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_PATIENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<PatientSearchResponseDTO> patients = transformQueryToResultList(query, PatientSearchResponseDTO.class);

        if (patients.isEmpty())
            PATIENT_NOT_FOUND.get();

        patients.get(0).setTotalItems(totalItems);

        return patients;
    }

    @Override
    public Long countOverallRegisteredPatients(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public String fetchLatestRegistrationNumber(Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return (String) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAIL_BY_APPOINTMENT_ID)
                .setParameter(APPOINTMENT_ID, appointmentId);

        try {
            return transformQueryToSingleResult(query, PatientMinDetailResponseDTO.class);
        } catch (NoResultException e) {
            log.error(PATIENT_NOT_FOUND_BY_APPOINTMENT_ID, appointmentId);
            throw new NoContentFoundException(Patient.class, "appointmentId", appointmentId.toString());
        }
    }

    @Override
    public Patient getPatientByHospitalPatientInfoId(Long hospitalPatientInfoId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_BY_HOSPITAL_PATIENT_INFO_ID)
                .setParameter(HOSPITAL_PATIENT_INFO_ID, hospitalPatientInfoId)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Patient) query.getSingleResult();
    }

    @Override
    public List<DropDownResponseDTO> fetchPatientEsewaId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ESEWA_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> list = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (list.isEmpty()) PATIENT_NOT_FOUND.get();

        return list;
    }

    @Override
    public PatientDetailsJasperResponseDTO getPatientDetailsForExcel(PatientSearchRequestDTO searchRequestDTO,
                                                                     Pageable pageable) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_GET_PATIENT_FOR_EXCEL(searchRequestDTO))
                .setParameter(HOSPITAL_ID,getLoggedInHospitalId());

        addPagination.accept(pageable, query);

        Integer totalItems = query.getResultList().size();

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            PATIENT_NOT_FOUND.get();

        List<PatientDetailsJasperResponse> responseDTOS = transformNativeQueryToResultList(query,
                PatientDetailsJasperResponse.class);

        return  PatientDetailsJasperResponseDTO.builder()
                .responseList(responseDTOS)
                .totalItems(totalItems)
                .build();
    }

    private Supplier<NoContentFoundException> PATIENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, PATIENT);
        throw new NoContentFoundException(Patient.class);
    };
}
