package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 12/05/20
 */
@Getter
@Setter
public class DDRExistingMinDTO implements Serializable {

    private Long ddrId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date toDate;

    private Integer totalDays;

    private Integer remainingDays;

    private Character status;

}
