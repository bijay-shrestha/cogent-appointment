package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 12/05/20
 */
@Getter
@Setter
public class DDRExistingAvailabilityRequestDTO implements Serializable {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;
}
