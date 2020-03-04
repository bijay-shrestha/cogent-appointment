package com.cogent.cogentappointment.client.dto.request.appointmentStatus;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 16/12/2019
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentStatusRequestDTO implements Serializable {

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    private Long doctorId;

    private Long specializationId;

    /*V= VACANT
     * PA= PENDING APPROVAL
     * A= APPROVAL
     * C= CANCELLED
     * ALL = EMPTY
     * */
    private String status;
}
