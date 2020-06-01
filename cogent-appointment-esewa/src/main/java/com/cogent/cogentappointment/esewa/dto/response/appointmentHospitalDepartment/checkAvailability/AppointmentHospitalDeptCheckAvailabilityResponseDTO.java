package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability;

import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 29/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHospitalDeptCheckAvailabilityResponseDTO implements Serializable {

    private Date queryDate;

    private Character hasRoom;

    private List<HospitalDeptDutyRosterRoomInfoResponseDTO> roomInfo;

//    private AppointmentHospitalDeptCheckAvailabilityWithoutRoomResponseDTO withoutRoomInfo;

    private Long roomId;

    private String roomNumber;

    private String hospitalDepartmentAvailableTime;

    private List<String> availableTimeSlots;
}
