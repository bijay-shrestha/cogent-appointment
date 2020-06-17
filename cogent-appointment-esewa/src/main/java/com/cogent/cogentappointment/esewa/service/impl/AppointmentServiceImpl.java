package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.commons.utils.NepaliDateUtility;
import com.cogent.cogentappointment.esewa.dto.request.appointment.appointmentTxnStatus.AppointmentTransactionStatusRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentHistorySearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentTransactionRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestForDTO;
import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appointmentTxnStatus.AppointmentTransactionStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.cancel.AppointmentCancelResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.*;
import com.cogent.cogentappointment.esewa.dto.response.appointment.save.AppointmentSuccessResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.exception.DataDuplicationException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.*;
import com.cogent.cogentappointment.esewa.service.*;
import com.cogent.cogentappointment.esewa.utils.AppointmentUtils;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.commons.utils.NepaliDateUtility.formatToDateString;
import static com.cogent.cogentappointment.esewa.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.esewa.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.esewa.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentHospitalDepartmentMessage.HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE_INVALID;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentHospitalDepartmentMessage.HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.*;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_APPOINTMENT_CHARGE_INVALID;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.*;
import static com.cogent.cogentappointment.esewa.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentModeLog.APPOINTMENT_MODE;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentReservationLog.APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentServiceTypeLog.APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.log.constants.PatientLog.PATIENT;
import static com.cogent.cogentappointment.esewa.utils.AppointmentFollowUpLogUtils.parseToAppointmentFollowUpLog;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentFollowUpLogUtils.parseToAppointmentHospitalDepartmentFollowUpLog;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentUtils.validateIfRequestedAppointmentTimeIsValid;
import static com.cogent.cogentappointment.esewa.utils.AppointmentTransactionDetailUtils.parseToAppointmentTransactionInfo;
import static com.cogent.cogentappointment.esewa.utils.AppointmentTransactionRequestLogUtils.parseToAppointmentTransactionStatusResponseDTO;
import static com.cogent.cogentappointment.esewa.utils.AppointmentTransactionRequestLogUtils.updateAppointmentTransactionRequestLog;
import static com.cogent.cogentappointment.esewa.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti on 2019-10-22
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final SpecializationService specializationService;

    private final AppointmentRepository appointmentRepository;

    private final DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final HospitalService hospitalService;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentRescheduleLogRepository appointmentRescheduleLogRepository;

    private final AppointmentFollowUpLogRepository appointmentFollowUpLogRepository;

    private final AppointmentReservationLogRepository appointmentReservationLogRepository;

    private final AppointmentFollowUpTrackerService appointmentFollowUpTrackerService;

    private final HospitalPatientInfoService hospitalPatientInfoService;

    private final PatientMetaInfoService patientMetaInfoService;

    private final PatientRelationInfoService patientRelationInfoService;

    private final PatientRelationInfoRepository patientRelationInfoRepository;

    private final AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService;

    private final AppointmentTransactionRequestLogService appointmentTransactionRequestLogService;

    private final AppointmentModeRepository appointmentModeRepository;

    private final AppointmentStatisticsRepository appointmentStatisticsRepository;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final AppointmentHospitalDepartmentReservationLogRepository appointmentHospitalDepartmentReservationLogRepository;

    private final HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository;

    private final Validator validator;

    private final AppointmentDoctorInfoRepository appointmentDoctorInfoRepository;

    private final AppointmentHospitalDepartmentInfoRepository appointmentHospitalDepartmentInfoRepository;

    private final AppointmentHospitalDepartmentFollowUpLogRepository appointmentHospitalDepartmentFollowUpLogRepository;

    private final AppointmentHospitalDepartmentFollowUpRequestLogService appointmentHospitalDepartmentFollowUpRequestLogService;

    private final HospitalAppointmentServiceTypeRepository hospitalAppointmentServiceTypeRepository;

    private final AppointmentServiceTypeRepository appointmentServiceTypeRepository;

    private final HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository;

    private final NepaliDateUtility nepaliDateUtility;

    public AppointmentServiceImpl(
            PatientService patientService,
            DoctorService doctorService,
            SpecializationService specializationService,
            AppointmentRepository appointmentRepository,
            DoctorDutyRosterRepository doctorDutyRosterRepository,
            DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
            AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
            HospitalService hospitalService,
            AppointmentRefundDetailRepository appointmentRefundDetailRepository,
            AppointmentRescheduleLogRepository appointmentRescheduleLogRepository,
            AppointmentFollowUpLogRepository appointmentFollowUpLogRepository,
            AppointmentReservationLogRepository appointmentReservationLogRepository,
            AppointmentFollowUpTrackerService appointmentFollowUpTrackerService,
            HospitalPatientInfoService hospitalPatientInfoService,
            PatientMetaInfoService patientMetaInfoService,
            PatientRelationInfoService patientRelationInfoService,
            PatientRelationInfoRepository patientRelationInfoRepository,
            AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService,
            AppointmentTransactionRequestLogService appointmentTransactionRequestLogService,
            AppointmentModeRepository appointmentModeRepository,
            AppointmentStatisticsRepository appointmentStatisticsRepository,
            HospitalPatientInfoRepository hospitalPatientInfoRepository,
            AppointmentHospitalDepartmentReservationLogRepository appointmentHospitalDepartmentReservationLogRepository,
            HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository,
            Validator validator,
            AppointmentDoctorInfoRepository appointmentDoctorInfoRepository,
            AppointmentHospitalDepartmentInfoRepository appointmentHospitalDepartmentInfoRepository,
            AppointmentHospitalDepartmentFollowUpLogRepository appointmentHospitalDepartmentFollowUpLogRepository,
            AppointmentHospitalDepartmentFollowUpRequestLogService appointmentHospitalDepartmentFollowUpRequestLogService,
            HospitalAppointmentServiceTypeRepository hospitalAppointmentServiceTypeRepository,
            AppointmentServiceTypeRepository appointmentServiceTypeRepository,
            HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository,
            NepaliDateUtility nepaliDateUtility) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.appointmentRepository = appointmentRepository;
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.hospitalService = hospitalService;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentRescheduleLogRepository = appointmentRescheduleLogRepository;
        this.appointmentFollowUpLogRepository = appointmentFollowUpLogRepository;
        this.appointmentReservationLogRepository = appointmentReservationLogRepository;
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
        this.hospitalPatientInfoService = hospitalPatientInfoService;
        this.patientMetaInfoService = patientMetaInfoService;
        this.patientRelationInfoService = patientRelationInfoService;
        this.patientRelationInfoRepository = patientRelationInfoRepository;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
        this.appointmentTransactionRequestLogService = appointmentTransactionRequestLogService;
        this.appointmentModeRepository = appointmentModeRepository;
        this.appointmentStatisticsRepository = appointmentStatisticsRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.appointmentHospitalDepartmentReservationLogRepository = appointmentHospitalDepartmentReservationLogRepository;
        this.hospitalDepartmentBillingModeInfoRepository = hospitalDepartmentBillingModeInfoRepository;
        this.validator = validator;
        this.appointmentDoctorInfoRepository = appointmentDoctorInfoRepository;
        this.appointmentHospitalDepartmentInfoRepository = appointmentHospitalDepartmentInfoRepository;
        this.appointmentHospitalDepartmentFollowUpLogRepository = appointmentHospitalDepartmentFollowUpLogRepository;
        this.appointmentHospitalDepartmentFollowUpRequestLogService = appointmentHospitalDepartmentFollowUpRequestLogService;
        this.hospitalAppointmentServiceTypeRepository = hospitalAppointmentServiceTypeRepository;
        this.appointmentServiceTypeRepository = appointmentServiceTypeRepository;
        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
        this.nepaliDateUtility = nepaliDateUtility;
    }


    @Override
    public AppointmentCheckAvailabilityResponseDTO fetchAvailableTimeSlots(AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        validateIfRequestIsPastDate(requestDTO.getAppointmentDate());

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        AppointmentCheckAvailabilityResponseDTO responseDTO =
                fetchAvailableTimeSlots(doctorDutyRosterInfo, requestDTO);

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentCheckAvailabilityResponseDTO fetchCurrentAvailableTimeSlots
            (AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        validateIfRequestIsPastDate(requestDTO.getAppointmentDate());

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        AppointmentCheckAvailabilityResponseDTO responseDTO =
                fetchCurrentAvailableTimeSlots(doctorDutyRosterInfo, requestDTO);

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    /*1.SAVE IN AppointmentTransactionRequestLog WHERE TRANSACTION STATUS = 'N'.
    * SAVED INITIALLY SUCH THAT IF ANY TIMEOUT OR SERVER ISSUES OCCUR,
    * THEN WHOLE APPOINTMENT CAN BE TRACED BY ITS STATUS.
    *
    * 2. VALIDATE IF AppointmentReservationLog(TEMPORARILY RESERVED TIME SLOT) EXISTS/STILL ACTIVE.
    * SINCE AFTER SOME TIME, THE ROW IS RIGHT AWAY DELETED FROM THE TABLE.
    * AND HENCE THE SELECTED TIME SLOT IS EXPIRED AND IS NO LONGER AVAILABLE FOR APPOINTMENT.
    *
    * 3. VALIDATE REQUEST INFO :
    *   A. VALIDATE IF ANY OTHER APPOINTMENT EXISTS ON THE SAME CRITERIA
    *
    * 4. SAVE Patient, PatientMetaInfo, HospitalPatientInfo
    *
    * 5. GENERATE UNIQUE APPOINTMENT NUMBER WHICH STARTS WITH '0001' AND INCREMENTS BY 1 TILL IT REACHES FISCAL YEAR.
    *    IT STARTS WITH '0001' IN NEXT FISCAL YEAR. GENERATE ON THE BASIS OF LATEST APPOINTMENT NUMBER & FISCAL YEAR.
    *
    * 6. SAVE APPOINTMENT
    *
    * 7. SAVE APPOINTMENT TRANSACTION DETAILS
    *
    * 8. IF isFollowUp = 'Y', SAVE APPOINTMENT FOLLOW UP TABLES
    *
    * 9. UPDATE TRANSACTION STATUS IN AppointmentTransactionRequestLog AS 'Y'
    * AND RETURN THE FINAL RESPONSE.
    * */
    @Override
    public AppointmentSuccessResponseDTO saveAppointmentForSelf(@Valid AppointmentRequestDTOForSelf requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        validateConstraintViolation(validator.validate(requestDTO));

        AppointmentTransactionRequestLog transactionRequestLog =
                appointmentTransactionRequestLogService.save(
                        requestDTO.getTransactionInfo().getTransactionDate(),
                        requestDTO.getTransactionInfo().getTransactionNumber(),
                        requestDTO.getPatientInfo().getName()
                );

        AppointmentMode appointmentMode = fetchActiveAppointmentModeIdByCode
                (requestDTO.getTransactionInfo().getAppointmentModeCode());

        validateEsewaId(requestDTO.getTransactionInfo().getAppointmentModeCode(),
                requestDTO.getPatientInfo().getESewaId());

        String code = Objects.isNull(requestDTO.getAppointmentInfo().getAppointmentServiceTypeCode())
                ? DOCTOR_CONSULTATION_CODE : requestDTO.getAppointmentInfo().getAppointmentServiceTypeCode();

        AppointmentSuccessResponseDTO responseDTO;
        switch (code.trim().toUpperCase()) {

            case DOCTOR_CONSULTATION_CODE:
                responseDTO = saveAppointmentForSelfDoctorWise(
                        requestDTO, appointmentMode, transactionRequestLog);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                responseDTO = saveAppointmentForSelfDepartmentWise(
                        requestDTO, appointmentMode, transactionRequestLog);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE, code));
        }

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentSuccessResponseDTO saveAppointmentForOthers(@Valid AppointmentRequestDTOForOthers requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        validateConstraintViolation(validator.validate(requestDTO));

        validateEsewaId(requestDTO.getTransactionInfo().getAppointmentModeCode(),
                requestDTO.getRequestBy().getESewaId());

        AppointmentTransactionRequestLog transactionRequestLog =
                appointmentTransactionRequestLogService.save(
                        requestDTO.getTransactionInfo().getTransactionDate(),
                        requestDTO.getTransactionInfo().getTransactionNumber(),
                        requestDTO.getRequestFor().getName()
                );

        AppointmentMode appointmentMode = fetchActiveAppointmentModeIdByCode
                (requestDTO.getTransactionInfo().getAppointmentModeCode());

        String code = Objects.isNull(requestDTO.getAppointmentInfo().getAppointmentServiceTypeCode())
                ? DOCTOR_CONSULTATION_CODE :
                requestDTO.getAppointmentInfo().getAppointmentServiceTypeCode();

        AppointmentSuccessResponseDTO responseDTO;
        switch (code.trim().toUpperCase()) {

            case DOCTOR_CONSULTATION_CODE:
                responseDTO = saveAppointmentForOthersDoctorWise(
                        requestDTO, appointmentMode, transactionRequestLog);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                responseDTO = saveAppointmentForOthersDepartmentWise(
                        requestDTO, appointmentMode, transactionRequestLog);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE, code));
        }

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentMinResponseWithStatusDTO fetchPendingAppointments(AppointmentHistorySearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PENDING_APPOINTMENTS);

        List<AppointmentMinResponseDTO> pendingAppointments =
                appointmentRepository.fetchPendingAppointments(searchDTO);

        log.info(FETCHING_PROCESS_COMPLETED, PENDING_APPOINTMENTS, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentMinResponseWithStatusDTO(pendingAppointments);
    }

    @Override
    public AppointmentCancelResponseDTO cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CANCELLING_PROCESS_STARTED);

        Appointment appointment = findPendingAppointmentById(cancelRequestDTO.getAppointmentId());

        parseAppointmentCancelledDetails(appointment, cancelRequestDTO.getRemarks());

        Double appointmentAmount = fetchAppointmentAmount(appointment.getId());

        Double refundAmount = fetchRefundAmount(appointment.getId());

        saveAppointmentRefundDetail(parseToAppointmentRefundDetail(appointment, refundAmount));

        AppointmentCancelResponseDTO cancelResponseDTO =
                parseAppointmentCancelResponse(appointmentAmount, refundAmount);

        log.info(CANCELLING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return cancelResponseDTO;
    }

    @Override
    public StatusResponseDTO rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(RESCHEDULE_PROCESS_STARTED);

        Appointment appointment = findPendingAppointmentById(rescheduleRequestDTO.getAppointmentId());

        switch (appointment.getHospitalAppointmentServiceType().getAppointmentServiceType().getCode()) {

            case DOCTOR_CONSULTATION_CODE: {
                AppointmentDoctorInfo appointmentDoctorInfo = fetchAppointmentDoctorInfo(appointment.getId());
                validateRequestedRescheduleInfoDoctorWise(rescheduleRequestDTO, appointmentDoctorInfo);
                break;
            }

            case DEPARTMENT_CONSULTATION_CODE: {
                AppointmentHospitalDepartmentInfo appointmentHospitalDepartmentInfo =
                        fetchAppointmentHospitalDepartmentInfo(appointment.getId());
                validateRequestedRescheduleInfoDepartmentWise(rescheduleRequestDTO, appointmentHospitalDepartmentInfo);
                break;
            }
        }

        saveAppointmentRescheduleLog(appointment, rescheduleRequestDTO);

        updateAppointmentDetails(appointment, rescheduleRequestDTO);

        log.info(RESCHEDULE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return parseToStatusResponseDTO();
    }

    @Override
    public AppointmentDetailResponseWithStatusDTO fetchAppointmentDetails(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);

        AppointmentDetailResponseDTO appointmentDetails =
                appointmentRepository.fetchAppointmentDetails(appointmentId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentDetailResponseWithStatusDTO(appointmentDetails);
    }

    @Override
    public AppointmentMinResponseWithStatusDTO fetchAppointmentHistory(AppointmentHistorySearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentMinResponseDTO> appointmentHistory =
                appointmentRepository.fetchAppointmentHistory(searchDTO);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentMinResponseWithStatusDTO(appointmentHistory);
    }

