package com.cogent.cogentthirdpartyconnector.utils;

import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.request.Properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, Object> getDynamicEsewaRequestBodyLog(List<String> requestBody,
                                                                    Appointment appointment,
                                                                    AppointmentTransactionDetail transactionDetail,
                                                                    AppointmentRefundDetail appointmentRefundDetail,
                                                                    Boolean isRefund) {

        Map<String, Object> map = new HashMap<>();

        requestBody.forEach(parameter -> {


            if (parameter.equalsIgnoreCase("esewa_id")) {
                map.put("esewa_id", appointment.getPatientId().getESewaId());
            }

            if (parameter.equalsIgnoreCase("txn_amount")) {
                map.put("txn_amount", transactionDetail.getAppointmentAmount());
            }

            if (parameter.equalsIgnoreCase("refund_amount")) {
                map.put("refund_amount", appointmentRefundDetail.getRefundAmount());
            }

            if (parameter.equalsIgnoreCase("product_code")) {
                map.put("product_code", appointment.getHospitalId().getEsewaMerchantCode());
            }

            if (parameter.equalsIgnoreCase("is_refund")) {
                map.put("is_refund", isRefund);
            }

            if (parameter.equalsIgnoreCase("remarks")) {
                map.put("remarks", "refund");
            }

        });

        return map;


    }
}
