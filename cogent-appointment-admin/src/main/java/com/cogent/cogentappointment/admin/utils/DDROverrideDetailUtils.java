package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideDetailRequestDTO;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;
import com.cogent.cogentappointment.persistence.model.Shift;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;

/**
 * @author smriti on 11/05/20
 */
public class DDROverrideDetailUtils {

    public static DDROverrideDetail parseToDdrOverrideDetail(DDROverrideDetailRequestDTO requestDTO,
                                                             DoctorDutyRosterShiftWise ddrShiftWise,
                                                             Shift shift) {

        DDROverrideDetail ddrOverrideDetail = new DDROverrideDetail();
        ddrOverrideDetail.setDate(requestDTO.getDate());
        ddrOverrideDetail.setStartTime(requestDTO.getStartTime());
        ddrOverrideDetail.setEndTime(requestDTO.getEndTime());
        ddrOverrideDetail.setOffStatus(requestDTO.getOffStatus());
        ddrOverrideDetail.setRosterGapDuration(requestDTO.getRosterGapDuration());
        ddrOverrideDetail.setDdrShiftWise(ddrShiftWise);
        ddrOverrideDetail.setShift(shift);
        ddrOverrideDetail.setStatus(requestDTO.getStatus());
        ddrOverrideDetail.setRemarks(requestDTO.getRemarks());
        ddrOverrideDetail.setIsAddedShift(NO);

        return ddrOverrideDetail;
    }
}
