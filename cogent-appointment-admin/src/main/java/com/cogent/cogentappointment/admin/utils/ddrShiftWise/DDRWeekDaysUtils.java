package com.cogent.cogentappointment.admin.utils.ddrShiftWise;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRWeekDaysDetailRequestDTO;
import com.cogent.cogentappointment.persistence.model.WeekDays;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRShiftDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRWeekDaysDetail;

/**
 * @author smriti on 10/05/20
 */
public class DDRWeekDaysUtils {

    public static DDRWeekDaysDetail parseToDDRWeekDaysDetail(DDRWeekDaysDetailRequestDTO weekDaysRequestDTO,
                                                             DDRShiftDetail ddrShiftDetail,
                                                             WeekDays weekDays) {

        DDRWeekDaysDetail ddrWeekDaysDetail = new DDRWeekDaysDetail();
        ddrWeekDaysDetail.setStartTime(weekDaysRequestDTO.getStartTime());
        ddrWeekDaysDetail.setEndTime(weekDaysRequestDTO.getEndTime());
        ddrWeekDaysDetail.setOffStatus(weekDaysRequestDTO.getOffStatus());
        ddrWeekDaysDetail.setHasBreak(weekDaysRequestDTO.getHasBreak());
        ddrWeekDaysDetail.setDdrShiftDetail(ddrShiftDetail);
        ddrWeekDaysDetail.setWeekDays(weekDays);
        return ddrWeekDaysDetail;
    }
}
