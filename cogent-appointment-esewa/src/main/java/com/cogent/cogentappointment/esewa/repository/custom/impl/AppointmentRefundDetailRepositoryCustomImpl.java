package com.cogent.cogentappointment.esewa.repository.custom.impl;


import com.cogent.cogentappointment.esewa.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.AppointmentRefundDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.TRANSACTION_NUMBER;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.ESEWA_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.ESEWA_MERCHANT_CODE;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentLog.APPOINTMENT_REFUND_DETAIL;
import static com.cogent.cogentappointment.esewa.query.AppointmentRefundDetailQuery.QUERY_TO_GET_APPOINTMENT_REFUND_DETAILS;


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
    public AppointmentRefundDetail fetchAppointmentRefundDetail(RefundStatusRequestDTO requestDTO) {
        try {
            AppointmentRefundDetail refundDetail = entityManager.createQuery(QUERY_TO_GET_APPOINTMENT_REFUND_DETAILS,
                    AppointmentRefundDetail.class)
                    .setParameter(ESEWA_ID, requestDTO.getEsewaId())
                    .setParameter(ESEWA_MERCHANT_CODE, requestDTO.getEsewaMerchantCode())
                    .setParameter(TRANSACTION_NUMBER, requestDTO.getTransactionNumber())
                    .getSingleResult();

            return refundDetail;
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND, APPOINTMENT_REFUND_DETAIL);
            throw APPOINTMENT_REFUND_DETAIL_NOT_FOUND.get();
        }
    }

    private Supplier<NoContentFoundException> APPOINTMENT_REFUND_DETAIL_NOT_FOUND = ()
            -> new NoContentFoundException(AppointmentRefundDetail.class);


}

