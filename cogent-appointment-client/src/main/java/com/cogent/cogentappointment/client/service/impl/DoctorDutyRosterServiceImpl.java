package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.constants.StatusConstants;
import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.*;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.*;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.client.repository.DoctorWeekDaysDutyRosterRepository;
import com.cogent.cogentappointment.client.service.DoctorDutyRosterService;
import com.cogent.cogentappointment.client.service.DoctorService;
import com.cogent.cogentappointment.client.service.SpecializationService;
import com.cogent.cogentappointment.client.service.WeekDaysService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.*;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.DoctorDutyRosterServiceMessages.BAD_REQUEST_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.DoctorDutyRosterServiceMessages.DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.DoctorDutyRosterLog.*;
import static com.cogent.cogentappointment.client.utils.DoctorDutyRosterOverrideUtils.*;
import static com.cogent.cogentappointment.client.utils.DoctorDutyRosterUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

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

    private final WeekDaysService weekDaysService;

    private final DoctorWeekDaysDutyRosterRepository doctorWeekDaysDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final AppointmentRepository appointmentRepository;

    public DoctorDutyRosterServiceImpl(DoctorDutyRosterRepository doctorDutyRosterRepository,
                                       DoctorService doctorService,
                                       SpecializationService specializationService,
                                       WeekDaysService weekDaysService,
                                       DoctorWeekDaysDutyRosterRepository doctorWeekDaysDutyRosterRepository,
                                       DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                       AppointmentRepository appointmentRepository) {
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.weekDaysService = weekDaysService;
        this.doctorWeekDaysDutyRosterRepository = doctorWeekDaysDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void save(DoctorDutyRosterRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);

        validateIsFirstDateGreater(requestDTO.getFromDate(), requestDTO.getToDate());

        validateDoctorDutyRosterCount(requestDTO.getDoctorId(), requestDTO.getSpecializationId(),
                requestDTO.getFromDate(), requestDTO.getToDate());

        DoctorDutyRoster doctorDutyRoster = parseToDoctorDutyRoster(
                requestDTO,
                findDoctorById(requestDTO.getDoctorId()),
                findSpecializationById(requestDTO.getSpecializationId()));

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

        //todo; refactor this
        List<DoctorWeekDaysDutyRoster> weekDaysDutyRosters =
                doctorWeekDaysDutyRosterRepository.fetchByDoctorDutyRosterId(doctorDutyRoster.getId());

        List<DoctorWeekDaysDutyRosterUpdateRequestDTO> unmatchedWeekDaysRosterList =
                filterOriginalAndUpdatedWeekDaysRoster(
                        updateRequestDTO.getWeekDaysDutyRosterUpdateRequestDTOS(), weekDaysDutyRosters);

        List<AppointmentBookedDateResponseDTO> bookedAppointments = fetchBookedAppointments(doctorDutyRoster);

        if (!ObjectUtils.isEmpty(bookedAppointments))
            filterUpdatedWeekDaysRosterAndAppointment(unmatchedWeekDaysRosterList, bookedAppointments);

        parseToUpdatedDoctorDutyRoster(doctorDutyRoster, updateRequestDTO);

        updateDoctorWeekDaysDutyRoster(doctorDutyRoster, unmatchedWeekDaysRosterList);

        updateDoctorDutyRosterOverrideStatus(doctorDutyRoster);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public DoctorRosterOverrideUpdateResponseDTO updateDoctorDutyRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER_OVERRIDE);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(updateRequestDTO.getDoctorDutyRosterId());

        validateIsFirstDateGreater(updateRequestDTO.getOverrideFromDate(), updateRequestDTO.getOverrideToDate());

        Long doctorId = doctorDutyRoster.getDoctorId().getId();
        Long specializationId = doctorDutyRoster.getSpecializationId().getId();
        Date overrideFromDate = updateRequestDTO.getOverrideFromDate();
        Date overrideToDate = updateRequestDTO.getOverrideToDate();

        validateIfOverrideDateIsBetweenDoctorDutyRoster(
                doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(), overrideFromDate, overrideToDate);

        /*UPDATE IS ALLOWED ONLY IF THERE ARE NO APPOINTMENTS WITHIN THAT RANGE*/
        validateAppointmentCount(overrideFromDate, overrideToDate, doctorId, specializationId);

        Long savedOverrideId;
        if (Objects.isNull(updateRequestDTO.getDoctorDutyRosterOverrideId())) {

            validateDoctorDutyRosterOverrideCount(
                    doctorDutyRosterOverrideRepository.fetchOverrideCount(
                            doctorId, specializationId, overrideFromDate, overrideToDate));

            savedOverrideId = saveDoctorRosterOverride(updateRequestDTO, doctorDutyRoster);

        } else {
            validateDoctorDutyRosterOverrideCount(
                    doctorDutyRosterOverrideRepository.fetchOverrideCount(
                            updateRequestDTO.getDoctorDutyRosterOverrideId(),
                            doctorId, specializationId, overrideFromDate, overrideToDate));

            savedOverrideId = updateDoctorRosterOverride(updateRequestDTO);
        }

        DoctorRosterOverrideUpdateResponseDTO updateResponse = parseToOverrideUpdateResponse(savedOverrideId);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));

        return updateResponse;
    }

    @Override
    public void deleteDoctorDutyRosterOverride(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER_OVERRIDE);

        DoctorDutyRosterOverride doctorDutyRosterOverride =
                doctorDutyRosterOverrideRepository.fetchById(deleteRequestDTO.getId());

        validateAppointmentCount(doctorDutyRosterOverride.getFromDate(),
                doctorDutyRosterOverride.getToDate(),
                doctorDutyRosterOverride.getDoctorDutyRosterId().getDoctorId().getId(),
                doctorDutyRosterOverride.getDoctorDutyRosterId().getSpecializationId().getId());

        parseDeletedOverrideDetails(doctorDutyRosterOverride, deleteRequestDTO.getStatus(), deleteRequestDTO.getRemarks());

        log.info(DELETING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void revertDoctorDutyRosterOverride(List<DoctorDutyRosterOverrideUpdateRequestDTO> updateOverrideRosters) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REVERTING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER_OVERRIDE);

        List<DoctorDutyRosterOverride> originalOverrideRosters =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrides(updateOverrideRosters);

        originalOverrideRosters.forEach(
                originalOverride -> updateOverrideRosters.stream()
                        .filter(updatedOverride -> originalOverride.getId().equals(updatedOverride.getDoctorDutyRosterOverrideId()))
                        .forEachOrdered(updatedOverride -> {

                            if (updatedOverride.isOriginal()) {
                                updateDoctorRosterOverrideDetails(originalOverride, updatedOverride);
                            } else {
                                updateDoctorRosterOverrideStatus(originalOverride, updatedOverride);
                            }
                        }));

        log.info(REVERTING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(deleteRequestDTO.getId());

        validateAppointmentCount(doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(),
                doctorDutyRoster.getDoctorId().getId(), doctorDutyRoster.getSpecializationId().getId());

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
    public List<DoctorExistingDutyRosterResponseDTO> fetchExistingDutyRosters(DoctorExistingDutyRosterRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, EXISTING_DOCTOR_DUTY_ROSTER);

        List<DoctorExistingDutyRosterResponseDTO> existingRosters =
                doctorDutyRosterRepository.fetchExistingDoctorDutyRosters(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, EXISTING_DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));

        return existingRosters;
    }

    @Override
    public DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long doctorDutyRosterId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, EXISTING_DOCTOR_DUTY_ROSTER);

        DoctorExistingDutyRosterDetailResponseDTO existingRosterDetails =
                doctorDutyRosterRepository.fetchExistingRosterDetails(doctorDutyRosterId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, EXISTING_DOCTOR_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));

        return existingRosterDetails;
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

    private void validateAppointmentCount(Date overrideFromDate, Date overrideToDate,
                                          Long doctorId, Long specializationId) {

        Long appointments = appointmentRepository.fetchBookedAppointmentCount(
                overrideFromDate, overrideToDate, doctorId, specializationId);

        if (appointments.intValue() > 0)
            throw new BadRequestException(AppointmentServiceMessage.APPOINTMENT_EXISTS_MESSAGE);
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

    private void validateDoctorDutyRosterOverrideCount(Long doctorDutyRosterOverrideCount) {
        if (doctorDutyRosterOverrideCount.intValue() > 0)
            throw new DataDuplicationException(DUPLICATION_MESSAGE);
    }


    private Doctor findDoctorById(Long doctorId) {
        return doctorService.fetchDoctorById(doctorId);
    }

    private Specialization findSpecializationById(Long specializationId) {
        return specializationService.fetchActiveSpecializationById(specializationId);
    }

    private WeekDays findWeekDaysById(Long weekDaysId) {
        return weekDaysService.fetchWeekDaysById(weekDaysId);
    }

    private void save(DoctorDutyRoster doctorDutyRoster) {
        doctorDutyRosterRepository.save(doctorDutyRoster);
    }

    private void saveDoctorWeekDaysDutyRoster(DoctorDutyRoster doctorDutyRoster,
                                              List<DoctorWeekDaysDutyRosterRequestDTO> weekDaysDutyRosterRequestDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_WEEK_DAYS_DUTY_ROSTER);

        List<DoctorWeekDaysDutyRoster> doctorWeekDaysDutyRosters = weekDaysDutyRosterRequestDTOS.stream()
                .map(requestDTO -> {

                    WeekDays weekDays = findWeekDaysById(requestDTO.getWeekDaysId());

                    return parseToDoctorWeekDaysDutyRoster(requestDTO, doctorDutyRoster, weekDays);
                }).collect(Collectors.toList());

        saveDoctorWeekDaysDutyRoster(doctorWeekDaysDutyRosters);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_WEEK_DAYS_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorDutyRosterOverride(DoctorDutyRoster doctorDutyRoster,
                                              List<DoctorDutyRosterOverrideRequestDTO> overrideRequestDTOS) {

        if (doctorDutyRoster.getHasOverrideDutyRoster().equals(StatusConstants.YES)) {

            Long startTime = getTimeInMillisecondsFromLocalDate();

            log.info(SAVING_PROCESS_STARTED, DOCTOR_DUTY_ROSTER_OVERRIDE);

            List<DoctorDutyRosterOverride> doctorDutyRosterOverrides = overrideRequestDTOS
                    .stream()
                    .map(requestDTO -> {

                        validateIfOverrideDateIsBetweenDoctorDutyRoster(
                                doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(),
                                requestDTO.getFromDate(), requestDTO.getToDate());

                        validateDoctorDutyRosterOverrideCount(
                                doctorDutyRosterOverrideRepository.fetchOverrideCount(
                                        doctorDutyRoster.getDoctorId().getId(),
                                        doctorDutyRoster.getSpecializationId().getId(),
                                        requestDTO.getFromDate(),
                                        requestDTO.getToDate()));

                        return parseToDoctorDutyRosterOverride(requestDTO, doctorDutyRoster);
                    }).collect(Collectors.toList());

            saveDoctorDutyRosterOverride(doctorDutyRosterOverrides);

            log.info(SAVING_PROCESS_COMPLETED, DOCTOR_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
        }
    }

    private void saveDoctorWeekDaysDutyRoster(List<DoctorWeekDaysDutyRoster> doctorWeekDaysDutyRosters) {
        doctorWeekDaysDutyRosterRepository.saveAll(doctorWeekDaysDutyRosters);
    }

    private Long saveDoctorRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO,
                                          DoctorDutyRoster doctorDutyRoster) {

        DoctorDutyRosterOverride doctorDutyRosterOverride = parseDoctorDutyRosterOverrideDetails(
                updateRequestDTO, new DoctorDutyRosterOverride());

        doctorDutyRosterOverride.setDoctorDutyRosterId(doctorDutyRoster);

        doctorDutyRosterOverrideRepository.save(doctorDutyRosterOverride);

        return doctorDutyRosterOverride.getId();
    }

    private Long updateDoctorRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {

        DoctorDutyRosterOverride doctorDutyRosterOverride =
                doctorDutyRosterOverrideRepository.fetchById(updateRequestDTO.getDoctorDutyRosterOverrideId());

        parseDoctorDutyRosterOverrideDetails(updateRequestDTO, doctorDutyRosterOverride);

        return doctorDutyRosterOverride.getId();
    }

    private void saveDoctorDutyRosterOverride(List<DoctorDutyRosterOverride> doctorDutyRosterOverrides) {
        doctorDutyRosterOverrideRepository.saveAll(doctorDutyRosterOverrides);
    }

    public DoctorDutyRoster findDoctorDutyRosterById(Long doctorDutyRosterId) {
        return doctorDutyRosterRepository.findDoctorDutyRosterById(doctorDutyRosterId)
                .orElseThrow(() -> DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND.apply(doctorDutyRosterId));
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
        if (doctorDutyRoster.getHasOverrideDutyRoster().equals(StatusConstants.NO))
            updateDutyRosterOverrideStatus(
                    doctorDutyRosterOverrideRepository.fetchByDoctorRosterId(doctorDutyRoster.getId()));
    }

    private List<AppointmentBookedDateResponseDTO> fetchBookedAppointments(DoctorDutyRoster doctorDutyRoster) {
        return appointmentRepository.fetchBookedAppointmentDates(
                doctorDutyRoster.getFromDate(),
                doctorDutyRoster.getToDate(),
                doctorDutyRoster.getDoctorId().getId(),
                doctorDutyRoster.getSpecializationId().getId());
    }

    private Function<Long, NoContentFoundException> DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(DoctorDutyRoster.class, "id", id.toString());
    };

    private void validateIsFirstDateGreater(Date fromDate, Date toDate) {
        boolean fromDateGreaterThanToDate = isFirstDateGreater(fromDate, toDate);

        if (fromDateGreaterThanToDate)
            throw new BadRequestException(INVALID_DATE_MESSAGE, INVALID_DATE_DEBUG_MESSAGE);
    }

}
