package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.DeptAppointmentStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.admin.service.AppointmentStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.ALL;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT_STATUS;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.DEPARTMENT_APPOINTMENT_STATUS;
import static com.cogent.cogentappointment.admin.utils.AppointmentStatusUtils.*;
import static com.cogent.cogentappointment.admin.utils.DoctorDutyRosterUtils.mergeOverrideAndActualDoctorDutyRoster;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author smriti ON 16/12/2019
 */
@Service
@Transactional
@Slf4j
public class AppointmentStatusServiceImpl implements AppointmentStatusService {

    private final DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final HospitalDeptDutyRosterOverrideRepository deptDutyRosterOverrideRepository;

    private final HospitalDeptDutyRosterRepository deptDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentService appointmentService;

    public AppointmentStatusServiceImpl(DoctorDutyRosterRepository doctorDutyRosterRepository,
                                        HospitalDeptDutyRosterOverrideRepository deptDutyRosterOverrideRepository,
                                        HospitalDeptDutyRosterRepository deptDutyRosterRepository,
                                        DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                        DoctorRepository doctorRepository,
                                        AppointmentService appointmentService) {
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.deptDutyRosterOverrideRepository = deptDutyRosterOverrideRepository;
        this.deptDutyRosterRepository = deptDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentService = appointmentService;
    }

    @Override
    public AppointmentStatusDTO fetchAppointmentStatusResponseDTO(AppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_STATUS);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus = fetchDoctorStatus(requestDTO);

        List<AppointmentStatusResponseDTO> appointments = fetchAppointmentStatus(requestDTO);

        doctorDutyRosterStatus = setDoctorTimeSlot(requestDTO.getStatus(), doctorDutyRosterStatus, appointments);

        List<DoctorDropdownDTO> doctorInfo =
                doctorRepository.fetchDoctorAvatarInfo(requestDTO.getHospitalId(), requestDTO.getDoctorId());

