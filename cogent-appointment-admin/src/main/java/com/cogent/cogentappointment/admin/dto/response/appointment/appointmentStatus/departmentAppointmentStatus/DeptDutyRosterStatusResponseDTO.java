package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
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
public class DeptDutyRosterStatusResponseDTO implements Serializable {

    private LocalDate date;

    private String startTime;

    private String endTime;

    private Character dayOffStatus;

    private Integer rosterGapDuration;

    private Long departmentId;

    private String departmentName;

    private Long roomId;

    private String roomNumber;

    private String weekDayName;

    /*FOR FRONT-END CONVENIENCE TO SHOW DETAIL MODAL*/
    private String patientDetails;

    private List<AppointmentTimeSlotResponseDTO> doctorTimeSlots;
}
