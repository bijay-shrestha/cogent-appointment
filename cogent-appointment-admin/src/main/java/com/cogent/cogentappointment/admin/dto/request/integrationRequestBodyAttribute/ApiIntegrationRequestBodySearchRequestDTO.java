package com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author rupak ON 2020/05/31-9:09 PM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiIntegrationRequestBodySearchRequestDTO implements Serializable{

    private Long featureTypeId;

    private Long requestBodyId;

    private Character status;

}
