package com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author smriti ON 14/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationDutyRosterStatusResponseDTO implements Serializable {

    private LocalDate date;

    private String startTime;

    private String endTime;

    private Character dayOffStatus;

    private Integer rosterGapDuration;

    private Long specializationId;

    private String specializationName;

    private String weekDayName;

    /*FOR FRONT-END CONVENIENCE TO SHOW DETAIL MODAL*/
    private String patientDetails;

    private List<PatientTimeSlotResponseDTO> patientTimeSlot;
}
