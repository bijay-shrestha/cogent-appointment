package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.*;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.CommonLogConstant;
import com.cogent.cogentappointment.admin.log.constants.DoctorDutyRosterLog;
import com.cogent.cogentappointment.admin.model.*;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.admin.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.admin.repository.DoctorWeekDaysDutyRosterRepository;
import com.cogent.cogentappointment.admin.service.DoctorDutyRosterService;
import com.cogent.cogentappointment.admin.service.DoctorService;
import com.cogent.cogentappointment.admin.service.SpecializationService;
import com.cogent.cogentappointment.admin.service.WeekDaysService;
import com.cogent.cogentappointment.admin.utils.DoctorDutyRosterOverrideUtils;
import com.cogent.cogentappointment.admin.utils.DoctorDutyRosterUtils;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
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

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER);

        validateDoctorDutyRosterCount(requestDTO.getDoctorId(), requestDTO.getSpecializationId(),
                requestDTO.getFromDate(), requestDTO.getToDate());

        DoctorDutyRoster doctorDutyRoster = DoctorDutyRosterUtils.parseToDoctorDutyRoster(
                requestDTO,
                findDoctorById(requestDTO.getDoctorId()),
                findSpecializationById(requestDTO.getSpecializationId()));

        save(doctorDutyRoster);

        saveDoctorWeekDaysDutyRoster(doctorDutyRoster, requestDTO.getDoctorWeekDaysDutyRosterRequestDTOS());

        saveDoctorDutyRosterOverride(doctorDutyRoster, requestDTO.getDoctorDutyRosterOverrideRequestDTOS());

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(DoctorDutyRosterUpdateRequestDTO updateRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(updateRequestDTO.getDoctorDutyRosterId());

        //todo; refactor this
        List<DoctorWeekDaysDutyRoster> weekDaysDutyRosters =
                doctorWeekDaysDutyRosterRepository.fetchByDoctorDutyRosterId(doctorDutyRoster.getId());

        List<DoctorWeekDaysDutyRosterUpdateRequestDTO> unmatchedWeekDaysRosterList =
                DoctorDutyRosterUtils.filterOriginalAndUpdatedWeekDaysRoster(
                        updateRequestDTO.getWeekDaysDutyRosterUpdateRequestDTOS(), weekDaysDutyRosters);

        List<AppointmentBookedDateResponseDTO> bookedAppointments = fetchBookedAppointments(doctorDutyRoster);

        if (!ObjectUtils.isEmpty(bookedAppointments))
            DoctorDutyRosterUtils.filterUpdatedWeekDaysRosterAndAppointment(unmatchedWeekDaysRosterList, bookedAppointments);

        DoctorDutyRosterUtils.parseToUpdatedDoctorDutyRoster(doctorDutyRoster, updateRequestDTO);

        updateDoctorWeekDaysDutyRoster(doctorDutyRoster, unmatchedWeekDaysRosterList);

        updateDoctorDutyRosterOverrideStatus(doctorDutyRoster);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateDoctorDutyRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER_OVERRIDE);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(updateRequestDTO.getDoctorDutyRosterId());

        Long doctorId = doctorDutyRoster.getDoctorId().getId();
        Long specializationId = doctorDutyRoster.getSpecializationId().getId();
        Date overrideFromDate = updateRequestDTO.getOverrideFromDate();
        Date overrideToDate = updateRequestDTO.getOverrideToDate();

        validateIfOverrideDateIsBetweenDoctorDutyRoster(
                doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(), overrideFromDate, overrideToDate);

        validateDoctorDutyRosterOverrideCount(doctorId, specializationId,
                updateRequestDTO.getOverrideFromDate(), updateRequestDTO.getOverrideToDate());

        /*UPDATE IS ALLOWED ONLY IF THERE ARE NO APPOINTMENTS WITHIN THAT RANGE*/
        validateAppointmentCount(overrideFromDate, overrideToDate, doctorId, specializationId);

        saveOrUpdateDoctorDutyRosterOverride(updateRequestDTO, doctorDutyRoster);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER_OVERRIDE, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    public void saveOrUpdateDoctorDutyRosterOverride(DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO,
                                                     DoctorDutyRoster doctorDutyRoster) {

        if (Objects.isNull(updateRequestDTO.getDoctorDutyRosterOverrideId())) {
            DoctorDutyRosterOverride doctorDutyRosterOverride = DoctorDutyRosterOverrideUtils.parseToUpdatedDoctorDutyRosterOverride(
                    updateRequestDTO, new DoctorDutyRosterOverride());
            doctorDutyRosterOverride.setDoctorDutyRosterId(doctorDutyRoster);
            saveDoctorDutyRosterOverride(doctorDutyRosterOverride);
        } else {
            DoctorDutyRosterOverride doctorDutyRosterOverride =
                    doctorDutyRosterOverrideRepository.fetchById(updateRequestDTO.getDoctorDutyRosterOverrideId());
            DoctorDutyRosterOverrideUtils.parseToUpdatedDoctorDutyRosterOverride(updateRequestDTO, doctorDutyRosterOverride);
        }
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.DELETING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER);

        DoctorDutyRoster doctorDutyRoster = findDoctorDutyRosterById(deleteRequestDTO.getId());

        validateAppointmentCount(doctorDutyRoster.getFromDate(), doctorDutyRoster.getToDate(),
                doctorDutyRoster.getDoctorId().getId(), doctorDutyRoster.getSpecializationId().getId());

        DoctorDutyRosterUtils.convertToDeletedDoctorDutyRoster(doctorDutyRoster, deleteRequestDTO);

        log.info(CommonLogConstant.DELETING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                           Pageable pageable) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SEARCHING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER);

        List<DoctorDutyRosterMinimalResponseDTO> responseDTOS =
                doctorDutyRosterRepository.search(searchRequestDTO, pageable);

        log.info(CommonLogConstant.SEARCHING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER);

        DoctorDutyRosterDetailResponseDTO responseDTO = doctorDutyRosterRepository.fetchDetailsById(id);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus
            (DoctorDutyRosterStatusRequestDTO requestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER_STATUS);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterOverrideStatus =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
                doctorDutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

        List<DoctorDutyRosterStatusResponseDTO> mergedList =
                DoctorDutyRosterUtils.mergeOverrideAndActualDoctorDutyRoster(doctorDutyRosterOverrideStatus, doctorDutyRosterStatus);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return mergedList;
    }

    private void validateAppointmentCount(Date overrideFromDate, Date overrideToDate,
                                          Long doctorId, Long specializationId) {

        Long appointments = appointmentRepository.fetchBookedAppointmentCount(
                overrideFromDate, overrideToDate, doctorId, specializationId);

        if (appointments.intValue() > 0)
            throw new BadRequestException(ErrorMessageConstants.AppointmentServiceMessage.APPOINTMENT_EXISTS_MESSAGE);
    }

    private void validateDoctorDutyRosterCount(Long doctorId, Long specializationId,
                                               Date fromDate, Date toDate) {

        Long doctorDutyRosterCount = doctorDutyRosterRepository.validateDoctorDutyRosterCount(
                doctorId, specializationId, fromDate, toDate);

        if (doctorDutyRosterCount.intValue() > 0)
            throw new DataDuplicationException(ErrorMessageConstants.DoctorDutyRosterServiceMessages.DUPLICATION_MESSAGE);
    }

    private void validateIfOverrideDateIsBetweenDoctorDutyRoster(Date dutyRosterFromDate,
                                                                 Date dutyRosterToDate,
                                                                 Date overrideFromDate,
                                                                 Date overrideToDate) {

        boolean isDateBetweenInclusive =
                DateUtils.isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, overrideFromDate)
                        && DateUtils.isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, overrideToDate);

        if (!isDateBetweenInclusive)
            throw new BadRequestException(ErrorMessageConstants.DoctorDutyRosterServiceMessages.BAD_REQUEST_MESSAGE);
    }

    private void validateDoctorDutyRosterOverrideCount(Long doctorId, Long specializationId,
                                                       Date fromDate, Date toDate) {

        Long doctorDutyRosterOverrideCount = doctorDutyRosterOverrideRepository.validateDoctorDutyRosterOverrideCount(
                doctorId, specializationId, fromDate, toDate);

        if (doctorDutyRosterOverrideCount.intValue() > 0)
            throw new DataDuplicationException(ErrorMessageConstants.DoctorDutyRosterServiceMessages.DUPLICATION_MESSAGE);
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

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_WEEK_DAYS_DUTY_ROSTER);

        List<DoctorWeekDaysDutyRoster> doctorWeekDaysDutyRosters = weekDaysDutyRosterRequestDTOS.stream()
                .map(requestDTO -> {
                    WeekDays weekDays = findWeekDaysById(requestDTO.getWeekDaysId());
                    return DoctorDutyRosterUtils.parseToDoctorWeekDaysDutyRoster(
                            requestDTO, doctorDutyRoster, weekDays);
                }).collect(Collectors.toList());

        saveDoctorWeekDaysDutyRoster(doctorWeekDaysDutyRosters);

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_WEEK_DAYS_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorDutyRosterOverride(DoctorDutyRoster doctorDutyRoster,
                                              List<DoctorDutyRosterOverrideRequestDTO> overrideRequestDTOS) {

        if (doctorDutyRoster.getHasOverrideDutyRoster().equals(StatusConstants.YES)) {

            Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

            log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER_OVERRIDE);

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

                        return DoctorDutyRosterOverrideUtils.parseToDoctorDutyRosterOverride(requestDTO, doctorDutyRoster);
                    }).collect(Collectors.toList());

            saveDoctorDutyRosterOverride(doctorDutyRosterOverrides);

            log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER_OVERRIDE, DateUtils.getDifferenceBetweenTwoTime(startTime));
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
                .orElseThrow(() -> DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND.apply(doctorDutyRosterId));
    }

    private void updateDoctorWeekDaysDutyRoster(DoctorDutyRoster doctorDutyRoster,
                                                List<DoctorWeekDaysDutyRosterUpdateRequestDTO> updateRequestDTOS) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorDutyRosterLog.DOCTOR_WEEK_DAYS_DUTY_ROSTER);

        List<DoctorWeekDaysDutyRoster> doctorWeekDaysDutyRosters = updateRequestDTOS.stream()
                .map(requestDTO -> {
                    WeekDays weekDays = findWeekDaysById(requestDTO.getWeekDaysId());

                    return DoctorDutyRosterUtils.parseToUpdatedDoctorWeekDaysDutyRoster(requestDTO, doctorDutyRoster, weekDays);
                }).collect(Collectors.toList());

        saveDoctorWeekDaysDutyRoster(doctorWeekDaysDutyRosters);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorDutyRosterLog.DOCTOR_WEEK_DAYS_DUTY_ROSTER, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorDutyRosterOverrideStatus(DoctorDutyRoster doctorDutyRoster) {
        if (doctorDutyRoster.getHasOverrideDutyRoster().equals(StatusConstants.NO))
            DoctorDutyRosterOverrideUtils.updateDutyRosterOverrideStatus(
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
}
