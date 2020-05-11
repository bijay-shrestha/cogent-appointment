package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save;

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

    private Date startTime;

    private Date endTime;

    private Character status;

}
