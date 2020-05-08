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
public class DDRDetailRequestDTO implements Serializable{

    @NotNull
    private Long hospitalId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Integer rosterGapDuration;

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    @NotNull
    @Status
    private Character status;
}
