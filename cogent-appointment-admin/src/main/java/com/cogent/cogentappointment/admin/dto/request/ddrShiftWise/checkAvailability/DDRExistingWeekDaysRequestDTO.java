package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
public class DDRExistingWeekDaysRequestDTO implements Serializable {

    @NotNull
    private Long ddrId;

    @NotNull
    private Long shiftId;
}
