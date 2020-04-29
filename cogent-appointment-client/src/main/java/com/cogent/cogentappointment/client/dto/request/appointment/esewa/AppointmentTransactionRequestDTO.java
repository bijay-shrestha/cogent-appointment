package com.cogent.cogentappointment.client.dto.request.appointment.esewa;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 04/02/2020
 */
@Getter
@Setter
public class AppointmentTransactionRequestDTO implements Serializable {

    private Date transactionDate;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionNumber;

    @NotNull
    private Double appointmentAmount;

    private Double taxAmount;

    private Double discountAmount;

    private Double serviceChargeAmount;

    @NotBlank
    @NotNull
    @NotEmpty
    private String appointmentModeCode;
}
