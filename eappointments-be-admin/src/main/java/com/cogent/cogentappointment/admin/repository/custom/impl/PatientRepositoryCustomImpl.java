package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.jasper.PatientDetailsJasperResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.jasper.PatientDetailsJasperResponse;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientMinDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS;
import static com.cogent.cogentappointment.admin.query.PatientQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional
@Slf4j
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


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
    public Long validatePatientDuplicity(PatientUpdateRequestDTO patientUpdateRequestDTO, Long patientId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_UPDATED_PATIENT_DUPLICITY)
                .setParameter(NAME, patientUpdateRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, patientUpdateRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(patientUpdateRequestDTO.getDateOfBirth()))
                .setParameter(ID, patientId)
                .setParameter(HOSPITAL_ID, patientUpdateRequestDTO.getHospitalId());

        return (Long) query.getSingleResult();
    }

    @Override
    public PatientDetailResponseDTO fetchDetailsById(Long hospitalPatientInfoId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(HOSPITAL_PATIENT_INFO_ID, hospitalPatientInfoId);

        try {
            PatientDetailResponseDTO detailResponseDTO =
                    transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
            return detailResponseDTO;
        } catch (NoResultException e) {
            log.error(PATIENT_NOT_FOUND_BY_HOSPITAL_PATINET_INFO_ID, hospitalPatientInfoId);
            throw new NoContentFoundException(Patient.class, "id", hospitalPatientInfoId.toString());
        }
    }

    @Override
    public List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_PATIENT(searchRequestDTO));

        addPagination.accept(pageable, query);

        Integer totalItems = query.getResultList().size();

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            PATIENT_NOT_FOUND();

        List<PatientResponseDTO> responseDTOS = transformQueryToResultList(query, PatientResponseDTO.class);

        responseDTOS.get(0).setTotalItems(totalItems);

        return responseDTOS;
    }

    @Override
    public Long countOverallRegisteredPatients(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS(hospitalId));

        return (Long) query.getSingleResult();
    }

    @Override
    public Patient getPatientByHospitalPatientInfoId(Long hospitalPatientInfoId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_BY_HOSPITAL_PATIENT_INFO_ID)
                .setParameter(HOSPITAL_PATIENT_INFO_ID, hospitalPatientInfoId);

        return (Patient) query.getSingleResult();
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
    public List<DropDownResponseDTO> fetchPatientEsewaId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ESEWA_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> list = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (list.isEmpty()) PATIENT_NOT_FOUND();

        return list;
    }

    @Override
    public PatientDetailsJasperResponseDTO getPatientDetailsForExcel(PatientSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_GET_PATIENT_FOR_EXCEL(searchRequestDTO));

        addPagination.accept(pageable, query);

        Integer totalItems = query.getResultList().size();

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            PATIENT_NOT_FOUND();

        List<PatientDetailsJasperResponse> responseDTOS = transformNativeQueryToResultList(query,
                PatientDetailsJasperResponse.class);

        return  PatientDetailsJasperResponseDTO.builder()
                .responseList(responseDTOS)
                .totalItems(totalItems)
                .build();
    }

    private void PATIENT_NOT_FOUND() {
        log.error(CONTENT_NOT_FOUND, PATIENT);
        throw new NoContentFoundException(Patient.class);
    }
}
