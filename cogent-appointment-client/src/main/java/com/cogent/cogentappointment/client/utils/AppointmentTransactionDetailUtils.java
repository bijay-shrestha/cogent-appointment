package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.eSewa.AppointmentTransactionRequestDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentappointment.persistence.model.Appointment;

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
