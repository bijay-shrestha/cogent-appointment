package com.cogent.cogentappointment.client.dto.request.appointment;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestByDTO;
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
public class AppointmentRequestDTOForSelf implements Serializable {

    private PatientRequestByDTO patientInfo;

    private AppointmentTransactionRequestDTO transactionInfo;

    private Boolean isNewRegistration;

    @NotNull
    private Long hospitalId;

    private Long patientId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    @NotEmpty
    private String appointmentTime;

    @NotNull
    @NotEmpty
    private String createdDateNepali;

    private Character isFreeFollowUp;

    private Long parentAppointmentId;
}
