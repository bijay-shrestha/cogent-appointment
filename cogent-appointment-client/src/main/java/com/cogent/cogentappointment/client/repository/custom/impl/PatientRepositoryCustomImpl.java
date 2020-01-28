package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.model.Patient;
import com.cogent.cogentappointment.client.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.client.utils.PatientUtils;
import com.cogent.cogentappointment.client.utils.commons.DateUtils;
import com.cogent.cogentappointment.client.utils.commons.PageableUtils;
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
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToSingleResult;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchPatientForValidation(String name, String mobileNumber, Date dateOfBirth) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_PATIENT_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(DATE_OF_BIRTH, DateUtils.utilDateToSqlDate(dateOfBirth));

        return (Long) query.getSingleResult();
    }

    @Override
    public PatientDetailResponseDTO search(PatientSearchRequestDTO searchRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS)
                .setParameter(ESEWA_ID, searchRequestDTO.getEsewaId())
                .setParameter(IS_SELF, searchRequestDTO.getIsSelf())
                .setParameter(HOSPITAL_ID, searchRequestDTO.getHospitalId());

        //TODO: calculate age
        try {
            return transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "eSewaId", searchRequestDTO.getEsewaId());
        }
    }

    @Override
    public List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientSearchRequestDTO searchRequestDTO,
                                                                   Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MINIMAL_PATIENT)
                .setParameter(ESEWA_ID, searchRequestDTO.getEsewaId())
                .setParameter(IS_SELF, searchRequestDTO.getIsSelf())
                .setParameter(HOSPITAL_ID, searchRequestDTO.getHospitalId());

        List<Object[]> results = query.getResultList();

        Integer totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        if (results.isEmpty()) throw new NoContentFoundException(Patient.class);

        else {
            List<PatientMinimalResponseDTO> responseDTOS = PatientUtils.parseToPatientMinimalResponseDTO(results);
            responseDTOS.get(0).setTotalItems(totalItems);
            return responseDTOS;
        }
    }

    @Override
    public PatientDetailResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(ID, id);

        try {
            return transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "id", id.toString());
        }
    }

//    @Override
//    public List<PatientMinimalResponseDTO> searchPatient(PatientSearchRequestDTO searchRequestDTO,
//                                                         Pageable pageable) {
//        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_PATIENT.apply(searchRequestDTO));
//
//        Integer totalItems = query.getResultList().size();
//
//        addPagination.accept(pageable, query);
//
//        List<PatientMinimalResponseDTO> minimalResponseDTOS = transformQueryToResultList(query,
//                PatientMinimalResponseDTO.class);
//
//        if (minimalResponseDTOS.isEmpty()) throw new NoContentFoundException(Patient.class);
//        else {
//            minimalResponseDTOS.get(0).setTotalItems(totalItems);
//            return minimalResponseDTOS;
//        }
//    }
//
//    @Override
//    public PatientDetailResponseDTO fetchPatientDetailsById(Long id) {
//        Query query = createQuery.apply(entityManager, FETCH_PATIENT_DETAILS_BY_ID)
//                .setParameter(QueryConstants.ID, id);
//        try {
//            PatientDetailResponseDTO singleResult = transformQueryToSingleResult(query,
//                    PatientDetailResponseDTO.class);
//            return singleResult;
//        } catch (NoResultException e) {
//            throw new NoContentFoundException(Patient.class, "id", id.toString());
//        }
//    }
//
//    @Override
//    public Optional<List<DropDownResponseDTO>> fetchActiveDropDownList() {
//        Query query = createQuery.apply(entityManager, QUERY_FOR_ACTIVE_DROP_DOWN_PATIENT);
//
//        List<DropDownResponseDTO> minimalResponseDTOS = transformQueryToResultList(query,
//                DropDownResponseDTO.class);
//
//        return minimalResponseDTOS.isEmpty() ? Optional.empty() : Optional.of(minimalResponseDTOS);
//    }
//
//    @Override
//    public Optional<List<DropDownResponseDTO>> fetchDropDownList() {
//        Query query = createQuery.apply(entityManager, QUERY_FOR_DROP_DOWN_PATIENT);
//
//        List<DropDownResponseDTO> minimalResponseDTOS = transformQueryToResultList(query,
//                DropDownResponseDTO.class);
//
//        return minimalResponseDTOS.isEmpty() ? Optional.empty() : Optional.of(minimalResponseDTOS);
//    }
//
//    @Override
//    public List<Object[]> getPatient() {
//        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_OBJECT);
//        List<Object[]> object = query.getResultList();
//        return object;
//    }
//
//    @Override
//    public String fetchLatestPatientHisNumber() {
//        List<String> s = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_PATIENT_HIS_NUMBER).getResultList();
//
//        if (!s.isEmpty()) {
//            return s.get(0);
//        }
//        return "";
//    }
}
