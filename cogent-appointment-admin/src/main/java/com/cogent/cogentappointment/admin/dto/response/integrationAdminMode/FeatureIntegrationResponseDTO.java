package com.cogent.cogentappointment.admin.dto.response.integrationAdminMode;

import com.cogent.cogentappointment.admin.dto.response.integration.ApiInfoResponseDTO;
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
public class FeatureIntegrationResponseDTO implements Serializable {

    private String featureCode;

    private String integrationChannelCode;

    private ApiInfoResponseDTO apiInfo;

}
