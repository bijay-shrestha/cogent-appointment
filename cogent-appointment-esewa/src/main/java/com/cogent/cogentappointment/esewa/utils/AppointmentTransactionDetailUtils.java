package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentTransactionRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;

import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.addCurrentTimeToDate;

/**
 * @author smriti ON 04/02/2020
 */
public class AppointmentTransactionDetailUtils {

    public static AppointmentTransactionDetail parseToAppointmentTransactionInfo(
            AppointmentTransactionRequestDTO requestDTO,
            Appointment appointment) {

        AppointmentTransactionDetail transactionDetail = new AppointmentTransactionDetail();
        transactionDetail.setAppointment(appointment);
        transactionDetail.setTransactionDate(addCurrentTimeToDate(requestDTO.getTransactionDate()));
        transactionDetail.setTransactionNumber(requestDTO.getTransactionNumber());
        transactionDetail.setAppointmentAmount(requestDTO.getAppointmentAmount());
        transactionDetail.setDiscountAmount(requestDTO.getDiscountAmount());
        transactionDetail.setServiceChargeAmount(requestDTO.getServiceChargeAmount());
        transactionDetail.setTaxAmount(requestDTO.getTaxAmount());
        return transactionDetail;
    }
}
