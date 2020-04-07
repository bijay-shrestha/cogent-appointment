package com.cogent.cogentappointment.logging.dto.response;

import lombok.*;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminLogStaticsResponseDTO {

    private String feature;

    private Long count;
}
