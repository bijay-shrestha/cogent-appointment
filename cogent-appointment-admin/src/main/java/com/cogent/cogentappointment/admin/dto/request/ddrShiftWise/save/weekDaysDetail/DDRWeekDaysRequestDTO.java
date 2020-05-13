package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date endTime;

    @NotNull
    @Status
    private Character offStatus;

    @NotNull
    @Status
    private Character hasBreak;

    @NotNull
    private Long weekDaysId;

    private List<DDRBreakRequestDTO> breakDetail;
}
