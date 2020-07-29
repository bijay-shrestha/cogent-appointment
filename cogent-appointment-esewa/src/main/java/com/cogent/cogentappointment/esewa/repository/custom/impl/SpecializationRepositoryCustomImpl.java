package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.SpecializationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Specialization;
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
import static com.cogent.cogentappointment.esewa.log.constants.SpecializationLog.SPECIALIZATION;
import static com.cogent.cogentappointment.esewa.query.SpecializationQuery.QUERY_TO_FETCH_SPECIALIZATION_BY_HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 2019-08-11
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class SpecializationRepositoryCustomImpl implements SpecializationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()){
            log.error(CONTENT_NOT_FOUND,SPECIALIZATION);
            throw SPECIALIZATION_NOT_FOUND.get();
        }
        else return results;
    }

    private Supplier<NoContentFoundException> SPECIALIZATION_NOT_FOUND = ()
            -> new NoContentFoundException(Specialization.class);
}

