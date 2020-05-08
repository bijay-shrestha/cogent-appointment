package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 08/05/20
 */
@Getter
@Setter
public class DDRBreakRequestDTO implements Serializable {

    @NotNull
    private Long breakTypeId;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    @Status
    private Character status;

}
