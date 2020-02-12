package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS;
import static com.cogent.cogentappointment.admin.query.PatientQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateConverterUtils.ageConverter;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertDateToLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Long validatePatientDuplicity(PatientUpdateRequestDTO patientUpdateRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_UPDATED_PATIENT_DUPLICITY)
                .setParameter(NAME, patientUpdateRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, patientUpdateRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(patientUpdateRequestDTO.getDateOfBirth()))
                .setParameter(ID, patientUpdateRequestDTO.getId())
                .setParameter(HOSPITAL_ID, patientUpdateRequestDTO.getHospitalId());

        return (Long) query.getSingleResult();
    }

    @Override
    public PatientDetailResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(ID, id);

        try {
            PatientDetailResponseDTO detailResponseDTO =
                    transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
            detailResponseDTO.setAge(ageConverter(convertDateToLocalDate(detailResponseDTO.getDateOfBirth())));
            return detailResponseDTO;
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "id", id.toString());
        }
    }

    @Override
    public List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT(searchRequestDTO));

        List<Object[]> results = query.getResultList();

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        if (results.isEmpty()) throw new NoContentFoundException(Patient.class);

        else {
            List<PatientResponseDTO> responseDTOS = transformQueryToResultList(query, PatientResponseDTO.class);
            responseDTOS.get(0).setTotalItems(totalItems);
            return responseDTOS;
        }
    }

    @Override
    public Long countOverallRegisteredPatients(Long HospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS)
                .setParameter(HOSPITAL_ID, HospitalId);

        return (Long) query.getSingleResult();
    }
}
