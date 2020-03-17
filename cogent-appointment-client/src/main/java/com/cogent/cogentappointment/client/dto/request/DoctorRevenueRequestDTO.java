package com.cogent.cogentappointment.client.dto.request;

import lombok.*;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRevenueRequestDTO implements Serializable {

    private Long doctorId;

    private Long hospitalId;

    private Long specializationId;
}
