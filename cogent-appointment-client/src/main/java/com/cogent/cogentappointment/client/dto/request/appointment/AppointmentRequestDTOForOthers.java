package com.cogent.cogentappointment.client.dto.request.appointment;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestForDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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
