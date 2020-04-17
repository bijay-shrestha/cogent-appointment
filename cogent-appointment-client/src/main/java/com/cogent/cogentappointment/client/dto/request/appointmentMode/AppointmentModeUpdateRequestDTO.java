package com.cogent.cogentappointment.client.dto.request.appointmentMode;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 4/17/2019
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentModeUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String code;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;

    @NotNull
    @Status
    private Character isEditable;
}
