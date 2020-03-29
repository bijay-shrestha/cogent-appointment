package com.cogent.cogentappointment.admin.repository.custom.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRefundDetailRepositoryCustom;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.FROM_DATE;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.TO_DATE;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.admin.utils.DashboardUtils.revenueStatisticsResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;


/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentRefundDetailRepositoryCustomImpl implements AppointmentRefundDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Double getTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_AMOUNT(refundAmountRequestDTO.getDoctorId(),
                refundAmountRequestDTO.getSpecializationId(),refundAmountRequestDTO.getHospitalId()))
                .setParameter(TO_DATE, refundAmountRequestDTO.getToDate())
                .setParameter(FROM_DATE, refundAmountRequestDTO.getFromDate());

        Double count=(Double) query.getSingleResult();

        return (count==null)? 0D:count;
    }
}
