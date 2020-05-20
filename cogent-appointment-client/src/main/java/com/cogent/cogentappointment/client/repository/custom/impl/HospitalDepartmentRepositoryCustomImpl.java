package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.HospitalDepartmentQuery.QUERY_TO_VALIDATE_DUPLICITY;
import static com.cogent.cogentappointment.client.query.HospitalDepartmentQuery.QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

/**
 * @author Sauravi Thapa ON 5/20/20
 */

@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentRepositoryCustomImpl implements HospitalDepartmentRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(HospitalDepartmentRequestDTO requestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode())
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(HospitalDepartmentUpdateRequestDTO requestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, requestDTO.getId())
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode())
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

}
