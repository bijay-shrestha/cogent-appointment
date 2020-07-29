package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.doctor.DoctorSalutationResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.DoctorSalutationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.SalutationLog.DOCTOR_SALUTATION;
import static com.cogent.cogentappointment.client.query.SalutationQuery.QUERY_TO_FETCH_DOCTOR_SALUTATION_BY_DOCTOR_ID;
import static com.cogent.cogentappointment.client.query.SalutationQuery.QUERY_TO_VALIDATE_DOCTOR_SALUTATION_COUNT;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak on 2020-05-13
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DoctorSalutationRepositoryCustomImpl implements DoctorSalutationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DoctorSalutationResponseDTO> fetchDoctorSalutationByDoctorId(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_SALUTATION_BY_DOCTOR_ID(id));

        List<DoctorSalutationResponseDTO> results = transformQueryToResultList(query, DoctorSalutationResponseDTO.class);

        if (results.isEmpty()) {
        return null;
        } else {
            return results;
        }

    }

    @Override
    public List<DoctorSalutation> validateDoctorSalutationCount(String ids) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DOCTOR_SALUTATION_COUNT(ids));

        List<DoctorSalutation> doctorSalutationList = query.getResultList();

        if (doctorSalutationList.isEmpty()) {
            error();
            throw DOCTOR_SALUTATION_NOT_FOUND.get();
        } else return doctorSalutationList;
    }

    private Supplier<NoContentFoundException> DOCTOR_SALUTATION_NOT_FOUND = () ->
            new NoContentFoundException(DoctorSalutation.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, DOCTOR_SALUTATION);
    }
}
