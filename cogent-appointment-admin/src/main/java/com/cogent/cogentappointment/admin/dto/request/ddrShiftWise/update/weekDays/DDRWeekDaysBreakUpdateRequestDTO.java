package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.weekDays;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 25/05/20
 */
@Getter
@Setter
public class DDRWeekDaysBreakUpdateRequestDTO implements Serializable {

    private Long ddrBreakId;

    private Long breakTypeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date endTime;

    private Character status;

    private String remarks;

    /*true -> updated
    * false -> not updated*/
    private Boolean isBreakUpdated;
}
