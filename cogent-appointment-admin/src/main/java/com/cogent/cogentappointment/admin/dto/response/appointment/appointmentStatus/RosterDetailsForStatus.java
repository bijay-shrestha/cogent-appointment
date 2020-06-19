package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/18/20
 */

@Getter
@Setter
public class RosterDetailsForStatus implements Serializable {

    private Long rosterId;

    private Integer rosterGapDuration;

    private String startTime;

    private String endTime;

    private Character dayOffStatus;

    private String weekDayName;

    private Character hasRosterOverRide;
}
