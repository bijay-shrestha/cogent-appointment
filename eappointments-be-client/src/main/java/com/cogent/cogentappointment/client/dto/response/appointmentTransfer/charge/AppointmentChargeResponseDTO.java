package com.cogent.cogentappointment.client.dto.response.appointmentTransfer.charge;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/8/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentChargeResponseDTO implements Serializable {

    private Double actualCharge,followUpCharge;
}
