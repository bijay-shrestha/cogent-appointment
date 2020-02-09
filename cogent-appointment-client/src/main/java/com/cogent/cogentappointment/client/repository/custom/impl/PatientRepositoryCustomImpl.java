package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
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
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.ESEWA_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.IS_SELF;
import static com.cogent.cogentappointment.client.query.PatientQuery.*;
import static com.cogent.cogentappointment.client.utils.PatientUtils.parseToPatientMinimalResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.TimeConverterUtils.calculateAge;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

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
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS)
                .setParameter(ESEWA_ID, searchRequestDTO.getEsewaId())
                .setParameter(IS_SELF, searchRequestDTO.getIsSelf())
                .setParameter(HOSPITAL_ID, searchRequestDTO.getHospitalId());

        try {
            PatientDetailResponseDTO detailResponseDTO =
                    transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
            detailResponseDTO.setAge(calculateAge(detailResponseDTO.getDateOfBirth()));
            return detailResponseDTO;
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "eSewaId", searchRequestDTO.getEsewaId());
        }
    }

    @Override
    public List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientMinSearchRequestDTO searchRequestDTO,
                                                                   Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MINIMAL_PATIENT)
                .setParameter(ESEWA_ID, searchRequestDTO.getEsewaId())
                .setParameter(IS_SELF, searchRequestDTO.getIsSelf())
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
    public PatientDetailResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(ID, id);

        try {
            PatientDetailResponseDTO detailResponseDTO =
                    transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
            detailResponseDTO.setAge(calculateAge(detailResponseDTO.getDateOfBirth()));
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

}
