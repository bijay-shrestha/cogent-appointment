package com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
public class AppointmentDetailRequestDTO implements Serializable {

    private Date date;

    private Long doctorId;

    private Long specializationId;

    private Long hospitalId;
}
