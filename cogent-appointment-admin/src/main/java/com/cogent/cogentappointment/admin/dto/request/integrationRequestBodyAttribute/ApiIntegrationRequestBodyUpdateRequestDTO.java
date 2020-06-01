package com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/01-11:47 AM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiIntegrationRequestBodyUpdateRequestDTO implements Serializable {

    private Long requestBodyId;

    private Character status;

}
