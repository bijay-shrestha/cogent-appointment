package com.cogent.cogentappointment.client.dto.request.appointmentTransfer;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/7/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorChargeRequestDTO implements Serializable{

    @NotNull
    private Long doctorId;

    @NotNull
    private Long specializationId;

    @Status(message = "Must be Y/N")
    private Character followUp;
}
