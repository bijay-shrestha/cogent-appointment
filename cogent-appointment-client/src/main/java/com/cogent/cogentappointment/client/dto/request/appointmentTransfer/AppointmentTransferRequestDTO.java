package com.cogent.cogentappointment.client.dto.request.appointmentTransfer;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/10/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTransferRequestDTO implements Serializable{

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Long doctorId;

    private Date appointmentDate;

    @NotNull
    @NotBlank
    @NotEmpty
    private String appointmentTime;

    @NotNull
    private Double appointmentCharge;

    @NotNull
    @NotBlank
    @NotEmpty
    private String remarks;

    @Status(message = "Must be Y/N")
    private Character isFollowUp;
}
