package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueSearchByTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.count.AppointmentCountWithStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.count.HospitalDepartmentRosterDetailsDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.count.HospitalDeptAppointmentStatusCountResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.CANCELLED;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.FOLLOW_UP;
import static com.cogent.cogentappointment.admin.utils.AppointmentStatusUtils.getAppointmentSlotCounts;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentUtils {

    public static AppointmentRefundDetail parseRefundRejectDetails(IntegrationRefundRequestDTO refundRequestDTO,
                                                                   AppointmentRefundDetail refundDetail) {
        refundDetail.setStatus(REJECTED);
        refundDetail.setRemarks(refundRequestDTO.getRemarks());

        return refundDetail;
    }

    public static AppointmentCountResponseDTO parseToAppointmentCountResponseDTO(Long overAllAppointment, Long newPatient,
                                                                                 Long registeredPatient, Long followUp,
                                                                                 Character pillType) {
        AppointmentCountResponseDTO countResponseDTO = new AppointmentCountResponseDTO();
        countResponseDTO.setTotalAppointment(overAllAppointment);
        countResponseDTO.setNewPatient(newPatient);
        countResponseDTO.setRegisteredPatient(registeredPatient);
        countResponseDTO.setPillType(pillType);
        countResponseDTO.setFollowUpPatient(followUp);

        return countResponseDTO;
    }

    public static void parseAppointmentRejectDetails(AppointmentRejectDTO rejectDTO,
                                                     Appointment appointment) {
        appointment.setStatus(REJECTED);
        appointment.setRemarks(rejectDTO.getRemarks());
    }

    public static List<AppointmentStatusResponseDTO> parseQueryResultToAppointmentStatusResponseDTOS
            (List<Object[]> results) {

        List<AppointmentStatusResponseDTO> appointmentStatusResponseDTOS = new ArrayList<>();

        results.forEach(result -> {
            final int APPOINTMENT_DATE_INDEX = 0;
            final int TIME_WITH_STATUS_DETAILS_INDEX = 1;
            final int DOCTOR_ID_INDEX = 2;
            final int SPECIALIZATION_ID_INDEX = 3;
            final int APPOINTMENT_NUMBER_INDEX = 4;
            final int PATIENT_NAME_INDEX = 5;
            final int GENDER_INDEX = 6;
            final int MOBILE_NUMBER_INDEX = 7;
            final int AGE_INDEX = 8;
            final int APPOINTMENT_ID_INDEX = 9;
            final int IS_FOLLOW_UP_INDEX = 10;
            final int HAS_TRANSFERRED_INDEX = 11;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];

            LocalDate appointmentLocalDate = new java.sql.Date(appointmentDate.getTime()).toLocalDate();

            AppointmentStatusResponseDTO appointmentStatusResponseDTO = AppointmentStatusResponseDTO.builder()
                    .date(appointmentLocalDate)
                    .appointmentTimeDetails(result[TIME_WITH_STATUS_DETAILS_INDEX].toString())
                    .doctorId(Long.parseLong(result[DOCTOR_ID_INDEX].toString()))
                    .specializationId(Long.parseLong(result[SPECIALIZATION_ID_INDEX].toString()))
                    .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                    .patientName(result[PATIENT_NAME_INDEX].toString())
                    .mobileNumber(result[MOBILE_NUMBER_INDEX].toString())
                    .age(result[AGE_INDEX].toString())
                    .gender(result[GENDER_INDEX].toString())
                    .appointmentId(Long.parseLong(result[APPOINTMENT_ID_INDEX].toString()))
                    .isFollowUp((Character) result[IS_FOLLOW_UP_INDEX])
                    .hasTransferred((Character) result[HAS_TRANSFERRED_INDEX])
                    .build();

            appointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
        });

        return appointmentStatusResponseDTOS;
    }

    public static Map<String, List<AppointmentQueueDTO>> parseQueryResultToAppointmentQueueForTodayByTimeResponse(
            List<Object[]> results) {

        List<AppointmentQueueSearchByTimeDTO> appointmentQueueSearchByTimeDTOS = new ArrayList<>();

        AppointmentQueueDTO appointmentQueueSearchDTO = new AppointmentQueueDTO();

        List<AppointmentQueueDTO> appointmentQueueByTimeDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int APPOINTMENT_TIME_INDEX = 0;
            final int DOCTOR_NAME_INDEX = 1;
            final int SPECIALIZATION_NAME_INDEX = 2;
            final int PATIENT_NAME_INDEX = 3;
            final int PATIENT_MOBILE_NUMBER_INDEX = 4;
            final int DOCTOR_AVATAR_INDEX = 5;

            AppointmentTimeDTO appointmentTimeDTO = AppointmentTimeDTO.builder()
                    .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                    .build();

            AppointmentQueueDTO appointmentQueueByTimeDTO =
                    AppointmentQueueDTO.builder()
                            .appointmentTime(appointmentTimeDTO.getAppointmentTime())
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientMobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .doctorAvatar(result[DOCTOR_AVATAR_INDEX].toString())
                            .build();

            appointmentQueueByTimeDTOS.add(appointmentQueueByTimeDTO);

        });

