package com.cogent.cogentappointment.esewa.dto.request.appointment;

import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestForDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 29/02/20
 */
@Getter
@Setter
public class AppointmentRequestDTOForOthers implements Serializable {

    private PatientRequestByDTO requestBy;

    private PatientRequestForDTO requestFor;

    private AppointmentTransactionRequestDTO transactionInfo;

    private AppointmentRequestDTO appointmentInfo;
}
