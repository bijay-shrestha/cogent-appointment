package com.cogent.cogentappointment.client.dto.request.appointment.eSewa;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestByDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 29/02/20
 */
@Getter
@Setter
public class AppointmentRequestDTOForSelf implements Serializable {

    private PatientRequestByDTO patientInfo;

    private AppointmentTransactionRequestDTO transactionInfo;

    private AppointmentRequestDTO appointmentInfo;
}
