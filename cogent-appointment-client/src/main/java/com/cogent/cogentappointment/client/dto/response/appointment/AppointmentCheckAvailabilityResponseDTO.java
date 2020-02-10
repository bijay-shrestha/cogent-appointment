package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti ON 25/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCheckAvailabilityResponseDTO implements Serializable {

    private Date queryDate;

    private String doctorAvailableTime;

    private List<String> availableTimeSlots;
}
