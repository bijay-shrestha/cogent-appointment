package com.cogent.cogentappointment.client.repository.custom;


import com.cogent.cogentappointment.client.dto.request.dashboard.RefundAmountRequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRefundDetailRepositoryCustom")
public interface AppointmentRefundDetailRepositoryCustom {

    Double getTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO, Long hospitalId);
}
