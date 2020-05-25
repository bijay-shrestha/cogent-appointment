package com.cogent.cogentappointment.admin.utils.ddrShiftWise;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRBreakDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.weekDays.DDRWeekDaysBreakUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.BreakType;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRBreakDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRWeekDaysDetail;

/**
 * @author smriti on 11/05/20
 */
public class DDRBreakDetailUtils {

    public static DDRBreakDetail parseToDDRBreakDetail(DDRBreakDetailRequestDTO requestDTO,
                                                       DDRWeekDaysDetail weekDaysDetail,
                                                       BreakType breakType) {

        DDRBreakDetail breakDetail = new DDRBreakDetail();
        breakDetail.setStartTime(requestDTO.getStartTime());
        breakDetail.setEndTime(requestDTO.getEndTime());
        breakDetail.setDdrWeekDaysDetail(weekDaysDetail);
        breakDetail.setBreakType(breakType);
        breakDetail.setStatus(requestDTO.getStatus());
        breakDetail.setRemarks(requestDTO.getRemarks());

        return breakDetail;
    }

    public static DDRBreakDetail parseUpdatedDDRBreakDetail(DDRWeekDaysBreakUpdateRequestDTO requestDTO,
                                                            BreakType breakType,
                                                            DDRBreakDetail breakDetail) {

        breakDetail.setStartTime(requestDTO.getStartTime());
        breakDetail.setEndTime(requestDTO.getEndTime());
        breakDetail.setBreakType(breakType);
        breakDetail.setStatus(requestDTO.getStatus());
        breakDetail.setRemarks(requestDTO.getRemarks());

        return breakDetail;
    }
}
