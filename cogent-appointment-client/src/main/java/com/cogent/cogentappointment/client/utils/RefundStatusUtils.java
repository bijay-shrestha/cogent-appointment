package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.*;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class RefundStatusUtils {

    public static final String REMARKS="AMBIGUOUS" ;

    public static BiFunction<Appointment,String,Appointment> changeAppointmentStatus=(appointment,remarks) -> {
        appointment.setStatus(REFUNDED);
        appointment.setRemarks(remarks);

        return appointment;
    };

    public static Function<Appointment,Appointment> defaultAppointmentStatusChange=(appointment -> {
        appointment.setStatus(CANCELLED);
        appointment.setRemarks(REMARKS);

        return appointment;
    });

    public static BiFunction<AppointmentRefundDetail,String,AppointmentRefundDetail> changeAppointmentRefundDetailStatus
            =(refundDetail,remarks) -> {
        refundDetail.setRefundedDate(new Date());
        refundDetail.setStatus(APPROVED);
        refundDetail.setRemarks(remarks);

        return refundDetail;
    };

    public static Function<AppointmentRefundDetail,AppointmentRefundDetail> defaultAppointmentRefundDetailStatusChange=(refundDetail -> {
        refundDetail.setRefundedDate(new Date());
        refundDetail.setStatus(PENDING_APPROVAL);
        refundDetail.setRemarks(REMARKS);

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
