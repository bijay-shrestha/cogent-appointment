package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.specializationDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentDutyRosterLog.HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterQuery.QUERY_TO_SEARCH_HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterQuery.VALIDATE_DEPT_DUTY_ROSTER_COUNT;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author Sauravi Thapa ON 5/18/20
 */

@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterRepositoryCustomImpl implements HospitalDeptDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateSpecializationDutyRosterCount(Long specializationId, Date fromDate, Date toDate) {
        Query query = createQuery.apply(entityManager, VALIDATE_DEPT_DUTY_ROSTER_COUNT)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                             Pageable pageable,
                                                             Long hospitalId) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_SEARCH_HOSPITAL_DEPARTMENT_DUTY_ROSTER(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalDeptDutyRosterMinResponseDTO> results = transformQueryToResultList(
                query, HospitalDeptDutyRosterMinResponseDTO.class);

        if (results.isEmpty())  HOSPITAL_DEPT_DUTY_ROSTER_NOT_FOUND.get();

        results.get(0).setTotalItems(totalItems);
        return results;
    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_DUTY_ROSTER);
        throw new NoContentFoundException(HospitalDepartmentDutyRoster.class);
    };
}
