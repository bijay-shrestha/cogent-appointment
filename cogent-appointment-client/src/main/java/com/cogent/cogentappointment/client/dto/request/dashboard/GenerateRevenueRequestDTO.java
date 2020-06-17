package com.cogent.cogentappointment.client.dto.request.dashboard;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa २०/२/१०
 */

@Getter
@Setter
public class GenerateRevenueRequestDTO implements Serializable {

    @NotNull
    private Date currentToDate;

    @NotNull
    private Date currentFromDate;

    @NotNull
    private Date previousToDate;

    @NotNull
    private Date previousFromDate;

    private Character filterType;

    private Long appointmentServiceTypeId;
}
