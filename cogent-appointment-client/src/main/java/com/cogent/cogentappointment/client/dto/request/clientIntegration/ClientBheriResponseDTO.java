package com.cogent.cogentappointment.client.dto.request.clientIntegration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientBheriResponseDTO implements Serializable {

    private int statusCode;
    private String responseMessage;
    private String responseData;
}
