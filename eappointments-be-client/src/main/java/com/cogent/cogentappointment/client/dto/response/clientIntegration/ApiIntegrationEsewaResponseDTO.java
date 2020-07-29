package com.cogent.cogentappointment.client.dto.response.clientIntegration;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/26/20
 */
@Getter
@Setter
public class ApiIntegrationEsewaResponseDTO implements Serializable {

    private String status;

    private String body;
}
