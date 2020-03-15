package com.cogent.cogentappointment.client.dto.response.eSewa;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDutyRosterAppointmentDateAndSpecilizationDTO implements Serializable {

    private Long id;

    private Character hasOverride;

    private Date fromDate;

    private Date toDate;

    private Long specilizationId;

    private String specilizationName;
}
