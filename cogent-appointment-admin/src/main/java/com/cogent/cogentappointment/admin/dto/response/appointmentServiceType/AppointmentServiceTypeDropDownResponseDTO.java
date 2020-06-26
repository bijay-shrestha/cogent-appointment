package com.cogent.cogentappointment.admin.dto.response.appointmentServiceType;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentServiceTypeDropDownResponseDTO implements Serializable {

    private String name;

    private String code;

    private Character isPrimary;
}
