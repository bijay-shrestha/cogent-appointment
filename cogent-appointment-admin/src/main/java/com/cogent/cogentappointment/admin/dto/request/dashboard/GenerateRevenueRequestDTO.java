package com.cogent.cogentappointment.admin.dto.request.dashboard;

import lombok.Getter;
import lombok.Setter;

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

    @NotNull
    private Long hospitalId;

}
