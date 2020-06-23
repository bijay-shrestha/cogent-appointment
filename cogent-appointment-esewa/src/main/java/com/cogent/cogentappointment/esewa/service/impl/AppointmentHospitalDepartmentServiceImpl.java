package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.commons.utils.NepaliDateUtility;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.availableDate.AppointmentAvailableDateDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.availableDate.AppointmentAvailableDateResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.HospitalDepartmentDoctorInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterAvailableDateResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.*;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentReservationLog;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentHospitalDepartmentMessage.APPOINTMENT_AVAILABLE_DATE_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDepartmentLog.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDepartmentReservationLog.APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOGS;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentDutyRosterLog.HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentUtils.*;
import static com.cogent.cogentappointment.esewa.utils.AppointmentUtils.parseToStatusResponseDTO;
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

    private final AppointmentHospitalDepartmentReservationLogRepository appointmentHospitalDepartmentReservationLogRepository;

    private final HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository weekDaysDutyRosterDoctorInfoRepository;

    private final NepaliDateUtility nepaliDateUtility;

    public AppointmentHospitalDepartmentServiceImpl(
            HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository,
            HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository,
            HospitalDeptWeekDaysDutyRosterRepository hospitalDeptWeekDaysDutyRosterRepository,
            AppointmentRepository appointmentRepository,
            HospitalDeptDutyRosterRoomInfoRepository hospitalDeptDutyRosterRoomInfoRepository,
            AppointmentHospitalDepartmentReservationLogRepository appointmentHospitalDepartmentReservationLogRepository,
            HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository weekDaysDutyRosterDoctorInfoRepository,
            NepaliDateUtility nepaliDateUtility) {
        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
        this.hospitalDeptDutyRosterOverrideRepository = hospitalDeptDutyRosterOverrideRepository;
        this.hospitalDeptWeekDaysDutyRosterRepository = hospitalDeptWeekDaysDutyRosterRepository;
        this.appointmentRepository = appointmentRepository;
        this.hospitalDeptDutyRosterRoomInfoRepository = hospitalDeptDutyRosterRoomInfoRepository;
        this.appointmentHospitalDepartmentReservationLogRepository = appointmentHospitalDepartmentReservationLogRepository;
        this.weekDaysDutyRosterDoctorInfoRepository = weekDaysDutyRosterDoctorInfoRepository;
        this.nepaliDateUtility = nepaliDateUtility;
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

    @Override
    public AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO fetchAvailableTimeSlotsRoomWise(
            AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        validateIfRequestIsPastDate(requestDTO.getAppointmentDate());

        HospitalDepartmentDutyRoster dutyRoster = fetchHospitalDepartmentDutyRoster(requestDTO.getHddRosterId());

        String roomNumber = fetchRoomNumber(requestDTO.getHddRosterId(), requestDTO.getHospitalDepartmentRoomInfoId());

        AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO responseDTO =
                parseAvailableTimeSlotsRoomWise(dutyRoster, requestDTO, roomNumber);

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public StatusResponseDTO cancelRegistration(Long appointmentReservationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOGS);

        AppointmentHospitalDepartmentReservationLog appointmentReservationLog =
                fetchAppointmentHospitalDeptReservationLogById(appointmentReservationId);

        if (!Objects.isNull(appointmentReservationLog))
            appointmentHospitalDepartmentReservationLogRepository.delete(appointmentReservationLog);

        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOGS,
                getDifferenceBetweenTwoTime(startTime));

        return parseToStatusResponseDTO();
    }

    @Override
    public AppointmentAvailableDateResponseDTO fetchAvailableDepartmentDates(Long hospitalDepartmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, AVAILABLE_APPOINTMENT_DATES);

        List<HospitalDepartmentDutyRoster> hospitalDepartmentDutyRosters =
                hospitalDeptDutyRosterRepository.fetchHospitalDeptDutyRoster(hospitalDepartmentId);

        List<AppointmentAvailableDateDTO> availableDates = new ArrayList<>();

        hospitalDepartmentDutyRosters.forEach(dutyRoster -> {

            fetchAvailableDatesFromWeekDaysRoster(dutyRoster, availableDates);

            if (dutyRoster.getHasOverrideDutyRoster().equals(YES))
                fetchAvailableDatesFromOverrideRoster(dutyRoster.getId(), availableDates);
        });

        if (ObjectUtils.isEmpty(availableDates))
            NO_AVAILABLE_APPOINTMENT_DATES_FOUND.get();

        availableDates.sort(Comparator.comparing(AppointmentAvailableDateDTO::getAvailableDate));

        AppointmentAvailableDateResponseDTO availableAppointmentDates =
                parseAvailableAppointmentDates(availableDates);

        log.info(FETCHING_PROCESS_COMPLETED, AVAILABLE_APPOINTMENT_DATES, getDifferenceBetweenTwoTime(startTime));

        return availableAppointmentDates;
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

        List<Long> hddRosterIds = dutyRosters.stream().map(HospitalDepartmentDutyRoster::getId)
                .collect(Collectors.toList());

        List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomInfo = fetchHospitalDeptRoomInfo(hddRosterIds);

        HospitalDeptDutyRosterRoomInfoResponseDTO roomInfo = availableRoomInfo.get(0);

        HospitalDepartmentDutyRoster dutyRoster = dutyRosters.stream()
                .filter(dutyRoster1 -> dutyRoster1.getId().equals(roomInfo.getHddRosterId()))
                .findFirst().orElse(null);

        return parseAvailableTimeSlotsWithRoomInfo(dutyRoster, requestDTO, availableRoomInfo, roomInfo);
    }

    private AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchAvailableTimeSlotsWithoutRoom(
            List<HospitalDepartmentDutyRoster> dutyRosters,
            AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        HospitalDepartmentDutyRoster dutyRoster = dutyRosters.get(0);

        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO = fetchHospitalDeptDutyRoster(
                dutyRoster, null,
                requestDTO.getAppointmentDate(), requestDTO.getHospitalDepartmentId()
        );

        Date date = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String startTime = getTimeFromDate(dutyRosterTimeResponseTO.getStartTime());
        String endTime = getTimeFromDate(dutyRosterTimeResponseTO.getEndTime());

        if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES))
            return parseToAvailabilityResponseWithoutRoom(date, startTime, endTime, new ArrayList<>(), new ArrayList<>());

        List<String> availableTimeSlots = filterHospitalDeptTimeWithAppointment(
                startTime, endTime,
                dutyRosterTimeResponseTO.getRosterGapDuration(), null,
                requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId()
        );

        filterHospitalDeptTimeWithAppointmentReservation(availableTimeSlots,
                requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId(), null
        );

        List<HospitalDepartmentDoctorInfoResponseDTO> availableDoctors = fetchAvailableDoctors(dutyRoster.getId(),
                getDayCodeFromDate(date));

        return parseToAvailabilityResponseWithoutRoom(
                date, startTime, endTime, availableTimeSlots, availableDoctors);
    }

    /*ASSUMING HOSPITAL DEPARTMENT CAN BE CREATED EITHER WITHOUT ROOM OR WITH ROOM(CAN BE MULTIPLE)*/
    private HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRoster(
            HospitalDepartmentDutyRoster dutyRoster,
            Long hospitalDepartmentRoomInfoId,
            Date appointmentDate,
            Long hospitalDepartmentId) {

        if (dutyRoster.getHasOverrideDutyRoster().equals(YES)) {
            HospitalDeptDutyRosterTimeResponseTO overrideRoster =
                    hospitalDeptDutyRosterOverrideRepository.fetchHospitalDeptDutyRosterOverrideTimeInfo(
                            dutyRoster.getId(),
                            appointmentDate, hospitalDepartmentId,
                            hospitalDepartmentRoomInfoId);

            if (!Objects.isNull(overrideRoster))
                return overrideRoster;
            else
                return hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                        dutyRoster.getId(), appointmentDate);

        } else {
            return hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                    dutyRoster.getId(), appointmentDate);
        }
    }

    private List<HospitalDeptDutyRosterRoomInfoResponseDTO> fetchHospitalDeptRoomInfo(List<Long> hddRosterIds) {

        List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomInfo =
                hospitalDeptDutyRosterRoomInfoRepository.fetchHospitalDeptRoomInfo(hddRosterIds);

        availableRoomInfo.sort(Comparator.comparing(HospitalDeptDutyRosterRoomInfoResponseDTO::getRoomNumber));

        return availableRoomInfo;
    }

    private List<String> filterHospitalDeptTimeWithAppointment(String startTime,
                                                               String endTime,
                                                               Integer rosterGapDuration,
                                                               Long hospitalDepartmentRoomInfoId,
                                                               Date appointmentDate,
                                                               Long hospitalDepartmentId) {

        List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                appointmentRepository.fetchBookedAppointmentDeptWise(
                        appointmentDate, hospitalDepartmentId, hospitalDepartmentRoomInfoId
                );

        final Duration gapDuration = Minutes.minutes(rosterGapDuration).toStandardDuration();

        return calculateAvailableTimeSlots(
                startTime, endTime, gapDuration, bookedAppointments, appointmentDate);
    }

    private HospitalDepartmentDutyRoster fetchHospitalDepartmentDutyRoster(Long hddRosterId) {
        return hospitalDeptDutyRosterRepository.fetchById(hddRosterId)
                .orElseThrow(() -> HOSPITAL_DEPT_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND.apply(hddRosterId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT_DUTY_ROSTER, id);
        throw new NoContentFoundException(HospitalDepartmentDutyRoster.class, "id", id.toString());
    };

    private AppointmentHospitalDeptCheckAvailabilityResponseDTO parseAvailableTimeSlotsWithRoomInfo(
            HospitalDepartmentDutyRoster dutyRoster,
            AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO,
            List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomInfo,
            HospitalDeptDutyRosterRoomInfoResponseDTO roomInfo) {

        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO = fetchHospitalDeptDutyRoster(
                dutyRoster,
                roomInfo.getHospitalDepartmentRoomInfoId(),
                requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId()
        );

        Date date = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String startTime = getTimeFromDate(dutyRosterTimeResponseTO.getStartTime());
        String endTime = getTimeFromDate(dutyRosterTimeResponseTO.getEndTime());

        if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES))
            return parseToAvailabilityResponseWithRoom(
                    date, availableRoomInfo, roomInfo, startTime, endTime, new ArrayList<>(), new ArrayList<>()
            );

        List<String> availableTimeSlots = filterHospitalDeptTimeWithAppointment(
                startTime, endTime,
                dutyRosterTimeResponseTO.getRosterGapDuration(), roomInfo.getHospitalDepartmentRoomInfoId(),
                requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId()
        );

        filterHospitalDeptTimeWithAppointmentReservation(availableTimeSlots,
                requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId(),
                roomInfo.getHospitalDepartmentRoomInfoId()
        );

        List<HospitalDepartmentDoctorInfoResponseDTO> availableDoctors = fetchAvailableDoctors(dutyRoster.getId(),
                getDayCodeFromDate(date));

        return parseToAvailabilityResponseWithRoom(
                date, availableRoomInfo, roomInfo, startTime, endTime, availableTimeSlots, availableDoctors
        );
    }

    private AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO parseAvailableTimeSlotsRoomWise(
            HospitalDepartmentDutyRoster dutyRoster,
            AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO requestDTO,
            String roomNumber) {

        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO = fetchHospitalDeptDutyRoster(
                dutyRoster,
                requestDTO.getHospitalDepartmentRoomInfoId(),
                requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId()
        );

        Date date = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String startTime = getTimeFromDate(dutyRosterTimeResponseTO.getStartTime());
        String endTime = getTimeFromDate(dutyRosterTimeResponseTO.getEndTime());

        if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES))
            return parseToAvailabilityResponseRoomWise(date, roomNumber, startTime, endTime, new ArrayList<>());

        List<String> availableTimeSlots = filterHospitalDeptTimeWithAppointment(
                startTime, endTime, dutyRosterTimeResponseTO.getRosterGapDuration(),
                requestDTO.getHospitalDepartmentRoomInfoId(), requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId()
        );

        filterHospitalDeptTimeWithAppointmentReservation(availableTimeSlots,
                requestDTO.getAppointmentDate(),
                requestDTO.getHospitalDepartmentId(),
                requestDTO.getHospitalDepartmentRoomInfoId()
        );

        return parseToAvailabilityResponseRoomWise(date, roomNumber, startTime, endTime, availableTimeSlots);
    }

    private String fetchRoomNumber(Long hddRosterId, Long hospitalDepartmentRoomInfoId) {
        return hospitalDeptDutyRosterRoomInfoRepository.fetchRoomNumber(hddRosterId, hospitalDepartmentRoomInfoId);
    }

    private void filterHospitalDeptTimeWithAppointmentReservation(List<String> availableTimeSlots,
                                                                  Date appointmentDate,
                                                                  Long hospitalDepartmentId,
                                                                  Long hospitalDepartmentRoomInfoId) {

        List<String> bookedAppointmentReservations =
                appointmentHospitalDepartmentReservationLogRepository.fetchBookedAppointmentReservations(
                        appointmentDate, hospitalDepartmentId, hospitalDepartmentRoomInfoId);

        availableTimeSlots.removeAll(bookedAppointmentReservations);
    }

    private List<HospitalDepartmentDoctorInfoResponseDTO> fetchAvailableDoctors(Long hddRosterId, String weekDayCode) {
        return weekDaysDutyRosterDoctorInfoRepository.fetchAvailableDoctors(hddRosterId, weekDayCode);
    }

    private AppointmentHospitalDepartmentReservationLog fetchAppointmentHospitalDeptReservationLogById(
            Long appointmentReservationId) {
        return appointmentHospitalDepartmentReservationLogRepository.findAppointmentReservationLogById(
                appointmentReservationId);
    }

    private void fetchAvailableDatesFromWeekDaysRoster(HospitalDepartmentDutyRoster dutyRoster,
                                                       List<AppointmentAvailableDateDTO> availableDates) {

        List<String> availableWeekDays =
                hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysCode(dutyRoster.getId());

        LocalDate startLocalDate = convertDateToLocalDate(dutyRoster.getFromDate());
        LocalDate endLocalDate = convertDateToLocalDate(dutyRoster.getToDate());

        Stream.iterate(startLocalDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1)
                .forEach(localDate -> {

                    boolean isPastDate = localDate.isBefore(LocalDate.now());
                    if (!isPastDate) {
                        String weekDayCode = localDate.getDayOfWeek().toString().substring(0, 3);

                        if (availableWeekDays.contains(weekDayCode) &&
                                !isAppointmentDateAdded(availableDates, localDate))
                            availableDates.add(parseAvailableAppointmentDates(localDate, nepaliDateUtility));
                    }
                });
    }

    private void fetchAvailableDatesFromOverrideRoster(Long hddRosterId,
                                                       List<AppointmentAvailableDateDTO> availableDates) {

        List<HospitalDeptDutyRosterAvailableDateResponseDTO> overrideRosterDates =
                hospitalDeptDutyRosterOverrideRepository.fetchOverrideRosterDates(hddRosterId);

        overrideRosterDates.forEach(overrideRoster -> {

            LocalDate startDate = convertDateToLocalDate(overrideRoster.getFromDate());
            LocalDate endDate = convertDateToLocalDate(overrideRoster.getToDate());

            Stream.iterate(startDate, date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                    .forEach(localDate -> {

                        if (overrideRoster.getDayOffStatus().equals(NO) &&
                                !isAppointmentDateAdded(availableDates, localDate))
                            availableDates.add(parseAvailableAppointmentDates(localDate, nepaliDateUtility));
                    });
        });
    }

    private Supplier<NoContentFoundException> NO_AVAILABLE_APPOINTMENT_DATES_FOUND = () -> {
        log.error(APPOINTMENT_AVAILABLE_DATE_NOT_FOUND);
        throw new NoContentFoundException(APPOINTMENT_AVAILABLE_DATE_NOT_FOUND);
    };

    private boolean isAppointmentDateAdded(List<AppointmentAvailableDateDTO> availableDates,
                                           LocalDate localDate) {

        boolean isAppointmentDateAdded = availableDates.stream()
                .anyMatch(availableDate -> availableDate.getAvailableDate().equals(localDate));

        System.out.println(isAppointmentDateAdded);

        return isAppointmentDateAdded;
    }
}
