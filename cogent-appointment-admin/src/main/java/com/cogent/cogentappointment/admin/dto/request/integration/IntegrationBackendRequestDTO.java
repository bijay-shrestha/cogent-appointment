package com.cogent.cogentappointment.admin.dto.request.integration;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/10-10:59 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationBackendRequestDTO implements Serializable {

    private Long hospitalId;

    @NotNull
    @NotEmpty
    private String featureCode;

    @NotNull
    @NotEmpty
    private String integrationChannelCode;

}
