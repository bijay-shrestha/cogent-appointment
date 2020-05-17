package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRBreakDetailRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 11/05/20
 */
@Getter
@Setter
public class DDROverrideDetailRequestDTO implements Serializable {

    private Date date;

    private Long shiftId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date endTime;

    private Integer rosterGapDuration;

    @NotNull
    @Status
    private Character offStatus;

    @NotNull
    @Status
    private Character hasBreak;

    private Character status;

    private String remarks;

    private List<DDRBreakDetailRequestDTO> breakDetail;
}
