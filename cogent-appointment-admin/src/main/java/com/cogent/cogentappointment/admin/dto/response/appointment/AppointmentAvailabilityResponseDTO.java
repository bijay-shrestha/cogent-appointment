package com.cogent.cogentappointment.admin.dto.response.appointment;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti ON 09/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentAvailabilityResponseDTO implements Serializable {

    private String startTime;

    private String endTime;
}
