package com.cogent.cogentappointment.admin.dto.request.integration;

import lombok.*;

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

    @NotNull
    private Long hospitalId;

    private String featureCode;

    @NotNull
    private Long appointmentId;

    private boolean patientStatus;

    private String integrationChannelCode;

    private String hospitalNumber;


}
