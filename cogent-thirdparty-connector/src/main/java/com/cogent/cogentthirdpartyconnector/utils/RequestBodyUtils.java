package com.cogent.cogentthirdpartyconnector.utils;

import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.request.Properties;

/**
 * @author rupak ON 2020/06/11-4:47 PM
 */
public class RequestBodyUtils {

    public static EsewaRefundRequestDTO getEsewaRequestBody(Appointment appointment,
                                                            AppointmentTransactionDetail transactionDetail,
                                                            AppointmentRefundDetail appointmentRefundDetail,
                                                            Boolean isRefund) {

        EsewaRefundRequestDTO esewaRefundRequestDTO = EsewaRefundRequestDTO.builder()
                .esewa_id(appointment.getPatientId().getESewaId())
                .is_refund(isRefund)
                .refund_amount(appointmentRefundDetail.getRefundAmount())
                .product_code(appointment.getHospitalId().getEsewaMerchantCode())
                .remarks("refund")
                .txn_amount(transactionDetail.getAppointmentAmount())
                .properties(Properties.builder()
                        .appointmentId(appointment.getId())
                        .hospitalName(appointment.getHospitalId().getName())
                        .build())
                .build();

        return esewaRefundRequestDTO;
    }
}
