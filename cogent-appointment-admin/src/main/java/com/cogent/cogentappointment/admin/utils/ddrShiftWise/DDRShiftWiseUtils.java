package com.cogent.cogentappointment.admin.utils.ddrShiftWise;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.*;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRResponseDTO;
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

    public static DDRExistingDetailResponseDTO parseToDDRExistingDetailResponseDTO(
            Character hasDDROverride,
            List<DDRShiftMinResponseDTO> shiftDetail,
            List<DDROverrideDetailResponseDTO> overrideDetail) {

        return DDRExistingDetailResponseDTO.builder()
                .hasOverride(hasDDROverride)
                .shiftDetail(shiftDetail)
                .overrideDetail(overrideDetail)
                .build();
    }

    public static void parseDDRDeleteStatus(DoctorDutyRosterShiftWise doctorDutyRoster,
                                            DeleteRequestDTO deleteRequestDTO) {
        doctorDutyRoster.setStatus(deleteRequestDTO.getStatus());
        doctorDutyRoster.setRemarks(deleteRequestDTO.getRemarks());
    }

    public static DDRDetailResponseDTO parseToDdrDetailResponseDTO(DDRResponseDTO ddrDetail,
                                                            List<DDRShiftMinResponseDTO> shiftDetail ,
                                                            List<DDROverrideDetailResponseDTO> overrideDetail){
        return DDRDetailResponseDTO.builder()
                .ddrDetail(ddrDetail)
                .shiftDetail(shiftDetail)
                .overrideDetail(overrideDetail)
                .build();

    }

}
