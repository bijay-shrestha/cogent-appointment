package com.cogent.cogentappointment.client.dto.request.appointmentTransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/7/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorChargeRequestDTO implements Serializable{

    private Long doctorId;

    private Long specializationId;
}
