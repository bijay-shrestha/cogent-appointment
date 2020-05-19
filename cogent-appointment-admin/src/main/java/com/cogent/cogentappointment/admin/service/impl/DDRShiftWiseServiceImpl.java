package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage.DDRSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.*;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override.DDROverrideBreakUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override.DDROverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.shift.DDRShiftUpdateDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.shift.DDRShiftUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.DDRTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.*;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDRDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDROverrideBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDRResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDRShiftDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail.DDRWeekDaysResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.update.DDROverrideUpdateResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DDRShiftWiseMessages.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRBreakDetailUtils.parseToDDRBreakDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRDateValidationUtils.validateBreakTimeOverlap;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRDateValidationUtils.validateTimeOverlap;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDROverrideBreakDetailUtils.parseToDDROverrideBreakDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDROverrideBreakDetailUtils.parseToUpdatedDDROverrideBreakDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDROverrideDetailUtils.parseToDdrOverrideDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDROverrideDetailUtils.parseToOverrideUpdateResponse;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRShiftDetailUtils.parseToDDRShiftDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRShiftDetailUtils.parseToUpdatedDDRShiftDetail;
import static com.cogent.cogentappointment.admin.utils.ddrShiftWise.DDRShiftWiseUtils.*;
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

    private final AppointmentRepository appointmentRepository;

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
                                   DDROverrideBreakDetailRepository ddrOverrideBreakDetailRepository,
                                   AppointmentRepository appointmentRepository) {
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
        this.appointmentRepository = appointmentRepository;
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
                ddrShiftDetailRepository.fetchMinShiftInfo(ddrId);

        List<DDROverrideDetailResponseDTO> overrideDetail = new ArrayList<>();

        if (hasDDROverride.equals(YES))
            overrideDetail = ddrOverrideDetailRepository.fetchDDROverrideDetail(ddrId);

        DDRExistingDetailResponseDTO responseDTO =
                parseToDDRExistingDetailResponseDTO(hasDDROverride, shiftDetail, overrideDetail);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DDRExistingWeekDaysResponseDTO> fetchExistingDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_WEEK_DAYS);

        List<DDRExistingWeekDaysResponseDTO> weekDaysDetail =
                ddrWeekDaysDetailRepository.fetchExistingDDRWeekDaysDetail(requestDTO);

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
    public List<DDRBreakDetailResponseDTO> fetchExistingOverrideBreakDetail(Long ddrOverrideId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_OVERRIDE_BREAK_DETAIL);

        List<DDRBreakDetailResponseDTO> breakDetails =
                ddrOverrideBreakDetailRepository.fetchExistingOverrideBreakDetail(ddrOverrideId);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_OVERRIDE_BREAK_DETAIL, getDifferenceBetweenTwoTime(startTime));

        return breakDetails;
    }

    @Override
    public List<DDRMinResponseDTO> search(DDRSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, DDR_SHIFT_WISE);

        List<DDRMinResponseDTO> minInfo = ddrShiftWiseRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DDR_SHIFT_WISE);

        DoctorDutyRosterShiftWise doctorDutyRoster = fetchDDRShiftWise(deleteRequestDTO.getId());

        validateAppointmentCount(doctorDutyRoster.getFromDate(),
                doctorDutyRoster.getToDate(),
                doctorDutyRoster.getDoctor().getId(),
                doctorDutyRoster.getSpecialization().getId()
        );

        parseDDRDeleteStatus(doctorDutyRoster, deleteRequestDTO);

        save(doctorDutyRoster);

        log.info(DELETING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public DDRDetailResponseDTO fetchDetailsById(Long ddrId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DDR_SHIFT_WISE);

        DDRResponseDTO ddrDetail = ddrShiftWiseRepository.fetchDDRDetail(ddrId);

        List<DDRShiftDetailResponseDTO> shiftDetail =
                ddrShiftDetailRepository.fetchShiftDetails(ddrId);

        List<DDROverrideDetailResponseDTO> overrideDetail = new ArrayList<>();

        if (ddrDetail.getHasOverride().equals(YES))
            overrideDetail = ddrOverrideDetailRepository.fetchDDROverrideDetail(ddrId);

        DDRDetailResponseDTO detailResponseDTO =
                parseToDdrDetailResponseDTO(ddrDetail, shiftDetail, overrideDetail);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));

        return detailResponseDTO;
    }

    @Override
    public List<DDRWeekDaysResponseDTO> fetchDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_WEEK_DAYS);

        List<DDRWeekDaysResponseDTO> weekDaysDetail =
                ddrWeekDaysDetailRepository.fetchDDRWeekDaysDetail(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_WEEK_DAYS, getDifferenceBetweenTwoTime(startTime));

        return weekDaysDetail;
    }

    @Override
    public List<DDROverrideBreakDetailResponseDTO> fetchDDROverrideBreakDetail(Long ddrOverrideDetailId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DDR_OVERRIDE_BREAK_DETAIL);

        List<DDROverrideBreakDetailResponseDTO> breakDetails =
                ddrOverrideBreakDetailRepository.fetchOverrideBreakDetail(ddrOverrideDetailId);

        log.info(FETCHING_PROCESS_COMPLETED, DDR_OVERRIDE_BREAK_DETAIL, getDifferenceBetweenTwoTime(startTime));

        return breakDetails;
    }

    /* REQUIRED VALIDATIONS:
    *  1. OVERRIDE DATE MUST BE BETWEEN ACTUAL DDR
    *  2. OVERRIDE END TIME MUST BE GREATER THAN END TIME
    *  3. REQUESTED SHIFT MUST BE ASSIGNED TO THE SELECTED DDR
    *  4. OVERRIDE CAN BE UPDATED ONLY IF NO APPOINTMENT EXISTS ON THAT DATE
    *  5. REQUESTED TIME SCHEDULES SHOULD NOT OVERLAP WITH EXISTING OVERRIDE TIME SCHEDULES
    *
    *  SAVED OVERRIDE ID IS RETURNED AS A RESPONSE SUCH THAT FRONT-END DO NOT NEED TO HIT API
    *  TO FETCH OVERRIDE DETAILS AGAIN.
    *  */
    @Override
    public DDROverrideUpdateResponseDTO updateDoctorDutyRosterOverride(DDROverrideUpdateRequestDTO updateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DDR_OVERRIDE);

        DoctorDutyRosterShiftWise ddrShiftWise = fetchDDRShiftWise(updateRequestDTO.getDdrId());

        updateDDROverrideStatus(ddrShiftWise, updateRequestDTO.getHasOverride());

        validateDDROverrideUpdateRequestInfo(ddrShiftWise, updateRequestDTO);

        DDROverrideDetail ddrOverrideDetail = saveOrUpdateDDROverrideDetail(updateRequestDTO, ddrShiftWise);

        if (!ObjectUtils.isEmpty(updateRequestDTO.getBreakDetail()))
            saveOrUpdateOverrideBreakDetails(
                    updateRequestDTO.getBreakDetail(),
                    ddrOverrideDetail,
                    Objects.isNull(updateRequestDTO.getDdrOverrideId()) ? YES : NO
            );

        DDROverrideUpdateResponseDTO updateResponse = parseToOverrideUpdateResponse(ddrOverrideDetail.getId());

        log.info(UPDATING_PROCESS_COMPLETED, DDR_OVERRIDE, getDifferenceBetweenTwoTime(startTime));

        return updateResponse;
    }

    /*1. If new shifts are saved while updating DDR,
    *   a. Validate if that requested shifts are not previously assigned
    *   b. Validate if requested shifts are assigned to the doctor
     * 2. If existing shifts are updated while updating DDR,
      * a. Validate if that shiftDetailId is valid and simply update roster gap duration & status*/
    @Override
    public void updateDDRShiftDetail(DDRShiftUpdateRequestDTO shiftUpdateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DDR_SHIFT_WISE);

        DoctorDutyRosterShiftWise ddrShiftWise = fetchDDRShiftWise(shiftUpdateRequestDTO.getDdrId());

        List<DDRShiftDetail> existingShiftDetails =
                ddrShiftDetailRepository.fetchByDDRId(shiftUpdateRequestDTO.getDdrId());

        shiftUpdateRequestDTO.getShiftDetail().forEach(
                updateRequestDTO -> {

                    if (Objects.isNull(updateRequestDTO.getDdrShiftDetailId())) {
                        validateUpdatedShiftRequestInfo(
                                existingShiftDetails,
                                updateRequestDTO.getShiftId(),
                                ddrShiftWise.getDoctor().getId());

                        saveDDRShiftDetail(
                                ddrShiftWise,
                                updateRequestDTO.getShiftId(),
                                updateRequestDTO.getRosterGapDuration(),
                                updateRequestDTO.getStatus()
                        );
                    } else
                        updateDDRShiftDetail(existingShiftDetails, updateRequestDTO);
                }
        );

        log.info(UPDATING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));
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
            throw new DataDuplicationException(DUPLICATE_SHIFT_MESSAGE);

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

                    DDRShiftDetail ddrShiftDetail = saveDDRShiftDetail(
                            ddrShiftWise,
                            shiftDetail.getShiftId(),
                            shiftDetail.getRosterGapDuration(),
                            shiftDetail.getStatus()
                    );

                    saveDDRWeekDaysDetail(shiftDetail.getWeekDaysDetail(), ddrShiftDetail, ddrWeekDaysDetails);
                }
        );
    }

    private DDRShiftDetail saveDDRShiftDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                              Long shiftId,
                                              Integer rosterGapDuration,
                                              Character status) {

        Shift shift = fetchShift(shiftId, ddrShiftWise.getHospital().getId());

        DDRShiftDetail ddrShiftDetail = parseToDDRShiftDetail(ddrShiftWise, shift, rosterGapDuration, status);

        return saveDDRShiftDetail(ddrShiftDetail);
    }

    private DDRShiftDetail saveDDRShiftDetail(DDRShiftDetail ddrShiftDetail) {
        return ddrShiftDetailRepository.save(ddrShiftDetail);
    }

    private void saveDDRWeekDaysDetail(List<DDRWeekDaysDetailRequestDTO> weekDaysRequestDTOS,
                                       DDRShiftDetail ddrShiftDetail,
                                       List<DDRWeekDaysDetail> ddrWeekDaysDetails) {

        weekDaysRequestDTOS.forEach(
                (DDRWeekDaysDetailRequestDTO weekDaysRequestDTO) -> {

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

    private DDRWeekDaysDetail saveWeekDaysDetail(DDRWeekDaysDetailRequestDTO weekDaysRequestDTO,
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
    private void validateDDRWeekDaysDetail(DDRWeekDaysDetailRequestDTO weekDaysRequestDTO,
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

    private void saveDDRBreakDetail(List<DDRBreakDetailRequestDTO> breakRequestDTOS,
                                    DDRWeekDaysDetail ddrWeekDaysDetail,
                                    Long hospitalId) {

        List<DDRBreakDetail> ddrBreakDetails = new ArrayList<>();

        breakRequestDTOS.forEach((DDRBreakDetailRequestDTO ddrBreakDetailRequestDTO) -> {

            validateDDRBreakInfo(ddrWeekDaysDetail, ddrBreakDetails, ddrBreakDetailRequestDTO);

            BreakType breakType = fetchBreakType(ddrBreakDetailRequestDTO.getBreakTypeId(), hospitalId);

            DDRBreakDetail ddrBreakDetail = parseToDDRBreakDetail(ddrBreakDetailRequestDTO, ddrWeekDaysDetail, breakType);

            ddrBreakDetails.add(ddrBreakDetail);
        });

        saveDDRBreakDetail(ddrBreakDetails);
    }

    private void validateDDRBreakInfo(DDRWeekDaysDetail ddrWeekDaysDetail,
                                      List<DDRBreakDetail> ddrBreakDetails,
                                      DDRBreakDetailRequestDTO ddrBreakDetailRequestDTO) {

        validateIfStartTimeGreater(ddrBreakDetailRequestDTO.getStartTime(), ddrBreakDetailRequestDTO.getEndTime());

        validateIfDDRBreakIsBetweenWeekDaysTime(ddrWeekDaysDetail, ddrBreakDetailRequestDTO);

        if (ddrBreakDetails.size() >= 1)
            validateDDRBreakTimeDuplicity(ddrBreakDetails, ddrBreakDetailRequestDTO);
    }

    private void validateIfDDRBreakIsBetweenWeekDaysTime(DDRWeekDaysDetail ddrWeekDaysDetail,
                                                         DDRBreakDetailRequestDTO breakRequestDTO) {

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
                                               DDRBreakDetailRequestDTO breakRequestDTO) {

        breakDetails.forEach(breakDetail -> validateBreakTimeDuplicity(
                breakDetail.getStartTime(),
                breakDetail.getEndTime(),
                breakRequestDTO.getStartTime(),
                breakRequestDTO.getEndTime()
                )
        );
    }

    private void saveDDROverrideDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                       List<DDROverrideDetailRequestDTO> overrideRequestDTOS) {

        validateOverrideShiftDetail(overrideRequestDTOS, ddrShiftWise.getId());

        List<DDROverrideDetail> overrideDetails = new ArrayList<>();

        overrideRequestDTOS.forEach(
                (DDROverrideDetailRequestDTO overrideRequestDTO) -> {

                    DDROverrideDetail ddrOverrideDetail = saveDDROverrideDetail(
                            ddrShiftWise,
                            overrideRequestDTO,
                            overrideDetails
                    );

                    if (!overrideRequestDTO.getBreakDetail().isEmpty())
                        saveDDROverrideBreakDetail(
                                overrideRequestDTO.getBreakDetail(),
                                ddrOverrideDetail,
                                ddrShiftWise.getHospital().getId()
                        );
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

    private void saveDDROverrideBreakDetail(List<DDRBreakDetailRequestDTO> breakRequestDTOS,
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

        validateIfOverrideDateIsBetweenDDR(
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
                            log.error(String.format(OVERRIDE_TIME_OVERLAP_MESSAGE,
                                    matchedResult.getShift().getName()));
                            throw new BadRequestException(String.format(OVERRIDE_TIME_OVERLAP_MESSAGE,
                                    matchedResult.getShift().getName()));
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
                                                     DDRBreakDetailRequestDTO breakRequestDTO) {

        validateIfStartTimeGreater(breakRequestDTO.getStartTime(), breakRequestDTO.getEndTime());

        validateIfOverrideBreakIsBetweenOverrideTime(
                overrideDetail,
                breakRequestDTO.getStartTime(),
                breakRequestDTO.getEndTime()
        );

        if (overrideBreakDetails.size() >= 1)
            validateOverrideBreakTimeDuplicity(overrideBreakDetails, breakRequestDTO);
    }

    private void validateIfOverrideBreakIsBetweenOverrideTime(DDROverrideDetail overrideDetail,
                                                              Date breakStartTime,
                                                              Date breakEndTime) {

        boolean isTimeOverlapped = validateBreakTimeOverlap(
                overrideDetail.getStartTime(),
                overrideDetail.getEndTime(),
                breakStartTime,
                breakEndTime
        );

        if (!isTimeOverlapped) {
            log.error(INVALID_BREAK_OVERRIDE_REQUEST);
            throw new BadRequestException(INVALID_BREAK_OVERRIDE_REQUEST);
        }
    }

    private void validateOverrideBreakTimeDuplicity(List<DDROverrideBreakDetail> overrideBreakDetails,
                                                    DDRBreakDetailRequestDTO breakRequestDTO) {

        overrideBreakDetails.forEach(overrideBreakDetail ->
                validateBreakTimeDuplicity(
                        overrideBreakDetail.getStartTime(),
                        overrideBreakDetail.getEndTime(),
                        breakRequestDTO.getStartTime(),
                        breakRequestDTO.getEndTime()
                )
        );
    }

    /*VALIDATE IF ALL REQUESTED SHIFTS ARE ASSIGNED TO DOCTOR
    *
    * 1. FETCH ACTUAL ASSIGNED DOCTOR SHIFTS
    * EG. COUNT = 2
    *
    * 2. IF REQUESTED COUNT !=ASSIGNED SHIFTS COUNT, MEANS INVALID SHIFT IDS ARE REQUESTED*/
    private void validateOverrideShiftDetail(List<DDROverrideDetailRequestDTO> overrideDetail,
                                             Long ddrId) {

        List<Long> requestedShiftIds = overrideDetail
                .stream().map(DDROverrideDetailRequestDTO::getShiftId)
                .distinct()
                .collect(Collectors.toList());

        Long assignedDDRShifts = ddrShiftDetailRepository.validateDDRShiftCount(requestedShiftIds, ddrId);

        if (assignedDDRShifts != requestedShiftIds.size())
            throw new BadRequestException(INVALID_DDR_SHIFT_REQUEST_MESSAGE);
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

    private void validateIfOverrideDateIsBetweenDDR(Date dutyRosterFromDate,
                                                    Date dutyRosterToDate,
                                                    Date overrideDate) {

        boolean isDateBetweenInclusive =
                isDateBetweenInclusive(dutyRosterFromDate, dutyRosterToDate, removeTime(overrideDate));

        if (!isDateBetweenInclusive) {
            log.error(String.format(INVALID_OVERRIDE_REQUEST_MESSAGE, dutyRosterFromDate, dutyRosterToDate));
            throw new BadRequestException(
                    String.format(INVALID_OVERRIDE_REQUEST_MESSAGE, dutyRosterFromDate, dutyRosterToDate));
        }
    }

    private void saveDDROverrideDetail(DDROverrideDetail overrideDetails) {
        ddrOverrideDetailRepository.save(overrideDetails);
    }

    private void validateAppointmentCount(Date fromDate, Date toDate,
                                          Long doctorId, Long specializationId) {

        Long appointments = appointmentRepository.fetchBookedAppointmentCount(
                fromDate, toDate, doctorId, specializationId);

        if (appointments.intValue() > 0) {
            log.error(APPOINTMENT_EXISTS_MESSAGE);
            throw new BadRequestException(APPOINTMENT_EXISTS_MESSAGE);
        }
    }

    private void validateDDROverrideUpdateRequestInfo(DoctorDutyRosterShiftWise ddrShiftWise,
                                                      DDROverrideUpdateRequestDTO overrideRequestDTO) {

        validateIfOverrideDateIsBetweenDDR(
                ddrShiftWise.getFromDate(),
                ddrShiftWise.getToDate(),
                overrideRequestDTO.getDate()
        );

        validateIfStartTimeGreater(
                overrideRequestDTO.getStartTime(),
                overrideRequestDTO.getEndTime()
        );

        validateUpdatedOverrideShifts(overrideRequestDTO.getShiftId(),
                ddrShiftWise.getId());

         /*UPDATE IS ALLOWED ONLY IF THERE ARE NO APPOINTMENTS WITHIN THAT RANGE*/
        validateAppointmentCount(overrideRequestDTO.getDate(),
                ddrShiftWise.getDoctor().getId(),
                ddrShiftWise.getSpecialization().getId()
        );
    }

    private void validateUpdatedOverrideShifts(Long shiftId, Long ddrId) {

        Long isAssignedShiftCount = ddrShiftDetailRepository.validateDDRShiftCount(
                Collections.singletonList(shiftId), ddrId);

        if (isAssignedShiftCount <= 0)
            throw new BadRequestException(INVALID_DDR_SHIFT_REQUEST_MESSAGE);
    }

    private void validateAppointmentCount(Date date, Long doctorId, Long specializationId) {

        Long appointments = appointmentRepository.fetchBookedAppointmentCount(date, doctorId, specializationId);

        if (appointments.intValue() > 0) {
            log.error(APPOINTMENT_EXISTS_MESSAGE);
            throw new BadRequestException(APPOINTMENT_EXISTS_MESSAGE);
        }
    }

    private Function<Long, NoContentFoundException> DDR_OVERRIDE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DDR_OVERRIDE, id);
        throw new NoContentFoundException(DDROverrideDetail.class, "id", id.toString());
    };

    private void validateUpdatedOverrideTimeDuplicity(List<DDRTimeResponseDTO> existingOverrideRosters,
                                                      DDROverrideUpdateRequestDTO updateRequestDTO) {

        existingOverrideRosters
                .forEach((DDRTimeResponseDTO existingOverrideRoster) -> {
                            boolean isTimeOverlapped = validateTimeOverlap(
                                    existingOverrideRoster.getStartTime(),
                                    existingOverrideRoster.getEndTime(),
                                    updateRequestDTO.getStartTime(),
                                    updateRequestDTO.getEndTime()
                            );

                            if (isTimeOverlapped) {
                                log.error(String.format(OVERRIDE_TIME_OVERLAP_MESSAGE, existingOverrideRoster.getShiftName()));
                                throw new BadRequestException(
                                        String.format(OVERRIDE_TIME_OVERLAP_MESSAGE, existingOverrideRoster.getShiftName()));
                            }
                        }
                );
    }

    private DDROverrideDetail saveOrUpdateDDROverrideDetail(DDROverrideUpdateRequestDTO updateRequestDTO,
                                                            DoctorDutyRosterShiftWise ddrShiftWise) {

        if (Objects.isNull(updateRequestDTO.getDdrOverrideId())) {

            /*SAVE NEW DOCTOR DUTY ROSTER OVERRIDE*/
            List<DDRTimeResponseDTO> existingOverrideRosters =
                    ddrOverrideDetailRepository.fetchDDROverrideTimeDetails(
                            updateRequestDTO.getDate(),
                            ddrShiftWise.getDoctor().getId(),
                            ddrShiftWise.getSpecialization().getId()
                    );

            if (existingOverrideRosters.size() >= 1)
                validateUpdatedOverrideTimeDuplicity(existingOverrideRosters, updateRequestDTO);

            return saveNewOverrideDetail(updateRequestDTO, ddrShiftWise);

        } else {

             /*UPDATE DOCTOR DUTY ROSTER OVERRIDE*/
            List<DDRTimeResponseDTO> existingOverrideRosters =
                    ddrOverrideDetailRepository.fetchDDROverrideTimeDetailsExceptCurrentId(
                            updateRequestDTO.getDdrOverrideId(),
                            updateRequestDTO.getDate(),
                            ddrShiftWise.getDoctor().getId(),
                            ddrShiftWise.getSpecialization().getId()
                    );

            if (existingOverrideRosters.size() >= 1)
                validateUpdatedOverrideTimeDuplicity(existingOverrideRosters, updateRequestDTO);

            return updateDDROverrideDetail(updateRequestDTO, ddrShiftWise.getHospital().getId());
        }
    }

    private DDROverrideDetail saveNewOverrideDetail(DDROverrideUpdateRequestDTO updateRequestDTO,
                                                    DoctorDutyRosterShiftWise ddrShiftWise) {

        Shift shift = fetchShift(updateRequestDTO.getShiftId(), ddrShiftWise.getHospital().getId());

        DDROverrideDetail ddrOverrideDetail =
                parseToDdrOverrideDetail(updateRequestDTO, shift, new DDROverrideDetail());

        ddrOverrideDetail.setDdrShiftWise(ddrShiftWise);

        saveDDROverrideDetail(ddrOverrideDetail);

        return ddrOverrideDetail;
    }

    private DDROverrideDetail updateDDROverrideDetail(DDROverrideUpdateRequestDTO updateRequestDTO,
                                                      Long hospitalId) {

        DDROverrideDetail ddrOverrideDetail =
                ddrOverrideDetailRepository.fetchById(updateRequestDTO.getDdrOverrideId())
                        .orElseThrow(() -> DDR_OVERRIDE_WITH_GIVEN_ID_NOT_FOUND.apply(updateRequestDTO.getDdrOverrideId()));

        Shift shift = fetchShift(updateRequestDTO.getShiftId(), hospitalId);

        saveDDROverrideDetail(parseToDdrOverrideDetail(updateRequestDTO, shift, ddrOverrideDetail));

        return ddrOverrideDetail;
    }


    /* EITHER SAVE NEW BREAK DETAIL OR UPDATE EXISTING BREAK DETAIL

    1. FETCH ALREADY ASSIGNED BREAK SCHEDULES
    2. IF ddrBreakId IN REQUEST IS NULL,
        VALIDATE REQUESTED TIME DETAILS WITH EACH OF (1).
        IF IT IS FINE, SAVE IT & ADD TO (1) LIST SINCE IT WILL BE USED FOR COMPARING REST OF BREAK REQUESTS
    3. IF ddrBreakId IN REQUEST IS NOT NULL,
         VALIDATE REQUESTED TIME DETAILS WITH (EACH OF (1) - REQUESTED ID)
        IF IT IS FINE SIMPLY UPDATE THE REQUESTED DETAILS OF CORRESPONDING 'ddrBreakId'
      */
    private void saveOrUpdateOverrideBreakDetails(List<DDROverrideBreakUpdateRequestDTO> breakUpdateRequestDTOS,
                                                  DDROverrideDetail ddrOverrideDetail,
                                                  Character isNewOverrideRequest) {

        List<DDROverrideBreakDetail> existingBreakDetails =
                isNewOverrideRequest.equals(YES) ? new ArrayList<>() :
                        ddrOverrideBreakDetailRepository.fetchByDDROverrideId(ddrOverrideDetail.getId());

        breakUpdateRequestDTOS
                .forEach(breakUpdateRequestDTO -> {

                    validatedUpdateBreakRequestInfo(breakUpdateRequestDTO, ddrOverrideDetail);

                    if (Objects.isNull(breakUpdateRequestDTO.getDdrBreakId())) {

                        DDROverrideBreakDetail ddrOverrideBreakDetail = saveNewOverrideBreakDetail(
                                breakUpdateRequestDTO,
                                ddrOverrideDetail,
                                existingBreakDetails
                        );

                        existingBreakDetails.add(ddrOverrideBreakDetail);

                    } else {

                        updateOverrideBreakDetail(
                                breakUpdateRequestDTO,
                                ddrOverrideDetail,
                                existingBreakDetails
                        );
                    }
                });
    }

    private void validatedUpdateBreakRequestInfo(DDROverrideBreakUpdateRequestDTO breakRequestDTO,
                                                 DDROverrideDetail ddrOverrideDetail) {

        validateIfStartTimeGreater(breakRequestDTO.getStartTime(), breakRequestDTO.getEndTime());

        validateIfOverrideBreakIsBetweenOverrideTime(
                ddrOverrideDetail,
                breakRequestDTO.getStartTime(),
                breakRequestDTO.getEndTime()
        );
    }

    private void validateBreakTimeDuplicity(Date compareStartTime,
                                            Date compareEndTime,
                                            Date requestStartTime,
                                            Date requestEndTime) {

        boolean isTimeOverlapped = validateTimeOverlap(
                compareStartTime,
                compareEndTime,
                requestStartTime,
                requestEndTime
        );

        if (isTimeOverlapped) {
            log.error(BREAK_TIME_OVERLAP_MESSAGE);
            throw new BadRequestException(BREAK_TIME_OVERLAP_MESSAGE);
        }
    }

    private DDROverrideBreakDetail saveNewOverrideBreakDetail(DDROverrideBreakUpdateRequestDTO breakUpdateRequestDTO,
                                                              DDROverrideDetail ddrOverrideDetail,
                                                              List<DDROverrideBreakDetail> existingBreakDetails) {

        if (existingBreakDetails.size() >= 1) {
            existingBreakDetails
                    .forEach(existing ->
                            validateBreakTimeDuplicity(
                                    existing.getStartTime(),
                                    existing.getEndTime(),
                                    breakUpdateRequestDTO.getStartTime(),
                                    breakUpdateRequestDTO.getEndTime()
                            ));
        }

        DDROverrideBreakDetail ddrOverrideBreakDetail =
                parseToUpdatedDDROverrideBreakDetail(
                        breakUpdateRequestDTO,
                        ddrOverrideDetail,
                        fetchBreakType(breakUpdateRequestDTO.getBreakTypeId(),
                                ddrOverrideDetail.getDdrShiftWise().getHospital().getId()),
                        new DDROverrideBreakDetail()
                );

        return ddrOverrideBreakDetailRepository.save(ddrOverrideBreakDetail);
    }

    private void updateOverrideBreakDetail(DDROverrideBreakUpdateRequestDTO breakUpdateRequestDTO,
                                           DDROverrideDetail ddrOverrideDetail,
                                           List<DDROverrideBreakDetail> existingBreakDetails) {

        existingBreakDetails.stream()
                .filter(existing -> !(existing.getId().equals(breakUpdateRequestDTO.getDdrBreakId()))
                )
                .forEach(unmatched -> {
                    validateBreakTimeDuplicity(
                            unmatched.getStartTime(),
                            unmatched.getEndTime(),
                            breakUpdateRequestDTO.getStartTime(),
                            breakUpdateRequestDTO.getEndTime()
                    );
                });

        Optional<DDROverrideBreakDetail> matchingObject =
                Optional.of(existingBreakDetails
                        .stream()
                        .filter(existing -> existing.getId().equals(breakUpdateRequestDTO.getDdrBreakId()))
                        .findAny()
                        .orElseThrow(() -> DDR_BREAK_DETAIL_NOT_FOUND.apply(breakUpdateRequestDTO.getDdrBreakId())));

        parseToUpdatedDDROverrideBreakDetail(breakUpdateRequestDTO,
                ddrOverrideDetail,
                fetchBreakType(breakUpdateRequestDTO.getBreakTypeId(),
                        ddrOverrideDetail.getDdrShiftWise().getHospital().getId()),
                matchingObject.get()
        );
    }

    private Function<Long, NoContentFoundException> DDR_BREAK_DETAIL_NOT_FOUND = (ddrBreakId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DDR_OVERRIDE_BREAK_DETAIL, ddrBreakId);
        throw new NoContentFoundException(DDROverrideBreakDetail.class, "ddrBreakId", ddrBreakId.toString());
    };

    private Function<Long, NoContentFoundException> DDR_SHIFT_DETAIL_NOT_FOUND = (ddrShiftDetailId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DDRShiftDetail.class, ddrShiftDetailId);
        throw new NoContentFoundException(DDRShiftDetail.class, "ddrShiftDetailId", ddrShiftDetailId.toString());
    };

    private void validateUpdatedShiftRequestInfo(List<DDRShiftDetail> existingShiftDetails,
                                                 Long shiftId, Long doctorId) {

        DDRShiftDetail matched = existingShiftDetails.stream()
                .filter(existing -> existing.getShift().getId().equals(shiftId))
                .findAny()
                .orElse(null);

        if (!Objects.isNull(matched))
            throw new DataDuplicationException(
                    String.format(DUPLICATE_SHIFT_ASSIGNMENT_MESSAGE, matched.getShift().getName()));

        Long assignedShiftCount =
                doctorService.validateDoctorShiftCount(Collections.singletonList(shiftId), doctorId);

        if (assignedShiftCount <= 0) throw new BadRequestException(INVALID_SHIFT_REQUEST_MESSAGE);
    }

    private void updateDDRShiftDetail(List<DDRShiftDetail> existingShiftDetails,
                                      DDRShiftUpdateDTO updateRequestDTO) {

        DDRShiftDetail ddrShiftDetail = existingShiftDetails.stream()
                .filter(existing -> existing.getId().equals(updateRequestDTO.getDdrShiftDetailId()))
                .findAny()
                .orElseThrow(() -> DDR_SHIFT_DETAIL_NOT_FOUND.apply(updateRequestDTO.getDdrShiftDetailId()));

        parseToUpdatedDDRShiftDetail(ddrShiftDetail,
                updateRequestDTO.getRosterGapDuration(),
                updateRequestDTO.getStatus());

        saveDDRShiftDetail(ddrShiftDetail);
    }
}
