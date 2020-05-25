package com.cogent.cogentappointment.client.repository.custom.impl;


import com.cogent.cogentappointment.client.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.client.repository.custom.AppointmentRefundDetailRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentRefundDetailQuery.QUERY_TO_FETCH_REFUND_APPOINTMENTS;
import static com.cogent.cogentappointment.client.log.constants.AppointmentRefundDetailQuery.QUERY_TO_GET_TOTAL_REFUND_AMOUNT;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_FETCH_REFUND_AMOUNT;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;


/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentRefundDetailRepositoryCustomImpl implements AppointmentRefundDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Double getTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_AMOUNT(refundAmountRequestDTO.getDoctorId(),
                refundAmountRequestDTO.getSpecializationId()))
                .setParameter(TO_DATE, refundAmountRequestDTO.getToDate())
                .setParameter(FROM_DATE, refundAmountRequestDTO.getFromDate())
                .setParameter(HOSPITAL_ID, hospitalId);

        Double count = (Double) query.getSingleResult();

        return (count == null) ? 0D : count;
    }

    @Override
    public RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO, Pageable pageable) {

        RefundStatusResponseDTO refundStatusResponseDTO = new RefundStatusResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_APPOINTMENTS(requestDTO))
                .setParameter(HOSPITAL_ID, getLoggedInHospitalId());

        Query getTotalRefundAmount=createQuery.apply(entityManager, QUERY_TO_GET_TOTAL_REFUND_AMOUNT)
                .setParameter(HOSPITAL_ID, getLoggedInHospitalId());


        addPagination.accept(pageable,query);

        List<RefundStatusDTO> response = transformQueryToResultList(query, RefundStatusDTO.class);

        refundStatusResponseDTO.setRefundAppointments(response);
        refundStatusResponseDTO.setTotalItems(query.getResultList().size());
        refundStatusResponseDTO.setTotalRefundAmount((Double) getTotalRefundAmount.getSingleResult());

        return refundStatusResponseDTO;
    }
}
