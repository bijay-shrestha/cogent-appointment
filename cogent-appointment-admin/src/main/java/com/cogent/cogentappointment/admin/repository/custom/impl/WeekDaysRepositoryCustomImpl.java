package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.model.WeekDays;
import com.cogent.cogentappointment.admin.query.WeekDaysQuery;
import com.cogent.cogentappointment.admin.repository.custom.WeekDaysRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author smriti on 25/11/2019
 */
@Transactional(readOnly = true)
public class WeekDaysRepositoryCustomImpl implements WeekDaysRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveWeekDays() {
        Query query = QueryUtils.createQuery.apply(entityManager, WeekDaysQuery.QUERY_TO_FETCH_ACTIVE_WEEK_DAYS);

        List<DropDownResponseDTO> results = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw new NoContentFoundException(WeekDays.class);
        else return results;
    }
}
