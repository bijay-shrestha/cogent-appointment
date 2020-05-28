package com.cogent.cogentappointment.admin.repository.custom.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.RefundStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRefundDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT_REFUND_DETAIL;
import static com.cogent.cogentappointment.admin.query.AppointmentRefundDetailQuery.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.QUERY_TO_FETCH_REFUND_AMOUNT;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;


/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentRefundDetailRepositoryCustomImpl implements AppointmentRefundDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Double getTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_AMOUNT(refundAmountRequestDTO.getDoctorId(),
                refundAmountRequestDTO.getSpecializationId(), refundAmountRequestDTO.getHospitalId()))
                .setParameter(TO_DATE, refundAmountRequestDTO.getToDate())
                .setParameter(FROM_DATE, refundAmountRequestDTO.getFromDate());

        Double count = (Double) query.getSingleResult();

        return (count == null) ? 0D : count;
    }

    @Override
    public RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO, Pageable pageable) {
        RefundStatusResponseDTO refundStatusResponseDTO = new RefundStatusResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_APPOINTMENTS(requestDTO))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        Query getTotalRefundAmount = createQuery.apply(entityManager, QUERY_TO_GET_TOTAL_REFUND_AMOUNT)
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        refundStatusResponseDTO.setTotalItems(query.getResultList().size());

        addPagination.accept(pageable, query);

        List<RefundStatusDTO> response = transformQueryToResultList(query, RefundStatusDTO.class);

        refundStatusResponseDTO.setRefundAppointments(response);
        refundStatusResponseDTO.setTotalRefundAmount((Double) getTotalRefundAmount.getSingleResult());

        return refundStatusResponseDTO;
    }

    @Override
    public AppointmentRefundDetail fetchAppointmentRefundDetail(RefundStatusRequestDTO requestDTO) {
        try {
            AppointmentRefundDetail refundDetail = entityManager.createQuery(QUERY_TO_GET_APPOINTMENT_REFUND_DETAILS,
                    AppointmentRefundDetail.class)
                    .setParameter(ESEWA_ID, requestDTO.getEsewaId())
                    .setParameter(TRANSACTION_NUMBER, requestDTO.getTransactionNumber())
                    .getSingleResult();

            return refundDetail;
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND, APPOINTMENT_REFUND_DETAIL);
            throw APPOINTMENT_REFUND_DETAIL_NOT_FOUND.get();
        }
    }

    @Override
    public AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_REFUNDED_DETAIL_BY_ID)
                .setParameter(APPOINTMENT_ID, appointmentId);
        try {
            return transformQueryToSingleResult(query, AppointmentRefundDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_DETAILS_NOT_FOUND.apply(appointmentId);
        }
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_DETAILS_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException
                (Appointment.class, "appointmentId", appointmentId.toString());
    };

    private Supplier<NoContentFoundException> APPOINTMENT_REFUND_DETAIL_NOT_FOUND = ()
            -> new NoContentFoundException(AppointmentRefundDetail.class);
}
