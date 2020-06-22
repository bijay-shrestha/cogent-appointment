package com.cogent.cogentappointment.client.dto.response.appointmentServiceType;

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
public class ApptServiceTypeDropDownResponseDTO implements Serializable {

    private String name;

    private String code;
}
