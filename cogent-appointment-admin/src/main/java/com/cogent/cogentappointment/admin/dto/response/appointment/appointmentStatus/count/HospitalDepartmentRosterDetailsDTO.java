package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.count;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Sauravi Thapa ON 6/28/20
 */

@Getter
@Setter
@Builder
public class HospitalDepartmentRosterDetailsDTO implements Serializable{

    private Long rosterId;

    private Integer rosterGapDuration;

    private String startTime;

    private String endTime;

    private Long hospitalDepartmentId;

    private String hospitalDepartmentName;

    private LocalDate date;
}
