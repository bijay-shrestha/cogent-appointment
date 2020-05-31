package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.repository.*;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDeptUtils;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
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
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDeptUtils.parseToAvailabilityResponseWithRoom;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDeptUtils.parseToAvailabilityResponseWithoutRoom;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeFromDate;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;

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

        return null;
    }

    private AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchAvailableTimeSlots(HospitalDepartmentDutyRoster dutyRoster,
                                                                                        Long roomId,
                                                                                        AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO =
                fetchHospitalDeptDutyRoster(dutyRoster, roomId, requestDTO);

        AppointmentCheckAvailabilityResponseDTO responseDTO;

        return null;

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


    private AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchHospitalDeptDutyRoster(
            AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        Date date = utilDateToSqlDate(requestDTO.getAppointmentDate());

        List<HospitalDepartmentDutyRoster> dutyRosters =
                hospitalDeptDutyRosterRepository.fetchHospitalDeptDutyRoster(
                        requestDTO.getAppointmentDate(), requestDTO.getHospitalDepartmentId()
                );

        boolean hasRoomEnabled = dutyRosters.stream().anyMatch(dutyRoster -> dutyRoster.getIsRoomEnabled().equals(YES));

        List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomInfo = new ArrayList<>();
        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO = null;
        HospitalDeptDutyRosterRoomInfoResponseDTO roomInfo = new HospitalDeptDutyRosterRoomInfoResponseDTO();

        AppointmentHospitalDeptCheckAvailabilityResponseDTO responseDTO = new AppointmentHospitalDeptCheckAvailabilityResponseDTO();

        if (hasRoomEnabled) {

            for (HospitalDepartmentDutyRoster dutyRoster : dutyRosters) {
                roomInfo = hospitalDeptDutyRosterRoomInfoRepository.fetchHospitalDeptRoomInfo(dutyRoster.getId());

                availableRoomInfo.add(roomInfo);

                if (Objects.isNull(dutyRosterTimeResponseTO)) {

                    dutyRosterTimeResponseTO = fetchHospitalDeptDutyRoster(
                            dutyRoster,
                            availableRoomInfo.get(0).getRoomId(), requestDTO
                    );

                    if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES))
                        return parseToAvailabilityResponseWithRoom(date, availableRoomInfo, roomInfo);
                }
            }

            List<String> availableTimeSlots = filterHospitalDeptTimeWithAppointment(
                    dutyRosterTimeResponseTO, roomInfo.getRoomId(), requestDTO);




        } else {
            dutyRosterTimeResponseTO = fetchHospitalDeptDutyRoster(dutyRosters.get(0), null, requestDTO);

            if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES))
                return parseToAvailabilityResponseWithoutRoom(date, availableRoomInfo);

            responseDTO = parseToAvailabilityResponseWithoutRoom(date, availableRoomInfo);

            List<String> availableTimeSlots = filterHospitalDeptTimeWithAppointment(
                    dutyRosterTimeResponseTO, roomInfo.getRoomId(), requestDTO);
        }


        return responseDTO;
    }


    private List<String> filterHospitalDeptTimeWithAppointment(HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO,
                                                               Long roomId,
                                                               AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                appointmentRepository.fetchBookedAppointmentDeptWise(requestDTO, roomId);

        final Duration rosterGapDuration = Minutes.minutes(dutyRosterTimeResponseTO.getRosterGapDuration())
                .toStandardDuration();

        return AppointmentHospitalDeptUtils.calculateAvailableTimeSlots(
                getTimeFromDate(dutyRosterTimeResponseTO.getStartTime()),
                getTimeFromDate(dutyRosterTimeResponseTO.getEndTime()),
                rosterGapDuration, bookedAppointments);
    }
}
