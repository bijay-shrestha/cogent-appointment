package com.cogent.cogentappointment.admin.utils.ddrShiftWise;

import com.cogent.cogentappointment.persistence.model.Shift;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRShiftDetail;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;

/**
 * @author smriti on 19/05/20
 */
public class DDRShiftDetailUtils {

    public static DDRShiftDetail parseToDDRShiftDetail(DoctorDutyRosterShiftWise doctorDutyRosterShiftWise,
                                                       Shift shift,
                                                       Integer rosterGapDuration,
                                                       Character status) {

        DDRShiftDetail ddrShiftDetail = new DDRShiftDetail();
        ddrShiftDetail.setDdrShiftWise(doctorDutyRosterShiftWise);
        ddrShiftDetail.setRosterGapDuration(rosterGapDuration);
        ddrShiftDetail.setShift(shift);
        ddrShiftDetail.setStatus(status);

        return ddrShiftDetail;
    }

    public static DDRShiftDetail parseToUpdatedDDRShiftDetail(DDRShiftDetail ddrShiftDetail,
                                                              Integer rosterGapDuration,
                                                              Character status) {

        ddrShiftDetail.setRosterGapDuration(rosterGapDuration);
        ddrShiftDetail.setStatus(status);
        return ddrShiftDetail;
    }

}
