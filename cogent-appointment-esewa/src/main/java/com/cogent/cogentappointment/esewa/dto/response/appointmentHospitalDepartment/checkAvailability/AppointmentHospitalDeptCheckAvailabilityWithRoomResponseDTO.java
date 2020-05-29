package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 29/05/20
 */
@Getter
@Setter
public class AppointmentHospitalDeptCheckAvailabilityWithRoomResponseDTO implements Serializable{

    private Long roomId;

    private String roomNumber;

    private String hospitalDepartmentAvailableTime;

    private List<String> availableTimeSlots;
}
