package com.cogent.cogentappointment.admin.dto.response.clientIntegration;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientIntegrationResponseDTO implements Serializable {

    private Long clientId;

    private List<FeatureIntegrationResponseDTO> features;

}
