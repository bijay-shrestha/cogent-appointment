package com.cogent.cogentappointment.admin.dto.response.integration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/05/29-1:14 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntegrationRequestBodyAttributeResponse  implements Serializable {

    private Long id;

    private String name;


}
