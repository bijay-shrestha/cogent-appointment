package com.cogent.cogentappointment.client.dto.request.appointment;

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
public class AppointmentCountRequestDTO implements Serializable {

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    @NotNull
    private Long hospitalId;
}
