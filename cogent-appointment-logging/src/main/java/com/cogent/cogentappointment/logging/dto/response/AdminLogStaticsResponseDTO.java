package com.cogent.cogentappointment.logging.dto.response;

import lombok.*;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminLogStaticsResponseDTO implements Serializable {

    private String feature;

    private Long count;
}
