package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.*;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.*;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DDRShiftWiseMessages.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRBreakDetailUtils.parseToDDRBreakDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRDateValidationUtils.validateBreakTimeOverlap;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRDateValidationUtils.validateTimeOverlap;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDROverrideBreakDetailUtils.parseToDDROverrideBreakDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDROverrideDetailUtils.parseToDdrOverrideDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRShiftWiseUtils.*;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRWeekDaysUtils.parseToDDRShiftDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRWeekDaysUtils.parseToDDRWeekDaysDetail;

/**
 * @author smriti on 08/05/20
 */
@Service
@Transactional
@Slf4j
public class DDRShiftWiseServiceImpl implements DDRShiftWiseService {

    private final Validator validator;

    private final DDRShiftWiseRepository ddrShiftWiseRepository;

    private final HospitalService hospitalService;

    private final SpecializationService specializationService;

    private final DoctorService doctorService;

    private final ShiftService shiftService;

    private final DDRShiftDetailRepository ddrShiftDetailRepository;

    private final DDRWeekDaysDetailRepository ddrWeekDaysDetailRepository;

    private final WeekDaysService weekDaysService;

    private final DDRBreakDetailRepository ddrBreakDetailRepository;

    private final BreakTypeService breakTypeService;

    private final DDROverrideDetailDetailRepository ddrOverrideDetailRepository;

    private final DDROverrideBreakDetailRepository ddrOverrideBreakDetailRepository;

    public DDRShiftWiseServiceImpl(Validator validator,
                                   DDRShiftWiseRepository ddrShiftWiseRepository,
                                   HospitalService hospitalService,
                                   SpecializationService specializationService,
                                   DoctorService doctorService,
                                   ShiftService shiftService,
                                   DDRShiftDetailRepository ddrShiftDetailRepository,
                                   DDRWeekDaysDetailRepository ddrWeekDaysDetailRepository,
                                   WeekDaysService weekDaysService,
                                   DDRBreakDetailRepository ddrBreakDetailRepository,
                                   BreakTypeService breakTypeService,
                                   DDROverrideDetailDetailRepository ddrOverrideDetailRepository,
                                   DDROverrideBreakDetailRepository ddrOverrideBreakDetailRepository) {
        this.validator = validator;
        this.ddrShiftWiseRepository = ddrShiftWiseRepository;
        this.hospitalService = hospitalService;
        this.specializationService = specializationService;
        this.doctorService = doctorService;
        this.shiftService = shiftService;
        this.ddrShiftDetailRepository = ddrShiftDetailRepository;
        this.ddrWeekDaysDetailRepository = ddrWeekDaysDetailRepository;
        this.weekDaysService = weekDaysService;
        this.ddrBreakDetailRepository = ddrBreakDetailRepository;
        this.breakTypeService = breakTypeService;
        this.ddrOverrideDetailRepository = ddrOverrideDetailRepository;
        this.ddrOverrideBreakDetailRepository = ddrOverrideBreakDetailRepository;
    }

    @Override
    public void saveDDRWeekDaysDetail(@Valid DDRRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DDR_SHIFT_WISE);

        validateDDRRequestInfo(requestDTO);

        DoctorDutyRosterShiftWise ddrShiftWise = saveDDRGeneralInfo(requestDTO.getDdrDetail());

        saveDDRWeekDaysDetail(ddrShiftWise, requestDTO.getShiftDetail());

