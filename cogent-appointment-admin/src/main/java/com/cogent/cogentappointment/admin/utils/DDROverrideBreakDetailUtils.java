package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRBreakRequestDTO;
import com.cogent.cogentappointment.persistence.model.BreakType;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideBreakDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideDetail;

/**
 * @author smriti on 11/05/20
 */
public class DDROverrideBreakDetailUtils {

    public static DDROverrideBreakDetail parseToDDROverrideBreakDetail(DDRBreakRequestDTO requestDTO,
                                                                       DDROverrideDetail overrideDetail,
                                                                       BreakType breakType) {
        DDROverrideBreakDetail breakDetail = new DDROverrideBreakDetail();
        breakDetail.setStartTime(requestDTO.getStartTime());
        breakDetail.setEndTime(requestDTO.getEndTime());
        breakDetail.setDdrOverrideDetail(overrideDetail);
        breakDetail.setBreakType(breakType);
        breakDetail.setStatus(requestDTO.getStatus());
        breakDetail.setRemarks(requestDTO.getRemarks());

        return breakDetail;
    }
}
