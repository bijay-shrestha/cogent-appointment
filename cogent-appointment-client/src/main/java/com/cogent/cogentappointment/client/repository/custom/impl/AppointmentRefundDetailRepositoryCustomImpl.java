package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentRefundDetailRepositoryCustom;
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
public class AppointmentRefundDetailRepositoryCustomImpl implements AppointmentRefundDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Double getRefundByDates(Date toDate, Date fromDate, Long hospitalId) {
        Query query=createQuery.apply(entityManager,QUERY_TO_GET_REFUND_BY_DATE)
                .setParameter(TO_DATE, toDate)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(HOSPITAL_ID, hospitalId);


        return (Double) query.getSingleResult();
    }
}
