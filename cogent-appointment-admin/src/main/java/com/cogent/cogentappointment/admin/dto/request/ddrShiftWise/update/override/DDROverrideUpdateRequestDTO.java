package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 17/05/20
 */
@Getter
@Setter
public class DDROverrideUpdateRequestDTO implements Serializable {

    @NotNull
    private Long ddrId;

    @NotNull
    @Status
    private Character hasOverride;

    @NotNull
    private Long ddrOverrideId;

    @NotNull
    private Date date;

    @NotNull
    private Long shiftId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date endTime;

    @NotNull
    private Integer rosterGapDuration;

    @NotNull
    @Status
    private Character offStatus;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;

    @NotNull
    @Status
    private Character hasBreak;

    private List<DDROverrideBreakUpdateRequestDTO> breakDetail;
}
