package com.cogent.cogentappointment.esewa.dto.eSewa;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DutyRosterOverrideAppointmentDate implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Character dayOff;
}
