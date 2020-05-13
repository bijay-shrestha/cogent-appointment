package com.cogent.cogentappointment.admin.utils.ddrShiftWise;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinResponseDTO;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;

import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;

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
        doctorDutyRoster.setStatus(requestDTO.getStatus());
        doctorDutyRoster.setHasOverride(NO);
        return doctorDutyRoster;
    }

    public static DDRExistingMinResponseDTO parseToExistingAvailabilityResponseDTO
            (List<DDRExistingMinDTO> existingRosters) {

        return DDRExistingMinResponseDTO.builder()
                .hasExistingRosters(!existingRosters.isEmpty())
                .existingRosters(existingRosters)
                .build();
    }

}
