package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRBreakRequestDTO;
import com.cogent.cogentappointment.persistence.model.BreakType;
import com.cogent.cogentappointment.persistence.model.DDRBreakDetail;
import com.cogent.cogentappointment.persistence.model.DDRWeekDaysDetail;

/**
 * @author smriti on 11/05/20
 */
public class DDRBreakDetailUtils {

    public static DDRBreakDetail parseToDDRBreakDetail(DDRBreakRequestDTO requestDTO,
                                                       DDRWeekDaysDetail weekDaysDetail,
                                                       BreakType breakType){
        DDRBreakDetail breakDetail = new DDRBreakDetail();
        breakDetail.setStartTime(requestDTO.getStartTime());
        breakDetail.setEndTime(requestDTO.getEndTime());
        breakDetail.setDdrWeekDaysDetail(weekDaysDetail);
        breakDetail.setBreakType(breakType);
        breakDetail.setStatus(requestDTO.getStatus());
        breakDetail.setRemarks(requestDTO.getRemarks());

        return breakDetail;
    }
}
