package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Sauravi Thapa ON 6/5/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterStatusResponseDTO implements Serializable {

    private Long hospitalDepartmentDutyRosterId;

    private String uniqueIdentifier;

    private LocalDate date;

    private String startTime;

    private String endTime;

    private Character dayOffStatus;

    private Integer rosterGapDuration;

    private Long hospitalDepartmentId;

    private String hospitalDepartmentName;

    private Long hospitalDepartmentRoomInfoId;

    private String roomNumber;

    private String weekDayName;

    /*FOR FRONT-END CONVENIENCE TO SHOW DETAIL MODAL*/
    private String patientDetails;

    private List<AppointmentTimeSlotResponseDTO> appointmentTimeSlots;

    private List<DropDownResponseDTO> roomList;
}
