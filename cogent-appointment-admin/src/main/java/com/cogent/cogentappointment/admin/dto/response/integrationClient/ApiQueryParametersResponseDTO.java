package com.cogent.cogentappointment.admin.dto.response.integrationClient;

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
public class ApiQueryParametersResponseDTO implements Serializable {

    private String type;
}
