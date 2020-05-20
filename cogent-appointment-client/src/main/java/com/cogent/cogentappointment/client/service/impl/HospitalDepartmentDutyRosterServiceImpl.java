package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.HospitalDepartmentDutyRosterService;
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
import static com.cogent.cogentappointment.client.utils.HospitalDeptDutyRosterUtils.parseToSpecializationDutyRosterOverride;
import static com.cogent.cogentappointment.client.utils.HospitalDeptDutyRosterUtils.parseToSpecializationWeekDaysDutyRoster;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author smriti on 20/05/20
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class HospitalDepartmentDutyRosterServiceImpl implements HospitalDepartmentDutyRosterService {

    private final HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository;

    private final HospitalDeptWeekDaysDutyRosterRepository weekDaysDutyRosterRepository;

    private final WeekDaysRepository weekDaysRepository;

    private final HospitalDeptDutyRosterOverrideRepository overrideRepository;

    private final SpecializationRepository specializationRepository;

    private final HospitalRepository hospitalRepository;

    public HospitalDepartmentDutyRosterServiceImpl(HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository,
                                                   HospitalDeptWeekDaysDutyRosterRepository weekDaysDutyRosterRepository,
                                                   WeekDaysRepository weekDaysRepository,
                                                   HospitalDeptDutyRosterOverrideRepository overrideRepository,
                                                   SpecializationRepository specializationRepository,
                                                   HospitalRepository hospitalRepository) {

        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
        this.weekDaysDutyRosterRepository = weekDaysDutyRosterRepository;
        this.weekDaysRepository = weekDaysRepository;
        this.overrideRepository = overrideRepository;
        this.specializationRepository = specializationRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void save(HospitalDepartmentDutyRosterRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, SPECIALIZATION_DUTY_ROSTER);

        validateIsFirstDateGreater(requestDTO.getFromDate(), requestDTO.getToDate());

//        validateSpecializationDutyRosterCount(requestDTO.getSpecializationId(),
//                requestDTO.getFromDate(),
//                requestDTO.getToDate());
//
//        HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster = parseToHospitalDepartmentDutyRoster(requestDTO,
//                fetchSpecializationByIdAndHospitalId(requestDTO.getSpecializationId(), hospitalId),
//                fetchHospitalById(hospitalId));
//
//        save(hospitalDepartmentDutyRoster);
//
//        saveSpecializationWeekDaysDutyRoster(hospitalDepartmentDutyRoster,
//                requestDTO.getHospitalDeptWeekDaysDutyRosterRequestDTOS());
//
//        saveSpecializationDutyRosterOverride(hospitalDepartmentDutyRoster,
//                requestDTO.getHospitalDeptDutyRosterOverrideRequestDTOS());

        log.info(SAVING_PROCESS_COMPLETED, SPECIALIZATION_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveSpecializationWeekDaysDutyRoster(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                                      List<HospitalDeptWeekDaysDutyRosterRequestDTO>
                                                              weekDaysDutyRosterRequestDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, SPECIALIZATION_WEEK_DAYS_DUTY_ROSTER);

        List<HospitalDepartmentWeekDaysDutyRoster> hospitalDepartmentWeekDaysDutyRosters = weekDaysDutyRosterRequestDTOS.stream()
                .map(requestDTO -> {

                    WeekDays weekDays = fetchWeekDaysById(requestDTO.getWeekDaysId());

                    return parseToSpecializationWeekDaysDutyRoster(requestDTO, hospitalDepartmentDutyRoster, weekDays);
                }).collect(Collectors.toList());

        saveSpecializationWeekDaysDutyRoster(hospitalDepartmentWeekDaysDutyRosters);

        log.info(SAVING_PROCESS_COMPLETED, SPECIALIZATION_WEEK_DAYS_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveSpecializationDutyRosterOverride(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                                      List<HospitalDeptDutyRosterOverrideRequestDTO> overrideRequestDTOS) {

        if (hospitalDepartmentDutyRoster.getHasOverrideDutyRoster().equals(YES)) {

            Long startTime = getTimeInMillisecondsFromLocalDate();

            log.info(SAVING_PROCESS_STARTED, SPECIALIZATION_DUTY_ROSTER_OVERRIDE);

            List<HospitalDepartmentDutyRosterOverride> hospitalDepartmentDutyRosterOverrides = new ArrayList<>();

            hospitalDepartmentDutyRosterOverrides = overrideRequestDTOS
                    .stream()
                    .map(requestDTO -> {

                        validateIfOverrideDateIsBetweenSpecializationDutyRoster(
                                hospitalDepartmentDutyRoster.getFromDate(), hospitalDepartmentDutyRoster.getToDate(),
                                requestDTO.getFromDate(), requestDTO.getToDate());

//                        validateSpecializationDutyRosterOverrideCount(
//                                overrideRepository.fetchOverrideCount(
//                                        hospitalDepartmentDutyRoster.getSpecialization().getId(),
//                                        requestDTO.getFromDate(),
//                                        requestDTO.getToDate()));

                        return parseToSpecializationDutyRosterOverride(requestDTO, hospitalDepartmentDutyRoster);
                    }).collect(Collectors.toList());

            saveSpecializationDutyRosterOverride(hospitalDepartmentDutyRosterOverrides);

            log.info(SAVING_PROCESS_COMPLETED, SPECIALIZATION_DUTY_ROSTER_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
        }
    }

    private void save(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster) {
        hospitalDeptDutyRosterRepository.save(hospitalDepartmentDutyRoster);
    }

    private void saveSpecializationWeekDaysDutyRoster(List<HospitalDepartmentWeekDaysDutyRoster> hospitalDepartmentWeekDaysDutyRosters) {
        weekDaysDutyRosterRepository.saveAll(hospitalDepartmentWeekDaysDutyRosters);
    }

    private void saveSpecializationDutyRosterOverride(List<HospitalDepartmentDutyRosterOverride> hospitalDepartmentDutyRosterOverrides) {
        overrideRepository.saveAll(hospitalDepartmentDutyRosterOverrides);
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

        Long specializationDutyRosterCount = hospitalDeptDutyRosterRepository.validateSpecializationDutyRosterCount(
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

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SPECIALIZATION, id);
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };

}


