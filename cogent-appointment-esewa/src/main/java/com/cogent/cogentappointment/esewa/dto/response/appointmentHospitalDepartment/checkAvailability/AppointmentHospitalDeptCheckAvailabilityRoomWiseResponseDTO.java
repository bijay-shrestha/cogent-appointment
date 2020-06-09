package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability;

import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 02/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO extends StatusResponseDTO
        implements Serializable {

    private Date queryDate;

    private String roomNumber;

    private String hospitalDepartmentAvailableTime;

    private List<String> availableTimeSlots;
}
