package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.weekdays.WeekDaysResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.WeekDaysRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.WeekDays;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.client.query.WeekDaysQuery.QUERY_TO_FETCH_ACTIVE_WEEK_DAYS;
import static com.cogent.cogentappointment.client.query.WeekDaysQuery.QUERY_TO_PREPARE_ACTIVE_WEEK_DAYS;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 25/11/2019
 */
@Transactional(readOnly = true)
public class WeekDaysRepositoryCustomImpl implements WeekDaysRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveWeekDays() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_WEEK_DAYS);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw new NoContentFoundException(WeekDays.class);
        else return results;
    }

    @Override
    public List<WeekDaysResponseDTO> fetchPrepareWeekDays() {
        Query query = createQuery.apply(entityManager, QUERY_TO_PREPARE_ACTIVE_WEEK_DAYS);

        List<WeekDaysResponseDTO> results = transformQueryToResultList(query, WeekDaysResponseDTO.class);

        if (results.isEmpty()) throw new NoContentFoundException(WeekDays.class);
        else return results;
    }
}
