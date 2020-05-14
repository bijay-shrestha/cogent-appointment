package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 14/05/20
 */
@Getter
@Setter
public class DDRSearchRequestDTO implements Serializable {

    private Long hospitalId;

    private Long doctorId;

    private Long specializationId;

    private Date fromDate;

    private Date toDate;

    private Character status;
}
