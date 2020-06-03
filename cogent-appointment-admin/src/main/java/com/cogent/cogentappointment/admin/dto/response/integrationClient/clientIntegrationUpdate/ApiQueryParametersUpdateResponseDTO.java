package com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-26
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiQueryParametersUpdateResponseDTO implements Serializable {

    private Long id;

    private String keyParam;

    private String valueParam;

    private String description;

    private Character Status;
}
