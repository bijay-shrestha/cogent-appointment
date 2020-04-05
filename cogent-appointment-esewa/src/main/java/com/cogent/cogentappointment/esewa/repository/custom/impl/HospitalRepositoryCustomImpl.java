package com.cogent.cogentappointment.esewa.repository.custom.impl;


import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.esewa.query.HospitalQuery.QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS;
import static com.cogent.cogentappointment.esewa.query.HospitalQuery.QUERY_TO_FETCH_HOSPITAL_FREE_FOLLOW_UP_INTERVAL_DAYS;
import static com.cogent.cogentappointment.esewa.query.HospitalQuery.QUERY_TO_FETCH_MIN_HOSPITAL;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformNativeQueryToResultList;

/**
 * @author sauravi ON 02/04/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalMinResponseDTO> fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_MIN_HOSPITAL(searchRequestDTO));

        List<HospitalMinResponseDTO> results = transformNativeQueryToResultList(query, HospitalMinResponseDTO.class);

        if (results.isEmpty()) throw HOSPITAL_NOT_FOUND();
        else return results;
    }

    @Override
    public Integer fetchHospitalFreeFollowUpIntervalDays(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FREE_FOLLOW_UP_INTERVAL_DAYS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    @Override
    public Integer fetchHospitalFollowUpIntervalDays(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    private NoContentFoundException HOSPITAL_NOT_FOUND () {
        log.error(CONTENT_NOT_FOUND,HOSPITAL);
        throw new NoContentFoundException(Hospital.class);
    }
}



