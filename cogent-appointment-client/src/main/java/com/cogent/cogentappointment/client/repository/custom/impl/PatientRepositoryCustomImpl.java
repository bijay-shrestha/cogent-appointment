package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientSearchResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.IS_SELF;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS;
import static com.cogent.cogentappointment.client.query.PatientQuery.*;
import static com.cogent.cogentappointment.client.utils.PatientUtils.parseToPatientMinimalResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.TimeConverterUtils.calculateAge;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validatePatientDuplicity(String name, String mobileNumber,
                                         Date dateOfBirth, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_PATIENT_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(dateOfBirth))
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

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
    public PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_FOR_SELF)
                .setParameter(NAME, searchRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, searchRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(searchRequestDTO.getDateOfBirth()))
                .setParameter(HOSPITAL_ID, searchRequestDTO.getHospitalId())
                .setParameter(IS_SELF, searchRequestDTO.getIsSelf());

        try {
            PatientDetailResponseDTO detailResponseDTO =
                    transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
            detailResponseDTO.setAge(calculateAge(detailResponseDTO.getDateOfBirth()));
            return detailResponseDTO;
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "name", searchRequestDTO.getName());
        }
    }

    @Override
    public List<PatientMinimalResponseDTO> searchForOthers(PatientMinSearchRequestDTO searchRequestDTO,
                                                           Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MINIMAL_PATIENT_FOR_OTHERS)
//                .setParameter(ESEWA_ID, searchRequestDTO.getEsewaId())
//                .setParameter(IS_SELF, searchRequestDTO.getIsSelf())
                .setParameter(HOSPITAL_ID, searchRequestDTO.getHospitalId());

        List<Object[]> results = query.getResultList();

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        if (results.isEmpty()) throw new NoContentFoundException(Patient.class);

        else {
            List<PatientMinimalResponseDTO> responseDTOS = parseToPatientMinimalResponseDTO(results);
            responseDTOS.get(0).setTotalItems(totalItems);
            return responseDTOS;
        }
    }

    @Override
    public PatientResponseDTO fetchPatientDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(ID, id);

        try {
            PatientResponseDTO patientResponseDTO = transformQueryToSingleResult(query, PatientResponseDTO.class);
            patientResponseDTO.setAge(calculateAge(patientResponseDTO.getDateOfBirth()));
            return patientResponseDTO;
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "id", id.toString());
        }
    }

    @Override
    public List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_PATIENT(searchRequestDTO));

        List<Object[]> results = query.getResultList();

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        if (results.isEmpty()) throw new NoContentFoundException(Patient.class);

        else {
            List<PatientSearchResponseDTO> responseDTOS = transformQueryToResultList(query, PatientSearchResponseDTO.class);
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
