package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 08/05/20
 */
@Getter
@Setter
public class DDRWeekDaysRequestDTO implements Serializable {

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    @Status
    private Character offStatus;

    @NotNull
    private Long weekDaysId;

    private List<DDRBreakRequestDTO> breakDetail;
}
