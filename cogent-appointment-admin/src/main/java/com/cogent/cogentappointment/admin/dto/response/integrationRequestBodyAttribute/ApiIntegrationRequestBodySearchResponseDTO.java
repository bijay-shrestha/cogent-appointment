package com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author rupak ON 2020/05/31-9:09 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiIntegrationRequestBodySearchResponseDTO extends AuditableResponseDTO implements Serializable {

    private BigInteger featureId;

    private String featureName;

    private String requestBody;

    private Character status;

}
