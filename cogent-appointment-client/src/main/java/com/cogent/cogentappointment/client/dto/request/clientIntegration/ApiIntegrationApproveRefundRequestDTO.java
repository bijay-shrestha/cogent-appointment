package com.cogent.cogentappointment.client.dto.request.clientIntegration;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-21
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiIntegrationApproveRefundRequestDTO implements Serializable {

    private Long id;

    private String status;

    private String body;
}
