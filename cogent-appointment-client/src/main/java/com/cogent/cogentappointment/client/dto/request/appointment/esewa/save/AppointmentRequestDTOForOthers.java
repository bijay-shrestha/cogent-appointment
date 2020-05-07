package com.cogent.cogentappointment.client.dto.request.appointment.esewa.save;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestForDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author smriti on 29/02/20
 */
@Getter
@Setter
public class AppointmentRequestDTOForOthers implements Serializable {

    @Valid
    private PatientRequestByDTO requestBy;

    @Valid
    private PatientRequestForDTO requestFor;

    @Valid
    private AppointmentTransactionRequestDTO transactionInfo;

    @Valid
    private AppointmentRequestDTO appointmentInfo;
}
