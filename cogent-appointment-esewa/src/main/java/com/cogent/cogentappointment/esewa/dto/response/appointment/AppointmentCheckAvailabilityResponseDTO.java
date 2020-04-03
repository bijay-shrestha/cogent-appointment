package com.cogent.cogentappointment.esewa.dto.response.appointment;

import lombok.*;
import org.springframework.http.HttpStatus;

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

    private int responseCode;

    private HttpStatus responseStatus;
}
