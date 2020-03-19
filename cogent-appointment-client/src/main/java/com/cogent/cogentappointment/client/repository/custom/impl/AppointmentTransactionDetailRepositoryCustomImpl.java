package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.client.utils.DashboardUtils.revenueStatisticsResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.DateConverterUtils.dateDifference;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentTransactionDetailRepositoryCustomImpl implements AppointmentTransactionDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Double getRevenueByDates(Date toDate, Date fromDate, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_REVENUE_BY_DATE)
                .setParameter(TO_DATE, toDate)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        Double count = (Double) query.getSingleResult();

        return (count == null) ? 0D : count;
    }

    @Override
    public RevenueTrendResponseDTO getRevenueTrend(Date toDate, Date fromDate,
                                                   Long hospitalId, Character filter) {

        final String queryByFilter = getQueryByFilter(filter);

        Query query = createQuery.apply(entityManager, queryByFilter)
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(HOSPITAL_ID, hospitalId);
        List<Object[]> objects=query.getResultList();

        RevenueTrendResponseDTO responseDTO=revenueStatisticsResponseDTO(objects,filter);

        return responseDTO;
    }

    @Override
    public List<DoctorRevenueResponseDTO> getDoctorRevenueTracker(Date toDate,
                                                                  Date fromDate,
                                                                  Long hospitalId,
                                                                  Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_GENERATE_DOCTOR_REVENEU_TRACKER)
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        Character filter = dateDifference(toDate,
                fromDate);

        List<DoctorRevenueResponseDTO> list = transformQueryToResultList(query, DoctorRevenueResponseDTO.class);

        if (list.isEmpty())
            throw DOCTOR_NOT_FOUND.get();
        else {
            list.get(0).setTotalItems(totalItems);
            list.get(0).setFilterType(filter);
            return list;
        }
    }


    private String getQueryByFilter(Character filter) {
        Map<Character, String> queriesWithFilterAsKey = new HashMap<>();
        queriesWithFilterAsKey.put('D', QUERY_TO_FETCH_REVENUE_DAILY);
        queriesWithFilterAsKey.put('W', QUERY_TO_FETCH_REVENUE_WEEKLY);
        queriesWithFilterAsKey.put('M', QUERY_TO_FETCH_REVENUE_MONTHLY);
        queriesWithFilterAsKey.put('Y', QUERY_TO_FETCH_REVENUE_YEARLY);

        return queriesWithFilterAsKey.get(filter);
    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () ->
            new NoContentFoundException(Doctor.class);
}
