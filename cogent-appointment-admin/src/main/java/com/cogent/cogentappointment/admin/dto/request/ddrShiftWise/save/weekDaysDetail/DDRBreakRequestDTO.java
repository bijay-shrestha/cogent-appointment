package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 08/05/20
 */
@Getter
@Setter
public class DDRBreakRequestDTO implements Serializable {

    private Long breakTypeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Kathmandu")
    private Date endTime;

    private Character status;

    private String remarks;
}
