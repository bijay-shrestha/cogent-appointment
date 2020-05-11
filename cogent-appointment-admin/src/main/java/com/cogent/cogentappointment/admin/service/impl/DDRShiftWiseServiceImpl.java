package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.*;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.repository.DDRBreakDetailRepository;
import com.cogent.cogentappointment.admin.repository.DDRShiftDetailRepository;
import com.cogent.cogentappointment.admin.repository.DDRShiftWiseRepository;
import com.cogent.cogentappointment.admin.repository.DDRWeekDaysDetailRepository;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DDRShiftWiseMessages.DUPLICATE_SHIFT_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DDRShiftWiseMessages.INVALID_SHIFT_REQUEST_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DoctorDutyRosterServiceMessages.DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.admin.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_SHIFT_WISE;
import static com.cogent.cogentappointment.admin.utils.DDRBreakDetailUtils.parseToDDRBreakDetail;
import static com.cogent.cogentappointment.admin.utils.DDRShiftWiseUtils.parseToDDRShiftWise;
import static com.cogent.cogentappointment.admin.utils.DDRWeekDaysUtils.parseToDDRShiftDetail;
import static com.cogent.cogentappointment.admin.utils.DDRWeekDaysUtils.parseToDDRWeekDaysDetail;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

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
                                   BreakTypeService breakTypeService) {
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
    }

    @Override
    public void save(@Valid DDRRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DDR_SHIFT_WISE);

        validateDDRRequestInfo(requestDTO);

        DoctorDutyRosterShiftWise ddrShiftWise = saveDDRShiftWise(requestDTO.getDdrDetail());

        saveDDRWeekDaysDetail(ddrShiftWise, requestDTO.getShiftDetail());

        log.info(SAVING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));

    }

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

    private DoctorDutyRosterShiftWise saveDDRShiftWise(DDRDetailRequestDTO ddrDetail) {

        Hospital hospital = fetchHospitalById(ddrDetail.getHospitalId());

        Specialization specialization = fetchSpecializationById(ddrDetail.getSpecializationId());

        Doctor doctor = fetchDoctorById(ddrDetail.getDoctorId());

        DoctorDutyRosterShiftWise ddrShiftWise =
                parseToDDRShiftWise(ddrDetail, hospital, specialization, doctor);

        return save(ddrShiftWise);
    }

    private void saveDDRWeekDaysDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                       List<DDRShiftRequestDTO> shiftRequestDTO) {

        shiftRequestDTO.forEach(
                shiftDetail -> {
                    Shift shift = fetchShift(
                            shiftDetail.getShiftId(),
                            ddrShiftWise.getHospital().getId()
                    );

                    DDRShiftDetail ddrShiftDetail =
                            saveDDRShiftDetail(ddrShiftWise, shift);

                    saveDDRWeekDaysDetail(shiftDetail.getWeekDaysDetail(), ddrShiftDetail);
                }
        );
    }

    private DDRShiftDetail saveDDRShiftDetail(DoctorDutyRosterShiftWise ddrShiftWise,
                                              Shift shift) {

        DDRShiftDetail ddrShiftDetail = parseToDDRShiftDetail(ddrShiftWise, shift);

        return ddrShiftDetailRepository.save(ddrShiftDetail);
    }

    private void saveDDRWeekDaysDetail(List<DDRWeekDaysRequestDTO> weekDaysRequestDTOS,
                                       DDRShiftDetail ddrShiftDetail) {

        weekDaysRequestDTOS.forEach(
                weekDaysRequestDTO -> {

                    DDRWeekDaysDetail weekDaysDetail = saveDDRWeekDaysDetail(weekDaysRequestDTO, ddrShiftDetail);

                    if (weekDaysRequestDTO.getBreakDetail().size() > 0) {
                        saveDDRBreakDetail(
                                weekDaysRequestDTO.getBreakDetail(),
                                weekDaysDetail,
                                ddrShiftDetail.getDdrShiftWise().getHospital().getId()
                        );
                    }
                }
        );
    }

    private DDRWeekDaysDetail saveDDRWeekDaysDetail(DDRWeekDaysRequestDTO weekDaysRequestDTO,
                                                    DDRShiftDetail ddrShiftDetail) {

        validateIsStartTimeGreater(weekDaysRequestDTO.getStartTime(), weekDaysRequestDTO.getEndTime());

        WeekDays weekDays = findWeekDaysById(weekDaysRequestDTO.getWeekDaysId());

        DDRWeekDaysDetail weekDaysDetail =
                parseToDDRWeekDaysDetail(weekDaysRequestDTO, ddrShiftDetail, weekDays);

        return saveDDRWeekDaysDetail(weekDaysDetail);
    }

    private void saveDDRBreakDetail(List<DDRBreakRequestDTO> breakRequestDTOS,
                                    DDRWeekDaysDetail ddrWeekDaysDetail,
                                    Long hospitalId) {

        List<DDRBreakDetail> ddrBreakDetails = breakRequestDTOS.stream()
                .map(ddrBreakRequestDTO -> {

                    validateIsStartTimeGreater(ddrBreakRequestDTO.getStartTime(), ddrBreakRequestDTO.getEndTime());

                    BreakType breakType = fetchBreakType(ddrBreakRequestDTO.getBreakTypeId(), hospitalId);

                    return parseToDDRBreakDetail(ddrBreakRequestDTO, ddrWeekDaysDetail, breakType);

                }).collect(Collectors.toList());

        saveDDRBreakDetail(ddrBreakDetails);
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

    private DDRWeekDaysDetail saveDDRWeekDaysDetail(DDRWeekDaysDetail ddrWeekDaysDetail) {
        return ddrWeekDaysDetailRepository.save(ddrWeekDaysDetail);
    }

    private void saveDDRBreakDetail(List<DDRBreakDetail> ddrBreakDetails) {
        ddrBreakDetailRepository.saveAll(ddrBreakDetails);
    }

    private BreakType fetchBreakType(Long breakTypeId, Long hospitalId) {
        return breakTypeService.fetchActiveBreakTypeByIdAndHospitalId(breakTypeId, hospitalId);
    }

}