//        appointmentQueueSearchDTO.setAppointmentQueueByTimeDTOList(appointmentQueueByTimeDTOS);
        appointmentQueueSearchDTO.setTotalItems(appointmentQueueByTimeDTOS.size());

        //group by price
        Map<String, List<AppointmentQueueDTO>> groupByPriceMap =
                appointmentQueueByTimeDTOS.stream().collect(Collectors.groupingBy(AppointmentQueueDTO::getAppointmentTime));

        return groupByPriceMap;

    }

    public static List<HospitalDeptAppointmentStatusResponseDTO> parseQueryResultToHospitalDeptAppointmentStatusResponseDTOS
            (List<Object[]> results) {

        List<HospitalDeptAppointmentStatusResponseDTO> HospitalDeptAppointmentStatusResponseDTOS = new ArrayList<>();

        results.forEach(result -> {
            final int APPOINTMENT_DATE_INDEX = 0;
            final int TIME_WITH_STATUS_DETAILS_INDEX = 1;
            final int HOSPITAL_DEPARTMENT_ID_INDEX = 2;
            final int HOSPITAL_DEPARTMENT_ROOM_INFO_ID_INDEX = 3;
            final int APPOINTMENT_NUMBER_INDEX = 4;
            final int PATIENT_NAME_INDEX = 5;
            final int GENDER_INDEX = 6;
            final int MOBILE_NUMBER_INDEX = 7;
            final int AGE_INDEX = 8;
            final int APPOINTMENT_ID_INDEX = 9;
            final int IS_FOLLOW_UP_INDEX = 10;
            final int HAS_TRANSFERRED_INDEX = 11;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];

            LocalDate appointmentLocalDate = new java.sql.Date(appointmentDate.getTime()).toLocalDate();

            HospitalDeptAppointmentStatusResponseDTO appointmentStatusResponseDTO = HospitalDeptAppointmentStatusResponseDTO.builder()
                    .date(appointmentLocalDate)
                    .appointmentTimeDetails(result[TIME_WITH_STATUS_DETAILS_INDEX].toString())
                    .departmentId(Long.parseLong(result[HOSPITAL_DEPARTMENT_ID_INDEX].toString()))
                    .hospitalDepartmentRoomInfoId((Objects.isNull(result[HOSPITAL_DEPARTMENT_ROOM_INFO_ID_INDEX])) ?
                            null : Long.parseLong(result[HOSPITAL_DEPARTMENT_ROOM_INFO_ID_INDEX].toString()))
                    .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                    .patientName(result[PATIENT_NAME_INDEX].toString())
                    .mobileNumber(result[MOBILE_NUMBER_INDEX].toString())
                    .age(result[AGE_INDEX].toString())
                    .gender(result[GENDER_INDEX].toString())
                    .appointmentId(Long.parseLong(result[APPOINTMENT_ID_INDEX].toString()))
                    .isFollowUp((Character) result[IS_FOLLOW_UP_INDEX])
                    .hasTransferred((Character) result[HAS_TRANSFERRED_INDEX])
                    .build();

            HospitalDeptAppointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
        });

        return HospitalDeptAppointmentStatusResponseDTOS;
    }

    public static HospitalDeptAppointmentStatusCountResponseDTO getVacantAppointmentSlotsCount(
            List<AppointmentCountWithStatusDTO> countWithStatusDTOS,
            Long totalAppointmentSlotCount,
            Long appointmentFollowUpCount) {

        HospitalDeptAppointmentStatusCountResponseDTO hospitalDeptAppointmentStatusCountResponseDTO = new
                HospitalDeptAppointmentStatusCountResponseDTO();

        HashMap<String, Long> appointmentStatusCount = new HashMap<>();

        Long totalOccupiedSlots = getOccupiedCount(countWithStatusDTOS).stream().mapToLong(i -> i).sum();

        Long vacantSlot = totalAppointmentSlotCount - totalOccupiedSlots;

        appointmentStatusCount.put(ALL, totalAppointmentSlotCount);

        appointmentStatusCount.put(VACANT, vacantSlot);

        for (AppointmentCountWithStatusDTO timeSlots : countWithStatusDTOS) {
            switch (timeSlots.getStatus()) {

                case BOOKED:
                    appointmentStatusCount.put(BOOKED, timeSlots.getCount());
                    break;

                case APPROVED:
                    appointmentStatusCount.put(APPROVED, timeSlots.getCount());
                    break;

                case CANCELLED:
                    appointmentStatusCount.put(CANCELLED, timeSlots.getCount());
                    break;
            }
        }

        if (!appointmentFollowUpCount.equals(0L)) {
            appointmentStatusCount.put(FOLLOW_UP, appointmentFollowUpCount);
        }


        hospitalDeptAppointmentStatusCountResponseDTO.setAppointmentStatusCount(appointmentStatusCount);

        List<String> strings=
                hospitalDeptAppointmentStatusCountResponseDTO.getAppointmentStatusCount().entrySet()
                        .stream().map(Map.Entry::getKey).collect(Collectors.toList());

        if (!strings.contains(APPROVED)){
            appointmentStatusCount.put(APPROVED,0L);
            hospitalDeptAppointmentStatusCountResponseDTO.setAppointmentStatusCount(appointmentStatusCount);
        }

        if (!strings.contains(FOLLOW_UP)){
            appointmentStatusCount.put(FOLLOW_UP,0L);
            hospitalDeptAppointmentStatusCountResponseDTO.setAppointmentStatusCount(appointmentStatusCount);
        }

        if (!strings.contains(BOOKED)){
            appointmentStatusCount.put(BOOKED,0L);
            hospitalDeptAppointmentStatusCountResponseDTO.setAppointmentStatusCount(appointmentStatusCount);
        }

        if (!strings.contains(CANCELLED)){
            appointmentStatusCount.put(CANCELLED,0L);
            hospitalDeptAppointmentStatusCountResponseDTO.setAppointmentStatusCount(appointmentStatusCount);
        }


        return hospitalDeptAppointmentStatusCountResponseDTO;

    }

    public static List<Long> getOccupiedCount(List<AppointmentCountWithStatusDTO> countWithStatusDTOS) {
        return countWithStatusDTOS
                .stream()
                .map(AppointmentCountWithStatusDTO::getCount)
                .collect(Collectors.toList());
    }

    public static Long getTotalAppointmentTimeSlotCount(List<HospitalDepartmentRosterDetailsDTO> rosterDetailsDTOS) {

        final Long[] count = {0L};

        rosterDetailsDTOS.forEach(rosterDetailsDTO -> {
            Long data = getAppointmentSlotCounts(rosterDetailsDTO.getStartTime(),
                    rosterDetailsDTO.getEndTime(),
                    rosterDetailsDTO.getRosterGapDuration());

            count[0] += data;
        });

        return count[0];
    }



}
