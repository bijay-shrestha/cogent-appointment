package com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/02-9:01 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationRequestBodyDetailResponseDTO extends AuditableResponseDTO implements Serializable{

    private String featureName;

    private String requestBody;
}
