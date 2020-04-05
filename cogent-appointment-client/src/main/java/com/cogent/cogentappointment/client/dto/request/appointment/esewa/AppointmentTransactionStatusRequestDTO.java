package com.cogent.cogentappointment.client.dto.request.appointment.esewa;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 31/03/20
 */
@Getter
@Setter
public class AppointmentTransactionStatusRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String transactionNumber;

    @NotNull
    @NotEmpty
    private String patientName;
}
