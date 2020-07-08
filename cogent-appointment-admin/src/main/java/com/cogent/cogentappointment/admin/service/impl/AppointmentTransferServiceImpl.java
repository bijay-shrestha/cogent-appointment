package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.*;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.previewDTO.AppointmentTransferPreviewResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.OverrideDatesResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.StartTimeAndEndTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentTransferService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_APPOINTMENT_CHARGE_INVALID;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransactionDetailLog.APPOINTMENT_TRANSACTION_DETAIL;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransactionRequestLogConstant.APPOINTMENT_TRANSACTION_REQUEST_LOG;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransactionRequestLogConstant.CONTENT_NOT_FOUND_BY_APPOINTMENT_NUMBER;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransferLog.*;
import static com.cogent.cogentappointment.admin.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.admin.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.admin.log.constants.SpecializationLog.SPECIALIZATION;
import static com.cogent.cogentappointment.admin.utils.AppointmentTransferUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Service
@Slf4j
@Transactional
public class AppointmentTransferServiceImpl implements AppointmentTransferService {

    private final AppointmentTransferRepository appointmentTransferRepository;

    private final AppointmentTransferTransactionDetailRepository transferTransactionRepository;

    private final AppointmentTransferTransactionRequestLogRepository transferTransactionRequestLogRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTransactionDetailRepository transactionDetailRepository;

    private final AppointmentTransactionRequestLogRepository transactionRequestLogRepository;

    private final DoctorRepository doctorRepository;

    private final SpecializationRepository specializationRepository;

    private final AppointmentDoctorInfoRepository appointmentDoctorInfoRepository;

    public AppointmentTransferServiceImpl(AppointmentTransferRepository appointmentTransferRepository,
                                          AppointmentTransferTransactionDetailRepository transferTransactionRepository,
                                          AppointmentTransferTransactionRequestLogRepository transferTransactionRequestLogRepository,
                                          AppointmentRepository appointmentRepository,
                                          AppointmentTransactionDetailRepository transactionDetailRepository,
                                          AppointmentTransactionRequestLogRepository transactionRequestLogRepository,
                                          DoctorRepository doctorRepository,
                                          SpecializationRepository specializationRepository,
                                          AppointmentDoctorInfoRepository appointmentDoctorInfoRepository) {
        this.appointmentTransferRepository = appointmentTransferRepository;
        this.transferTransactionRepository = transferTransactionRepository;
        this.transferTransactionRequestLogRepository = transferTransactionRequestLogRepository;
        this.appointmentRepository = appointmentRepository;
        this.transactionDetailRepository = transactionDetailRepository;
        this.transactionRequestLogRepository = transactionRequestLogRepository;
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
        this.appointmentDoctorInfoRepository = appointmentDoctorInfoRepository;
    }

    /* FETCH APPOINTMENT DATES BASED ON DOCTOR ID AND SPECIALIZATION ID */
    @Override
    public List<Date> fetchAvailableDatesByDoctorIdAndSpecializationId(AppointmentDateRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<Date> actualDutyRosterDate = new ArrayList<>();

        List<DoctorDatesResponseDTO> dutyRosterList = appointmentTransferRepository.getDutyRosterByDoctorIdAndSpecializationId(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

        dutyRosterList.forEach(rosterId -> {

            List<Date> dates = getActualdate(appointmentTransferRepository.getDayOffDaysByRosterId(rosterId.getId()),
                    getDates(rosterId.getFromDate(), rosterId.getToDate()));

            actualDutyRosterDate.addAll(dates);
        });

        List<OverrideDatesResponseDTO> overrideDates = appointmentTransferRepository.getOverrideDatesByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

        List<Date> overrideDayOffDates = filterOverrideDayOffDates(overrideDates);

        List<Date> overrideAvailableDates = filterOverrideAvaliableDates(overrideDates);

        List<Date> mergeOverrideAndActualDateList = utilDateListToSqlDateList(
                mergeOverrideAndActualDateList(
                        overrideAvailableDates,
                        actualDutyRosterDate,
                        overrideDayOffDates));

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return mergeOverrideAndActualDateList;
    }

    /* FETCH VACANT APPOINTMENT TIME BASED ON SPECIFIC DATE, DOCTOR ID AND SPECIALIZATION ID */
    @Override
    public List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<String> time = new ArrayList<>();

        List<String> unavailableTime = appointmentTransferRepository.getUnavailableTimeByDateAndDoctorId(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getDate(),
                requestDTO.getHospitalId());

        Date sqlRequestDate = utilDateToSqlDate(requestDTO.getDate());

        time.addAll(checkOverrideRoster(requestDTO, unavailableTime, sqlRequestDate));

        if (time.size() == 0) {
            time.addAll(checkDutyRoster(requestDTO, unavailableTime, sqlRequestDate));
        }

        if (time.isEmpty())
            throw APPOINTMENT_TIME_NOT_AVAILABLE.get();

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return time;
    }

