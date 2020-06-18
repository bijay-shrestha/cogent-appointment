package com.cogent.cogentappointment.client.dto.request.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/10-10:59 AM
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationBackendRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    private String featureCode;

    private boolean patientStatus;

    /*true -> no hospital number | new registration patient
    * false -> hospital number   | registered patient*/
    private boolean isPatientNew;

    private String integrationChannelCode;

    private String hospitalNumber;


}
