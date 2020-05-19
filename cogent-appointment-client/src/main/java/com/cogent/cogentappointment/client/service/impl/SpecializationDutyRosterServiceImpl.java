package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.specializationDutyRoster.SpecializationDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specializationDutyRoster.SpecializationDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.specializationDutyRoster.SpecializationWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.SpecializationDutyRosterService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_DATE_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_DATE_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.SpecializationDutyRosterServiceMessages.BAD_REQUEST_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.SpecializationDutyRosterServiceMessages.DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.client.log.constants.SpecializationDutyRosterLog.*;
import static com.cogent.cogentappointment.client.log.constants.SpecializationLog.SPECIALIZATION;
import static com.cogent.cogentappointment.client.log.constants.WeekDaysLog.WEEK_DAYS;
import static com.cogent.cogentappointment.client.utils.SpecializationDutyRosterUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author Sauravi Thapa ON 5/18/20
 */

@Service
@Slf4j
@Transactional(readOnly = true)
public class SpecializationDutyRosterServiceImpl implements SpecializationDutyRosterService {

    private final SpecializationDutyRosterRepository specializationDutyRosterRepository;

    private final SpecializationWeekDaysDutyRosterRepository specializationWeekDaysDutyRosterRepository;

    private final WeekDaysRepository weekDaysRepository;

    private final SpecializationDutyRosterOverrideRepository specializationDutyRosterOverrideRepository;

    private final SpecializationRepository specializationRepository;

    private final HospitalRepository hospitalRepository;

    public SpecializationDutyRosterServiceImpl(SpecializationDutyRosterRepository specializationDutyRosterRepository,
                                               SpecializationWeekDaysDutyRosterRepository
                                                       specializationWeekDaysDutyRosterRepository,
                                               WeekDaysRepository weekDaysRepository,
                                               SpecializationDutyRosterOverrideRepository
                                                       specializationDutyRosterOverrideRepository,
                                               SpecializationRepository specializationRepository,
                                               HospitalRepository hospitalRepository) {
        this.specializationDutyRosterRepository = specializationDutyRosterRepository;
        this.specializationWeekDaysDutyRosterRepository = specializationWeekDaysDutyRosterRepository;
        this.weekDaysRepository = weekDaysRepository;
        this.specializationDutyRosterOverrideRepository = specializationDutyRosterOverrideRepository;
        this.specializationRepository = specializationRepository;
        this.hospitalRepository = hospitalRepository;
    }


    @Override
    public void save(SpecializationDutyRosterRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, SPECIALIZATION_DUTY_ROSTER);

        Long hospitalId = getLoggedInHospitalId();

        validateIsFirstDateGreater(requestDTO.getFromDate(), requestDTO.getToDate());

        validateSpecializationDutyRosterCount(requestDTO.getSpecializationId(),
                requestDTO.getFromDate(),
                requestDTO.getToDate());

        SpecializationDutyRoster specializationDutyRoster = parseToSpecializationDutyRoster(requestDTO,
                fetchSpecializationByIdAndHospitalId(requestDTO.getSpecializationId(), hospitalId),
                fetchHospitalById(hospitalId));

        save(specializationDutyRoster);

        saveSpecializationWeekDaysDutyRoster(specializationDutyRoster,
                requestDTO.getSpecializationWeekDaysDutyRosterRequestDTOS());

        saveSpecializationDutyRosterOverride(specializationDutyRoster,
                requestDTO.getSpecializationDutyRosterOverrideRequestDTOS());

