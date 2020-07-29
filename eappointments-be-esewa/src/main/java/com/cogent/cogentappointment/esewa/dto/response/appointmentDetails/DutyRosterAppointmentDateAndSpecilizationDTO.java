package com.cogent.cogentappointment.esewa.dto.response.appointmentDetails;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DutyRosterAppointmentDateAndSpecilizationDTO implements Serializable {

    private Long id;

    private Character hasOverride;

    private Date fromDate;

    private Date toDate;

    private Long specializationId;

    private String specializationName;
}
