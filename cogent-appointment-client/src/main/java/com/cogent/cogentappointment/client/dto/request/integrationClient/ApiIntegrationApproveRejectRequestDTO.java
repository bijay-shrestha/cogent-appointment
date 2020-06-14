package com.cogent.cogentappointment.client.dto.request.clientIntegration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-21
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiIntegrationApproveRejectRequestDTO implements Serializable {

    private Long id;

    private String status;

    private String remarks;
}
