package com.cogent.cogentappointment.client.dto.request.integrationClient;

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
public class ApiIntegrationRequestDTO implements Serializable {

    private Long hospitalId;
}
