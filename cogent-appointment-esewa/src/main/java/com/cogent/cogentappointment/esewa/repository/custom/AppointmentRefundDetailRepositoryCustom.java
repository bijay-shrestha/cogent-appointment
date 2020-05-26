package com.cogent.cogentappointment.esewa.repository.custom;


import com.cogent.cogentappointment.esewa.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRefundDetailRepositoryCustom")
public interface AppointmentRefundDetailRepositoryCustom {

    AppointmentRefundDetail fetchAppointmentRefundDetail(RefundStatusRequestDTO requestDTO);
}
