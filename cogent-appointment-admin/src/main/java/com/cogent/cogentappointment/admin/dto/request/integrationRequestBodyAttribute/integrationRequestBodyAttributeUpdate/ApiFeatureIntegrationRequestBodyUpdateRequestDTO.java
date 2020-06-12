package com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.integrationRequestBodyAttributeUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/06/01-11:20 AM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiFeatureIntegrationRequestBodyUpdateRequestDTO implements Serializable {

    private Long featureId;

    private List<ApiIntegrationRequestBodyUpdateRequestDTO> requestBodyUpdateRequestDTOS;

    private String remarks;

}
