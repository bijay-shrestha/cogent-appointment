package com.cogent.cogentappointment.esewa.repository.custom.impl;


import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Hospital;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.query.HospitalQuery.QUERY_TO_FETCH_MIN_HOSPITAL;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createNativeQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformNativeQueryToResultList;

/**
 * @author sauravi ON 02/04/2020
 */
@Repository
@Transactional(readOnly = true)
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalMinResponseDTO> fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_MIN_HOSPITAL(searchRequestDTO));

        List<HospitalMinResponseDTO> results = transformNativeQueryToResultList(query, HospitalMinResponseDTO.class);

        if (results.isEmpty()) throw HOSPITAL_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> HOSPITAL_NOT_FOUND = () -> new NoContentFoundException(Hospital.class);
}



