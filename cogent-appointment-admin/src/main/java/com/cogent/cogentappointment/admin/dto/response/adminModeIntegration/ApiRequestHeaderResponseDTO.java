package com.cogent.cogentappointment.admin.dto.response.adminModeIntegration;

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
public class ApiRequestHeaderResponseDTO implements Serializable {

    private String keyName;

    private String keyValue;

}