        log.info(SAVING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void saveDDROverrideDetail(DDROverrideRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DDR_OVERRIDE);

        DoctorDutyRosterShiftWise ddrShiftWise = fetchDDRShiftWise(requestDTO.getDdrId());

        updateDDROverrideStatus(ddrShiftWise, requestDTO.getHasOverride());

        if (ddrShiftWise.getHasOverride().equals(YES))
            saveDDROverrideDetail(ddrShiftWise, requestDTO.getOverrideDetail());

        log.info(SAVING_PROCESS_COMPLETED, DDR_OVERRIDE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public DDRExistingMinResponseDTO fetchMinExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_SHIFT_WISE);

        List<DDRExistingMinDTO> existingRosters = ddrShiftWiseRepository.fetchExistingDDR(requestDTO);

        DDRExistingMinResponseDTO responseDTO = parseToExistingAvailabilityResponseDTO(existingRosters);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DDRExistingDetailResponseDTO fetchDetailExistingDDR(Long ddrId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_SHIFT_WISE);

        Character hasDDROverride = ddrShiftWiseRepository.fetchOverrideStatus(ddrId)
                .orElseThrow(() -> DDR_WITH_GIVEN_ID_NOT_FOUND.apply(ddrId));

        List<DDRShiftMinResponseDTO> shiftDetail =
                ddrShiftDetailRepository.fetchExistingShift(ddrId);

        List<DDROverrideDetailResponseDTO> overrideDetail = new ArrayList<>();

        if (hasDDROverride.equals(YES))
            overrideDetail = ddrOverrideDetailRepository.fetchDDROverrideDetail(ddrId);

        DDRExistingDetailResponseDTO responseDTO =
                parseToDDRExistingDetailResponseDTO(hasDDROverride, shiftDetail, overrideDetail);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DDRExistingWeekDaysResponseDTO> fetchDDRWeekDaysDetail(DDRExistingWeekDaysRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_WEEK_DAYS);

        List<DDRExistingWeekDaysResponseDTO> weekDaysDetail =
                ddrWeekDaysDetailRepository.fetchDDRWeekDaysDetail(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_WEEK_DAYS, getDifferenceBetweenTwoTime(startTime));

        return weekDaysDetail;
    }

    @Override
    public List<DDRBreakDetailResponseDTO> fetchWeekDaysBreakDetail(Long ddrWeekDaysDetailId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_BREAK_DETAIL);

