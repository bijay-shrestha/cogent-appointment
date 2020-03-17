package com.cogent.cogentappointment.admin.repository.custom.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DoctorRevenueResponseListDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.DoctorUtils;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
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
import static com.cogent.cogentappointment.admin.utils.DoctorUtils.parseTodoctorRevenueResponseListDTO;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;


/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
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
    public DoctorRevenueResponseListDTO getDoctorRevenue(Date toDate,
                                                         Date fromDate,
                                                         DoctorRevenueRequestDTO requestDTO,
                                                         Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GENERATE_DOCTOR_REVENUE_LIST(requestDTO))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        DoctorRevenueResponseListDTO responseListDTO = parseTodoctorRevenueResponseListDTO(objects);

        if (responseListDTO.getDoctorRevenueResponseDTOList().isEmpty()) {
            log.error("Doctor Revenue List Not Found");
            throw DOCTOR_REVENUE_NOT_FOUND.get();
        }

        return responseListDTO;
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

    private Supplier<NoContentFoundException> DOCTOR_REVENUE_NOT_FOUND = () ->
            new NoContentFoundException(AppointmentTransactionDetail.class);
}
