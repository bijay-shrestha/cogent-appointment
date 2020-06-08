package com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate;

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
public class ClientApiQueryParamtersUpdateRequestDTO implements Serializable {

    private Long id;

    private String keyParam;

    private String valueParam;

    private Character Status;

}
