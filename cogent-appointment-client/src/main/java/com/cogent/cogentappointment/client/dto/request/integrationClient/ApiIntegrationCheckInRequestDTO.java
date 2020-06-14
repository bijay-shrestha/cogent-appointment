package com.cogent.cogentappointment.client.dto.request.integrationClient;

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
public class ApiIntegrationCheckInRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private String hospitalNumber;

    @NotNull
    private boolean status;
}
