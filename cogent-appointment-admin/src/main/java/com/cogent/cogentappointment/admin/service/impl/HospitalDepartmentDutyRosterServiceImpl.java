package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.HospitalDeptExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptDutyRosterOverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.save.HospitalDeptWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterRoomUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptWeekDaysDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.detail.HospitalDeptDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.HospitalDepartmentDutyRosterService;
import com.cogent.cogentappointment.admin.service.RoomService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.HospitalDeptDutyRosterMessages.*;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NO_RECORD_FOUND;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.HospitalDepartmentDutyRosterLog.*;
import static com.cogent.cogentappointment.admin.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.CLIENT;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.admin.log.constants.WeekDaysLog.WEEK_DAYS;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterRoomUtils.parseRoomDetails;
import static com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterRoomUtils.updateRoomDetails;
import static com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterUtils.*;
import static com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster.HospitalDeptOverrideDutyRosterUtils.*;
import static com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster.HospitalDeptWeekDaysDutyRosterUtils.parseToHospitalDeptWeekDaysDutyRoster;
import static com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster.HospitalDeptWeekDaysDutyRosterUtils.parseUpdatedWeekDaysDetails;

/**
 * @author smriti on 20/05/20
 */
@Service
@Slf4j
@Transactional
public class HospitalDepartmentDutyRosterServiceImpl implements HospitalDepartmentDutyRosterService {

    private final HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository;

    private final HospitalDeptWeekDaysDutyRosterRepository weekDaysDutyRosterRepository;

    private final WeekDaysRepository weekDaysRepository;

    private final HospitalDeptDutyRosterOverrideRepository overrideRepository;

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    private final RoomService roomService;

    private final HospitalDeptDutyRosterRoomInfoRepository dutyRosterRoomInfoRepository;

    private final HospitalRepository hospitalRepository;

