package com.cogent.cogentappointment.client.dto.response.eSewa;

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
public class AvailableDatesWithDoctorResponseDTO implements Serializable {
    private List<AvailableDatesWithDoctor> availableDatesWithDoctor;

    private int status;
}
