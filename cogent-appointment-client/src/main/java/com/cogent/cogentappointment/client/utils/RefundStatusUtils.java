package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class RefundStatusUtils {

    public static Appointment changeAppointmentStatus(Appointment appointment,
                                                      RefundStatusRequestDTO requestDTO){
        appointment.setStatus(requestDTO.getStatus());
        appointment.setRemarks(requestDTO.getRemarks());

        return appointment;
    }

    public static AppointmentRefundDetail changeAppointmentRefundDetailStatus(AppointmentRefundDetail appointmentRefundDetail,
                                                                  RefundStatusRequestDTO requestDTO){
        appointmentRefundDetail.setStatus(requestDTO.getStatus());
        appointmentRefundDetail.setRemarks(requestDTO.getRemarks());

        return appointmentRefundDetail;
    }
}
