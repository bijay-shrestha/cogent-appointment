package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentTransactionRequestDTO;
import com.cogent.cogentappointment.client.model.Appointment;
import com.cogent.cogentappointment.client.model.AppointmentTransactionDetail;

/**
 * @author smriti ON 04/02/2020
 */
public class AppointmentTransactionDetailUtils {

    public static AppointmentTransactionDetail parseToAppointmentTransactionInfo(
            AppointmentTransactionRequestDTO requestDTO,
            Appointment appointment) {

        AppointmentTransactionDetail transactionDetail = new AppointmentTransactionDetail();
        transactionDetail.setAppointment(appointment);
        transactionDetail.setTransactionDate(requestDTO.getTransactionDate());
        transactionDetail.setTransactionNumber(requestDTO.getTransactionNumber());
        transactionDetail.setAppointmentAmount(requestDTO.getAppointmentAmount());
        transactionDetail.setDiscountAmount(requestDTO.getDiscountAmount());
        transactionDetail.setServiceChargeAmount(requestDTO.getServiceChargeAmount());
        transactionDetail.setTaxAmount(requestDTO.getTaxAmount());
        return transactionDetail;
    }
}
