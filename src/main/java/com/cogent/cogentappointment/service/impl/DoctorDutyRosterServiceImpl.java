package com.cogent.cogentappointment.service.impl;

import com.cogent.admin.dto.commons.DeleteRequestDTO;
import com.cogent.admin.dto.request.doctorDutyRoster.*;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterDetailResponseDTO;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterMinimalResponseDTO;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.admin.exception.BadRequestException;
import com.cogent.admin.exception.DataDuplicationException;
import com.cogent.admin.exception.NoContentFoundException;
import com.cogent.admin.feign.dto.request.appointment.AppointmentCountRequestDTO;
import com.cogent.admin.feign.dto.response.appointment.AppointmentDateResponseDTO;
import com.cogent.admin.feign.service.AppointmentService;
import com.cogent.admin.feign.service.EmailService;
import com.cogent.admin.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.admin.repository.DoctorDutyRosterRepository;
import com.cogent.admin.repository.DoctorWeekDaysDutyRosterRepository;
import com.cogent.admin.service.*;
import com.cogent.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cogent.admin.constants.ErrorMessageConstants.AppointmentServiceMessage.APPOINTMENT_EXISTS_MESSAGE;
import static com.cogent.admin.constants.ErrorMessageConstants.DoctorDutyRosterServiceMessages.BAD_REQUEST_MESSAGE;
import static com.cogent.admin.constants.ErrorMessageConstants.DoctorDutyRosterServiceMessages.DUPLICATION_MESSAGE;
import static com.cogent.admin.constants.StatusConstants.NO;
import static com.cogent.admin.constants.StatusConstants.YES;
import static com.cogent.admin.log.CommonLogConstant.*;
import static com.cogent.admin.log.constants.DoctorDutyRosterLog.*;
import static com.cogent.admin.utils.DateUtils.*;
import static com.cogent.admin.utils.DoctorDutyRosterOverrideUtils.*;
import static com.cogent.admin.utils.DoctorDutyRosterUtils.*;

/**
 * @author smriti on 26/11/2019
 */
@Service
@Transactional
@Slf4j
public class DoctorDutyRosterServiceImpl implements DoctorDutyRosterService {

    private final DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final DoctorService doctorService;

    private final SpecializationService specializationService;

    private final DoctorTypeService doctorTypeService;

    private final WeekDaysService weekDaysService;

    private final DoctorWeekDaysDutyRosterRepository doctorWeekDaysDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final EmailService emailService;

    private final AppointmentService appointmentService;

    public DoctorDutyRosterServiceImpl(DoctorDutyRosterRepository doctorDutyRosterRepository,
                                       DoctorService doctorService,
                                       SpecializationService specializationService,
                                       DoctorTypeService doctorTypeService,
                                       WeekDaysService weekDaysService,
                                       DoctorWeekDaysDutyRosterRepository doctorWeekDaysDutyRosterRepository,
                                       DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                       EmailService emailService,
                                       AppointmentService appointmentService) {
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.doctorTypeService = doctorTypeService;
        this.weekDaysService = weekDaysService;
        this.doctorWeekDaysDutyRosterRepository = doctorWeekDaysDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.emailService = emailService;
        this.appointmentService = appointmentService;
    }

    @Override
    public void save(DoctorDutyRosterRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);

        validateDoctorDutyRosterCount(requestDTO.getDoctorId(), requestDTO.getSpecializationId(),
                requestDTO.getFromDate(), requestDTO.getToDate());

        DoctorDutyRoster doctorDutyRoster = parseToDoctorDutyRoster(requestDTO,
                findDoctorById(requestDTO.getDoctorId()),
                findSpecializationById(requestDTO.getSpecializationId()),
                findDoctorTypeById(requestDTO.getDoctorTypeId()));

        save(doctorDutyRoster);

        saveDoctorWeekDaysDutyRoster(doctorDutyRoster, requestDTO.getDoctorWeekDaysDutyRosterRequestDTOS());

