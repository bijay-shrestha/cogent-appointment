package com.cogent.cogentappointment.esewa.dto.request.appointment;

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

    private Long patientId;

    private Long doctorId;

    private Long specializationId;

    private Long hospitalId;

    private Date appointmentDate;

    private String appointmentTime;
}
