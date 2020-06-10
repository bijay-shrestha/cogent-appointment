package com.cogent.cogentappointment.client.dto.request.appointmentTransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/8/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDateRequestDTO implements Serializable{

    @NotNull
    private Long doctorId;

    @NotNull
    private Long specializationId;
}
