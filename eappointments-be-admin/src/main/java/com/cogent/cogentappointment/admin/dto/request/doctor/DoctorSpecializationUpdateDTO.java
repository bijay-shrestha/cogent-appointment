package com.cogent.cogentappointment.admin.dto.request.doctor;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-29
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSpecializationUpdateDTO implements Serializable {

    private Long doctorSpecializationId;

    @NotNull
    private Long specializationId;

    @NotNull
    @Status
    private Character status;
}