    public HospitalDepartmentDutyRosterServiceImpl(HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository,
                                                   HospitalDeptWeekDaysDutyRosterRepository weekDaysDutyRosterRepository,
                                                   WeekDaysRepository weekDaysRepository,
                                                   HospitalDeptDutyRosterOverrideRepository overrideRepository,
                                                   HospitalDepartmentRepository hospitalDepartmentRepository,
                                                   RoomService roomService,
                                                   HospitalDeptDutyRosterRoomInfoRepository dutyRosterRoomInfoRepository,
                                                   HospitalRepository hospitalRepository) {

        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
        this.weekDaysDutyRosterRepository = weekDaysDutyRosterRepository;
        this.weekDaysRepository = weekDaysRepository;
        this.overrideRepository = overrideRepository;
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
        this.roomService = roomService;
        this.dutyRosterRoomInfoRepository = dutyRosterRoomInfoRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void save(HospitalDepartmentDutyRosterRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        validateHDDRosterRequestInfo(requestDTO);

        HospitalDepartmentDutyRoster dutyRoster = parseToHospitalDepartmentDutyRoster(
                requestDTO,
                fetchHospitalDepartment(requestDTO.getHospitalDepartmentId(), requestDTO.getHospitalId()),
                findHospitalById(requestDTO.getHospitalId()));

        save(dutyRoster);

        if (dutyRoster.getIsRoomEnabled().equals(YES))
            saveDutyRosterRoomInfo(dutyRoster, requestDTO.getRoomId());

        saveWeekDaysDutyRoster(dutyRoster, requestDTO.getWeekDaysDetail());

        if (dutyRoster.getHasOverrideDutyRoster().equals(YES))
            saveDutyRosterOverride(dutyRoster, requestDTO.getOverrideDetail(), requestDTO.getRoomId());

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                             Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        List<HospitalDeptDutyRosterMinResponseDTO> minInfo =
                hospitalDeptDutyRosterRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        HospitalDepartmentDutyRoster departmentDutyRoster = findHospitalDeptDutyRosterById(deleteRequestDTO.getId());

        parseDeletedDetails(departmentDutyRoster, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public HospitalDeptDutyRosterDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        HospitalDeptDutyRosterDetailResponseDTO responseDTO =
                hospitalDeptDutyRosterRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void update(HospitalDeptDutyRosterUpdateRequestDTO updateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        HospitalDepartmentDutyRoster dutyRoster =
                findHospitalDeptDutyRosterById(updateRequestDTO.getUpdateDetail().getHddRosterId());

        validateUpdateHDDRosterDuplicity(updateRequestDTO, dutyRoster);

        parseToUpdatedRosterDetails(dutyRoster, updateRequestDTO.getUpdateDetail());

        saveOrUpdateRosterInfo(dutyRoster, updateRequestDTO.getRoomDetail());

        updateWeekDaysDutyRoster(updateRequestDTO.getWeekDaysDetail());

        updateDutyRosterOverrideStatus(dutyRoster);

        updateDutyRosterRoomStatus(dutyRoster,
                updateRequestDTO.getRoomDetail().getRoomId(),
                updateRequestDTO.getUpdateDetail().getIsRoomUpdated()
        );

        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public HospitalDeptDutyRosterOverrideUpdateResponseDTO updateOverride(
            HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE);

        HospitalDepartmentDutyRoster dutyRoster = findHospitalDeptDutyRosterById(updateRequestDTO.getHddRosterId());

        validateUpdatedOverrideRequestInfo(dutyRoster, updateRequestDTO);

        Room room = null;
        if (!Objects.isNull(updateRequestDTO.getRoomId()))
            room = fetchRoom(updateRequestDTO.getRoomId());

        Long savedOverrideId = Objects.isNull(updateRequestDTO.getRosterOverrideId()) ?
                saveHDDRosterOverride(updateRequestDTO, dutyRoster, room) :
                updateHDDRosterOverride(updateRequestDTO, dutyRoster, room);

        HospitalDeptDutyRosterOverrideUpdateResponseDTO updateResponse =
                parseOverrideUpdateResponse(savedOverrideId);

        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE,
                getDifferenceBetweenTwoTime(startTime));

        return updateResponse;
    }

    @Override
    public void deleteOverride(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE);

        HospitalDepartmentDutyRosterOverride override = fetchOverrideById(deleteRequestDTO.getId());

        parseDeletedOverrideDetails(override, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE,
                getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void revertOverride(List<HospitalDeptDutyRosterOverrideUpdateRequestDTO> updateInfo) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REVERTING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE);

        List<HospitalDepartmentDutyRosterOverride> originalOverrideRosters =
                overrideRepository.fetchOverrideList(updateInfo);

        originalOverrideRosters.forEach(
                originalOverride -> updateInfo.stream()
                        .filter(updatedOverride -> isOriginalUpdatedCondition(originalOverride, updatedOverride))
                        .forEachOrdered(updatedOverride -> {
                            Room room = fetchRoom(updatedOverride.getRoomId());
                            parseOverrideDetails(updatedOverride, originalOverride, room);
                        }));

        log.info(REVERTING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE,
                getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<HospitalDeptExistingDutyRosterResponseDTO> fetchExistingDutyRosters(
            HospitalDeptExistingDutyRosterRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, EXISTING_HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        List<HospitalDeptExistingDutyRosterResponseDTO> existingRosters =
                hospitalDeptDutyRosterRepository.fetchExistingDutyRosters(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, EXISTING_HOSPITAL_DEPARTMENT_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));

        return existingRosters;
    }

    @Override
    public HospitalDeptExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long hddRosterId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, EXISTING_HOSPITAL_DEPARTMENT_DUTY_ROSTER);

        HospitalDeptExistingDutyRosterDetailResponseDTO existingRosterDetails =
                hospitalDeptDutyRosterRepository.fetchExistingRosterDetails(hddRosterId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, EXISTING_HOSPITAL_DEPARTMENT_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));

