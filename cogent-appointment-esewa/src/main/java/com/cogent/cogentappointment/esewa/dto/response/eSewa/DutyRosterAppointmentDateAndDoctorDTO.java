package com.cogent.cogentappointment.esewa.dto.response.eSewa;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DutyRosterAppointmentDateAndDoctorDTO implements Serializable {

    private Long id;

    private Character hasOverride;

    private Date fromDate;

    private Date toDate;

    private Long doctorId;

    private String doctorName;
}
