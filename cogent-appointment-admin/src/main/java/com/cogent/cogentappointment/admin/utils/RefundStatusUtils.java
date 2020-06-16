package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentthirdpartyconnector.request.EsewaPayementStatus;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.util.Date;
import java.util.function.BiFunction;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.*;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class RefundStatusUtils {

    public static BiFunction<Appointment, String, Appointment> changeAppointmentStatus = (appointment, remarks) -> {
        appointment.setStatus(REFUNDED);
        appointment.setRemarks(remarks);

        return appointment;
    };

    public static BiFunction<Appointment, String, Appointment> defaultAppointmentStatusChange = (appointment, remarks) -> {
        appointment.setRemarks(remarks);

        return appointment;
    };

    public static BiFunction<AppointmentRefundDetail, String, AppointmentRefundDetail> changeAppointmentRefundDetailStatus
            = (refundDetail, remarks) -> {
        refundDetail.setRefundedDate(new Date());
        refundDetail.setStatus(APPROVED);
        refundDetail.setRemarks(remarks);

        return refundDetail;
    };

    public static BiFunction<AppointmentRefundDetail, String, AppointmentRefundDetail>
            defaultAppointmentRefundDetailStatusChange = (refundDetail, remarks) -> {
        refundDetail.setRemarks(remarks);

        return refundDetail;
    };

    public static EsewaPayementStatus parseToEsewaPaymentStatus(RefundStatusRequestDTO requestDTO) {
        return EsewaPayementStatus.builder()
                .esewa_id("9841409090")
                .product_code("testBir")
                .transaction_code("5VP")
                .build();
    }
}
