package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_GET_REVENUE_BY_DATE;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentTransactionDetailRepositoryCustomImpl implements AppointmentTransactionDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Double getRevenueByDates(Date toDate, Date fromDate,Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_REVENUE_BY_DATE)
                .setParameter(TO_DATE, toDate)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Double) query.getSingleResult();
    }
}
