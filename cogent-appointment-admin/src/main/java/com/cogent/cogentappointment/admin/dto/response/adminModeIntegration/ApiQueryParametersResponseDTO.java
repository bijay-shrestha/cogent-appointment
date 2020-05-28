package com.cogent.cogentappointment.admin.dto.response.adminModeIntegration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiQueryParametersResponseDTO implements Serializable {

    private Long id;

    private String keyParam;

    private String valueParam;

    private String description;

    private Character status;


}
