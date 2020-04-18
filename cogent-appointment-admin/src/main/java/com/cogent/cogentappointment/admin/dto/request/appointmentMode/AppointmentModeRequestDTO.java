package com.cogent.cogentappointment.admin.dto.request.appointmentMode;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi Thapa 4/17/2019
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentModeRequestDTO implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    @Status
    private Character isEditable;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private String description;

}