    @Override
    public Double fetchDoctorChargeByDoctorId(DoctorChargeRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        AppointmentChargeResponseDTO charge = appointmentTransferRepository.getAppointmentChargeByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getHospitalId());

        Double response = requestDTO.getFollowUp().equals(NO) ? charge.getActualCharge() : charge.getFollowUpCharge();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public void appointmentTransfer(AppointmentTransferRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPOINTMENT_TRANSFER_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        validateAppointmentAmount(requestDTO.getDoctorId(), requestDTO.getHospitalId(),
                requestDTO.getIsFollowUp(), requestDTO.getAppointmentCharge());

        Appointment appointment = fetchAppointmentById(requestDTO.getAppointmentId(), requestDTO.getHospitalId());

        AppointmentTransactionDetail transactionDetail = fetchAppointmentTransactionDetailByappointmentId(
                requestDTO.getAppointmentId());

        AppointmentDoctorInfo appointmentDoctorInfo = appointmentRepository.getPreviousAppointmentDoctorAndSpecialization
                (appointment.getId());

        if (appointmentDoctorInfo.getSpecialization().getId() == requestDTO.getSpecializationId() &&
                (transactionDetail.getAppointmentAmount().compareTo(requestDTO.getAppointmentCharge()) == 0)) {

            transferForSameSpecializationAndCharge(appointment, requestDTO, appointmentDoctorInfo, transactionDetail);

        } else {

            transferWithNewCharge(appointment, requestDTO, appointmentDoctorInfo, transactionDetail);

        }

        log.info(APPOINTMENT_TRANSFER_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));
    }

    private void transferForSameSpecializationAndCharge(Appointment appointment,
                                                        AppointmentTransferRequestDTO requestDTO,
                                                        AppointmentDoctorInfo appointmentDoctorInfo,
                                                        AppointmentTransactionDetail transactionDetail) {
        Doctor previousDoctor = fetchDoctorById(appointmentDoctorInfo.getDoctor().getId(), requestDTO.getHospitalId());

        Specialization previousSpecialization = fetchSpecializationById(appointmentDoctorInfo.getSpecialization().getId(),
                requestDTO.getHospitalId());

        Doctor transferredToDoctor = fetchDoctorById(requestDTO.getDoctorId(), requestDTO.getHospitalId());

        Specialization transferredToSpecialization = fetchSpecializationById(requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());


        AppointmentTransfer appointmentTransfer = parseToAppointmentTransfer(appointment,
                requestDTO,
                transferredToDoctor,
                transferredToSpecialization,
                previousDoctor,
                previousSpecialization);

        Appointment transferredAppointment = parseAppointmentTransferDetail(appointment,
                requestDTO);

        SaveAppointmentDoctorInfo(parseAppointmentDoctorInfo
                (transferredToDoctor,transferredToSpecialization,appointmentDoctorInfo));

        AppointmentTransferTransactionDetail transferTransactionDetail = parseToAppointmentTransferTransactionDetail(
                transactionDetail,
                requestDTO.getAppointmentCharge(),
                requestDTO.getRemarks(),
                appointmentTransfer);

        save(transferredAppointment, appointmentTransfer);
        saveTransferTransaction(transferTransactionDetail);
    }

    private void transferWithNewCharge(Appointment appointment,
                                       AppointmentTransferRequestDTO requestDTO,
                                       AppointmentDoctorInfo appointmentDoctorInfo,
                                       AppointmentTransactionDetail transactionDetail) {

        Doctor transferredToDoctor = fetchDoctorById(requestDTO.getDoctorId(), requestDTO.getHospitalId());

        Specialization transferredToSpecialization = fetchSpecializationById(requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

        Doctor previousDoctor = fetchDoctorById(appointmentDoctorInfo.getDoctor().getId(), requestDTO.getHospitalId());

        Specialization previousSpecialization = fetchSpecializationById(appointmentDoctorInfo.getSpecialization().getId(),
                requestDTO.getHospitalId());

        AppointmentTransfer appointmentTransfer = parseToAppointmentTransfer(appointment,
                requestDTO,
                transferredToDoctor,
                transferredToSpecialization,
                previousDoctor,
                previousSpecialization);

        Appointment transferredAppointment = parseAppointmentTransferDetail(appointment,
                requestDTO);

        SaveAppointmentDoctorInfo(parseAppointmentDoctorInfo
                (transferredToDoctor,transferredToSpecialization,appointmentDoctorInfo));

        AppointmentTransferTransactionDetail transferTransactionDetail = parseToAppointmentTransferTransactionDetail(
                transactionDetail,
                requestDTO.getAppointmentCharge(),
                requestDTO.getRemarks(),
                appointmentTransfer);

        AppointmentTransactionDetail appointmentTransactionDetail = parseToAppointmentTransactionDetail(
                transactionDetail,
                requestDTO);

        AppointmentTransactionRequestLog requestLog = fetchByTransactionNumber(transactionDetail.getTransactionNumber());

        AppointmentTransferTransactionRequestLog transferTransactionRequestLog =
                parseToAppointmentTransferTransactionRequestLog(requestLog,
                        requestDTO.getRemarks(),
                        transferTransactionDetail);

        AppointmentTransactionRequestLog transactionRequestLog = parseToAppointmentTransactionRequestLog(requestLog);

        save(transferredAppointment, appointmentTransfer);

        saveTransferDetails(transferTransactionDetail,
                transferTransactionRequestLog,
                appointmentTransactionDetail,
                transactionRequestLog);
    }

    @Override
    public AppointmentTransferLogResponseDTO searchTransferredAppointment(AppointmentTransferSearchRequestDTO requestDTO,
                                                                          Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        AppointmentTransferLogResponseDTO appointmentTransferLogDTOS = appointmentTransferRepository.
                getApptTransferredList(requestDTO,
                        pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return appointmentTransferLogDTOS;
    }

    @Override
    public AppointmentTransferPreviewResponseDTO fetchAppointmentTransferDetailById(Long appointmentTransferId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        AppointmentTransferPreviewResponseDTO response = appointmentTransferRepository.fetchAppointmentTransferDetailById(appointmentTransferId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    /* CHECKS AVAILABLE APPT. TIME FROM OVERRIDE TABLE */
    private List<String> checkOverrideRoster(AppointmentTransferTimeRequestDTO requestDTO, List<String> unavailableTime,
                                             Date sqlRequestDate) {

        List<String> finalOverridetime = new ArrayList<>();

        List<OverrideDateAndTimeResponseDTO> overrideDateAndTime = appointmentTransferRepository.getOverideRosterDateAndTime(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

        for (OverrideDateAndTimeResponseDTO override : overrideDateAndTime) {

            List<Date> overrideDates = utilDateListToSqlDateList(getDates(override.getFromDate(),
                    override.getToDate()));

            if (compareIfRequestedDateExists(overrideDates, sqlRequestDate)) {

                List<String> overrideTime = getGapDuration(override.getStartTime(),
                        override.getEndTime(),
                        override.getGapDuration(),
                        requestDTO.getDate());

                finalOverridetime = getVacantTime(overrideTime, unavailableTime);

                break;
            }
        }
        return finalOverridetime;
    }

    /* CHECKS AVAILABLE APPT. TIME FROM DUTY ROSTER TABLE */
    private List<String> checkDutyRoster(AppointmentTransferTimeRequestDTO requestDTO, List<String> unavailableTime,
                                         Date sqlRequestDate) {

        List<String> finaltime = new ArrayList<>();

        List<ActualDateAndTimeResponseDTO> actualDateAndTime = appointmentTransferRepository.getActualTimeByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

        for (ActualDateAndTimeResponseDTO actualDutyRosterDateAndTime : actualDateAndTime) {

            List<Date> actualDates = getActualdate(appointmentTransferRepository.getDayOffDaysByRosterId(actualDutyRosterDateAndTime.getId()),
                    getDates(actualDutyRosterDateAndTime.getFromDate(), actualDutyRosterDateAndTime.getToDate()));


            if (compareIfRequestedDateExists(actualDates, sqlRequestDate)) {

                String code = requestDTO.getDate().toString().substring(0, 3);

                StartTimeAndEndTimeDTO codeAndTime = appointmentTransferRepository.getWeekDaysByRosterIdAndCode(
                        actualDutyRosterDateAndTime.getId(),
                        code);

                List<String> actualTime = getGapDuration(codeAndTime.getStartTime(), codeAndTime.getEndTime(),
                        actualDutyRosterDateAndTime.getGapDuration(), sqlRequestDate);

                finaltime = getVacantTime(actualTime, unavailableTime);

                break;
            }
        }
        return finaltime;
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
        throw new NoContentFoundException(DoctorDutyRoster.class);
    };

    private Supplier<NoContentFoundException> APPOINTMENT_TIME_NOT_AVAILABLE = () -> {
        log.error("Appointment Time Not Available");
        throw new NoContentFoundException("Appointment Time Not Available");
    };

    private void validateAppointmentAmount(Long doctorId, Long hospitalId,
                                           Character isFollowUp, Double appointmentAmount) {

        AppointmentChargeResponseDTO responseDTO = fetchAppointmentCharge(doctorId, hospitalId);

        Double actualAppointmentCharge = isFollowUp.equals(YES)
                ? responseDTO.getFollowUpCharge()
                : responseDTO.getActualCharge();

        if (!(Double.compare(actualAppointmentCharge, appointmentAmount) == 0)) {
            log.error(String.format(DOCTOR_APPOINTMENT_CHARGE_INVALID, appointmentAmount));
            throw new BadRequestException(String.format(DOCTOR_APPOINTMENT_CHARGE_INVALID, appointmentAmount),
                    DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE);
        }
    }

    public AppointmentChargeResponseDTO fetchAppointmentCharge(Long doctorId, Long hospitalId) {
        return appointmentTransferRepository.getAppointmentChargeByDoctorId(doctorId,
                hospitalId);
    }

    private Appointment fetchAppointmentById(Long appointmentId, Long hospitalId) {
        return appointmentRepository.fetchAppointmentById(appointmentId, hospitalId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetailByappointmentId(Long appointmentId) {
        return transactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Doctor fetchDoctorById(Long id, Long hospitalId) {
        return doctorRepository.fetchDoctorById(id, hospitalId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Specialization fetchSpecializationById(Long id, Long hospitalId) {
        return specializationRepository.fetchActiveSpecializationById(id, hospitalId)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private AppointmentTransactionRequestLog fetchByTransactionNumber(String transactionNumber) {
        return transactionRequestLogRepository.fetchByTransactionNumber(transactionNumber)
                .orElseThrow(() -> APPOINTMENT_TRANSACTION_REQUEST_LOG_WITH_GIVEN_TXN_NUMBER_NOT_FOUND
                        .apply(transactionNumber));
    }

    private void save(Appointment appointment, AppointmentTransfer appointmentTransfer) {
        appointmentRepository.save(appointment);
        appointmentTransferRepository.save(appointmentTransfer);
    }

    private void saveTransferTransaction(AppointmentTransferTransactionDetail transferTransactionDetail) {
        transferTransactionRepository.save(transferTransactionDetail);
    }

    private void saveTransferDetails(AppointmentTransferTransactionDetail transferTransactionDetail,
                                     AppointmentTransferTransactionRequestLog transferTransactionRequestLog,
                                     AppointmentTransactionDetail transactionDetail,
                                     AppointmentTransactionRequestLog transactionRequestLog) {
        saveTransferTransaction(transferTransactionDetail);
        transferTransactionRequestLogRepository.save(transferTransactionRequestLog);
        transactionDetailRepository.save(transactionDetail);
        transactionRequestLogRepository.save(transactionRequestLog);
    }

    public void SaveAppointmentDoctorInfo(AppointmentDoctorInfo appointmentDoctorInfo){
        appointmentDoctorInfoRepository.save(appointmentDoctorInfo);
    }

    private Function<String, NoContentFoundException> APPOINTMENT_TRANSACTION_REQUEST_LOG_WITH_GIVEN_TXN_NUMBER_NOT_FOUND = (transactionNumber) -> {
        log.error(CONTENT_NOT_FOUND_BY_APPOINTMENT_NUMBER, APPOINTMENT_TRANSACTION_REQUEST_LOG, transactionNumber);
        throw new NoContentFoundException(AppointmentTransactionRequestLog.class, "transactionNumber", transactionNumber.toString());
    };

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR, id);
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SPECIALIZATION, id);
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };


    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_TRANSACTION_DETAIL, appointmentId);
        throw new NoContentFoundException(AppointmentTransactionDetail.class, "appointmentId", appointmentId.toString());
    };


}
