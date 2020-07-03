package com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/05/29-12:44 PM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiFeatureIntegrationRequestBodyRequestDTO implements Serializable {

    private Long featureId;

    private List<Long> requestBodyAttributes;


}
