package com.cogent.cogentappointment.admin.repository.custom.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DoctorRevenueResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Doctor;
import lombok.extern.slf4j.Slf4j;
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

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.admin.utils.DashboardUtils.revenueStatisticsResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.DateConverterUtils.dateDifference;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;


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
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_REVENUE_BY_DATE(hospitalId))
                .setParameter(TO_DATE, toDate)
                .setParameter(FROM_DATE, fromDate);

        Double amount = (Double) query.getSingleResult();
        return (amount == null) ? 0D : amount;
    }

    @Override
    public RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO, Character filter) {

        final String queryByFilter = getQueryByFilter(dashBoardRequestDTO.getHospitalId(), filter);

        Query query = createQuery.apply(entityManager, queryByFilter)
                .setParameter(TO_DATE, dashBoardRequestDTO.getToDate())
                .setParameter(FROM_DATE, dashBoardRequestDTO.getFromDate());
        List<Object[]> objects = query.getResultList();

        RevenueTrendResponseDTO responseDTO = revenueStatisticsResponseDTO(objects, filter);

        return responseDTO;
    }

    @Override
    public List<DoctorRevenueResponseDTO> getDoctorRevenue(DoctorRevenueRequestDTO requestDTO,
                                                           Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GENERATE_DOCTOR_REVENEU_LIST)
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        Character filter = dateDifference(requestDTO.getToDate(),
                requestDTO.getFromDate());

        List<DoctorRevenueResponseDTO> list = transformQueryToResultList(query, DoctorRevenueResponseDTO.class);

        if (list.isEmpty()) {
            throw DOCTOR_NOT_FOUND.get();
        } else {
            list.get(0).setTotalItems(totalItems);
            list.get(0).setFilterType(filter);
            return list;
        }
    }

    private String getQueryByFilter(Long hospitalId, Character filter) {
        Map<Character, String> queriesWithFilterAsKey = new HashMap<>();
        queriesWithFilterAsKey.put('D', QUERY_TO_FETCH_REVENUE_DAILY(hospitalId));
        queriesWithFilterAsKey.put('W', QUERY_TO_FETCH_REVENUE_WEEKLY(hospitalId));
        queriesWithFilterAsKey.put('M', QUERY_TO_FETCH_REVENUE_MONTHLY(hospitalId));
        queriesWithFilterAsKey.put('Y', QUERY_TO_FETCH_REVENUE_YEARLY(hospitalId));

        return queriesWithFilterAsKey.get(filter);
    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () ->
            new NoContentFoundException(Doctor.class);
}
