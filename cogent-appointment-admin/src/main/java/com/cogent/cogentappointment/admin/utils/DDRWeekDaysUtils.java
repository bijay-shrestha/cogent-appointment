package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRWeekDaysRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRShiftDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRWeekDaysDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author smriti on 10/05/20
 */
public class DDRWeekDaysUtils {

    public static DDRShiftDetail parseToDDRShiftDetail(DoctorDutyRosterShiftWise doctorDutyRosterShiftWise,
                                                       Shift shift) {
        DDRShiftDetail ddrShiftDetail = new DDRShiftDetail();
        ddrShiftDetail.setDdrShiftWise(doctorDutyRosterShiftWise);
        ddrShiftDetail.setShift(shift);
        ddrShiftDetail.setStatus(ACTIVE);

        return ddrShiftDetail;
    }

    public static DDRWeekDaysDetail parseToDDRWeekDaysDetail(DDRWeekDaysRequestDTO weekDaysRequestDTO,
                                                             DDRShiftDetail ddrShiftDetail,
                                                             WeekDays weekDays) {

        DDRWeekDaysDetail ddrWeekDaysDetail = new DDRWeekDaysDetail();
        ddrWeekDaysDetail.setStartTime(weekDaysRequestDTO.getStartTime());
        ddrWeekDaysDetail.setEndTime(weekDaysRequestDTO.getEndTime());
        ddrWeekDaysDetail.setOffStatus(weekDaysRequestDTO.getOffStatus());
        ddrWeekDaysDetail.setDdrShiftDetail(ddrShiftDetail);
        ddrWeekDaysDetail.setWeekDays(weekDays);
        return ddrWeekDaysDetail;
    }
}
