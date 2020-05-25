package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class RefundStatusUtils {

    public static Appointment changeAppointmentStatus(Appointment appointment){
        appointment.setStatus("A");
        appointment.setRemarks("Through refund status page");

        return appointment;
    }

    public static AppointmentRefundDetail changeAppointmentRefundDetailStatus(AppointmentRefundDetail appointmentRefundDetail){
        appointmentRefundDetail.setRefundedDate(new Date());
        appointmentRefundDetail.setStatus("RE");
        appointmentRefundDetail.setRemarks("Through refund status page");

        return appointmentRefundDetail;
    }
}
