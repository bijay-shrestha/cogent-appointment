package com.cogent.cogentappointment.client.dto.response.integration;

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
public class IntegrationRequestBodyAttributeResponse implements Serializable {

    private String name;


}