        return existingRosterDetails;
    }

    private void validateHDDRosterRequestInfo(HospitalDepartmentDutyRosterRequestDTO requestDTO) {
        validateIsFirstDateGreater(requestDTO.getFromDate(), requestDTO.getToDate());

        validateRoomRequestInfo(requestDTO.getIsRoomEnabled(), requestDTO.getRoomId());

        validateHDDRosterDuplicity(requestDTO);
    }

    private void validateRoomRequestInfo(Character isRoomEnabled, Long roomId) {
        if (isRoomEnabled.equals(YES) && Objects.isNull(roomId)) {
            log.error(BAD_ROOM_REQUEST);
            throw new BadRequestException(BAD_ROOM_REQUEST);
        }
    }

    /* cases:
  1. If DDR has been created without room initially, do not allow to save other DDR and vice-versa
  2. If DDR has been created with room, validate if the request contains same room id
  3. ASSUMING SAME DDR CAN BE CREATED FOR DIFFERENT ROOMS

  IF EXISTING ROOM STATUS IS 'N', THEN NO DDR CAN BE ADDED
  IF EXISTING ROOM STATUS IS 'Y', VALIDATE IF IT IS FOR SAME ROOM REQUEST
  */
    private void validateHDDRosterDuplicity(HospitalDepartmentDutyRosterRequestDTO requestDTO) {

        Character roomStatus = hospitalDeptDutyRosterRepository.fetchRoomStatusIfExists(
                requestDTO.getHospitalDepartmentId(),
                requestDTO.getFromDate(),
                requestDTO.getToDate()
        );

        if (!Objects.isNull(roomStatus)) {

            if (roomStatus.equals(NO)) {
                DUPLICATE_HOSPITAL_DEPT_DUTY_ROSTER_WITHOUT_ROOM_EXCEPTION(
                        requestDTO.getFromDate(), requestDTO.getToDate());
            } else {

                if (requestDTO.getIsRoomEnabled().equals(YES)) {
                    Long rosterCount = dutyRosterRoomInfoRepository.fetchRoomCount(
                            requestDTO.getHospitalDepartmentId(),
                            requestDTO.getFromDate(),
                            requestDTO.getToDate(),
                            requestDTO.getRoomId()
                    );

                    if (rosterCount > 0)
                        DUPLICATE_HOSPITAL_DEPT_DUTY_ROSTER_WITH_ROOM_EXCEPTION(
                                requestDTO.getFromDate(), requestDTO.getToDate());
                } else {
                    DUPLICATE_HOSPITAL_DEPT_DUTY_ROSTER_WITHOUT_ROOM_EXCEPTION(
                            requestDTO.getFromDate(), requestDTO.getToDate());
                }
            }
        }
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

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_WEEK_DAYS_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDutyRosterOverride(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                        List<HospitalDeptDutyRosterOverrideRequestDTO> overrideRequestDTOS,
                                        Long roomId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE);

        overrideRequestDTOS
                .forEach(requestDTO -> {
                    validateOverrideRequestInfo(hospitalDepartmentDutyRoster, requestDTO, roomId);

                    Room room = hospitalDepartmentDutyRoster.getIsRoomEnabled().equals(YES)
                            ? fetchRoom(roomId) : null;

                    saveDutyRosterOverride(parseOverrideDetails(requestDTO, hospitalDepartmentDutyRoster, room));
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

        boolean isDateBetweenInclusive = isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, overrideFromDate)
                && isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, overrideToDate);

        if (!isDateBetweenInclusive) {
            log.error(String.format(BAD_REQUEST_MESSAGE, utilDateToSqlDate(dutyRosterFromDate),
                    utilDateToSqlDate(dutyRosterToDate)));
            throw new BadRequestException(String.format(BAD_REQUEST_MESSAGE, utilDateToSqlDate(dutyRosterFromDate),
                    utilDateToSqlDate(dutyRosterToDate)));
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
        return hospitalDepartmentRepository.fetchActiveByIdAndHospitalId(hospitalDepartmentId, hospitalId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT, id);
        throw new NoContentFoundException(HospitalDepartment.class, "id", id.toString());
    };

    private void validateOverrideRequestInfo(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                             HospitalDeptDutyRosterOverrideRequestDTO requestDTO,
                                             Long roomId) {

        validateIsFirstDateGreater(requestDTO.getFromDate(), requestDTO.getToDate());

        validateIfOverrideDateIsBetweenActualDutyRoster(
                hospitalDepartmentDutyRoster.getFromDate(), hospitalDepartmentDutyRoster.getToDate(),
                requestDTO.getFromDate(), requestDTO.getToDate());

        validateOverrideRoomInfo(hospitalDepartmentDutyRoster, roomId,
                requestDTO.getFromDate(), requestDTO.getToDate());
    }

    private void validateOverrideRoomInfo(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                          Long roomId,
                                          Date fromDate,
                                          Date toDate) {

        if (hospitalDepartmentDutyRoster.getIsRoomEnabled().equals(YES)) {
            Long count = overrideRepository.fetchOverrideCountWithRoom(
                    hospitalDepartmentDutyRoster.getHospitalDepartment().getId(), fromDate, toDate, roomId);

            validateOverrideCountWithRoom(count, fromDate, toDate);
        } else {

            Long count = overrideRepository.fetchOverrideCountWithoutRoom(
                    hospitalDepartmentDutyRoster.getHospitalDepartment().getId(), fromDate, toDate);

            validateOverrideCountWithoutRoom(count, fromDate, toDate);
        }
    }

    private void validateOverrideCountWithoutRoom(Long overrideCount,
                                                  Date fromDate,
                                                  Date toDate) {
        if (overrideCount.intValue() > 0) {
            log.error(String.format(DUPLICATE_DUTY_ROSTER_OVERRIDE_WITHOUT_ROOM,
                    utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
            throw new DataDuplicationException(String.format(DUPLICATE_DUTY_ROSTER_OVERRIDE_WITHOUT_ROOM,
                    utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
        }
    }

    private void validateOverrideCountWithRoom(Long overrideCount,
                                               Date fromDate,
                                               Date toDate) {
        if (overrideCount.intValue() > 0) {
            log.error(String.format(DUPLICATE_DUTY_ROSTER_OVERRIDE_WITH_ROOM,
                    utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
            throw new DataDuplicationException(String.format(DUPLICATE_DUTY_ROSTER_OVERRIDE_WITH_ROOM,
                    utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
        }
    }

    private void saveDutyRosterRoomInfo(HospitalDepartmentDutyRoster dutyRoster,
                                        Long roomId) {

        Room room = fetchRoom(roomId);
        HospitalDepartmentDutyRosterRoomInfo roomInfo = parseRoomDetails(dutyRoster, room);
        saveDutyRosterRoomInfo(roomInfo);
    }

    private Room fetchRoom(Long roomId) {
        return roomService.fetchActiveRoom(roomId);
    }

    private void saveDutyRosterRoomInfo(HospitalDepartmentDutyRosterRoomInfo roomInfo) {
        dutyRosterRoomInfoRepository.save(roomInfo);
    }

    private HospitalDepartmentDutyRoster findHospitalDeptDutyRosterById(Long dutyRosterId) {
        return hospitalDeptDutyRosterRepository.fetchById(dutyRosterId)
                .orElseThrow(() -> HOSPITAL_DEPT_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND.apply(dutyRosterId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT_DUTY_ROSTER, id);
        throw new NoContentFoundException(HospitalDepartmentDutyRoster.class, "id", id.toString());
    };

    private void updateWeekDaysDutyRoster(List<HospitalDeptWeekDaysDutyRosterUpdateRequestDTO> weekDaysDetail) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_WEEK_DAYS_DUTY_ROSTER);

        List<HospitalDepartmentWeekDaysDutyRoster> weekDaysDutyRosters = weekDaysDetail.stream()
                .map(requestDTO -> {
                    validateIfStartTimeGreater(requestDTO.getStartTime(), requestDTO.getEndTime());

                    HospitalDepartmentWeekDaysDutyRoster weekDaysDutyRoster =
                            fetchHospitalDeptWeekDaysRoster(requestDTO.getRosterWeekDaysId());

                    return parseUpdatedWeekDaysDetails(requestDTO, weekDaysDutyRoster);
                }).collect(Collectors.toList());

        saveWeekDaysDutyRoster(weekDaysDutyRosters);

        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_WEEK_DAYS_DUTY_ROSTER,
                getDifferenceBetweenTwoTime(startTime));
    }

    private HospitalDepartmentWeekDaysDutyRoster fetchHospitalDeptWeekDaysRoster(Long rosterWeekDaysId) {
        return weekDaysDutyRosterRepository.fetchById(rosterWeekDaysId)
                .orElseThrow(() -> new NoContentFoundException(HospitalDepartmentWeekDaysDutyRoster.class,
                        "rosterWeekDaysId", rosterWeekDaysId.toString()));
    }

    /*UPDATE ALL EXISTING OVERRIDE ROSTER STATUS IF DUTY ROSTER'S HAS OVERRIDE FLAG IS 'N'*/
    private void updateDutyRosterOverrideStatus(HospitalDepartmentDutyRoster dutyRoster) {
        if (dutyRoster.getHasOverrideDutyRoster().equals(NO))
            overrideRepository.updateOverrideStatus(dutyRoster.getId());
    }

    /*IF ROOM IS UPDATED :
    A. ORIGINALLY NO ROOM, NOW ROOM IS ENABLED
    B. ORIGINALLY ROOM, NOW ROOM IS DISABLED
    C. ORIGINALLY ROOM1, NOW UPDATED TO ROOM2
    UPDATE ALL EXISTING OVERRIDE ROSTER ROOM ID LIKEWISE AS PER ABOVE CONDITIONS*/
    private void updateDutyRosterRoomStatus(HospitalDepartmentDutyRoster dutyRoster,
                                            Long roomId, Character isRoomUpdated) {

        if (dutyRoster.getHasOverrideDutyRoster().equals(YES) && isRoomUpdated.equals(YES))
            overrideRepository.updateOverrideRoomInfo(dutyRoster.getId(), roomId);
    }

    private void saveOrUpdateRosterInfo(HospitalDepartmentDutyRoster dutyRoster,
                                        HospitalDeptDutyRosterRoomUpdateRequestDTO roomUpdateRequestDTO) {

        if (Objects.isNull(roomUpdateRequestDTO.getRosterRoomId()) && dutyRoster.getIsRoomEnabled().equals(YES))
            saveDutyRosterRoomInfo(dutyRoster, roomUpdateRequestDTO.getRoomId());

        if (!Objects.isNull(roomUpdateRequestDTO.getRosterRoomId())) {
            Room room = fetchRoom(roomUpdateRequestDTO.getRoomId());
            updateDutyRosterRoomInfo(roomUpdateRequestDTO.getRosterRoomId(), room, roomUpdateRequestDTO.getStatus());
        }
    }

    private void updateDutyRosterRoomInfo(Long rosterRoomId, Room room, Character status) {

        HospitalDepartmentDutyRosterRoomInfo rosterRoomInfo = dutyRosterRoomInfoRepository.fetchById(rosterRoomId)
                .orElseThrow(() -> new NoContentFoundException(HospitalDepartmentDutyRosterRoomInfo.class));

        updateRoomDetails(room, status, rosterRoomInfo);
    }

    private void validateUpdatedOverrideRequestInfo(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                                    HospitalDeptDutyRosterOverrideUpdateRequestDTO requestDTO) {

        validateIsFirstDateGreater(requestDTO.getFromDate(), requestDTO.getToDate());

        validateIfOverrideDateIsBetweenActualDutyRoster(
                hospitalDepartmentDutyRoster.getFromDate(), hospitalDepartmentDutyRoster.getToDate(),
                requestDTO.getFromDate(), requestDTO.getToDate());
    }

    private void validateUpdatedOverrideRoomInfo(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                                 HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {

        Date fromDate = updateRequestDTO.getFromDate();
        Date toDate = updateRequestDTO.getToDate();

        if (hospitalDepartmentDutyRoster.getIsRoomEnabled().equals(YES)) {
            Long count = overrideRepository.fetchOverrideCountWithRoomExceptCurrentId(
                    hospitalDepartmentDutyRoster.getHospitalDepartment().getId(), fromDate,
                    toDate, updateRequestDTO.getRoomId(), updateRequestDTO.getRosterOverrideId()
            );

            validateOverrideCountWithRoom(count, fromDate, toDate);
        } else {

            Long count = overrideRepository.fetchOverrideCountWithoutRoomExceptCurrentId(
                    hospitalDepartmentDutyRoster.getHospitalDepartment().getId(), fromDate,
                    toDate, updateRequestDTO.getRosterOverrideId());

            validateOverrideCountWithoutRoom(count, fromDate, toDate);
        }
    }

    private Long saveHDDRosterOverride(HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO,
                                       HospitalDepartmentDutyRoster dutyRoster,
                                       Room room) {

        validateOverrideRoomInfo(dutyRoster, updateRequestDTO.getRoomId(),
                updateRequestDTO.getFromDate(), updateRequestDTO.getToDate());

        HospitalDepartmentDutyRosterOverride override = parseOverrideDetails(
                updateRequestDTO, new HospitalDepartmentDutyRosterOverride(), room);

        override.setHospitalDepartmentDutyRoster(dutyRoster);

        overrideRepository.save(override);

        return override.getId();
    }

    private Long updateHDDRosterOverride(HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO,
                                         HospitalDepartmentDutyRoster dutyRoster,
                                         Room room) {

        validateUpdatedOverrideRoomInfo(dutyRoster, updateRequestDTO);

        HospitalDepartmentDutyRosterOverride override = fetchOverrideById(updateRequestDTO.getRosterOverrideId());
        parseOverrideDetails(updateRequestDTO, override, room);

        return override.getId();
    }

    private HospitalDepartmentDutyRosterOverride fetchOverrideById(Long rosterOverrideId) {
        return overrideRepository.fetchById(rosterOverrideId).orElseThrow(() ->
                HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_WITH_GIVEN_ID_NOT_FOUND.apply(rosterOverrideId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_WITH_GIVEN_ID_NOT_FOUND =
            (rosterOverrideId) -> {
                log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT_DUTY_ROSTER_OVERRIDE, rosterOverrideId);
                throw new NoContentFoundException(HospitalDepartmentDutyRosterOverride.class, "rosterOverrideId",
                        rosterOverrideId.toString());
            };

    private static boolean isOriginalUpdatedCondition(HospitalDepartmentDutyRosterOverride originalOverride,
                                                      HospitalDeptDutyRosterOverrideUpdateRequestDTO updatedOverride) {
        return originalOverride.getId().equals(updatedOverride.getRosterOverrideId());
    }

    private void DUPLICATE_HOSPITAL_DEPT_DUTY_ROSTER_WITHOUT_ROOM_EXCEPTION(Date fromDate, Date toDate) {
        log.error(String.format(DUPLICATE_DUTY_ROSTER_WITHOUT_ROOM,
                utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
        throw new DataDuplicationException(String.format(DUPLICATE_DUTY_ROSTER_WITHOUT_ROOM,
                utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
    }

    private void DUPLICATE_HOSPITAL_DEPT_DUTY_ROSTER_WITH_ROOM_EXCEPTION(Date fromDate, Date toDate) {
        log.error(String.format(DUPLICATE_DUTY_ROSTER_WITH_ROOM,
                utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
        throw new DataDuplicationException(String.format(DUPLICATE_DUTY_ROSTER_WITH_ROOM,
                utilDateToSqlDate(fromDate), utilDateToSqlDate(toDate)));
    }

    /*1. FETCH ANY EXISTING DDR EXCEPT FOR REQUESTED ONE
   *   IF IT EXISTS:
   *       A. REQUEST = 'N', EXISTING = 'N' -> NOT ALLOWED
   *       B. REQUEST = 'Y', VALIDATE WITH OTHER DDR IF SAME ROOM REQUEST EXISTS*/
    private void validateUpdateHDDRosterDuplicity(HospitalDeptDutyRosterUpdateRequestDTO requestDTO,
                                                  HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster) {

        validateRoomRequestInfo(requestDTO.getUpdateDetail().getIsRoomEnabled(), requestDTO.getRoomDetail().getRoomId());

        Character roomStatus = hospitalDeptDutyRosterRepository.fetchRoomStatusIfExistsExceptCurrentId(
                hospitalDepartmentDutyRoster.getHospitalDepartment().getId(),
                hospitalDepartmentDutyRoster.getFromDate(),
                hospitalDepartmentDutyRoster.getToDate(),
                hospitalDepartmentDutyRoster.getId()
        );

        if (!Objects.isNull(roomStatus)) {

            if (requestDTO.getUpdateDetail().getIsRoomEnabled().equals(NO)) {

                if (roomStatus.equals(NO))
                    DUPLICATE_HOSPITAL_DEPT_DUTY_ROSTER_WITHOUT_ROOM_EXCEPTION(
                            hospitalDepartmentDutyRoster.getFromDate(), hospitalDepartmentDutyRoster.getToDate());
            } else {
                Long rosterCount = dutyRosterRoomInfoRepository.fetchRoomCountExceptCurrentId(
                        hospitalDepartmentDutyRoster.getHospitalDepartment().getId(),
                        hospitalDepartmentDutyRoster.getFromDate(),
                        hospitalDepartmentDutyRoster.getToDate(),
                        requestDTO.getRoomDetail().getRoomId(),
                        hospitalDepartmentDutyRoster.getId()
                );

                if (rosterCount > 0)
                    DUPLICATE_HOSPITAL_DEPT_DUTY_ROSTER_WITH_ROOM_EXCEPTION(
                            hospitalDepartmentDutyRoster.getFromDate(), hospitalDepartmentDutyRoster.getToDate());
            }
        }
    }

    private Hospital findHospitalById(Long hospitalId) {
        return hospitalRepository.findActiveHospitalById(hospitalId)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (hospitalId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, hospitalId);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND, CLIENT), "hospitalId", hospitalId.toString());
    };


}


