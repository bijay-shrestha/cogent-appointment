package com.cogent.cogentappointment.admin.dto.request.hospital;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 26/05/20
 */
@Getter
@Setter
public class HospitalAppointmentServiceTypeUpdateRequestDTO implements Serializable {

    private Long hospitalAppointmentServiceTypeId;

    @NotNull
    private Long appointmentServiceTypeId;

    @Status
    private Character status;
}
