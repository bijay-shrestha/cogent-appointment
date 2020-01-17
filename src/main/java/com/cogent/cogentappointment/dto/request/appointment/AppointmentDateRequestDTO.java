package com.cogent.cogentappointment.dto.request.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 17/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDateRequestDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long doctorId;

    private Long specializationId;
}