        saveDoctorDutyRosterOverride(doctorDutyRoster, requestDTO.getDoctorDutyRosterOverrideRequestDTOS());

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(DoctorDutyRosterUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(updateRequestDTO.getDoctorDutyRosterId());

        List<DoctorWeekDaysDutyRoster> weekDaysDutyRosters =
                doctorWeekDaysDutyRosterRepository.fetchByDoctorDutyRosterId(doctorDutyRoster.getId());

        List<DoctorWeekDaysDutyRosterUpdateRequestDTO> unmatchedWeekDaysRosterList =
                filterOriginalAndUpdatedWeekDaysRoster(
                        updateRequestDTO.getWeekDaysDutyRosterUpdateRequestDTOS(), weekDaysDutyRosters);

        List<AppointmentDateResponseDTO> appointmentDateResponseDTO = fetchAppointmentDateResponseDTOS(doctorDutyRoster);

        filterUpdatedWeekDaysRosterAndAppointment(unmatchedWeekDaysRosterList, appointmentDateResponseDTO);

        parseToUpdatedDoctorDutyRoster(doctorDutyRoster, updateRequestDTO);

        updateDoctorWeekDaysDutyRoster(doctorDutyRoster, unmatchedWeekDaysRosterList);

        updateDoctorDutyRosterOverrideStatus(doctorDutyRoster);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateDoctorDutyRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER_OVERRIDE);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(updateRequestDTO.getDoctorDutyRosterId());

        Long doctorId = doctorDutyRoster.getDoctorId().getId();
        Long specializationId = doctorDutyRoster.getSpecializationId().getId();
        Date overrideFromDate = updateRequestDTO.getOverrideFromDate();
        Date overrideToDate = updateRequestDTO.getOverrideToDate();

        validateIfOverrideDateIsBetweenDoctorDutyRoster(
                doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(), overrideFromDate, overrideToDate);

        validateDoctorDutyRosterOverrideCount(doctorId, specializationId,
                updateRequestDTO.getOverrideFromDate(), updateRequestDTO.getOverrideToDate());

        validateAppointmentCount(parseToAppointmentCountRequestTO(
                overrideFromDate, overrideToDate, doctorId, specializationId));

