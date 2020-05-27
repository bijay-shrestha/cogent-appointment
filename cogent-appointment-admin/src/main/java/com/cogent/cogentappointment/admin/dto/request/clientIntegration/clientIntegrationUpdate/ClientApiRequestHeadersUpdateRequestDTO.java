package com.cogent.cogentappointment.admin.dto.request.clientIntegration.clientIntegrationUpdate;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-27
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiRequestHeadersUpdateRequestDTO implements Serializable {

    private Long id;

    private String keyParam;

    private String valueParam;

    private String description;

    private Character Status;



}
