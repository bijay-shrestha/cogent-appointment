package com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateResponseDTO;
import com.cogent.cogentappointment.commons.utils.NepaliDateUtility;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertDateToLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.isLocalDateBetweenInclusive;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptOverrideDutyRosterUtils {

    public static HospitalDepartmentDutyRosterOverride parseOverrideDetails(
            HospitalDeptDutyRosterOverrideRequestDTO requestDTO,
            HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
            HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo) {

        HospitalDepartmentDutyRosterOverride override = new HospitalDepartmentDutyRosterOverride();
        override.setFromDate(requestDTO.getFromDate());
        override.setToDate(requestDTO.getToDate());
        override.setStartTime(requestDTO.getStartTime());
        override.setEndTime(requestDTO.getEndTime());
        override.setDayOffStatus(requestDTO.getDayOffStatus());
        override.setStatus(requestDTO.getStatus());
        override.setRemarks(requestDTO.getRemarks());
        override.setHospitalDepartmentDutyRoster(hospitalDepartmentDutyRoster);
        override.setHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfo);


        return override;
    }

    public static HospitalDepartmentDutyRosterOverride parseOverrideDetails(
            HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO,
            HospitalDepartmentDutyRosterOverride override,
            HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo) {

        override.setFromDate(updateRequestDTO.getFromDate());
        override.setToDate(updateRequestDTO.getToDate());
        override.setEndTime(updateRequestDTO.getEndTime());
        override.setStartTime(updateRequestDTO.getStartTime());
        override.setDayOffStatus(updateRequestDTO.getDayOffStatus());
        override.setStatus(updateRequestDTO.getStatus());
        override.setRemarks(updateRequestDTO.getRemarks());
        override.setHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfo);
        return override;
    }

    public static HospitalDeptDutyRosterOverrideUpdateResponseDTO parseOverrideUpdateResponse(Long savedOverrideId) {
        return HospitalDeptDutyRosterOverrideUpdateResponseDTO.builder()
                .savedOverrideId(savedOverrideId)
                .build();
    }

    public static void parseDeletedOverrideDetails(HospitalDepartmentDutyRosterOverride override,
                                                   DeleteRequestDTO requestDTO) {

        override.setStatus(requestDTO.getStatus());
        override.setRemarks(requestDTO.getRemarks());
    }

    /*ADD TO FINAL LIST ONLY IF QUERY RESULT IS WITHIN THE SELECTED SEARCH DATE RANGE*/
    public static List<HospitalDeptDutyRosterStatusResponseDTO> parseQueryResultToHospitalDeptDutyRosterStatusResponseDTO
    (List<Object[]> results,
     Date searchFromDate,
     Date searchToDate) {

        LocalDate searchFromLocalDate = convertDateToLocalDate(searchFromDate);
        LocalDate searchToLocalDate = convertDateToLocalDate(searchToDate);

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatusResponseDTOS = new ArrayList<>();

        results.forEach(result -> {

            final int FROM_DATE_INDEX = 0;
            final int TO_DATE_INDEX = 1;
            final int START_TIME_INDEX = 2;
            final int END_TIME_INDEX = 3;
            final int DAY_OFF_STATUS_INDEX = 4;
            final int ROSTER_GAP_DURATION_INDEX = 5;
            final int HOSPITAL_DEPARTMENT_ID_INDEX = 6;
            final int HOSPITAL_DEPARTMENT_NAME_INDEX = 7;
            final int ROOM_ID_INDEX = 8;
            final int ROOM_NUMBER_INDEX = 9;

            LocalDate startLocalDate = convertDateToLocalDate((Date) result[FROM_DATE_INDEX]);
            LocalDate endLocalDate = convertDateToLocalDate((Date) result[TO_DATE_INDEX]);

            Stream.iterate(startLocalDate, date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1)
                    .forEach(localDate -> {

                        if (isLocalDateBetweenInclusive(searchFromLocalDate, searchToLocalDate, localDate)) {

                            HospitalDeptDutyRosterStatusResponseDTO responseDTO =
                                    HospitalDeptDutyRosterStatusResponseDTO.builder()
                                            .date(localDate)
                                            .startTime(result[START_TIME_INDEX].toString())
                                            .endTime(result[END_TIME_INDEX].toString())
                                            .rosterGapDuration(Integer.parseInt(result[ROSTER_GAP_DURATION_INDEX].toString()))
                                            .dayOffStatus(result[DAY_OFF_STATUS_INDEX].toString().charAt(0))
                                            .hospitalDepartmentId(Long.parseLong(result[HOSPITAL_DEPARTMENT_ID_INDEX].toString()))
                                            .hospitalDepartmentName(result[HOSPITAL_DEPARTMENT_NAME_INDEX].toString())
                                            .hospitalDepartmentRoomInfoId(Objects.isNull(result[ROOM_ID_INDEX]) ?
                                                    null : Long.parseLong(result[ROOM_ID_INDEX].toString()))
                                            .roomNumber(result[ROOM_NUMBER_INDEX].toString())
                                            .build();

                            hospitalDeptDutyRosterStatusResponseDTOS.add(responseDTO);
                        }
                    });
        });

        return hospitalDeptDutyRosterStatusResponseDTOS;
    }


}
