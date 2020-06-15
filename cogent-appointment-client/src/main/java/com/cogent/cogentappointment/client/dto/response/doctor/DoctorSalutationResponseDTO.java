package com.cogent.cogentappointment.client.dto.response.doctor;

import lombok.*;

/**
 * @author rupak on 2020-05-13
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSalutationResponseDTO {

    private Long doctorSalutationId;

    private Long salutationId;

    private String salutationName;

}
