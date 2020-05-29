package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability;

import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 29/05/20
 */
@Getter
@Setter
public class AppointmentHospitalDeptCheckAvailabilityResponseDTO extends StatusResponseDTO implements Serializable {

    private Date queryDate;

    private Character hasRoom;

    private AppointmentHospitalDeptCheckAvailabilityWithoutRoomResponseDTO withoutRoomInfo;

    private List<AppointmentHospitalDeptCheckAvailabilityWithRoomResponseDTO> withRoomInfo;
}
