package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.DDRDetailRequestDTO;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterShiftWise;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;

/**
 * @author smriti on 08/05/20
 */
public class DDRShiftWiseUtils {

    public static DoctorDutyRosterShiftWise parseToDDRShiftWise(DDRDetailRequestDTO requestDTO,
                                                                Hospital hospital,
                                                                Specialization specialization,
                                                                Doctor doctor) {

        DoctorDutyRosterShiftWise doctorDutyRoster = new DoctorDutyRosterShiftWise();
        doctorDutyRoster.setHospital(hospital);
        doctorDutyRoster.setSpecialization(specialization);
        doctorDutyRoster.setDoctor(doctor);
        doctorDutyRoster.setFromDate(requestDTO.getFromDate());
        doctorDutyRoster.setToDate(requestDTO.getToDate());
        doctorDutyRoster.setRosterGapDuration(requestDTO.getRosterGapDuration());
        doctorDutyRoster.setStatus(requestDTO.getStatus());
        return doctorDutyRoster;
    }

}
