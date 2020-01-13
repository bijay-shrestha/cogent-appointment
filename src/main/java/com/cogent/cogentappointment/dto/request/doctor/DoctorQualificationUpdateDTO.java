package com.cogent.cogentappointment.dto.request.doctor;

import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 13/11/2019
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorQualificationUpdateDTO implements Serializable {

    private Long doctorQualificationId;

    @NotNull
    private Long qualificationId;

    @NotNull
    @Status
    private Character status;
}