        AppointmentStatusDTO appointmentStatusDTO = parseToAppointmentStatusDTO(doctorDutyRosterStatus, doctorInfo);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_STATUS, getDifferenceBetweenTwoTime(startTime));

        return appointmentStatusDTO;
    }

    @Override
    public DeptAppointmentStatusDTO fetchDeptAppointmentStatusResponseDTO(HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DEPARTMENT_APPOINTMENT_STATUS);

        log.info(FETCHING_PROCESS_COMPLETED, DEPARTMENT_APPOINTMENT_STATUS, getDifferenceBetweenTwoTime(startTime));

        return null;
    }

    /*IN CASE OF PAST DATE ->
    * IF THERE ANY APPOINTMENTS, THEN SHOW AVAILABLE TIME SLOTS IRRESPECTIVE OF DAY OFF STATUS
    * BUT THERE ARE NO APPOINTMENT, THEN SHOW AVAILABLE TIME SLOTS ONLY IF DAY OFF STATUS = 'N'
    *
    * IN CASE OF FUTURE DATE ->
    * SHOW AVAILABLE TIME SLOTS ONLY IF DAY OFF STATUS = 'N'*/
    private List<DoctorDutyRosterStatusResponseDTO> setDoctorTimeSlot(String status,
                                                                      List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus,
                                                                      List<AppointmentStatusResponseDTO> appointmentStatusResponseDTOS) {

        switch (status) {
            case ALL:
                setDoctorTimeSlotForAllAppointmentStatus(doctorDutyRosterStatus, appointmentStatusResponseDTOS);
                break;
            case VACANT:
                setDoctorTimeSlotForVacantAppointmentStatus(doctorDutyRosterStatus, appointmentStatusResponseDTOS);
                break;
            default:
                doctorDutyRosterStatus = setDoctorTimeSlotForSelectedAppointmentStatus(doctorDutyRosterStatus,
                        appointmentStatusResponseDTOS);
                break;
        }

        return doctorDutyRosterStatus;
    }

    /*FETCH DOCTOR DUTY ROSTER FROM DOCTOR_DUTY_ROSTER_OVERRIDE FIRST
      AND THEN DOCTOR_DUTY ROSTER. THEN MERGE BOTH ROSTERS BASED ON THE REQUESTED SEARCH DATE, DOCTOR AND SPECIALIZATION*/
    private List<DoctorDutyRosterStatusResponseDTO> fetchDoctorStatus(AppointmentStatusRequestDTO requestDTO) {

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterOverrideStatus =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
                doctorDutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

        if (doctorDutyRosterOverrideStatus.isEmpty() && doctorDutyRosterStatus.isEmpty())
            throw new NoContentFoundException(DoctorDutyRoster.class);

        return mergeOverrideAndActualDoctorDutyRoster(doctorDutyRosterOverrideStatus, doctorDutyRosterStatus);
    }

    /*FETCH APPOINTMENT DETAILS WITHIN SELECTED DATE RANGE*/
    private List<AppointmentStatusResponseDTO> fetchAppointmentStatus(AppointmentStatusRequestDTO requestDTO) {
        return appointmentService.fetchAppointmentForAppointmentStatus(requestDTO);
    }

    /*IF DOCTOR DAY OFF STATUS= 'Y', THEN THERE ARE NO ANY TIME SLOTS
     * IF DAY OFF STATUS = 'N' :
     * IF APPOINTMENT EXISTS WITHIN SELECTED DOCTOR DUTY ROSTER RANGE, THEN FILTER APPOINTMENT STATUS ACCORDINGLY
     * ELSE APPOINTMENT STATUS IS VACANT FOR ALL TIME SLOTS*/
    private void setDoctorTimeSlotForAllAppointmentStatus(List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS,
                                                          List<AppointmentStatusResponseDTO> appointments) {

        for (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO : doctorDutyRosterStatusResponseDTOS) {

            doctorDutyRosterStatusResponseDTO.setWeekDayName(
                    doctorDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            List<DoctorTimeSlotResponseDTO> doctorTimeSlots = new ArrayList<>();

            boolean isDateBefore = convertLocalDateToDate(doctorDutyRosterStatusResponseDTO.getDate())
                    .before(new Date());

            if (isDateBefore) {
                setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO, doctorTimeSlots, appointments);
            } else {
                if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                    setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO, doctorTimeSlots, appointments);
            }
        }
    }

    private void setTimeSlotForAllAppointmentStatus
            (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
             List<DoctorTimeSlotResponseDTO> doctorTimeSlots,
             List<AppointmentStatusResponseDTO> appointments) {

        List<AppointmentStatusResponseDTO> appointmentMatchedWithRoster =
                appointments.stream()
                        .filter(appointment -> hasAppointment(appointment, doctorDutyRosterStatusResponseDTO))
                        .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(appointmentMatchedWithRoster)) {

            setTimeSlotHavingAppointments(appointmentMatchedWithRoster, doctorTimeSlots);

            doctorTimeSlots = calculateTimeSlotsForAll(doctorDutyRosterStatusResponseDTO, doctorTimeSlots);
        } else {
            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                doctorTimeSlots = calculateTimeSlotsForAll(doctorDutyRosterStatusResponseDTO, doctorTimeSlots);
        }

        doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
    }

    /*FOR SIMPLICITY, ADD ALL MATCHED APPOINTMENT LIST INTO FINAL LIST*/
    private void setTimeSlotHavingAppointments(List<AppointmentStatusResponseDTO> appointmentMatchedWithRoster,
                                               List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        for (AppointmentStatusResponseDTO appointment : appointmentMatchedWithRoster) {
            parseAppointmentDetails(appointment, doctorTimeSlots);
        }
    }

    private List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForAll(
            DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
            List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        return calculateTimeSlotsForAllAppointmentStatus(
                doctorDutyRosterStatusResponseDTO.getDate(),
                doctorDutyRosterStatusResponseDTO.getStartTime(),
                doctorDutyRosterStatusResponseDTO.getEndTime(),
                doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                doctorTimeSlots);
    }

    private void setDoctorTimeSlotForVacantAppointmentStatus(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS,
            List<AppointmentStatusResponseDTO> appointments) {

        for (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO : doctorDutyRosterStatusResponseDTOS) {

            doctorDutyRosterStatusResponseDTO.setWeekDayName(
                    doctorDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            List<DoctorTimeSlotResponseDTO> doctorTimeSlots = new ArrayList<>();

            boolean isDateBefore = convertLocalDateToDate(doctorDutyRosterStatusResponseDTO.getDate())
                    .before(new Date());

            if (isDateBefore) {
                setTimeSlotForVacantAppointmentStatus
                        (doctorDutyRosterStatusResponseDTO, appointments, doctorTimeSlots);

            } else {
                if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                    setTimeSlotForVacantAppointmentStatus
                            (doctorDutyRosterStatusResponseDTO, appointments, doctorTimeSlots);
            }
        }
    }

    private void setTimeSlotForVacantAppointmentStatus
            (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
             List<AppointmentStatusResponseDTO> appointments,
             List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

         /*FILTER APPOINTMENT THAT MATCHES WITH DOCTOR DUTY ROSTER INFO*/
        List<AppointmentStatusResponseDTO> appointmentMatchedWithRoster =
                appointments.stream()
                        .filter(appointment -> hasAppointment(appointment, doctorDutyRosterStatusResponseDTO))
                        .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(appointmentMatchedWithRoster)) {

            /*JOIN MATCHED APPOINTMENTS INTO COMMA SEPARATED STRING eg. 10:00-PA, 10:20-PA*/
            String matchedAppointmentWithStatus =
                    appointmentMatchedWithRoster.stream()
                            .map(AppointmentStatusResponseDTO::getAppointmentTimeDetails)
                            .collect(Collectors.joining(COMMA_SEPARATED));

            doctorTimeSlots = calculateTimeSlotsForVacantAppointmentStatus(
                    doctorDutyRosterStatusResponseDTO.getDate(),
                    doctorDutyRosterStatusResponseDTO.getStartTime(),
                    doctorDutyRosterStatusResponseDTO.getEndTime(),
                    doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                    matchedAppointmentWithStatus,
                    doctorTimeSlots
            );
        } else {

            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO)) {

                doctorTimeSlots = calculateTimeSlotsForVacantAppointmentStatus(
                        doctorDutyRosterStatusResponseDTO.getDate(),
                        doctorDutyRosterStatusResponseDTO.getStartTime(),
                        doctorDutyRosterStatusResponseDTO.getEndTime(),
                        doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                        null,
                        doctorTimeSlots
                );
            }
        }

        doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
    }

    /*IF STATUS IN SEARCH DTO IS NOT EMPTY AND IS NOT VACANT (i.e,PA/A/C),
     THEN RETURN ONLY APPOINTMENT DETAILS WITH RESPECTIVE STATUS.
     NO NEED TO FILTER WITH DOCTOR DUTY ROSTER RANGE*/

    /* FIRST VALIDATE IF DUTY ROSTER DATE IS BEFORE CURRENT DATE THEN SHOW ALL APPOINTMENTS IRRESPECTIVE OF DAYOFF
    * ELSE SHOW APPOINTMENT ONLY IF DAYOFF IS 'N'
    *
    * eg. DDR DATE = 2020-01-20, CURRENT DATE = 2020-01-22, DAYOFF = 'Y'/'N', APPOINTMENT = 2020-01-20
     * SEARCH = 2020-01-20 to 2020-01-20 -> SHOW ALL APPOINTMENT AS PER STATUS
    *   DDR DATE = 2020-01-23, CURRENT DATE = 2020-01-22, DAYOFF = 'Y', APPOINTMENT = 2020-01-22
     * SEARCH = 2020-01-22 to 2020-01-23 -> NO RECORDS BECAUSE DAYOFF ='Y'
    * */
    private List<DoctorDutyRosterStatusResponseDTO> setDoctorTimeSlotForSelectedAppointmentStatus(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus,
            List<AppointmentStatusResponseDTO> appointments) {

        /*THROW EXCEPTION IF NO APPOINTMENT EXISTS FOR THE SELECTED STATUS*/
        if (appointments.isEmpty()) {
            log.error(CONTENT_NOT_FOUND, APPOINTMENT);
            throw new NoContentFoundException(Appointment.class);
        }

        /*FILTER OUT FROM DOCTOR DUTY ROSTERS SUCH THAT IT CONTAINS ONLY THOSE ROSTERS HAVING
         * APPOINTMENT*/
        List<DoctorDutyRosterStatusResponseDTO> rostersWithAppointment = doctorDutyRosterStatus.stream()
                .filter(doctorDutyRoster -> (appointments.stream()
                        .anyMatch(appointment -> hasAppointment(appointment, doctorDutyRoster)))
                )
                .collect(Collectors.toList());

         /*ADD TO LIST ONLY IF DOCTOR DAY OFF STATUS IS 'N'
         * AND APPOINTMENT CONDITION MATCHES*/
        rostersWithAppointment.forEach(doctorDutyRoster -> {
            List<DoctorTimeSlotResponseDTO> doctorTimeSlotResponseDTOS = new ArrayList<>();
            appointments
                    .stream()
                    .filter(appointment -> hasAppointment(appointment, doctorDutyRoster))
                    .forEach(appointment -> {

                        doctorDutyRoster.setWeekDayName(appointment.getDate().getDayOfWeek().toString());

                        boolean isDateBefore = convertLocalDateToDate(appointment.getDate()).before(new Date());

                        if (isDateBefore) {
                            parseAppointmentDetails(appointment, doctorTimeSlotResponseDTOS);
                        } else {
                            if (doctorDutyRoster.getDayOffStatus().equals(NO))
                                parseAppointmentDetails(appointment, doctorTimeSlotResponseDTOS);
                        }

                        doctorDutyRoster.setDoctorTimeSlots(doctorTimeSlotResponseDTOS);
                    });
        });

        return rostersWithAppointment;
    }

    private boolean hasAppointment(AppointmentStatusResponseDTO appointment,
                                   DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatus) {

        return appointment.getDate().equals(doctorDutyRosterStatus.getDate())
                && (appointment.getDoctorId().equals(doctorDutyRosterStatus.getDoctorId()))
                && (appointment.getSpecializationId().equals(doctorDutyRosterStatus.getSpecializationId()));
    }

    /*FETCH DOCTOR DUTY ROSTER FROM DOCTOR_DUTY_ROSTER_OVERRIDE FIRST
     AND THEN DOCTOR_DUTY ROSTER. THEN MERGE BOTH ROSTERS BASED ON THE REQUESTED SEARCH DATE, DOCTOR AND SPECIALIZATION*/
    private List<HospitalDeptDutyRosterStatusResponseDTO> fetchDepartmentStatus(HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        List<HospitalDeptDutyRosterStatusResponseDTO> doctorDutyRosterOverrideStatus =
                deptDutyRosterOverrideRepository.fetchHospitalDeptDutyRosterOverrideStatus(requestDTO);

//        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
//                doctorDutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

//        if (doctorDutyRosterOverrideStatus.isEmpty() && doctorDutyRosterStatus.isEmpty())
//            throw new NoContentFoundException(DoctorDutyRoster.class);

        return null;
    }


}
