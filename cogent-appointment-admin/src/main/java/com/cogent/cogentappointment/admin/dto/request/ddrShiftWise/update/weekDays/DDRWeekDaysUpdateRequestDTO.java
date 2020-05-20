package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.weekDays;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 19/05/20
 */
@Getter
@Setter
public class DDRWeekDaysUpdateRequestDTO implements Serializable {

    private DDREditedShiftDetailRequestDTO editedShiftDetails;

    private DDRCompareShiftRequestDTO compareShiftDetails;
}
