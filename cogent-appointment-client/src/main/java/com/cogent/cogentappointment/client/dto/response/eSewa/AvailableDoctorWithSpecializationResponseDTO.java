package com.cogent.cogentappointment.client.dto.response.eSewa;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 16/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDoctorWithSpecializationResponseDTO implements Serializable {

   private  List<AvailableDoctorWithSpecialization> availableDoctorWithSpecializations;

    private int responseCode;

    private HttpStatus responseStatus;
}
