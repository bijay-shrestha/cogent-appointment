package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail.HospitalDeptDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.HospitalDepartmentDutyRosterService;
import com.cogent.cogentappointment.client.service.RoomService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.HospitalDepartmentDutyRosterServiceMessages.BAD_REQUEST_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.HospitalDepartmentDutyRosterServiceMessages.DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentDutyRosterLog.*;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.client.log.constants.WeekDaysLog.WEEK_DAYS;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterRoomUtils.parseRoomDetails;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterUtils.parseDeletedDetails;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterUtils.parseToHospitalDepartmentDutyRoster;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptOverrideDutyRosterUtils.parseToSpecializationDutyRosterOverride;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptWeekDaysDutyRosterUtils.parseToHospitalDeptWeekDaysDutyRoster;

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

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    private final RoomService roomService;

    private final HospitalDeptDutyRosterRoomInfoRepository dutyRosterRoomInfoRepository;

    public HospitalDepartmentDutyRosterServiceImpl(HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository,
                                                   HospitalDeptWeekDaysDutyRosterRepository weekDaysDutyRosterRepository,
                                                   WeekDaysRepository weekDaysRepository,
                                                   HospitalDeptDutyRosterOverrideRepository overrideRepository,
                                                   HospitalDepartmentRepository hospitalDepartmentRepository,
                                                   RoomService roomService,
                                                   HospitalDeptDutyRosterRoomInfoRepository dutyRosterRoomInfoRepository) {

        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
        this.weekDaysDutyRosterRepository = weekDaysDutyRosterRepository;
        this.weekDaysRepository = weekDaysRepository;
        this.overrideRepository = overrideRepository;
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
        this.roomService = roomService;
        this.dutyRosterRoomInfoRepository = dutyRosterRoomInfoRepository;
    }

    @Override
    public void save(HospitalDepartmentDutyRosterRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        validateIsFirstDateGreater(requestDTO.getFromDate(), requestDTO.getToDate());

        Long hospitalId = getLoggedInHospitalId();

//        validateSpecializationDutyRosterCount(requestDTO.getSpecializationId(),
//                requestDTO.getFromDate(),
//                requestDTO.getToDate());
//
        HospitalDepartmentDutyRoster dutyRoster = parseToHospitalDepartmentDutyRoster(
                requestDTO,
                fetchHospitalDepartment(requestDTO.getHospitalDepartmentId(), hospitalId)
        );

        save(dutyRoster);

        if (dutyRoster.getIsRoomEnabled().equals(YES))
            saveDutyRosterRoomInfo(dutyRoster, requestDTO.getRoomId(), hospitalId);

        saveWeekDaysDutyRoster(dutyRoster,
                requestDTO.getWeekDaysDetail());

        if (dutyRoster.getHasOverrideDutyRoster().equals(YES))
            saveDutyRosterOverride(dutyRoster, requestDTO.getOverrideDetail());

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                             Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        List<HospitalDeptDutyRosterMinResponseDTO> minInfo =
                hospitalDeptDutyRosterRepository.search(searchRequestDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        HospitalDepartmentDutyRoster departmentDutyRoster = findDeptDutyRosterById(deleteRequestDTO.getId());

        parseDeletedDetails(departmentDutyRoster, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public HospitalDeptDutyRosterDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        Long hospitalId = getLoggedInHospitalId();

        HospitalDeptDutyRosterDetailResponseDTO responseDTO =
                hospitalDeptDutyRosterRepository.fetchDetailsById(id, hospitalId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private void saveWeekDaysDutyRoster(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                        List<HospitalDeptWeekDaysDutyRosterRequestDTO>
                                                weekDaysDutyRosterRequestDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_WEEK_DAYS_DUTY_ROSTER);

        List<HospitalDepartmentWeekDaysDutyRoster> weekDaysDutyRosters =
                weekDaysDutyRosterRequestDTOS.stream().map(requestDTO -> {

                    validateIfStartTimeGreater(requestDTO.getStartTime(), requestDTO.getEndTime());

                    WeekDays weekDays = fetchWeekDaysById(requestDTO.getWeekDaysId());

                    return parseToHospitalDeptWeekDaysDutyRoster(requestDTO, hospitalDepartmentDutyRoster, weekDays);
                }).collect(Collectors.toList());

        saveWeekDaysDutyRoster(weekDaysDutyRosters);

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_WEEK_DAYS_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDutyRosterOverride(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                        List<HospitalDeptDutyRosterOverrideRequestDTO> overrideRequestDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE);

        overrideRequestDTOS
                .forEach(requestDTO -> {
                    validateOverrideRequestInfo(hospitalDepartmentDutyRoster, requestDTO);

                    saveDutyRosterOverride(parseToSpecializationDutyRosterOverride(
                            requestDTO, hospitalDepartmentDutyRoster)
                    );
                });

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE,
                getDifferenceBetweenTwoTime(startTime));
    }


    private WeekDays fetchWeekDaysById(Long id) {
        return weekDaysRepository.fetchActiveWeekDaysById(id)
                .orElseThrow(() -> WEEK_DAYS_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> WEEK_DAYS_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, WEEK_DAYS, id);
        throw new NoContentFoundException(WeekDays.class, "id", id.toString());
    };

    private void validateIfOverrideDateIsBetweenActualDutyRoster(Date dutyRosterFromDate,
                                                                 Date dutyRosterToDate,
                                                                 Date overrideFromDate,
                                                                 Date overrideToDate) {

        boolean isDateBetweenInclusive =
                isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, removeTime(overrideFromDate))
                        && isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, removeTime(overrideToDate));

        if (!isDateBetweenInclusive) {
            log.error(String.format(BAD_REQUEST_MESSAGE, dutyRosterFromDate, dutyRosterToDate));
            throw new BadRequestException(String.format(BAD_REQUEST_MESSAGE, dutyRosterFromDate, dutyRosterToDate));
        }
    }

    private void validateSpecializationDutyRosterOverrideCount(Long specializationDutyRosterOverrideCount) {
        if (specializationDutyRosterOverrideCount.intValue() > 0) {
            log.error(DUPLICATION_MESSAGE);
            throw new DataDuplicationException(DUPLICATION_MESSAGE);
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

    private void save(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster) {
        hospitalDeptDutyRosterRepository.save(hospitalDepartmentDutyRoster);
    }

    private void saveWeekDaysDutyRoster(List<HospitalDepartmentWeekDaysDutyRoster> weekDaysDutyRosters) {
        weekDaysDutyRosterRepository.saveAll(weekDaysDutyRosters);
    }

    private void saveDutyRosterOverride(HospitalDepartmentDutyRosterOverride overrideRosters) {
        overrideRepository.save(overrideRosters);
    }

    private HospitalDepartment fetchHospitalDepartment(Long hospitalDepartmentId, Long hospitalId) {
        return hospitalDepartmentRepository.fetchByIdAndHospitalId(hospitalDepartmentId, hospitalId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT, id);
        throw new NoContentFoundException(HospitalDepartment.class, "id", id.toString());
    };

    private void validateOverrideRequestInfo(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                             HospitalDeptDutyRosterOverrideRequestDTO requestDTO) {

        validateIfOverrideDateIsBetweenActualDutyRoster(
                hospitalDepartmentDutyRoster.getFromDate(), hospitalDepartmentDutyRoster.getToDate(),
                requestDTO.getFromDate(), requestDTO.getToDate());

        validateSpecializationDutyRosterOverrideCount(
                overrideRepository.fetchOverrideCount(
                        hospitalDepartmentDutyRoster.getId(),
                        requestDTO.getFromDate(),
                        requestDTO.getToDate())
        );
    }

    private void saveDutyRosterRoomInfo(HospitalDepartmentDutyRoster dutyRoster,
                                        Long roomId, Long hospitalId) {

        Room room = fetchRoom(roomId, hospitalId);
        HospitalDepartmentDutyRosterRoomInfo roomInfo = parseRoomDetails(dutyRoster, room);
        saveDutyRosterRoomInfo(roomInfo);
    }

    private Room fetchRoom(Long roomId, Long hospitalId) {
        return roomService.fetchActiveRoom(roomId, hospitalId);
    }

    private void saveDutyRosterRoomInfo(HospitalDepartmentDutyRosterRoomInfo roomInfo) {
        dutyRosterRoomInfoRepository.save(roomInfo);
    }

    private HospitalDepartmentDutyRoster findDeptDutyRosterById(Long dutyRosterId) {
        return hospitalDeptDutyRosterRepository.fetchById(dutyRosterId)
                .orElseThrow(() -> HOSPITAL_DEPT_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND.apply(dutyRosterId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR_DUTY_ROSTER, id);
        throw new NoContentFoundException(HospitalDepartmentDutyRoster.class, "id", id.toString());
    };


}


