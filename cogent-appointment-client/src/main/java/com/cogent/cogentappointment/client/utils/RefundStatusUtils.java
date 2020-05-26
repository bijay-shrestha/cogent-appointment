package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.util.Date;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.REFUNDED;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class RefundStatusUtils {

    public static final String REMARKS=" Refund Re-initiated at, " ;

    public static Function<Appointment,Appointment> changeAppointmentStatus=(appointment -> {
        appointment.setStatus(REFUNDED);
        appointment.setRemarks(REMARKS + new Date());

        return appointment;
    });

    public static Function<AppointmentRefundDetail,AppointmentRefundDetail> changeAppointmentRefundDetailStatus=(refundDetail -> {
        refundDetail.setRefundedDate(new Date());
        refundDetail.setStatus(APPROVED);
        refundDetail.setRemarks(REMARKS + new Date());

        return refundDetail;
    });

    public static EsewaPayementStatus parseToEsewaPayementStatus(RefundStatusRequestDTO requestDTO){
        return  EsewaPayementStatus.builder()
                .esewa_id(requestDTO.getEsewaId())
                .product_code(requestDTO.getEsewaMerchantCode())
                .transaction_code(requestDTO.getTransactionNumber())
                .build();
    }
}
