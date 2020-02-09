package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.PatientMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.query.PatientMetaInfoQuery.QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN;
import static com.cogent.cogentappointment.client.query.PatientMetaInfoQuery.QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
@Transactional(readOnly = true)
public class PatientMetaInfoRepositoryCustomImpl implements PatientMetaInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchMinPatientMetaInfo(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_META_INFO_FOR_DROP_DOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw PATIENT_META_INFO_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinPatientMetaInfo(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_PATIENT_META_INFO_FOR_DROP_DOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw PATIENT_META_INFO_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> PATIENT_META_INFO_NOT_FOUND = ()
            -> new NoContentFoundException(PatientMetaInfo.class);
}
