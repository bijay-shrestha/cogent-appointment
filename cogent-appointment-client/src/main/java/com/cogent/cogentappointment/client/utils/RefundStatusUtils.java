package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.util.Date;
import java.util.function.BiFunction;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.REFUNDED;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class RefundStatusUtils {


    public static BiFunction<Appointment,String,Appointment> changeAppointmentStatus=(appointment,remarks) -> {
        appointment.setStatus(REFUNDED);
        appointment.setRemarks(remarks);

        return appointment;
    };

    public static BiFunction<Appointment,String,Appointment> defaultAppointmentStatusChange=(appointment,remarks) -> {
        appointment.setRemarks(remarks);

        return appointment;
    };

    public static BiFunction<AppointmentRefundDetail,String,AppointmentRefundDetail> changeAppointmentRefundDetailStatus
            =(refundDetail,remarks) -> {
        refundDetail.setRefundedDate(new Date());
        refundDetail.setStatus(APPROVED);
        refundDetail.setRemarks(remarks);

        return refundDetail;
    };

    public static BiFunction<AppointmentRefundDetail,String,AppointmentRefundDetail>
            defaultAppointmentRefundDetailStatusChange=(refundDetail,remarks) -> {

        refundDetail.setRemarks(remarks);

        return refundDetail;
    };

//    public static EsewaPayementStatus parseToEsewaPayementStatus(RefundStatusRequestDTO requestDTO){
//        return  EsewaPayementStatus.builder()
//                .esewa_id(requestDTO.getEsewaId())
//                .product_code(requestDTO.getEsewaMerchantCode())
//                .transaction_code(requestDTO.getTransactionNumber())
//                .build();
//    }
}
