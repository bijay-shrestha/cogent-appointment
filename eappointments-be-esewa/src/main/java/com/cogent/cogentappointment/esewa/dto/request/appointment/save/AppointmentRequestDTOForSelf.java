package com.cogent.cogentappointment.esewa.dto.request.appointment.save;

import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestByDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author smriti on 29/02/20
 */
@Getter
@Setter
public class AppointmentRequestDTOForSelf implements Serializable {

    @Valid
    private PatientRequestByDTO patientInfo;

    @Valid
    private AppointmentTransactionRequestDTO transactionInfo;

    @Valid
    private AppointmentRequestDTO appointmentInfo;
}
