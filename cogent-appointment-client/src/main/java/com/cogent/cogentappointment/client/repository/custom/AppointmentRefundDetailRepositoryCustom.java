package com.cogent.cogentappointment.client.repository.custom;


import com.cogent.cogentappointment.client.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRefundDetailRepositoryCustom")
public interface AppointmentRefundDetailRepositoryCustom {

    Double getTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO, Long hospitalId);

    RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO);
}
