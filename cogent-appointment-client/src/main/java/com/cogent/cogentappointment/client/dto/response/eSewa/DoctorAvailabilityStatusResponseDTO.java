package com.cogent.cogentappointment.client.dto.response.eSewa;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAvailabilityStatusResponseDTO implements Serializable {

    private Character status;

    private String message;
}
