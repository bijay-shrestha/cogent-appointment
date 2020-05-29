package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 28/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHospitalDeptCheckAvailabilityResponseDTO implements Serializable {

    private Date queryDate;

    private String doctorAvailableTime;

    private List<String> availableTimeSlots;

    private int responseCode;

    private HttpStatus responseStatus;
}
