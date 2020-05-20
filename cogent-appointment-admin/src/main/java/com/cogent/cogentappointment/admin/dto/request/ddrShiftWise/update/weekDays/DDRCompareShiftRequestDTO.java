package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.weekDays;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 19/05/20
 */
@Getter
@Setter
public class DDRCompareShiftRequestDTO implements Serializable {

    private List<Long> ddrShiftDetailId;

    private List<Long> weekDaysId;
}
