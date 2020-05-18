package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 17/05/20
 */
@Getter
@Setter
public class DDROverrideBreakUpdateRequestDTO implements Serializable {

    private Long ddrBreakId;

    private Long breakTypeId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date endTime;

    private Character status;

    private String remarks;
}
