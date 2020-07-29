package com.cogent.cogentappointment.esewa.dto.response.appointmentDetails;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDatesWithDoctorResponseDTO implements Serializable {
    private List<AvailableDatesWithDoctor> availableDatesWithDoctor;

    private int responseCode;

    private HttpStatus responseStatus;
}
