package com.cogent.cogentappointment.esewa.dto.response.appointmentDetails;

import lombok.*;

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
public class DoctorWithAvaliableDatesResponseDTO implements Serializable {
    private List<AvailableDatesWithDoctor> datesAndDoctors;

    private int status;
}