        log.info(SAVING_PROCESS_COMPLETED, SPECIALIZATION_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveSpecializationWeekDaysDutyRoster(SpecializationDutyRoster specializationDutyRoster,
                                                      List<SpecializationWeekDaysDutyRosterRequestDTO>
                                                              weekDaysDutyRosterRequestDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, SPECIALIZATION_WEEK_DAYS_DUTY_ROSTER);

        List<SpecializationWeekDaysDutyRoster> specializationWeekDaysDutyRosters = weekDaysDutyRosterRequestDTOS.stream()
                .map(requestDTO -> {

                    WeekDays weekDays = fetchWeekDaysById(requestDTO.getWeekDaysId());

                    return parseToSpecializationWeekDaysDutyRoster(requestDTO, specializationDutyRoster, weekDays);
                }).collect(Collectors.toList());

        saveSpecializationWeekDaysDutyRoster(specializationWeekDaysDutyRosters);

        log.info(SAVING_PROCESS_COMPLETED, SPECIALIZATION_WEEK_DAYS_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveSpecializationDutyRosterOverride(SpecializationDutyRoster specializationDutyRoster,
                                                      List<SpecializationDutyRosterOverrideRequestDTO> overrideRequestDTOS) {

        if (specializationDutyRoster.getHasOverrideDutyRoster().equals(YES)) {

            Long startTime = getTimeInMillisecondsFromLocalDate();

            log.info(SAVING_PROCESS_STARTED, SPECIALIZATION_DUTY_ROSTER_OVERRIDE);

            List<SpecializationDutyRosterOverride> specializationDutyRosterOverrides = new ArrayList<>();

            specializationDutyRosterOverrides = overrideRequestDTOS
                    .stream()
                    .map(requestDTO -> {

                        validateIfOverrideDateIsBetweenSpecializationDutyRoster(
                                specializationDutyRoster.getFromDate(), specializationDutyRoster.getToDate(),
                                requestDTO.getFromDate(), requestDTO.getToDate());

                        validateSpecializationDutyRosterOverrideCount(
                                specializationDutyRosterOverrideRepository.fetchOverrideCount(
                                        specializationDutyRoster.getSpecialization().getId(),
                                        requestDTO.getFromDate(),
                                        requestDTO.getToDate()));

                        return parseToSpecializationDutyRosterOverride(requestDTO, specializationDutyRoster);
                    }).collect(Collectors.toList());

            saveSpecializationDutyRosterOverride(specializationDutyRosterOverrides);

            log.info(SAVING_PROCESS_COMPLETED, SPECIALIZATION_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
        }
    }

    private void save(SpecializationDutyRoster specializationDutyRoster) {
        specializationDutyRosterRepository.save(specializationDutyRoster);
    }

    private void saveSpecializationWeekDaysDutyRoster(List<SpecializationWeekDaysDutyRoster> specializationWeekDaysDutyRosters) {
        specializationWeekDaysDutyRosterRepository.saveAll(specializationWeekDaysDutyRosters);
    }

    private void saveSpecializationDutyRosterOverride(List<SpecializationDutyRosterOverride> specializationDutyRosterOverrides) {
        specializationDutyRosterOverrideRepository.saveAll(specializationDutyRosterOverrides);
    }


    private WeekDays fetchWeekDaysById(Long id) {
        return weekDaysRepository.fetchActiveWeekDaysById(id)
                .orElseThrow(() -> WEEK_DAYS_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> WEEK_DAYS_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, WEEK_DAYS, id);
        throw new NoContentFoundException(WeekDays.class, "id", id.toString());
    };

    private void validateIfOverrideDateIsBetweenSpecializationDutyRoster(Date dutyRosterFromDate,
                                                                         Date dutyRosterToDate,
                                                                         Date overrideFromDate,
                                                                         Date overrideToDate) {

        boolean isDateBetweenInclusive =
                isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, removeTime(overrideFromDate))
                        && isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, removeTime(overrideToDate));

        if (!isDateBetweenInclusive) {
            log.error(BAD_REQUEST_MESSAGE);
            throw new BadRequestException(BAD_REQUEST_MESSAGE);
        }
    }

    private void validateSpecializationDutyRosterOverrideCount(Long specializationDutyRosterOverrideCount) {
        if (specializationDutyRosterOverrideCount.intValue() > 0) {
            log.error(DUPLICATION_MESSAGE);
            throw new DataDuplicationException(DUPLICATION_MESSAGE);
        }
    }

    private void validateIsFirstDateGreater(Date fromDate, Date toDate) {
        boolean fromDateGreaterThanToDate = isFirstDateGreater(fromDate, toDate);

        if (fromDateGreaterThanToDate) {
            log.error(INVALID_DATE_DEBUG_MESSAGE);
            throw new BadRequestException(INVALID_DATE_MESSAGE, INVALID_DATE_DEBUG_MESSAGE);
        }
    }

    private void validateSpecializationDutyRosterCount(Long specializationId,
                                                       Date fromDate, Date toDate) {

        Long specializationDutyRosterCount = specializationDutyRosterRepository.validateSpecializationDutyRosterCount(
                specializationId, fromDate, toDate);

        if (specializationDutyRosterCount.intValue() > 0) {
            log.error(DUPLICATION_MESSAGE);
            throw new DataDuplicationException(DUPLICATION_MESSAGE);
        }
    }

    private Specialization fetchSpecializationByIdAndHospitalId(Long specializationId, Long hospitalId) {
        return specializationRepository
                .findActiveSpecializationByIdAndHospitalId(specializationId, hospitalId)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(specializationId));
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalRepository.findActiveHospitalById(hospitalId)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalId));
    }

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SPECIALIZATION, id);
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };

}


