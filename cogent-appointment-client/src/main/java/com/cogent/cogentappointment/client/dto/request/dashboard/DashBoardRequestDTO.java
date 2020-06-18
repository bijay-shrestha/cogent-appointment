package com.cogent.cogentappointment.client.dto.request.dashboard;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa २०/२/९
 */
@Getter
@Setter
public class DashBoardRequestDTO implements Serializable {

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    @NotNull(message = "Appointment Service Type Cannot be Null")
    private String appointmentServiceTypeCode;

}
