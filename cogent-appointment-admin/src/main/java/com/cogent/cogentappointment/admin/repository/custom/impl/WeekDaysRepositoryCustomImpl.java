package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.weekdays.WeekDaysResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.WeekDaysQuery;
import com.cogent.cogentappointment.admin.repository.custom.WeekDaysRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.WeekDays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.ERROR_LOG;
import static com.cogent.cogentappointment.admin.log.constants.WeekDaysLog.WEEK_DAYS;
import static com.cogent.cogentappointment.admin.query.WeekDaysQuery.QUERY_TO_FETCH_ACTIVE_WEEK_DAYS;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 25/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class WeekDaysRepositoryCustomImpl implements WeekDaysRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveWeekDays() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_WEEK_DAYS);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw new NoContentFoundException(WeekDays.class);
        } else return results;
    }

    @Override
    public List<WeekDaysResponseDTO> fetchPrepareWeekDays() {
        Query query = createQuery.apply(entityManager, WeekDaysQuery.QUERY_TO_PREPARE_ACTIVE_WEEK_DAYS);

        List<WeekDaysResponseDTO> results = transformQueryToResultList(query, WeekDaysResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw new NoContentFoundException(WeekDays.class);
        } else return results;
    }

    private void error() {
        log.error(ERROR_LOG, WEEK_DAYS);
    }
}
