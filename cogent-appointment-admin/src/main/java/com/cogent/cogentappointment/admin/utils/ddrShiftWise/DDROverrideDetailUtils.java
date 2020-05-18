package com.cogent.cogentappointment.admin.utils.ddrShiftWise;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override.DDROverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.update.DDROverrideUpdateResponseDTO;
import com.cogent.cogentappointment.persistence.model.Shift;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;

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
        ddrOverrideDetail.setHasBreak(requestDTO.getHasBreak());
        ddrOverrideDetail.setIsAddedShift(NO);

        return ddrOverrideDetail;
    }

    public static DDROverrideDetail parseToDdrOverrideDetail(DDROverrideUpdateRequestDTO requestDTO,
                                                             Shift shift,
                                                             DDROverrideDetail ddrOverrideDetail ) {

        ddrOverrideDetail.setDate(requestDTO.getDate());
        ddrOverrideDetail.setStartTime(requestDTO.getStartTime());
        ddrOverrideDetail.setEndTime(requestDTO.getEndTime());
        ddrOverrideDetail.setOffStatus(requestDTO.getOffStatus());
        ddrOverrideDetail.setRosterGapDuration(requestDTO.getRosterGapDuration());
        ddrOverrideDetail.setShift(shift);
        ddrOverrideDetail.setStatus(requestDTO.getStatus());
        ddrOverrideDetail.setRemarks(requestDTO.getRemarks());
        ddrOverrideDetail.setHasBreak(requestDTO.getHasBreak());
        ddrOverrideDetail.setIsAddedShift(NO);

        return ddrOverrideDetail;
    }

    public static DDROverrideUpdateResponseDTO parseToOverrideUpdateResponse(Long savedOverrideId) {
        return DDROverrideUpdateResponseDTO.builder()
                .ddrOverrideId(savedOverrideId)
                .build();
    }
}
