package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorShiftMinResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DoctorShiftRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorShift;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DOCTOR_ID;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.DoctorLog.DOCTOR_SHIFT;
import static com.cogent.cogentappointment.admin.query.DoctorShiftQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 10/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DoctorShiftRepositoryCustomImpl implements DoctorShiftRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DoctorShiftMinResponseDTO> fetchAssignedDoctorShifts(Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_SHIFTS)
                .setParameter(DOCTOR_ID, doctorId);

        List<DoctorShiftMinResponseDTO> results = transformQueryToResultList(query, DoctorShiftMinResponseDTO.class);

        if (results.isEmpty())
            DOCTOR_SHIFTS_NOT_FOUND.get();

        return results;
    }

    @Override
    public List<Long> fetchActiveAssignedDoctorShiftIds(Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ASSIGNED_DOCTOR_SHIFTS)
                .setParameter(DOCTOR_ID, doctorId);

        return query.getResultList();
    }

    @Override
    public Long validateDoctorShiftCount(List<Long> shiftIds,
                                         Long doctorId) {

        String shifts =  StringUtils.join(shiftIds, COMMA_SEPARATED);
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_SHIFT_COUNT(shifts, doctorId));

        return (Long) query.getSingleResult();
    }

    private Supplier<NoContentFoundException> DOCTOR_SHIFTS_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR_SHIFT);
        throw new NoContentFoundException(DoctorShift.class);
    };
}
