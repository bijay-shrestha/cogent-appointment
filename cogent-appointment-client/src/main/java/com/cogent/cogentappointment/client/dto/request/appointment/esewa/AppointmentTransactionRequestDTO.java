package com.cogent.cogentappointment.client.dto.request.appointment.esewa;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 04/02/2020
 */
@Getter
@Setter
public class AppointmentTransactionRequestDTO implements Serializable {

    private Date transactionDate;

    private String transactionNumber;

    private Double appointmentAmount;

    private Double taxAmount;

    private Double discountAmount;

    private Double serviceChargeAmount;

    private String appointmentModeCode;
}