        saveOrUpdateDoctorDutyRosterOverride(updateRequestDTO, doctorDutyRoster);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
    }

    public void saveOrUpdateDoctorDutyRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO,
                                                     DoctorDutyRoster doctorDutyRoster) {

        if (Objects.isNull(updateRequestDTO.getDoctorDutyRosterOverrideId())) {
            DoctorDutyRosterOverride doctorDutyRosterOverride = parseToUpdatedDoctorDutyRosterOverride(
                    updateRequestDTO, doctorDutyRoster, new DoctorDutyRosterOverride());
            saveDoctorDutyRosterOverride(doctorDutyRosterOverride);
        } else {
            DoctorDutyRosterOverride doctorDutyRosterOverride =
                    doctorDutyRosterOverrideRepository.fetchById(updateRequestDTO.getDoctorDutyRosterOverrideId());
            parseToUpdatedDoctorDutyRosterOverride(updateRequestDTO, doctorDutyRoster, doctorDutyRosterOverride);
        }
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(deleteRequestDTO.getId());

        convertToDeletedDoctorDutyRoster(doctorDutyRoster, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                           Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);

        List<DoctorDutyRosterMinimalResponseDTO> responseDTOS =
                doctorDutyRosterRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);

        DoctorDutyRosterDetailResponseDTO responseDTO = doctorDutyRosterRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterTime(DoctorDutyRosterTimeRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);
        DoctorDutyRosterTimeResponseDTO responseDTO;

        responseDTO = doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideTime(requestDTO);

        if (Objects.isNull(responseDTO))
            responseDTO = doctorDutyRosterRepository.fetchDoctorDutyRosterTime(requestDTO);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
        return responseDTO;
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus
            (DoctorDutyRosterStatusRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DOCTOR_DUTY_ROSTER_STATUS);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterOverrideStatus =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
                doctorDutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

        List<DoctorDutyRosterStatusResponseDTO> mergedList =
                mergeOverrideAndActualDoctorDutyRoster(doctorDutyRosterOverrideStatus, doctorDutyRosterStatus);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));

        return mergedList;
    }

    private void validateAppointmentCount(AppointmentCountRequestDTO appointmentCountRequestDTO) {
        List<AppointmentDateResponseDTO> appointmentCount =
                appointmentService.fetchAppointmentDates(appointmentCountRequestDTO);

        if (appointmentCount.size() > 0)
            throw new BadRequestException(APPOINTMENT_EXISTS_MESSAGE);
    }

    private void validateDoctorDutyRosterCount(Long doctorId, Long specializationId,
                                               Date fromDate, Date toDate) {

        Long doctorDutyRosterCount = doctorDutyRosterRepository.validateDoctorDutyRosterCount(
                doctorId, specializationId, fromDate, toDate);

        if (doctorDutyRosterCount.intValue() > 0)
            throw new DataDuplicationException(DUPLICATION_MESSAGE);
    }

    private void validateIfOverrideDateIsBetweenDoctorDutyRoster(Date dutyRosterFromDate,
                                                                 Date dutyRosterToDate,
                                                                 Date overrideFromDate,
                                                                 Date overrideToDate) {

        boolean isDateBetweenInclusive =
                isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, overrideFromDate)
                        && isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, overrideToDate);

        if (!isDateBetweenInclusive)
            throw new BadRequestException(BAD_REQUEST_MESSAGE);
    }

    private void validateDoctorDutyRosterOverrideCount(Long doctorId, Long specializationId,
                                                       Date fromDate, Date toDate) {

        Long doctorDutyRosterOverrideCount = doctorDutyRosterOverrideRepository.validateDoctorDutyRosterOverrideCount(
                doctorId, specializationId, fromDate, toDate);

        if (doctorDutyRosterOverrideCount.intValue() > 0)
            throw new DataDuplicationException(DUPLICATION_MESSAGE);
    }

    private Doctor findDoctorById(Long doctorId) {
        return doctorService.fetchDoctorById(doctorId);
    }

    private Specialization findSpecializationById(Long specializationId) {
        return specializationService.fetchActiveSpecializationById(specializationId);
    }

    private DoctorType findDoctorTypeById(Long doctorTypeId) {
        return doctorTypeService.fetchDoctorTypeById(doctorTypeId);
    }

    private WeekDays findWeekDaysById(Long weekDaysId) {
        return weekDaysService.fetchWeekDaysById(weekDaysId);
    }

    private void save(DoctorDutyRoster doctorDutyRoster) {
        doctorDutyRosterRepository.save(doctorDutyRoster);
    }

    private void saveDoctorWeekDaysDutyRoster(DoctorDutyRoster doctorDutyRoster,
                                              List<DoctorWeekDaysDutyRosterRequestDTO> doctorWeekDaysDutyRosterRequestDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_WEEK_DAYS_DUTY_ROSTER);

        List<DoctorWeekDaysDutyRoster> doctorWeekDaysDutyRosters = doctorWeekDaysDutyRosterRequestDTOS.stream()
                .map(requestDTO -> {
                    WeekDays weekDays = findWeekDaysById(requestDTO.getWeekDaysId());
                    return parseToDoctorWeekDaysDutyRoster(
                            requestDTO, doctorDutyRoster, weekDays, new DoctorWeekDaysDutyRoster()
                    );
                }).collect(Collectors.toList());

        saveDoctorWeekDaysDutyRoster(doctorWeekDaysDutyRosters);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_WEEK_DAYS_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorDutyRosterOverride(DoctorDutyRoster doctorDutyRoster,
                                              List<DoctorDutyRosterOverrideRequestDTO> overrideRequestDTOS) {

        if (doctorDutyRoster.getHasOverrideDutyRoster().equals(YES)) {

            Long startTime = getTimeInMillisecondsFromLocalDate();

            log.info(SAVING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER_OVERRIDE);

            List<DoctorDutyRosterOverride> doctorDutyRosterOverrides = overrideRequestDTOS
                    .stream()
                    .map(requestDTO -> {

                        validateIfOverrideDateIsBetweenDoctorDutyRoster(
                                doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(),
                                requestDTO.getFromDate(), requestDTO.getToDate());

                        validateDoctorDutyRosterOverrideCount(
                                doctorDutyRoster.getDoctorId().getId(),
                                doctorDutyRoster.getSpecializationId().getId(),
                                requestDTO.getFromDate(),
                                requestDTO.getToDate());

                        return parseToDoctorDutyRosterOverride(
                                requestDTO, doctorDutyRoster, new DoctorDutyRosterOverride());
                    }).collect(Collectors.toList());

            saveDoctorDutyRosterOverride(doctorDutyRosterOverrides);

            log.info(SAVING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
        }
    }

    private void saveDoctorWeekDaysDutyRoster(List<DoctorWeekDaysDutyRoster> doctorWeekDaysDutyRosters) {
        doctorWeekDaysDutyRosterRepository.saveAll(doctorWeekDaysDutyRosters);
    }

    private void saveDoctorDutyRosterOverride(DoctorDutyRosterOverride doctorDutyRosterOverride) {
        doctorDutyRosterOverrideRepository.save(doctorDutyRosterOverride);
    }

    private void saveDoctorDutyRosterOverride(List<DoctorDutyRosterOverride> doctorDutyRosterOverrides) {
        doctorDutyRosterOverrideRepository.saveAll(doctorDutyRosterOverrides);
    }

    public DoctorDutyRoster findDoctorDutyRosterById(Long doctorDutyRosterId) {
        return doctorDutyRosterRepository.findDoctorDutyRosterById(doctorDutyRosterId)
                .orElseThrow(() -> new NoContentFoundException(
                        DoctorDutyRoster.class, "id", doctorDutyRosterId.toString()));
    }

    private void updateDoctorWeekDaysDutyRoster(DoctorDutyRoster doctorDutyRoster,
                                                List<DoctorWeekDaysDutyRosterUpdateRequestDTO> updateRequestDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_WEEK_DAYS_DUTY_ROSTER);

        List<DoctorWeekDaysDutyRoster> doctorWeekDaysDutyRosters = updateRequestDTOS.stream()
                .map(requestDTO -> {
                    WeekDays weekDays = findWeekDaysById(requestDTO.getWeekDaysId());

                    return parseToUpdatedDoctorWeekDaysDutyRoster(requestDTO, doctorDutyRoster, weekDays);
                }).collect(Collectors.toList());

        saveDoctorWeekDaysDutyRoster(doctorWeekDaysDutyRosters);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_WEEK_DAYS_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorDutyRosterOverrideStatus(DoctorDutyRoster doctorDutyRoster) {
        if (doctorDutyRoster.getHasOverrideDutyRoster().equals(NO))
            updateDutyRosterOverrideStatus(
                    doctorDutyRosterOverrideRepository.fetchByDoctorRosterId(doctorDutyRoster.getId()));
    }

    private List<AppointmentDateResponseDTO> fetchAppointmentDateResponseDTOS(DoctorDutyRoster doctorDutyRoster) {
        return appointmentService.fetchAppointmentDates
                (parseToAppointmentCountRequestTO(
                        doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(),
                        doctorDutyRoster.getDoctorId().getId(), doctorDutyRoster.getSpecializationId().getId())
                );
    }

    private void filterUpdatedWeekDaysRosterAndAppointment(
            List<DoctorWeekDaysDutyRosterUpdateRequestDTO> unmatchedWeekDaysRosterList,
            List<AppointmentDateResponseDTO> appointmentDateResponseDTO) {

        unmatchedWeekDaysRosterList.forEach(unmatchedList ->
                appointmentDateResponseDTO
                        .stream()
                        .map(appointmentDates ->
                                convertDateToLocalDate(appointmentDates.getAppointmentDate()).getDayOfWeek().toString())
                        .filter(weekName ->
                                unmatchedList.getWeekName().equals(weekName))
                        .forEachOrdered(weekName -> {
                            throw new BadRequestException(APPOINTMENT_EXISTS_MESSAGE);
                        }));
    }
}
