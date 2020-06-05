package com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 6/5/20
 */
@Setter
@Getter
public class DeptAppointmentStatusRequestDTO implements Serializable {
    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    private Long hospitalId;

    private Long departmentId;

    private Long roomId;

    /*V= VACANT
     * PA= PENDING APPROVAL
     * A= APPROVAL
     * C= CANCELLED
     * ALL = EMPTY
     * */
    private String status;
}
