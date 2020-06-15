package com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster;

import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterUpdateDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.detail.*;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertDateToLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.isLocalDateBetweenInclusive;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterUtils {

    public static HospitalDepartmentDutyRoster parseToHospitalDepartmentDutyRoster(
            HospitalDepartmentDutyRosterRequestDTO requestDTO,
            HospitalDepartment hospitalDepartment,
            Hospital hospital) {

        HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster = new HospitalDepartmentDutyRoster();
        hospitalDepartmentDutyRoster.setHospital(hospital);
        hospitalDepartmentDutyRoster.setHospitalDepartment(hospitalDepartment);
        hospitalDepartmentDutyRoster.setFromDate(requestDTO.getFromDate());
        hospitalDepartmentDutyRoster.setToDate(requestDTO.getToDate());
        hospitalDepartmentDutyRoster.setRosterGapDuration(requestDTO.getRosterGapDuration());
        hospitalDepartmentDutyRoster.setStatus(requestDTO.getStatus());
        hospitalDepartmentDutyRoster.setHasOverrideDutyRoster(requestDTO.getHasOverrideDutyRoster());
        hospitalDepartmentDutyRoster.setIsRoomEnabled(requestDTO.getIsRoomEnabled());
        return hospitalDepartmentDutyRoster;
    }

    public static void parseDeletedDetails(HospitalDepartmentDutyRoster doctorDutyRoster,
                                           DeleteRequestDTO deleteRequestDTO) {
        doctorDutyRoster.setStatus(deleteRequestDTO.getStatus());
        doctorDutyRoster.setRemarks(deleteRequestDTO.getRemarks());
    }

    public static HospitalDeptDutyRosterDetailResponseDTO parseHDDRosterDetails(
            HospitalDeptDutyRosterResponseDTO dutyRosterDetail,
            HospitalDeptDutyRosterRoomResponseDTO roomInfo,
            List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysRosters,
            List<HospitalDeptDutyRosterOverrideResponseDTO> overrideRosters) {

        return HospitalDeptDutyRosterDetailResponseDTO.builder()
                .dutyRosterDetail(dutyRosterDetail)
                .roomInfo(roomInfo)
                .weekDaysRosters(weekDaysRosters)
                .overrideRosters(overrideRosters)
                .build();
    }

    public static void parseToUpdatedRosterDetails(HospitalDepartmentDutyRoster dutyRoster,
                                                   HospitalDeptDutyRosterUpdateDTO updateRequestDTO) {

        dutyRoster.setRosterGapDuration(updateRequestDTO.getRosterGapDuration());
        dutyRoster.setStatus(updateRequestDTO.getStatus());
        dutyRoster.setRemarks(updateRequestDTO.getRemarks());
        dutyRoster.setHasOverrideDutyRoster(updateRequestDTO.getHasOverrideDutyRoster());
        dutyRoster.setIsRoomEnabled(updateRequestDTO.getIsRoomEnabled());
    }

    public static HospitalDeptExistingDutyRosterDetailResponseDTO parseToExistingRosterDetails(
            List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysRosters,
            List<HospitalDeptDutyRosterOverrideResponseDTO> overrideRosters) {

        return HospitalDeptExistingDutyRosterDetailResponseDTO.builder()
                .weekDaysRosters(weekDaysRosters)
                .overrideRosters(overrideRosters)
                .build();
    }

    /*ADD TO FINAL LIST ONLY IF QUERY RESULT IS WITHIN THE SELECTED SEARCH DATE RANGE*/
    public static List<HospitalDeptDutyRosterStatusResponseDTO> parseQueryResultToHospitalDeptDutyRosterStatusResponseDTOS(
            List<Object[]> queryResults,
            Date searchFromDate,
            Date searchToDate) {

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatusResponseDTOS = new ArrayList<>();

        LocalDate searchFromLocalDate = convertDateToLocalDate(searchFromDate);
        LocalDate searchToLocalDate = convertDateToLocalDate(searchToDate);

        queryResults.forEach(result -> {

            final int START_DATE_INDEX = 0;
            final int END_DATE_INDEX = 1;
            final int ROSTER_GAP_DURATION_INDEX = 2;
            final int HOSPITAL_DEPARTMENT_ID_INDEX = 3;
            final int HOSPITAL_DEPARTMENT_NAME_INDEX = 4;
            final int ROOM_ID_INDEX = 5;
            final int ROOM_NUMBER_INDEX = 6;
            final int DEPARTMENT_TIME_DETAILS_INDEX = 7;
            final int DUTY_ROSTER_ID_INDEX = 8;


            LocalDate startLocalDate = convertDateToLocalDate((Date) result[START_DATE_INDEX]);
            LocalDate endLocalDate = convertDateToLocalDate((Date) result[END_DATE_INDEX]);

            List<String> timeDetails = Arrays.asList(result[DEPARTMENT_TIME_DETAILS_INDEX].toString()
                    .split(StringConstant.COMMA_SEPARATED));

            Stream.iterate(startLocalDate, date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1)
                    .forEach(localDate -> {

                        if (isLocalDateBetweenInclusive(searchFromLocalDate, searchToLocalDate, localDate)) {

                            String dayOfWeek = localDate.getDayOfWeek().toString();

                            String weekMatched = timeDetails.stream()
                                    .filter(str -> str.contains(dayOfWeek))
                                    .findAny().orElse(null);

                            if (!Objects.isNull(weekMatched)) {

                                /*START TIME - END TIME - DAY OFF STATUS - WEEK NAME*/
                                String[] weekMatchedSplit = weekMatched.split(StringConstant.HYPHEN);

                                HospitalDeptDutyRosterStatusResponseDTO responseDTO = HospitalDeptDutyRosterStatusResponseDTO.builder()
                                        .uniqueIdentifier(Long.parseLong(result[DUTY_ROSTER_ID_INDEX].toString())
                                                +"-"+
                                                localDate)
                                        .date(localDate)
                                        .startTime(weekMatchedSplit[0])
                                        .endTime(weekMatchedSplit[1])
                                        .dayOffStatus(weekMatchedSplit[2].charAt(0))
                                        .hospitalDepartmentId(Long.parseLong(result[HOSPITAL_DEPARTMENT_ID_INDEX].toString()))
                                        .hospitalDepartmentName(result[HOSPITAL_DEPARTMENT_NAME_INDEX].toString())
                                        .hospitalDepartmentRoomInfoId(Objects.isNull(result[ROOM_ID_INDEX]) ?
                                                null : Long.parseLong(result[ROOM_ID_INDEX].toString()))
                                        .roomNumber(result[ROOM_NUMBER_INDEX].toString())
                                        .rosterGapDuration(Integer.parseInt(result[ROSTER_GAP_DURATION_INDEX].toString()))
                                        .hospitalDepartmentDutyRosterId(Long.parseLong(result[DUTY_ROSTER_ID_INDEX].toString()))
                                        .build();

                                hospitalDeptDutyRosterStatusResponseDTOS.add(responseDTO);
                            }
                        }
                    });
        });

        return hospitalDeptDutyRosterStatusResponseDTOS;
    }

    public static List<HospitalDeptDutyRosterStatusResponseDTO> mergeOverrideAndActualHospitalDeptDutyRoster(
            List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterOverrideStatus,
            List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus) {

        /*COUNT <1 WILL RETURN UNMATCHED VALUES AND COUNT > 0 WILL RETURN MATCHED VALUES*/
        /*FETCH ONLY THOSE DOCTOR DUTY ROSTER EXCEPT OVERRIDE DUTY ROSTER
        BY COMPARING DATE, DOCTOR ID AND SPECIALIZATION ID*/
        List<HospitalDeptDutyRosterStatusResponseDTO> unmatchedList = hospitalDeptDutyRosterStatus.stream()
                .filter(rosterStatus -> (hospitalDeptDutyRosterOverrideStatus.stream()
                        .filter(overrideStatus -> (overrideStatus.getDate().equals(rosterStatus.getDate()))
                                && (overrideStatus.getHospitalDepartmentId().equals(rosterStatus.getHospitalDepartmentId()))
                                && (overrideStatus.getHospitalDepartmentRoomInfoId()
                                .equals(rosterStatus.getHospitalDepartmentRoomInfoId())))
                        .count()) < 1)
                .collect(Collectors.toList());

        /*MERGE DUTY ROSTER LIST (UNMATCHED LIST) WITH REMAINING OVERRIDE DUTY ROSTER LIST  */
        hospitalDeptDutyRosterOverrideStatus.addAll(unmatchedList);

        /*SORT BY DATE*/
        hospitalDeptDutyRosterOverrideStatus.sort(Comparator.comparing(HospitalDeptDutyRosterStatusResponseDTO::getDate));

        return hospitalDeptDutyRosterOverrideStatus;
    }

}
