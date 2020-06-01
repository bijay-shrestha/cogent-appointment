package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.repository.*;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDepartmentLog.CHECK_AVAILABILITY_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDepartmentLog.CHECK_AVAILABILITY_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDeptUtils.*;
import static com.cogent.cogentappointment.esewa.utils.AppointmentUtils.validateIfRequestIsPastDate;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti on 28/05/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentServiceImpl implements AppointmentHospitalDepartmentService {

    private final HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository;

    private final HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository;

    private final HospitalDeptWeekDaysDutyRosterRepository hospitalDeptWeekDaysDutyRosterRepository;

    private final AppointmentRepository appointmentRepository;

    private final HospitalDeptDutyRosterRoomInfoRepository hospitalDeptDutyRosterRoomInfoRepository;

    public AppointmentHospitalDepartmentServiceImpl(
            HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository,
            HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository,
            HospitalDeptWeekDaysDutyRosterRepository hospitalDeptWeekDaysDutyRosterRepository,
            AppointmentRepository appointmentRepository,
            HospitalDeptDutyRosterRoomInfoRepository hospitalDeptDutyRosterRoomInfoRepository) {
        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
        this.hospitalDeptDutyRosterOverrideRepository = hospitalDeptDutyRosterOverrideRepository;
        this.hospitalDeptWeekDaysDutyRosterRepository = hospitalDeptWeekDaysDutyRosterRepository;
        this.appointmentRepository = appointmentRepository;
        this.hospitalDeptDutyRosterRoomInfoRepository = hospitalDeptDutyRosterRoomInfoRepository;
    }

    @Override
    public AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchAvailableTimeSlots
            (AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        validateIfRequestIsPastDate(requestDTO.getAppointmentDate());

        AppointmentHospitalDeptCheckAvailabilityResponseDTO availableTimeSlots =
                fetchAvailableHospitalDeptTimeSlots(requestDTO);

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return availableTimeSlots;
    }

    private AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchAvailableHospitalDeptTimeSlots(
            AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        List<HospitalDepartmentDutyRoster> dutyRosters =
                hospitalDeptDutyRosterRepository.fetchHospitalDeptDutyRoster(
                        requestDTO.getAppointmentDate(), requestDTO.getHospitalDepartmentId()
                );

        boolean hasRoomEnabled = dutyRosters.stream().anyMatch(dutyRoster -> dutyRoster.getIsRoomEnabled().equals(YES));

        if (hasRoomEnabled)
            return fetchAvailableTimeSlotsWithRoomInfo(dutyRosters, requestDTO);

        else
            return fetchAvailableTimeSlotsWithoutRoom(dutyRosters, requestDTO);
    }

    private AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchAvailableTimeSlotsWithRoomInfo(
            List<HospitalDepartmentDutyRoster> dutyRosters,
            AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        Date date = utilDateToSqlDate(requestDTO.getAppointmentDate());

        HospitalDeptDutyRosterRoomInfoResponseDTO roomInfo = new HospitalDeptDutyRosterRoomInfoResponseDTO();
        List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomInfo = new ArrayList<>();
        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO = null;

        for (HospitalDepartmentDutyRoster dutyRoster : dutyRosters) {
            roomInfo = hospitalDeptDutyRosterRoomInfoRepository.fetchHospitalDeptRoomInfo(dutyRoster.getId());

            availableRoomInfo.add(roomInfo);

            if (Objects.isNull(dutyRosterTimeResponseTO)) {

                dutyRosterTimeResponseTO = fetchHospitalDeptDutyRoster(
                        dutyRoster,
                        availableRoomInfo.get(0).getRoomId(), requestDTO
                );
            }
        }

        assert dutyRosterTimeResponseTO != null;
        String startTime = getTimeFromDate(dutyRosterTimeResponseTO.getStartTime());
        String endTime = getTimeFromDate(dutyRosterTimeResponseTO.getEndTime());

        if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES))
            return parseToAvailabilityResponseWithRoom(
                    date, availableRoomInfo, roomInfo, startTime, endTime, new ArrayList<>()
            );

        List<String> availableTimeSlots = filterHospitalDeptTimeWithAppointment(
                startTime, endTime, dutyRosterTimeResponseTO.getRosterGapDuration(), roomInfo.getRoomId(), requestDTO
        );

        return parseToAvailabilityResponseWithRoom(
                date, availableRoomInfo, roomInfo, startTime, endTime, availableTimeSlots
        );
    }

    private AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchAvailableTimeSlotsWithoutRoom(
            List<HospitalDepartmentDutyRoster> dutyRosters,
            AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO = fetchHospitalDeptDutyRoster(
                dutyRosters.get(0), null, requestDTO
        );

        Date date = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String startTime = getTimeFromDate(dutyRosterTimeResponseTO.getStartTime());
        String endTime = getTimeFromDate(dutyRosterTimeResponseTO.getEndTime());

        if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES))
            return parseToAvailabilityResponseWithoutRoom(date, startTime, endTime, new ArrayList<>());

        List<String> availableTimeSlots = filterHospitalDeptTimeWithAppointment(
                startTime, endTime, dutyRosterTimeResponseTO.getRosterGapDuration(), null, requestDTO);

        return parseToAvailabilityResponseWithoutRoom(date, startTime, endTime, availableTimeSlots);
    }

    /*ASSUMING HOSPITAL DEPARTMENT CAN BE CREATED EITHER WITHOUT ROOM OR WITH ROOM(CAN BE MULTIPLE)*/
    private HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRoster(
            HospitalDepartmentDutyRoster dutyRoster,
            Long roomId,
            AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        if (dutyRoster.getHasOverrideDutyRoster().equals(YES)) {
            HospitalDeptDutyRosterTimeResponseTO overrideRoster =
                    hospitalDeptDutyRosterOverrideRepository.fetchHospitalDeptDutyRosterOverrideTimeInfo(
                            dutyRoster.getId(),
                            requestDTO.getAppointmentDate(),
                            requestDTO.getHospitalDepartmentId(),
                            roomId);

            if (!Objects.isNull(overrideRoster)) {
                return overrideRoster;
            } else {
                return hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                        dutyRoster.getId(), requestDTO.getAppointmentDate());
            }

        } else {
            return hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                    dutyRoster.getId(), requestDTO.getAppointmentDate());
        }
    }

    private List<String> filterHospitalDeptTimeWithAppointment(String startTime,
                                                               String endTime,
                                                               Integer rosterGapDuration,
                                                               Long roomId,
                                                               AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                appointmentRepository.fetchBookedAppointmentDeptWise(requestDTO, roomId);

        final Duration gapDuration = Minutes.minutes(rosterGapDuration).toStandardDuration();

        return calculateAvailableTimeSlots(startTime, endTime, gapDuration, bookedAppointments, requestDTO.getAppointmentDate());
    }
}