        List<DDRBreakDetailResponseDTO> breakDetails =
                ddrBreakDetailRepository.fetchWeekDaysBreakDetails(ddrWeekDaysDetailId);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_BREAK_DETAIL, getDifferenceBetweenTwoTime(startTime));

        return breakDetails;
    }

    @Override
    public List<DDRBreakDetailResponseDTO> fetchOverrideBreakDetail(Long ddrOverrideDetailId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_OVERRIDE_BREAK_DETAIL);

        List<DDRBreakDetailResponseDTO> breakDetails =
                ddrOverrideBreakDetailRepository.fetchOverrideBreakDetail(ddrOverrideDetailId);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_OVERRIDE_BREAK_DETAIL, getDifferenceBetweenTwoTime(startTime));

        return breakDetails;
    }

    /*1. VALIDATE CONSTRAINTS VIOLATION
    * 2. VALIDATE IF FIRST DATE IS GREATER THAN TO DATE
    * 3. VALIDATE REQUESTED SHIFT DETAILS
    *    A. VALIDATE IF REQUEST CONTAINS DUPLICATE SHIFT IDS
    *    B. VALIDATE IF REQUESTED SHIFT IDS IS ASSIGNED TO DOCTOR
    * 4. VALIDATE IF DDR ALREADY EXISTS FOR SAME DOCTOR, SPECIALIZATION & DATE
    * */

    private void validateDDRRequestInfo(DDRRequestDTO requestDTO) {

        validateConstraintViolation(validator.validate(requestDTO));

        validateIsFirstDateGreater(requestDTO.getDdrDetail().getFromDate(),
                requestDTO.getDdrDetail().getToDate());

        validateShiftDetail(requestDTO.getShiftDetail(), requestDTO.getDdrDetail().getDoctorId());

        validateDoctorDutyRosterDuplicity(
                requestDTO.getDdrDetail().getDoctorId(),
                requestDTO.getDdrDetail().getSpecializationId(),
                requestDTO.getDdrDetail().getFromDate(),
                requestDTO.getDdrDetail().getToDate()
        );
    }

    /*1. VALIDATE IF REQUEST CONTAINS DUPLICATE SHIFT IDS
    * 2. VALIDATE IF REQUESTED SHIFT IDS IS ASSIGNED TO DOCTOR
    * */
    private void validateShiftDetail(List<DDRShiftRequestDTO> shiftDetail,
                                     Long doctorId) {

        List<Long> requestedShiftIds = shiftDetail
                .stream().map(DDRShiftRequestDTO::getShiftId)
                .distinct()
                .collect(Collectors.toList());

        if (requestedShiftIds.size() != shiftDetail.size())
            throw new BadRequestException(DUPLICATE_SHIFT_MESSAGE);

        Long assignedDoctorShifts = doctorService.validateDoctorShiftCount(requestedShiftIds, doctorId);

        if (assignedDoctorShifts != requestedShiftIds.size())
            throw new BadRequestException(INVALID_SHIFT_REQUEST_MESSAGE);
    }

    private void validateDoctorDutyRosterDuplicity(Long doctorId, Long specializationId,
                                                   Date fromDate, Date toDate) {

        Long doctorDutyRosterCount = ddrShiftWiseRepository.validateDoctorDutyRosterCount(
                doctorId, specializationId, fromDate, toDate);

        if (doctorDutyRosterCount.intValue() > 0) {
            log.error(DUPLICATION_MESSAGE);
            throw new DataDuplicationException(DUPLICATION_MESSAGE);
        }
    }

    private DoctorDutyRosterShiftWise saveDDRGeneralInfo(DDRDetailRequestDTO ddrDetail) {

        Hospital hospital = fetchHospitalById(ddrDetail.getHospitalId());

        Specialization specialization = fetchSpecializationById(ddrDetail.getSpecializationId());

        Doctor doctor = fetchDoctorById(ddrDetail.getDoctorId());

        DoctorDutyRosterShiftWise ddrShiftWise =
                parseToDDRShiftWise(ddrDetail, hospital, specialization, doctor);

        return save(ddrShiftWise);
    }

    private void saveDDRWeekDaysDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                       List<DDRShiftRequestDTO> shiftRequestDTO) {

        List<DDRWeekDaysDetail> ddrWeekDaysDetails = new ArrayList<>();

        shiftRequestDTO.forEach(
                shiftDetail -> {
                    Shift shift = fetchShift(
                            shiftDetail.getShiftId(),
                            ddrShiftWise.getHospital().getId()
                    );

                    DDRShiftDetail ddrShiftDetail =
                            saveDDRShiftDetail(ddrShiftWise, shift, shiftDetail.getRosterGapDuration());

                    saveDDRWeekDaysDetail(shiftDetail.getWeekDaysDetail(), ddrShiftDetail, ddrWeekDaysDetails);
                }
        );
    }

    private DDRShiftDetail saveDDRShiftDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                              Shift shift,
                                              Integer rosterGapDuration) {

        DDRShiftDetail ddrShiftDetail = parseToDDRShiftDetail(ddrShiftWise, shift, rosterGapDuration);

        return ddrShiftDetailRepository.save(ddrShiftDetail);
    }

    private void saveDDRWeekDaysDetail(List<DDRWeekDaysRequestDTO> weekDaysRequestDTOS,
                                       DDRShiftDetail ddrShiftDetail,
                                       List<DDRWeekDaysDetail> ddrWeekDaysDetails) {

        weekDaysRequestDTOS.forEach(
                (DDRWeekDaysRequestDTO weekDaysRequestDTO) -> {

                    DDRWeekDaysDetail weekDaysDetail = saveWeekDaysDetail(
                            weekDaysRequestDTO,
                            ddrShiftDetail,
                            ddrWeekDaysDetails
                    );

                    if (!weekDaysRequestDTO.getBreakDetail().isEmpty()) {
                        saveDDRBreakDetail(
                                weekDaysRequestDTO.getBreakDetail(),
                                weekDaysDetail,
                                ddrShiftDetail.getDdrShiftWise().getHospital().getId()
                        );
                    }
                }
        );
    }

    private DDRWeekDaysDetail saveWeekDaysDetail(DDRWeekDaysRequestDTO weekDaysRequestDTO,
                                                 DDRShiftDetail ddrShiftDetail,
                                                 List<DDRWeekDaysDetail> ddrWeekDaysDetails) {

        validateDDRWeekDaysDetail(weekDaysRequestDTO, ddrWeekDaysDetails);

        WeekDays weekDays = findWeekDaysById(weekDaysRequestDTO.getWeekDaysId());

        DDRWeekDaysDetail weekDaysDetail =
                parseToDDRWeekDaysDetail(weekDaysRequestDTO, ddrShiftDetail, weekDays);

        saveDDRWeekDaysDetail(weekDaysDetail);

        ddrWeekDaysDetails.add(weekDaysDetail);

        return weekDaysDetail;
    }

    /* 1. VALIDATE IF END TIME IS GREATER THAN START TIME
    * 2. VALIDATE IF TIME OVERLAPS IRRESPECTIVE OF SHIFTS
    * eg. MORNING SHIFT -> SUNDAY -> 9AM-12PM -> ALLOWED
    *    DAY SHIFT -> SUNDAY -> 11AM-4PM -> NOT ALLOWED */
    private void validateDDRWeekDaysDetail(DDRWeekDaysRequestDTO weekDaysRequestDTO,
                                           List<DDRWeekDaysDetail> ddrWeekDaysDetails) {

        validateIfStartTimeGreater(weekDaysRequestDTO.getStartTime(), weekDaysRequestDTO.getEndTime());

        if (ddrWeekDaysDetails.size() >= 1) {
            ddrWeekDaysDetails.stream()
                    .filter(ddrWeekDaysDetail ->
                            ddrWeekDaysDetail.getWeekDays().getId().equals(weekDaysRequestDTO.getWeekDaysId()))
                    .forEach(matchedResult -> {

                        boolean isTimeOverlapped = validateTimeOverlap(
                                matchedResult.getStartTime(),
                                matchedResult.getEndTime(),
                                weekDaysRequestDTO.getStartTime(),
                                weekDaysRequestDTO.getEndTime()
                        );

                        if (isTimeOverlapped) {
                            log.error(String.format(INVALID_WEEK_DAYS_REQUEST, matchedResult.getWeekDays().getName()));
                            throw new BadRequestException(String.format(INVALID_WEEK_DAYS_REQUEST,
                                    matchedResult.getWeekDays().getName()));
                        }
                    });
        }
    }

    private void saveDDRBreakDetail(List<DDRBreakRequestDTO> breakRequestDTOS,
                                    DDRWeekDaysDetail ddrWeekDaysDetail,
                                    Long hospitalId) {

        List<DDRBreakDetail> ddrBreakDetails = new ArrayList<>();

        breakRequestDTOS.forEach((DDRBreakRequestDTO ddrBreakRequestDTO) -> {

            validateDDRBreakInfo(ddrWeekDaysDetail, ddrBreakDetails, ddrBreakRequestDTO);

            BreakType breakType = fetchBreakType(ddrBreakRequestDTO.getBreakTypeId(), hospitalId);

            DDRBreakDetail ddrBreakDetail = parseToDDRBreakDetail(ddrBreakRequestDTO, ddrWeekDaysDetail, breakType);

            ddrBreakDetails.add(ddrBreakDetail);
        });

        saveDDRBreakDetail(ddrBreakDetails);
    }

    private void validateDDRBreakInfo(DDRWeekDaysDetail ddrWeekDaysDetail,
                                      List<DDRBreakDetail> ddrBreakDetails,
                                      DDRBreakRequestDTO ddrBreakRequestDTO) {

        validateIfStartTimeGreater(ddrBreakRequestDTO.getStartTime(), ddrBreakRequestDTO.getEndTime());

        validateIfDDRBreakIsBetweenWeekDaysTime(ddrWeekDaysDetail, ddrBreakRequestDTO);

        if (ddrBreakDetails.size() >= 1)
            validateDDRBreakTimeDuplicity(ddrBreakDetails, ddrBreakRequestDTO);
    }

    private void validateIfDDRBreakIsBetweenWeekDaysTime(DDRWeekDaysDetail ddrWeekDaysDetail,
                                                         DDRBreakRequestDTO breakRequestDTO) {

        boolean isTimeOverlapped = validateBreakTimeOverlap(
                ddrWeekDaysDetail.getStartTime(),
                ddrWeekDaysDetail.getEndTime(),
                breakRequestDTO.getStartTime(),
                breakRequestDTO.getEndTime()
        );

        if (!isTimeOverlapped) {
            log.error(String.format(INVALID_WEEK_DAYS_BREAK_REQUEST, ddrWeekDaysDetail.getWeekDays().getName()));
            throw new BadRequestException(String.format(INVALID_WEEK_DAYS_BREAK_REQUEST,
                    ddrWeekDaysDetail.getWeekDays().getName()));
        }
    }

    private void validateDDRBreakTimeDuplicity(List<DDRBreakDetail> breakDetails,
                                               DDRBreakRequestDTO breakRequestDTO) {

        breakDetails.forEach(breakDetail -> {

                    boolean isTimeOverlapped = validateTimeOverlap(
                            breakDetail.getStartTime(),
                            breakDetail.getEndTime(),
                            breakRequestDTO.getStartTime(),
                            breakRequestDTO.getEndTime()
                    );

                    if (isTimeOverlapped) {
                        log.error(BREAK_TIME_OVERLAP_MESSAGE);
                        throw new BadRequestException(BREAK_TIME_OVERLAP_MESSAGE);
                    }
                }
        );
    }


    private void saveDDROverrideDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                       List<DDROverrideDetailRequestDTO> overrideRequestDTOS) {

        validateOverrideShiftDetail(overrideRequestDTOS, ddrShiftWise.getDoctor().getId());

        List<DDROverrideDetail> overrideDetails = new ArrayList<>();

        overrideRequestDTOS.forEach(
                (DDROverrideDetailRequestDTO overrideRequestDTO) -> {

                    DDROverrideDetail ddrOverrideDetail = saveDDROverrideDetail(
                            ddrShiftWise,
                            overrideRequestDTO,
                            overrideDetails
                    );

                    if (!overrideRequestDTO.getBreakDetail().isEmpty()) {
                        saveDDROverrideBreakDetail(
                                overrideRequestDTO.getBreakDetail(),
                                ddrOverrideDetail,
                                ddrShiftWise.getHospital().getId()
                        );
                    }
                }
        );
    }

    private DDROverrideDetail saveDDROverrideDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                                    DDROverrideDetailRequestDTO overrideRequestDTO,
                                                    List<DDROverrideDetail> overrideDetails) {

        validateDDROverrideRequestInfo(ddrShiftWise, overrideRequestDTO, overrideDetails);

        Shift shift = fetchShift(overrideRequestDTO.getShiftId(), ddrShiftWise.getHospital().getId());

        DDROverrideDetail ddrOverrideDetail =
                parseToDdrOverrideDetail(overrideRequestDTO, ddrShiftWise, shift);

        saveDDROverrideDetail(ddrOverrideDetail);

        overrideDetails.add(ddrOverrideDetail);

        return ddrOverrideDetail;
    }

    private void saveDDROverrideBreakDetail(List<DDRBreakRequestDTO> breakRequestDTOS,
                                            DDROverrideDetail overrideDetail,
                                            Long hospitalId) {

        List<DDROverrideBreakDetail> ddrOverrideBreakDetails = new ArrayList<>();

        breakRequestDTOS.forEach(ddrBreakRequestDTO -> {

            validateDDROverrideBreakRequestInfo(overrideDetail, ddrOverrideBreakDetails, ddrBreakRequestDTO);

            BreakType breakType = fetchBreakType(ddrBreakRequestDTO.getBreakTypeId(), hospitalId);

            DDROverrideBreakDetail ddrOverrideBreakDetail =
                    parseToDDROverrideBreakDetail(ddrBreakRequestDTO, overrideDetail, breakType);

            ddrOverrideBreakDetails.add(ddrOverrideBreakDetail);
        });

        saveDDROverrideBreakDetail(ddrOverrideBreakDetails);
    }

    /*1. VALIDATE IF REQUESTED OVERRIDE DATE LIES IN BETWEEN ORIGINAL DDR FROM DATE & TO DATE
  * 2. VALIDATE IF END TIME IS GREATER THAN START TIME
  * 3. FOR THE SAME DAY,
  *  IF THERE ARE MULTIPLE OVERRIDE REQUESTS, VALIDATE IF START TIME & END TIME OVERLAPS WITH OTHER
  *  (SHOULD BE EXCLUSIVE)
   *  EG: IF FIRST REQUEST IS 2020-05-10 (9AM-5PM)
   *      NOW NEXT REQUEST FOR 2020-05-10(9AM-1PM) IS NOT ALLOWED SINCE IT IS BETWEEN (9AM-5PM)
   * */
    private void validateDDROverrideRequestInfo(DoctorDutyRosterShiftWise ddrShiftWise,
                                                DDROverrideDetailRequestDTO overrideRequestDTO,
                                                List<DDROverrideDetail> overrideDetails) {

        validateIfOverrideDateIsBetweenDDDR(
                ddrShiftWise.getFromDate(),
                ddrShiftWise.getToDate(),
                overrideRequestDTO.getDate()
        );

        validateIfStartTimeGreater(
                overrideRequestDTO.getStartTime(),
                overrideRequestDTO.getEndTime()
        );

        if (overrideDetails.size() >= 1) {
            overrideDetails.stream()
                    .filter(overrideDetail ->
                            overrideDetail.getDate().equals(overrideRequestDTO.getDate())
                    ).forEach(matchedResult -> {

                        boolean isTimeOverlapped = validateTimeOverlap(
                                matchedResult.getStartTime(),
                                matchedResult.getEndTime(),
                                overrideRequestDTO.getStartTime(),
                                overrideRequestDTO.getEndTime()
                        );

                        if (isTimeOverlapped) {
                            log.error(OVERRIDE_TIME_OVERLAP_MESSAGE);
                            throw new BadRequestException(OVERRIDE_TIME_OVERLAP_MESSAGE);
                        }
                    }
            );
        }
    }

    /*1. VALIDATE IF END TIME IS GREATER THAN START TIME
    * 2. VALIDATE IF OVERRIDE BREAK TIME SCHEDULES IS BETWEEN DDR OVERRIDE FROM DATE-TO DATE (SHOULD BE INCLUSIVE)
   *  3. IF THERE ARE MULTIPLE OVERRIDE BREAK REQUESTS, VALIDATE IF START TIME & END TIME OVERLAPS WITH OTHER
   *  EG: IF FIRST REQUEST IS 2020-05-10 (9AM-12PM)
   *      NOW NEXT REQUEST FOR 2020-05-10(9AM-11PM) IS NOT ALLOWED SINCE IT IS BETWEEN (9AM-12PM)
 * */
    private void validateDDROverrideBreakRequestInfo(DDROverrideDetail overrideDetail,
                                                     List<DDROverrideBreakDetail> overrideBreakDetails,
                                                     DDRBreakRequestDTO breakRequestDTO) {

        validateIfStartTimeGreater(breakRequestDTO.getStartTime(), breakRequestDTO.getEndTime());

        validateIfOverrideBreakIsBetweenOverrideTime(overrideDetail, breakRequestDTO);

        if (overrideBreakDetails.size() >= 1)
            validateOverrideBreakTimeDuplicity(overrideBreakDetails, breakRequestDTO);
    }

    private void validateIfOverrideBreakIsBetweenOverrideTime(DDROverrideDetail overrideDetail,
                                                              DDRBreakRequestDTO breakRequestDTO) {

        boolean isTimeOverlapped = validateBreakTimeOverlap(
                overrideDetail.getStartTime(),
                overrideDetail.getEndTime(),
                breakRequestDTO.getStartTime(),
                breakRequestDTO.getEndTime()
        );

        if (!isTimeOverlapped) {
            log.error(INVALID_BREAK_OVERRIDE_REQUEST);
            throw new BadRequestException(INVALID_BREAK_OVERRIDE_REQUEST);
        }
    }

    private void validateOverrideBreakTimeDuplicity(List<DDROverrideBreakDetail> overrideBreakDetails,
                                                    DDRBreakRequestDTO breakRequestDTO) {

        overrideBreakDetails.forEach(overrideBreakDetail -> {

                    boolean isTimeOverlapped = validateTimeOverlap(
                            overrideBreakDetail.getStartTime(),
                            overrideBreakDetail.getEndTime(),
                            breakRequestDTO.getStartTime(),
                            breakRequestDTO.getEndTime()
                    );

                    if (isTimeOverlapped) {
                        log.error(BREAK_TIME_OVERLAP_MESSAGE);
                        throw new BadRequestException(BREAK_TIME_OVERLAP_MESSAGE);
                    }
                }
        );
    }

    /*VALIDATE IF ALL REQUESTED SHIFTS ARE ASSIGNED TO DOCTOR
    *
    * 1. FETCH ACTUAL ASSIGNED DOCTOR SHIFTS
    * EG. COUNT = 2
    *
    * 2. IF REQUESTED COUNT !=ASSIGNED SHIFTS COUNT, MEANS INVALID SHIFT IDS ARE REQUESTED*/
    private void validateOverrideShiftDetail(List<DDROverrideDetailRequestDTO> overrideDetail,
                                             Long doctorId) {

        List<Long> requestedShiftIds = overrideDetail
                .stream().map(DDROverrideDetailRequestDTO::getShiftId)
                .distinct()
                .collect(Collectors.toList());

        Long assignedDoctorShifts = doctorService.validateDoctorShiftCount(requestedShiftIds, doctorId);

        if (assignedDoctorShifts != requestedShiftIds.size())
            throw new BadRequestException(INVALID_SHIFT_REQUEST_MESSAGE);
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private Doctor fetchDoctorById(Long doctorId) {
        return doctorService.fetchActiveDoctorById(doctorId);
    }

    private Specialization fetchSpecializationById(Long specializationId) {
        return specializationService.fetchActiveSpecializationById(specializationId);
    }

    private DoctorDutyRosterShiftWise save(DoctorDutyRosterShiftWise ddrShiftWise) {
        return ddrShiftWiseRepository.save(ddrShiftWise);
    }

    private Shift fetchShift(Long shiftId, Long hospitalId) {
        return shiftService.fetchActiveShiftByIdAndHospitalId(shiftId, hospitalId);
    }

    private WeekDays findWeekDaysById(Long weekDaysId) {
        return weekDaysService.fetchWeekDaysById(weekDaysId);
    }

    private void saveDDRWeekDaysDetail(DDRWeekDaysDetail ddrWeekDaysDetail) {
        ddrWeekDaysDetailRepository.save(ddrWeekDaysDetail);
    }

    private void saveDDRBreakDetail(List<DDRBreakDetail> ddrBreakDetails) {
        ddrBreakDetailRepository.saveAll(ddrBreakDetails);
    }

    private void saveDDROverrideBreakDetail(List<DDROverrideBreakDetail> ddrBreakDetails) {
        ddrOverrideBreakDetailRepository.saveAll(ddrBreakDetails);
    }

    private BreakType fetchBreakType(Long breakTypeId, Long hospitalId) {
        return breakTypeService.fetchActiveBreakTypeByIdAndHospitalId(breakTypeId, hospitalId);
    }

    private DoctorDutyRosterShiftWise fetchDDRShiftWise(Long ddrId) {
        return ddrShiftWiseRepository.findDoctorDutyRosterById(ddrId)
                .orElseThrow(() -> DDR_WITH_GIVEN_ID_NOT_FOUND.apply(ddrId));
    }

    private Function<Long, NoContentFoundException> DDR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DDR_SHIFT_WISE, id);
        throw new NoContentFoundException(DoctorDutyRosterShiftWise.class, "id", id.toString());
    };

    private void updateDDROverrideStatus(DoctorDutyRosterShiftWise ddrShiftWise,
                                         Character hasOverride) {

        ddrShiftWise.setHasOverride(hasOverride);
        save(ddrShiftWise);
    }

    private void validateIfOverrideDateIsBetweenDDDR(Date dutyRosterFromDate,
                                                     Date dutyRosterToDate,
                                                     Date overrideDate) {

        validateIsFirstDateGreater(dutyRosterFromDate, dutyRosterToDate);

        boolean isDateBetweenInclusive =
                isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, removeTime(overrideDate));

        if (!isDateBetweenInclusive) {
            log.error(INVALID_OVERRIDE_REQUEST_MESSAGE);
            throw new BadRequestException(INVALID_OVERRIDE_REQUEST_MESSAGE);
        }
    }

    private void saveDDROverrideDetail(DDROverrideDetail overrideDetails) {
        ddrOverrideDetailRepository.save(overrideDetails);
    }


}
