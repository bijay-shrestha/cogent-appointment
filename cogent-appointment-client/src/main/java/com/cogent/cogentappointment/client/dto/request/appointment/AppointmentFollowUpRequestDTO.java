package com.cogent.cogentappointment.client.dto.request.appointment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 16/02/20
 */
@Getter
@Setter
public class AppointmentFollowUpRequestDTO implements Serializable {

    private Date appointmentDate;

    private Long patientId;

    private Long doctorId;

    private Long specializationId;

    private Long hospitalId;
}
