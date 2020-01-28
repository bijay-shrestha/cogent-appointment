package com.cogent.cogentappointment.admin.dto.request.appointment;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * @author smriti ON 08/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    @NotEmpty
    private String remarks;
}