//    todo: serviceType Code must be dynamic
    @Override
    public AppointmentResponseWithStatusDTO searchAppointments(AppointmentSearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

//        AppointmentServiceType appointmentServiceType =
//                fetchAppointmentServiceType(searchDTO.getAppointmentServiceTypeId());

        AppointmentResponseWithStatusDTO appointments = searchDTO.getIsSelf().equals(YES)
                ? appointmentRepository.searchAppointmentsForSelf(searchDTO, "DOC")
                : appointmentRepository.searchAppointmentsForOthers(searchDTO, "DOC");

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return appointments;
    }

    @Override
    public StatusResponseDTO cancelRegistration(Long appointmentReservationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_RESERVATION_LOG);

        AppointmentReservationLog appointmentReservationLog =
                fetchAppointmentReservationLogById(appointmentReservationId);

        if (!Objects.isNull(appointmentReservationLog))
            appointmentReservationLogRepository.delete(appointmentReservationLog);

        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));

        return parseToStatusResponseDTO();
    }

    @Override
    public AppointmentTransactionStatusResponseDTO fetchAppointmentTransactionStatus
            (AppointmentTransactionStatusRequestDTO requestDTO) {

        Character appointmentTransactionRequestLogStatus =
                appointmentTransactionRequestLogService.fetchAppointmentTransactionStatus
                        (requestDTO.getTransactionNumber(), requestDTO.getPatientName());

        return parseToAppointmentTransactionStatusResponseDTO(appointmentTransactionRequestLogStatus);
    }

    private void validateEsewaId(String appointmentModeCode, String esewaId) {

        if (appointmentModeCode.equals(APPOINTMENT_MODE_ESEWA_CODE) && ObjectUtils.isEmpty(esewaId)) {
            throw new BadRequestException(ESEWA_ID_CANNOT_BE_NULL);
        }
    }

    /*IF DOCTOR DAY OFF STATUS = 'Y', THEN THERE ARE NO AVAILABLE TIME SLOTS
    * ELSE, CALCULATE AVAILABLE TIME SLOTS BASED ON DOCTOR DUTY ROSTER AND APPOINTMENT FOR THE SELECTED CRITERIA
    * (APPOINTMENT DATE, DOCTOR AND SPECIALIZATION)
    * FETCH APPOINTMENT RESERVATIONS FOR THE SELECTED CRITERIA AND FINALLY FILTER OUT ONLY AVAILABLE TIME SLOTS
    * (EXCLUDING APPOINTMENT RESERVATION)*/
    private AppointmentCheckAvailabilityResponseDTO fetchAvailableTimeSlots(
            DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Date queryDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String doctorStartTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String doctorEndTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        AppointmentCheckAvailabilityResponseDTO responseDTO;

        if (doctorDutyRosterInfo.getDayOffStatus().equals(YES)) {
            responseDTO = parseToAvailabilityResponse(doctorStartTime, doctorEndTime, queryDate, new ArrayList<>());
        } else {
            final Duration rosterGapDuration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration())
                    .toStandardDuration();

            List<String> availableTimeSlots = filterDoctorTimeWithAppointments(
                    rosterGapDuration, doctorStartTime, doctorEndTime, requestDTO);

            List<String> bookedAppointmentReservations =
                    fetchBookedAppointmentReservations(requestDTO);

            filterAvailableSlotsWithAppointmentReservation(availableTimeSlots, bookedAppointmentReservations);

            responseDTO = parseToAvailabilityResponse(doctorStartTime, doctorEndTime, queryDate, availableTimeSlots);
        }
        return responseDTO;
    }

    /*IF DOCTOR DAY OFF STATUS = 'Y', THEN THERE ARE NO AVAILABLE TIME SLOTS
* ELSE, CALCULATE AVAILABLE TIME SLOTS BASED ON DOCTOR DUTY ROSTER AND APPOINTMENT FOR THE SELECTED CRITERIA
* (APPOINTMENT DATE, DOCTOR AND SPECIALIZATION)
* FETCH APPOINTMENT RESERVATIONS FOR THE SELECTED CRITERIA AND FINALLY FILTER OUT ONLY AVAILABLE TIME SLOTS
* (EXCLUDING APPOINTMENT RESERVATION)*/
    private AppointmentCheckAvailabilityResponseDTO fetchCurrentAvailableTimeSlots(
            DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Date queryDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String doctorStartTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String doctorEndTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        AppointmentCheckAvailabilityResponseDTO responseDTO;

        if (doctorDutyRosterInfo.getDayOffStatus().equals(YES)) {
            responseDTO = parseToAvailabilityResponse(doctorStartTime, doctorEndTime, queryDate, new ArrayList<>());
        } else {
            final Duration rosterGapDuration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration())
                    .toStandardDuration();

            List<String> availableTimeSlots = filterCurrentDoctorTimeWithAppointments(
                    rosterGapDuration, doctorStartTime, doctorEndTime, requestDTO);

            List<String> bookedAppointmentReservations =
                    fetchBookedAppointmentReservations(requestDTO);

            filterAvailableSlotsWithAppointmentReservation(availableTimeSlots, bookedAppointmentReservations);

            responseDTO = parseToAvailabilityResponse(doctorStartTime, doctorEndTime, queryDate, availableTimeSlots);
        }
        return responseDTO;
    }

    private List<String> filterDoctorTimeWithAppointments(Duration rosterGapDuration,
                                                          String doctorStartTime,
                                                          String doctorEndTime,
                                                          AppointmentCheckAvailabilityRequestDTO requestDTO) {

        List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                appointmentRepository.fetchBookedAppointments(requestDTO);

        return calculateAvailableTimeSlots(
                doctorStartTime, doctorEndTime, rosterGapDuration, bookedAppointments);
    }

    private List<String> filterCurrentDoctorTimeWithAppointments(Duration rosterGapDuration,
                                                                 String doctorStartTime,
                                                                 String doctorEndTime,
                                                                 AppointmentCheckAvailabilityRequestDTO requestDTO) {

        List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                appointmentRepository.fetchBookedAppointments(requestDTO);

        return calculateCurrentAvailableTimeSlots(
                doctorStartTime, doctorEndTime, rosterGapDuration, bookedAppointments, requestDTO.getAppointmentDate());
    }

    private List<String> fetchBookedAppointmentReservations
            (AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return appointmentReservationLogRepository.fetchBookedAppointmentReservations(requestDTO);
    }

    private void filterAvailableSlotsWithAppointmentReservation(List<String> availableTimeSlots,
                                                                List<String> appointmentReservations) {
        availableTimeSlots.removeAll(appointmentReservations);
    }

    private Doctor fetchDoctor(Long doctorId, Long hospitalId) {
        return doctorService.fetchActiveDoctorByIdAndHospitalId(doctorId, hospitalId);
    }

    private Specialization fetchSpecialization(Long specializationId, Long hospitalId) {
        return specializationService.fetchActiveSpecializationByIdAndHospitalId(specializationId, hospitalId);
    }

    private Patient fetchPatientForSelf(Boolean isNewRegistration,
                                        Long patientId,
                                        Hospital hospital,
                                        PatientRequestByDTO patientRequestDTO) {

        Patient patient;

        if (isNewRegistration) {
            patient = patientService.saveSelfPatient(patientRequestDTO);
            patientMetaInfoService.savePatientMetaInfo(patient);
        } else
            patient = patientService.fetchPatientById(patientId);

        hospitalPatientInfoService.saveHospitalPatientInfoForSelf(
                hospital, patient,
                patientRequestDTO.getEmail(),
                patientRequestDTO.getAddress()
        );

        return patient;
    }

    /*CASE 1: BOOK APPOINTMENT FOR SELF AND THEN FOR OTHERS
 * CASE 2 : BOOK APPOINTMENT FOR OTHERS AND THEN FOR SELF
 *
 * FIRST SAVE REQUESTED BY PATIENT INFO IF IT HAS NOT BEEN SAVED (CASE 2)
 * VALIDATE IF REQUESTED FOR PATIENT INFO EXISTS
 * IF YES,
 *      FETCH CORRESPONDING PATIENT RELATION INFO AND SIMPLY CHANGE STATUS AS ACTIVE IF IT IS DELETED
 *      OTHERWISE THROW PATIENT DUPLICITY EXCEPTION
 *      (THIS IS NECESSARY BECAUSE USER CAN DELETE 'OTHER' CARD
 *       AND ENTER SAME INFORMATION (NAME, MOBILE NUMBER, DATE OF BIRTH) AGAIN)
 * IF NO,
 *      SAVE CORRESPONDING REQUESTED CHILD PATIENT DETAILS IN
 *      'Patient', 'PatientMetaInfo' AND 'PatientRelationInfo' ENTITY
 *      SAVE IN PATIENT RELATION INFO (IF ALREADY SAVED- CHECK STATUS AND SET AS ACTIVE IF IT IS DELETED.)
 *
 * SAVE IN HOSPITAL PATIENT INFO IF IT HAS NOT BEEN SAVED PREVIOUSLY IN REQUESTED HOSPITAL
 * */
    private Patient fetchPatientForOthers(Boolean isNewRegistration,
                                          Long patientId,
                                          Hospital hospital,
                                          PatientRequestByDTO requestByPatientInfo,
                                          PatientRequestForDTO requestForPatientInfo) {

        Patient parentPatient;
        Patient childPatient;

        if (isNewRegistration) {
            parentPatient = patientService.saveSelfPatient(requestByPatientInfo);

            childPatient = patientService.fetchPatient(requestForPatientInfo);

            if (!Objects.isNull(childPatient)) {
                validatePatientDuplicity(parentPatient, childPatient, requestForPatientInfo);
            } else {
                childPatient = patientService.saveOtherPatient(requestForPatientInfo);
                patientMetaInfoService.savePatientMetaInfo(childPatient);
                patientRelationInfoService.savePatientRelationInfo(parentPatient, childPatient);
            }

        } else
            childPatient = patientService.fetchPatientById(patientId);

        hospitalPatientInfoService.saveHospitalPatientInfoForOthers(
                hospital, childPatient,
                requestForPatientInfo.getEmail(),
                requestForPatientInfo.getAddress()
        );

        return childPatient;
    }

    private Appointment findPendingAppointmentById(Long appointmentId) {
        return appointmentRepository.fetchPendingAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private AppointmentMode fetchActiveAppointmentModeIdByCode(String appointmentModeCode) {
        return appointmentModeRepository.fetchActiveAppointmentModeByCode(appointmentModeCode)
                .orElseThrow(() -> APPOINTMENT_MODE_WITH_GIVEN_CODE_NOT_FOUND.apply(appointmentModeCode));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private Function<String, NoContentFoundException> APPOINTMENT_MODE_WITH_GIVEN_CODE_NOT_FOUND = (code) -> {
        log.error(CONTENT_NOT_FOUND_BY_CODE, APPOINTMENT_MODE, code);
        throw new NoContentFoundException(AppointmentMode.class, "code", code);
    };

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    /*FETCH DOCTOR DUTY ROSTER INFO FOR SELECTED DATE*/
    private DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterInfo(Date date,
                                                                      Long doctorId,
                                                                      Long specializationId) {

        DoctorDutyRosterTimeResponseDTO overrideRosters =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideTime(date, doctorId, specializationId);

        if (Objects.isNull(overrideRosters))
            return doctorDutyRosterRepository.fetchDoctorDutyRosterTime(date, doctorId, specializationId);

        return overrideRosters;
    }


    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private void saveAppointmentTransactionDetail(AppointmentTransactionRequestDTO requestDTO,
                                                  Appointment appointment) {

        AppointmentTransactionDetail transactionDetail = parseToAppointmentTransactionInfo(requestDTO, appointment);
        appointmentTransactionDetailRepository.save(transactionDetail);
    }

    private void saveAppointmentRefundDetail(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private void saveAppointmentRescheduleLog(Appointment appointment,
                                              AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        AppointmentRescheduleLog appointmentRescheduleLog =
                appointmentRescheduleLogRepository.findByAppointmentId(appointment.getId());

        appointmentRescheduleLog = Objects.isNull(appointmentRescheduleLog)
                ? parseToAppointmentRescheduleLog(appointment, rescheduleRequestDTO, new AppointmentRescheduleLog())
                : parseToAppointmentRescheduleLog(appointment, rescheduleRequestDTO, appointmentRescheduleLog);

        appointmentRescheduleLogRepository.save(appointmentRescheduleLog);
    }

    private void saveAppointmentFollowUpDetailsDoctorWise(Long parentAppointmentId, Long followUpAppointmentId) {

        updateAppointmentFollowUpRequestLog(parentAppointmentId);

        saveAppointmentFollowUpLogDoctorWise(parentAppointmentId, followUpAppointmentId);
    }

    private void saveAppointmentFollowUpDetailsDepartmentWise(Long parentAppointmentId, Long followUpAppointmentId) {

        updateAppointmentHospitalDepartmentFollowUpRequestLog(parentAppointmentId);

        saveAppointmentFollowUpLogDepartmentWise(parentAppointmentId, followUpAppointmentId);
    }

    /*RELATION BETWEEN APPOINTMENT AND ITS CONSECUTIVE APPOINTMENT LOG*/
    private void saveAppointmentFollowUpLogDoctorWise(Long parentAppointmentId, Long followUpAppointmentId) {

        appointmentFollowUpLogRepository.save(
                parseToAppointmentFollowUpLog(parentAppointmentId, followUpAppointmentId)
        );
    }

    /*RELATION BETWEEN APPOINTMENT AND ITS CONSECUTIVE APPOINTMENT LOG*/
    private void saveAppointmentFollowUpLogDepartmentWise(Long parentAppointmentId, Long followUpAppointmentId) {

        appointmentHospitalDepartmentFollowUpLogRepository.save(
                parseToAppointmentHospitalDepartmentFollowUpLog(parentAppointmentId, followUpAppointmentId)
        );
    }

    /*INCREMENT APPOINTMENT FOLLOW UP REQUEST COUNT BY 1*/
    private void updateAppointmentFollowUpRequestLog(Long parentAppointmentId) {

        Long appointmentFollowUpTrackerId =
                appointmentFollowUpTrackerService.fetchByParentAppointmentId(parentAppointmentId);

        appointmentFollowUpRequestLogService.update(appointmentFollowUpTrackerId);
    }

    /*INCREMENT APPOINTMENT FOLLOW UP REQUEST COUNT BY 1*/
    private void updateAppointmentHospitalDepartmentFollowUpRequestLog(Long parentAppointmentId) {

        Long appointmentFollowUpTrackerId =
                appointmentFollowUpTrackerService.fetchByParentAppointmentId(parentAppointmentId);

        appointmentHospitalDepartmentFollowUpRequestLogService.update(appointmentFollowUpTrackerId);
    }

    private void validatePatientDuplicity(Patient parentPatient,
                                          Patient childPatient,
                                          PatientRequestForDTO requestForPatientInfo) {

        PatientRelationInfo patientRelationInfo =
                patientRelationInfoRepository.fetchPatientRelationInfo(parentPatient.getId(), childPatient.getId());

        if (!Objects.isNull(patientRelationInfo)) {

            if (patientRelationInfo.getStatus().equals(DELETED))
                patientRelationInfo.setStatus(ACTIVE);
            else
                PATIENT_DUPLICATION_EXCEPTION(requestForPatientInfo.getName(),
                        requestForPatientInfo.getMobileNumber(), requestForPatientInfo.getDateOfBirth());
        }
    }

    private static void PATIENT_DUPLICATION_EXCEPTION(String name, String mobileNumber, Date dateOfBirth) {
        log.error(NAME_AND_MOBILE_NUMBER_DUPLICATION_ERROR, PATIENT, name, mobileNumber, dateOfBirth);
        throw new DataDuplicationException(String.format(DUPLICATE_PATIENT_MESSAGE,
                name, mobileNumber, utilDateToSqlDate(dateOfBirth))
        );
    }

    private AppointmentReservationLog validateAppointmentReservationIsActive(Long appointmentReservationId) {
        AppointmentReservationLog appointmentReservationLog =
                fetchAppointmentReservationLogById(appointmentReservationId);

        if (Objects.isNull(appointmentReservationLog))
            throw new BadRequestException(APPOINTMENT_FAILED_MESSAGE, APPOINTMENT_FAILED_DEBUG_MESSAGE);

        return appointmentReservationLog;
    }

    private AppointmentHospitalDepartmentReservationLog validateAppointmentHospitalDeptReservationIsActive(
            Long appointmentReservationId) {

        AppointmentHospitalDepartmentReservationLog appointmentReservationLog =
                fetchAppointmentHospitalDeptReservationLogById(appointmentReservationId);

        if (Objects.isNull(appointmentReservationLog))
            throw new BadRequestException(APPOINTMENT_FAILED_MESSAGE, APPOINTMENT_FAILED_DEBUG_MESSAGE);

        return appointmentReservationLog;
    }

    /*VALIDATE IF APPOINTMENT ALREADY EXISTS ON SELECTED DATE AND TIME */
    private void validateIfParentAppointmentExistsDoctorWise(AppointmentReservationLog appointmentReservationLog) {

        String appointmentTime = getTimeFromDate(appointmentReservationLog.getAppointmentTime());

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                appointmentReservationLog.getAppointmentDate(),
                appointmentTime,
                appointmentReservationLog.getDoctorId(),
                appointmentReservationLog.getSpecializationId()
        );

        validateAppointmentExists(appointmentCount, appointmentTime);
    }

    private void validateIfParentAppointmentExistsDeptWise(
            AppointmentHospitalDepartmentReservationLog appointmentReservationLog) {

        String appointmentTime = getTimeFromDate(appointmentReservationLog.getAppointmentTime());

        Long hospitalDepartmentRoomInfoId = Objects.isNull(appointmentReservationLog.getHospitalDepartmentRoomInfo())
                ? null : appointmentReservationLog.getHospitalDepartmentRoomInfo().getId();

        Long appointmentCount = appointmentRepository.validateIfAppointmentExistsDeptWise(
                appointmentReservationLog.getAppointmentDate(),
                appointmentTime,
                appointmentReservationLog.getHospitalDepartment().getId(),
                hospitalDepartmentRoomInfoId
        );

        validateAppointmentExists(appointmentCount, appointmentTime);
    }

    private void validateAppointmentExists(Long appointmentCount, String appointmentTime) {

        if (appointmentCount.intValue() > 0) {
            log.error(APPOINTMENT_EXISTS, convert24HourTo12HourFormat(appointmentTime));
            throw new DataDuplicationException(String.format(APPOINTMENT_EXISTS,
                    convert24HourTo12HourFormat(appointmentTime)));
        }
    }

    private void validateAppointmentAmountDoctorWise(Long doctorId, Long hospitalId,
                                                     Character isFollowUp, Double appointmentAmount) {

        Double actualAppointmentCharge = isFollowUp.equals(YES)
                ? doctorService.fetchDoctorFollowupAppointmentCharge(doctorId, hospitalId)
                : doctorService.fetchDoctorAppointmentCharge(doctorId, hospitalId);

        if (!(Double.compare(actualAppointmentCharge, appointmentAmount) == 0)) {
            log.error(String.format(DOCTOR_APPOINTMENT_CHARGE_INVALID, appointmentAmount));
            throw new BadRequestException(String.format(DOCTOR_APPOINTMENT_CHARGE_INVALID, appointmentAmount),
                    DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE);
        }
    }

    private void validateAppointmentAmountDeptWise(Long hospitalDepartmentId,
                                                   Long hospitalDepartmentBillingModeId,
                                                   Character isFollowUp,
                                                   Double appointmentAmount) {

        Double actualAppointmentCharge = isFollowUp.equals(YES)
                ? fetchHospitalDeptAppointmentFollowUpCharge(hospitalDepartmentBillingModeId, hospitalDepartmentId)
                : fetchHospitalDeptAppointmentCharge(hospitalDepartmentBillingModeId, hospitalDepartmentId);

        if (!(Double.compare(actualAppointmentCharge, appointmentAmount) == 0)) {
            log.error(HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE, appointmentAmount);
            throw new BadRequestException(String.format(HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE_INVALID
                    , appointmentAmount),
                    HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE);
        }
    }

    private void saveAppointmentStatistics(AppointmentRequestDTO appointmentInfo,
                                           Appointment appointment) {

        if (Objects.isNull(appointmentInfo.getPatientId())) {
            saveAppointmentStatistics(parseAppointmentStatisticsForNew(appointment));
        } else {
            checkForRegisteredPatient(appointmentInfo, appointment, appointment.getHospitalId());
        }
    }

    private void checkForRegisteredPatient(AppointmentRequestDTO appointmentInfo,
                                           Appointment appointment,
                                           Hospital hospital) {

        Long patientId = hospitalPatientInfoRepository.checkIfPatientIsRegistered(
                appointmentInfo.getPatientId(),
                hospital.getId()
        );

        if (Objects.isNull(patientId)) {
            saveAppointmentStatistics(parseAppointmentStatisticsForNew(appointment));
        } else {
            saveAppointmentStatistics(parseAppointmentStatisticsForRegistered(appointment));
        }
    }

    private void saveAppointmentStatistics(AppointmentStatistics appointmentStatistics) {
        appointmentStatisticsRepository.save(appointmentStatistics);
    }

    private AppointmentReservationLog fetchAppointmentReservationLogById(Long appointmentReservationId) {
        return appointmentReservationLogRepository.findAppointmentReservationLogById(appointmentReservationId);
    }

    private AppointmentHospitalDepartmentReservationLog fetchAppointmentHospitalDeptReservationLogById(
            Long appointmentReservationId) {
        return appointmentHospitalDepartmentReservationLogRepository.findAppointmentReservationLogById(appointmentReservationId);
    }

    private void validateRequestedRescheduleInfoDoctorWise(AppointmentRescheduleRequestDTO rescheduleRequestDTO,
                                                           AppointmentDoctorInfo appointmentDoctorInfo) {

        validateIfRequestIsBeforeCurrentDateTime(
                rescheduleRequestDTO.getRescheduleDate(), rescheduleRequestDTO.getRescheduleTime());

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime(),
                appointmentDoctorInfo.getDoctor().getId(),
                appointmentDoctorInfo.getSpecialization().getId()
        );

        validateAppointmentExists(appointmentCount, rescheduleRequestDTO.getRescheduleTime());

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo =
                fetchDoctorDutyRosterInfo(rescheduleRequestDTO.getRescheduleDate(),
                        appointmentDoctorInfo.getDoctor().getId(),
                        appointmentDoctorInfo.getSpecialization().getId()
                );

        boolean isTimeValid = AppointmentUtils.validateIfRequestedAppointmentTimeIsValid(
                doctorDutyRosterInfo, rescheduleRequestDTO.getRescheduleTime()
        );

        if (!isTimeValid) {
            log.error(INVALID_APPOINTMENT_TIME, convert24HourTo12HourFormat(rescheduleRequestDTO.getRescheduleTime()));
            throw new NoContentFoundException(String.format(INVALID_APPOINTMENT_TIME,
                    convert24HourTo12HourFormat(rescheduleRequestDTO.getRescheduleTime())));
        }
    }

    private void validateRequestedRescheduleInfoDepartmentWise(
            AppointmentRescheduleRequestDTO rescheduleRequestDTO,
            AppointmentHospitalDepartmentInfo appointmentHospitalDepartmentInfo) {

        validateIfRequestIsBeforeCurrentDateTime(
                rescheduleRequestDTO.getRescheduleDate(), rescheduleRequestDTO.getRescheduleTime());

        Long hospitalDepartmentRoomInfoId =
                Objects.isNull(appointmentHospitalDepartmentInfo.getHospitalDepartmentRoomInfo())
                        ? null
                        : appointmentHospitalDepartmentInfo.getHospitalDepartmentRoomInfo().getId();

        Long appointmentCount = appointmentRepository.validateIfAppointmentExistsDeptWise(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime(),
                appointmentHospitalDepartmentInfo.getHospitalDepartment().getId(),
                hospitalDepartmentRoomInfoId
        );

        validateAppointmentExists(appointmentCount, rescheduleRequestDTO.getRescheduleTime());

        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO =
                fetchHospitalDeptDutyRoster(rescheduleRequestDTO.getRescheduleDate(),
                        appointmentHospitalDepartmentInfo.getHospitalDepartment().getId(),
                        hospitalDepartmentRoomInfoId
                );

        boolean isTimeValid = validateIfRequestedAppointmentTimeIsValid(
                dutyRosterTimeResponseTO, rescheduleRequestDTO.getRescheduleTime()
        );

        if (!isTimeValid) {
            log.error(INVALID_APPOINTMENT_TIME, convert24HourTo12HourFormat(rescheduleRequestDTO.getRescheduleTime()));
            throw new NoContentFoundException(String.format(INVALID_APPOINTMENT_TIME,
                    convert24HourTo12HourFormat(rescheduleRequestDTO.getRescheduleTime())));
        }
    }

    private Double fetchAppointmentAmount(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchAppointmentAmount(appointmentId);
    }

    private Double fetchRefundAmount(Long appointmentId) {
        return appointmentRepository.calculateRefundAmount(appointmentId);
    }

    private AppointmentSuccessResponseDTO saveAppointmentForSelfDoctorWise(
            AppointmentRequestDTOForSelf requestDTO,
            AppointmentMode appointmentMode,
            AppointmentTransactionRequestLog transactionRequestLog) {

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentReservationLog appointmentReservationLog =
                validateAppointmentRequestInfoDoctorWise(appointmentInfo, requestDTO.getTransactionInfo());

        Hospital hospital = fetchHospital(appointmentReservationLog.getHospitalId());

        HospitalAppointmentServiceType hospitalAppointmentServiceType =
                fetchHospitalAppointmentServiceType(hospital.getId(), appointmentInfo.getAppointmentServiceTypeCode());

        Patient patient = fetchPatientForSelf(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getPatientInfo()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                appointmentReservationLog.getHospitalId()
        );

        Appointment appointment = parseToAppointment(
                requestDTO.getAppointmentInfo(),
                appointmentReservationLog.getAppointmentDate(),
                appointmentReservationLog.getAppointmentTime(),
                appointmentNumber,
                YES,
                patient,
                hospital,
                appointmentMode,
                hospitalAppointmentServiceType
        );

        appointment.setAppointmentDateInNepali(getNepaliDate(appointment.getAppointmentDate()));

        save(appointment);

        saveAppointmentDoctorInfo(appointment, appointmentReservationLog);

        if (appointmentInfo.getIsFollowUp().equals(YES))
            saveAppointmentFollowUpDetailsDoctorWise(appointmentInfo.getParentAppointmentId(), appointment.getId());

        return parseAppointmentResponseDetails(
                appointment, appointmentInfo, requestDTO.getTransactionInfo(), transactionRequestLog
        );
    }

    private AppointmentSuccessResponseDTO saveAppointmentForSelfDepartmentWise(
            AppointmentRequestDTOForSelf requestDTO,
            AppointmentMode appointmentMode,
            AppointmentTransactionRequestLog transactionRequestLog) {

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentHospitalDepartmentReservationLog appointmentReservationLog =
                validateAppointmentDeptWiseRequestInfo(appointmentInfo, requestDTO.getTransactionInfo());

        Hospital hospital = fetchHospital(appointmentReservationLog.getHospital().getId());

        HospitalAppointmentServiceType hospitalAppointmentServiceType =
                fetchHospitalAppointmentServiceType(hospital.getId(), appointmentInfo.getAppointmentServiceTypeCode());

        Patient patient = fetchPatientForSelf(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getPatientInfo()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                appointmentReservationLog.getHospital().getId()
        );

        Appointment appointment = parseToAppointment(
                requestDTO.getAppointmentInfo(),
                appointmentReservationLog.getAppointmentDate(),
                appointmentReservationLog.getAppointmentTime(),
                appointmentNumber,
                YES,
                patient,
                hospital,
                appointmentMode,
                hospitalAppointmentServiceType
        );

        appointment.setAppointmentDateInNepali(getNepaliDate(appointment.getAppointmentDate()));

        save(appointment);

        saveAppointmentHospitalDepartmentInfo(appointment, appointmentReservationLog);

        if (appointmentInfo.getIsFollowUp().equals(YES))
            saveAppointmentFollowUpDetailsDepartmentWise(appointmentInfo.getParentAppointmentId(), appointment.getId());

        return parseAppointmentResponseDetails(
                appointment, appointmentInfo, requestDTO.getTransactionInfo(), transactionRequestLog
        );
    }

    private AppointmentReservationLog validateAppointmentRequestInfoDoctorWise(
            AppointmentRequestDTO appointmentInfo,
            AppointmentTransactionRequestDTO transactionInfo) {

        AppointmentReservationLog appointmentReservationLog =
                validateAppointmentReservationIsActive(appointmentInfo.getAppointmentReservationId());

        validateIfParentAppointmentExistsDoctorWise(appointmentReservationLog);

        validateAppointmentAmountDoctorWise(appointmentReservationLog.getDoctorId(),
                appointmentReservationLog.getHospitalId(),
                appointmentInfo.getIsFollowUp(),
                transactionInfo.getAppointmentAmount()
        );

        return appointmentReservationLog;
    }

    private AppointmentHospitalDepartmentReservationLog validateAppointmentDeptWiseRequestInfo(
            AppointmentRequestDTO appointmentInfo,
            AppointmentTransactionRequestDTO transactionInfo) {

        AppointmentHospitalDepartmentReservationLog appointmentReservationLog =
                validateAppointmentHospitalDeptReservationIsActive(appointmentInfo.getAppointmentReservationId());

        validateIfParentAppointmentExistsDeptWise(appointmentReservationLog);

        validateAppointmentAmountDeptWise(appointmentReservationLog.getHospitalDepartment().getId(),
                appointmentReservationLog.getHospitalDepartmentBillingModeInfo().getId(),
                appointmentInfo.getIsFollowUp(),
                transactionInfo.getAppointmentAmount()
        );

        return appointmentReservationLog;
    }

    private Double fetchHospitalDeptAppointmentCharge(Long hospitalDepartmentBillingModeId,
                                                      Long hospitalDepartmentId) {

        return hospitalDepartmentBillingModeInfoRepository.fetchHospitalDeptAppointmentCharge(
                hospitalDepartmentBillingModeId, hospitalDepartmentId);
    }

    private Double fetchHospitalDeptAppointmentFollowUpCharge(Long hospitalDepartmentBillingModeId,
                                                              Long hospitalDepartmentId) {

        return hospitalDepartmentBillingModeInfoRepository.fetchHospitalDeptAppointmentFollowUpCharge(
                hospitalDepartmentBillingModeId, hospitalDepartmentId);
    }

    private void saveAppointmentDoctorInfo(Appointment appointment,
                                           AppointmentReservationLog appointmentReservationLog) {

        AppointmentDoctorInfo appointmentDoctorInfo = parseAppointmentDoctorInfo(
                appointment,
                fetchDoctor(appointmentReservationLog.getDoctorId(), appointmentReservationLog.getHospitalId()),
                fetchSpecialization(appointmentReservationLog.getSpecializationId(),
                        appointmentReservationLog.getHospitalId())
        );

        appointmentDoctorInfoRepository.save(appointmentDoctorInfo);
    }

    private void saveAppointmentHospitalDepartmentInfo(Appointment appointment,
                                                       AppointmentHospitalDepartmentReservationLog reservationLog) {

        AppointmentHospitalDepartmentInfo appointmentHospitalDepartmentInfo = parseAppointmentHospitalDepartmentInfo(
                appointment,
                reservationLog.getHospitalDepartment(),
                reservationLog.getHospitalDepartmentRoomInfo(),
                reservationLog.getHospitalDepartmentBillingModeInfo()
        );

        appointmentHospitalDepartmentInfoRepository.save(appointmentHospitalDepartmentInfo);
    }

    private AppointmentSuccessResponseDTO saveAppointmentForOthersDoctorWise(
            AppointmentRequestDTOForOthers requestDTO,
            AppointmentMode appointmentMode,
            AppointmentTransactionRequestLog transactionRequestLog) {

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentReservationLog appointmentReservationLog =
                validateAppointmentRequestInfoDoctorWise(appointmentInfo, requestDTO.getTransactionInfo());

        Hospital hospital = fetchHospital(appointmentReservationLog.getHospitalId());

        HospitalAppointmentServiceType hospitalAppointmentServiceType = fetchHospitalAppointmentServiceType(
                hospital.getId(), appointmentInfo.getAppointmentServiceTypeCode()
        );

        Patient patient = fetchPatientForOthers(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getRequestBy(),
                requestDTO.getRequestFor()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                appointmentReservationLog.getHospitalId()
        );

        Appointment appointment = parseToAppointment(
                appointmentInfo,
                appointmentReservationLog.getAppointmentDate(),
                appointmentReservationLog.getAppointmentTime(),
                appointmentNumber,
                NO,
                patient,
                hospital,
                appointmentMode,
                hospitalAppointmentServiceType
        );

        appointment.setAppointmentDateInNepali(getNepaliDate(appointment.getAppointmentDate()));

        save(appointment);

        saveAppointmentDoctorInfo(appointment, appointmentReservationLog);

        if (appointmentInfo.getIsFollowUp().equals(YES))
            saveAppointmentFollowUpDetailsDoctorWise(appointmentInfo.getParentAppointmentId(), appointment.getId());

        return parseAppointmentResponseDetails(
                appointment, appointmentInfo, requestDTO.getTransactionInfo(), transactionRequestLog
        );
    }

    private AppointmentSuccessResponseDTO saveAppointmentForOthersDepartmentWise(
            AppointmentRequestDTOForOthers requestDTO,
            AppointmentMode appointmentMode,
            AppointmentTransactionRequestLog transactionRequestLog) {

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentHospitalDepartmentReservationLog appointmentReservationLog =
                validateAppointmentDeptWiseRequestInfo(appointmentInfo, requestDTO.getTransactionInfo());

        Hospital hospital = appointmentReservationLog.getHospital();

        HospitalAppointmentServiceType hospitalAppointmentServiceType = fetchHospitalAppointmentServiceType(
                hospital.getId(), appointmentInfo.getAppointmentServiceTypeCode()
        );

        Patient patient = fetchPatientForOthers(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getRequestBy(),
                requestDTO.getRequestFor()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                hospital.getId()
        );

        Appointment appointment = parseToAppointment(
                appointmentInfo,
                appointmentReservationLog.getAppointmentDate(),
                appointmentReservationLog.getAppointmentTime(),
                appointmentNumber,
                NO,
                patient,
                hospital,
                appointmentMode,
                hospitalAppointmentServiceType
        );

        appointment.setAppointmentDateInNepali(getNepaliDate(appointment.getAppointmentDate()));

        save(appointment);

        saveAppointmentHospitalDepartmentInfo(appointment, appointmentReservationLog);

        if (appointmentInfo.getIsFollowUp().equals(YES))
            saveAppointmentFollowUpDetailsDepartmentWise(appointmentInfo.getParentAppointmentId(), appointment.getId());

        return parseAppointmentResponseDetails(
                appointment, appointmentInfo, requestDTO.getTransactionInfo(), transactionRequestLog
        );
    }

    private AppointmentSuccessResponseDTO parseAppointmentResponseDetails(
            Appointment appointment,
            AppointmentRequestDTO appointmentInfo,
            AppointmentTransactionRequestDTO transactionInfo,
            AppointmentTransactionRequestLog transactionRequestLog) {

        saveAppointmentStatistics(appointmentInfo, appointment);

        saveAppointmentTransactionDetail(transactionInfo, appointment);

        updateAppointmentTransactionRequestLog(transactionRequestLog);

        return parseToAppointmentSuccessResponseDTO(appointment.getAppointmentNumber(),
                transactionRequestLog.getTransactionStatus(), appointment.getHospitalId().getRefundPercentage()
        );
    }

    private AppointmentServiceType fetchAppointmentServiceType(Long appointmentServiceTypeId) {
        return appointmentServiceTypeRepository.fetchActiveById(appointmentServiceTypeId)
                .orElseThrow(() -> APPOINTMENT_SERVICE_TYPE_NOT_FOUND.apply(appointmentServiceTypeId));
    }

    private AppointmentDoctorInfo fetchAppointmentDoctorInfo(Long appointmentId) {
        return appointmentDoctorInfoRepository.fetchAppointmentDoctorInfo(appointmentId)
                .orElseThrow(() -> APPOINTMENT_DOCTOR_INFO_NOT_FOUND.apply(appointmentId));
    }

    private AppointmentHospitalDepartmentInfo fetchAppointmentHospitalDepartmentInfo(Long appointmentId) {
        return appointmentHospitalDepartmentInfoRepository.fetchAppointmentHospitalDepartmentInfo(appointmentId)
                .orElseThrow(() -> APPOINTMENT_HOSPITAL_DEPARTMENT_INFO_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_SERVICE_TYPE_NOT_FOUND = (appointmentServiceTypeId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_SERVICE_TYPE);
        throw new NoContentFoundException(AppointmentServiceType.class,
                "appointmentServiceTypeId", appointmentServiceTypeId.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_DOCTOR_INFO_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_DOCTOR_INFO);
        throw new NoContentFoundException(AppointmentDoctorInfo.class,
                "appointmentId", appointmentId.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_HOSPITAL_DEPARTMENT_INFO_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_HOSPITAL_DEPARTMENT_INFO);
        throw new NoContentFoundException(AppointmentHospitalDepartmentInfo.class,
                "appointmentId", appointmentId.toString());
    };

    private HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRoster(Date appointmentDate,
                                                                             Long hospitalDepartmentId,
                                                                             Long hospitalDepartmentRoomInfoId) {

        return hospitalDeptDutyRosterRepository.fetchHospitalDeptDutyRoster(
                appointmentDate, hospitalDepartmentId, hospitalDepartmentRoomInfoId
        );
    }

    private String getNepaliDate(Date date) {

        String nepaliDate = nepaliDateUtility.getNepaliDateFromDate(date);

        return formatToDateString(nepaliDate);
    }

    private HospitalAppointmentServiceType fetchHospitalAppointmentServiceType(Long hospitalId,
                                                                               String appointmentServiceTypeCode) {

        return hospitalAppointmentServiceTypeRepository.fetchHospitalAppointmentServiceType(
                hospitalId, appointmentServiceTypeCode);
    }
}