package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.admin.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.admin.repository.DoctorRepository;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.admin.service.AppointmentStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.ALL;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT_STATUS;
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

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentService appointmentService;

    public AppointmentStatusServiceImpl(DoctorDutyRosterRepository doctorDutyRosterRepository,
                                        DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                        DoctorRepository doctorRepository,
                                        AppointmentService appointmentService) {
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentService = appointmentService;
    }

    @Override
    public AppointmentStatusDTO fetchAppointmentStatusResponseDTO
            (AppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_STATUS);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus = fetchDoctorStatus(requestDTO);

        validateDoctorDutyRosterStatus(doctorDutyRosterStatus);

        List<AppointmentStatusResponseDTO> appointments = fetchAppointmentStatus(requestDTO);

        setDoctorTimeSlot(requestDTO.getStatus(), doctorDutyRosterStatus, appointments);

        List<DoctorDropdownDTO> doctorInfo =
                doctorRepository.fetchDoctorAvatarInfo(requestDTO.getHospitalId(), requestDTO.getDoctorId());

        AppointmentStatusDTO appointmentStatusDTO = parseToAppointmentStatusDTO(doctorDutyRosterStatus, doctorInfo);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_STATUS, getDifferenceBetweenTwoTime(startTime));

        return appointmentStatusDTO;
    }

    private void setDoctorTimeSlot(String status,
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
                setDoctorTimeSlotForSelectedAppointmentStatus(doctorDutyRosterStatus, appointmentStatusResponseDTOS);
                break;
        }
    }

    /*FETCH DOCTOR DUTY ROSTER FROM DOCTOR_DUTY_ROSTER_OVERRIDE FIRST
      AND THEN DOCTOR_DUTY ROSTER. THEN MERGE BOTH ROSTERS BASED ON THE REQUESTED SEARCH DATE, DOCTOR AND SPECIALIZATION*/
    private List<DoctorDutyRosterStatusResponseDTO> fetchDoctorStatus(AppointmentStatusRequestDTO requestDTO) {

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterOverrideStatus =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
                doctorDutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

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

            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO)) {

                List<AppointmentStatusResponseDTO> appointmentMatchedWithRoster =
                        appointments.stream()
                                .filter(appointment -> hasAppointment(appointment, doctorDutyRosterStatusResponseDTO))
                                .collect(Collectors.toList());

                if (!ObjectUtils.isEmpty(appointmentMatchedWithRoster)) {

                    for (AppointmentStatusResponseDTO appointment : appointmentMatchedWithRoster) {

                        DoctorTimeSlotResponseDTO responseDTO = new DoctorTimeSlotResponseDTO();
                        responseDTO.setAppointmentNumber(appointment.getAppointmentNumber());
                        String[] appointmentTimeDetails = appointment.getAppointmentTimeDetails().split(COMMA_SEPARATED);

                        for (String appointmentTimeAndStatus : appointmentTimeDetails) {
                            String[] timeAndStatus = appointmentTimeAndStatus.split(HYPHEN);

                            responseDTO.setAppointmentTime(convert24HourTo12HourFormat(timeAndStatus[0]));
                            responseDTO.setStatus(timeAndStatus[1]);

                            parseAppointmentDetails(responseDTO, appointment);
                        }
                        doctorTimeSlots.add(responseDTO);

                    }

                    setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO, doctorTimeSlots);

                } else {
                    setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO, doctorTimeSlots);
                }
            }
        }
    }

    private void setDoctorTimeSlotForVacantAppointmentStatus(List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS,
                                                             List<AppointmentStatusResponseDTO> appointments) {

        for (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO : doctorDutyRosterStatusResponseDTOS) {

            doctorDutyRosterStatusResponseDTO.setWeekDayName(
                    doctorDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            List<DoctorTimeSlotResponseDTO> doctorTimeSlots = new ArrayList<>();

            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO)) {

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

                    setTimeSlotForVacantAppointmentStatus(
                            doctorDutyRosterStatusResponseDTO, matchedAppointmentWithStatus, doctorTimeSlots);

                } else {
                    setTimeSlotForVacantAppointmentStatus(doctorDutyRosterStatusResponseDTO,
                            null, doctorTimeSlots);
                }
            }
        }
    }

    /*IF STATUS IN SEARCH DTO IS NOT EMPTY AND IS NOT VACANT (i.e,PA/A/C),
     THEN RETURN ONLY APPOINTMENT DETAILS WITH RESPECTIVE STATUS.
     NO NEED TO FILTER WITH DOCTOR DUTY ROSTER RANGE
     */
    private void setDoctorTimeSlotForSelectedAppointmentStatus(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus,
            List<AppointmentStatusResponseDTO> appointments) {

        /*THROW EXCEPTION IF NO APPOINTMENT EXISTS FOR THE SELECTED STATUS*/
        if (appointments.isEmpty())
            throw new NoContentFoundException(Appointment.class);

        /*FILTER OUT FROM DOCTOR DUTY ROSTERS SUCH THAT IT CONTAINS ONLY THOSE ROSTERS HAVING
         * APPOINTMENT*/
        List<DoctorDutyRosterStatusResponseDTO> rostersWithAppointment = doctorDutyRosterStatus.stream()
                .filter(doctorDutyRoster -> (appointments.stream()
                        .anyMatch(appointment -> hasAppointment(appointment, doctorDutyRoster)))
                )
                .collect(Collectors.toList());

        /*ADD TO LIST ONLY IF DOCTOR DAY OFF STATUS IS 'N'
         * AND APPOINTMENT CONDITION MATCHES*/
        for (DoctorDutyRosterStatusResponseDTO doctorDutyRoster : rostersWithAppointment) {
            List<DoctorTimeSlotResponseDTO> doctorTimeSlotResponseDTOS = new ArrayList<>();

            if (doctorDutyRoster.getDayOffStatus().equals(NO)) {
                for (AppointmentStatusResponseDTO appointment : appointments) {

                    if (hasAppointment(appointment, doctorDutyRoster)) {
                        DoctorTimeSlotResponseDTO responseDTO = new DoctorTimeSlotResponseDTO();

                        doctorDutyRoster.setWeekDayName(doctorDutyRoster.getDate().getDayOfWeek().toString());

                        /*APPOINTMENT TIME - APPOINTMENT STATUS*/
                        String[] appointmentTimeDetails = appointment.getAppointmentTimeDetails()
                                .split(COMMA_SEPARATED);

                        for (String appointmentTimeAndStatus : appointmentTimeDetails) {
                            String[] timeAndStatus = appointmentTimeAndStatus.split(HYPHEN);

                            responseDTO.setAppointmentTime(convert24HourTo12HourFormat(timeAndStatus[0]));
                            responseDTO.setStatus(timeAndStatus[1]);

                            parseAppointmentDetails(responseDTO, appointment);
                            doctorTimeSlotResponseDTOS.add(responseDTO);
                        }
                    }

                    doctorDutyRoster.setDoctorTimeSlots(doctorTimeSlotResponseDTOS);
                }
            }
        }
    }

    private boolean hasAppointment(AppointmentStatusResponseDTO appointment,
                                   DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatus) {

        return appointment.getDate().equals(doctorDutyRosterStatus.getDate())
                && (appointment.getDoctorId().equals(doctorDutyRosterStatus.getDoctorId()))
                && (appointment.getSpecializationId().equals(doctorDutyRosterStatus.getSpecializationId()));
    }

    private static void setTimeSlotForAllAppointmentStatus
            (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
             List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        doctorTimeSlots = calculateTimeSlotsForAllAppointmentStatus(
                doctorDutyRosterStatusResponseDTO.getStartTime(),
                doctorDutyRosterStatusResponseDTO.getEndTime(),
                doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                doctorTimeSlots);

        doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
    }


    private static void setTimeSlotForVacantAppointmentStatus
            (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
             String matchedAppointmentWithStatus,
             List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        doctorTimeSlots = calculateTimeSlotsForVacantAppointmentStatus(
                doctorDutyRosterStatusResponseDTO.getStartTime(),
                doctorDutyRosterStatusResponseDTO.getEndTime(),
                doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                matchedAppointmentWithStatus,
                doctorTimeSlots
        );
        doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
    }

    /*IF DOCTOR DUTY ROSTERS IS NOT SET UP, THEN THROW EXCEPTION*/
    private void validateDoctorDutyRosterStatus(List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus) {
        if (doctorDutyRosterStatus.isEmpty())
            throw new NoContentFoundException(DoctorDutyRoster.class);
    }
}
