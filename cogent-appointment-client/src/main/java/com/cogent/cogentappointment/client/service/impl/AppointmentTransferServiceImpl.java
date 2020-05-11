package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.*;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.AppointmentTransferService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransactionDetailLog.APPOINTMENT_TRANSACTION_DETAIL;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransactionRequestLogConstant.APPOINTMENT_TRANSACTION_REQUEST_LOG;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransactionRequestLogConstant.CONTENT_NOT_FOUND_BY_APPOINTMENT_NUMBER;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransferLog.*;
import static com.cogent.cogentappointment.client.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.client.log.constants.SpecializationLog.SPECIALIZATION;
import static com.cogent.cogentappointment.client.utils.AppointmentTransferUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Service
@Slf4j
@Transactional
public class AppointmentTransferServiceImpl implements AppointmentTransferService {

    private final AppointmentTransferRepository repository;

    private final AppointmentTransferTransactionDetailRepository transferTransactionRepository;

    private final AppointmentTransferTransactionRequestLogRepository transferTransactionRequestLogRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTransactionDetailRepository transactionDetailRepository;

    private final AppointmentTransactionRequestLogRepository transactionRequestLogRepository;

    private final DoctorRepository doctorRepository;

    private final SpecializationRepository specializationRepository;

    public AppointmentTransferServiceImpl(AppointmentTransferRepository repository,
                                          AppointmentTransferTransactionDetailRepository transferTransactionRepository,
                                          AppointmentTransferTransactionRequestLogRepository transferTransactionRequestLogRepository,
                                          AppointmentRepository appointmentRepository,
                                          AppointmentTransactionDetailRepository transactionDetailRepository,
                                          AppointmentTransactionRequestLogRepository transactionRequestLogRepository,
                                          DoctorRepository doctorRepository,
                                          SpecializationRepository specializationRepository) {
        this.repository = repository;
        this.transferTransactionRepository = transferTransactionRepository;
        this.transferTransactionRequestLogRepository = transferTransactionRequestLogRepository;
        this.appointmentRepository = appointmentRepository;
        this.transactionDetailRepository = transactionDetailRepository;
        this.transactionRequestLogRepository = transactionRequestLogRepository;
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
    }

    /* FETCH APPOINTMENT DATES BASED ON DOCTOR ID AND SPECIALIZATION ID */
    @Override
    public List<Date> fetchAvailableDatesByDoctorId(AppointmentDateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<Date> actualDate = new ArrayList<>();

        List<Date> overrideDate = new ArrayList<>();

        List<DoctorDatesResponseDTO> rosterDates = repository.getDatesByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

        rosterDates.forEach(rosterId -> {

            List<Date> dates = getActualdate(repository.getDayOffDaysByRosterId(rosterId.getId()),
                    getDates(rosterId.getFromDate(), rosterId.getToDate()));

            actualDate.addAll(dates);
        });

        List<DoctorDatesResponseDTO> overrideDates = repository.getOverrideDatesByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

        overrideDates.forEach(date -> {
            overrideDate.addAll(getDates(date.getFromDate(), date.getToDate()));
        });

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return utilDateListToSqlDateList(mergeOverrideAndActualDateList(overrideDate, actualDate));
    }

    /* FETCH VACANT APPOINTMENT TIME BASED ON SPECIFIC DATE, DOCTOR ID AND SPECIALIZATION ID */
    @Override
    public List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<String> time = new ArrayList<>();

        List<String> unavailableTime = repository.getUnavailableTimeByDateAndDoctorId(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getDate());

        Date sqlRequestDate = utilDateToSqlDate(requestDTO.getDate());

        time.addAll(checkOverride(requestDTO, unavailableTime, sqlRequestDate));

        if (time.size() == 0) {
            time.addAll(checkActual(requestDTO, unavailableTime, sqlRequestDate));
        }

        if (time.isEmpty())
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return time;
    }

    /* FETCH DOCTOR CHARGE BY DOCTOR ID
    *
    *  IF FOLLOWUP - 'N' ACTUAL CHARGE
    *  ELSE FOLLOWUP CHARGE*/
    @Override
    public Double fetchDoctorChargeByDoctorId(DoctorChargeRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        AppointmentChargeResponseDTO charge = repository.getAppointmentChargeByDoctorId(requestDTO.getDoctorId());

        Double response = requestDTO.getFollowUp().equals(NO) ? charge.getActualCharge() : charge.getFollowUpCharge();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public void appointmentTransfer(AppointmentTransferRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPOINTMENT_TRANSFER_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        Appointment appointment = fetchAppointmentById(requestDTO.getAppointmentId());

        AppointmentTransactionDetail transactionDetail = fetchAppointmentTransactionDetailByappointmentId(
                requestDTO.getAppointmentId());

        Long specializationId = appointment.getSpecializationId().getId();

        if (specializationId == requestDTO.getSpecializationId() &&
                (transactionDetail.getAppointmentAmount().compareTo(requestDTO.getAppointmentCharge()) == 0)) {

            AppointmentTransfer appointmentTransfer = parseToAppointmentTransfer(appointment,
                    requestDTO.getRemarks());

            Appointment transferredAppointment = parseAppointmentTransferDetail(appointment,
                    requestDTO,
                    fetchDoctorById(requestDTO.getDoctorId()));

            save(transferredAppointment, appointmentTransfer);
        } else {
            AppointmentTransfer appointmentTransfer = parseToAppointmentTransfer(appointment,
                    requestDTO.getRemarks());

            Appointment transferredAppointment = parseAppointmentForSpecialization(appointment,
                    requestDTO,
                    fetchDoctorById(requestDTO.getDoctorId()),
                    fetchSpecializationById(requestDTO.getSpecializationId()));

            AppointmentTransferTransactionDetail transferTransactionDetail = parseToAppointmentTransferTransactionDetail(
                    transactionDetail,
                    requestDTO.getRemarks(),appointmentTransfer);

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

        log.info(APPOINTMENT_TRANSFER_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentTransferLogDTO> searchTransferredAppointment(AppointmentTransferSearchRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<AppointmentTransferLogDTO> appointmentTransferLogDTOS=repository.getFinalAppTransferredInfo(requestDTO);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return appointmentTransferLogDTOS;
    }

    /* CHECKS AVAILABLE APPT. TIME FROM OVERRIDE TABLE */
    private List<String> checkOverride(AppointmentTransferTimeRequestDTO requestDTO, List<String> unavailableTime,
                                       Date sqlRequestDate) {

        List<String> finalOverridetime = new ArrayList<>();

        List<OverrideDateAndTimeResponseDTO> overrideDateAndTime = repository.getOverideRosterDateAndTime(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

        for (OverrideDateAndTimeResponseDTO override : overrideDateAndTime) {

            List<Date> overrideDates = utilDateListToSqlDateList(getDates(override.getFromDate(),
                    override.getToDate()));

            Date date = compareAndGetDate(overrideDates, sqlRequestDate);

            if (!Objects.isNull(date)) {

                List<String> overrideTime = getGapDuration(override.getStartTime(),
                        override.getEndTime(),
                        override.getGapDuration());

                finalOverridetime = getVacantTime(overrideTime, unavailableTime, date);
            }
        }
        return finalOverridetime;
    }

    /* CHECKS AVAILABLE APPT. TIME FROM DUTY ROSTER TABLE */
    private List<String> checkActual(AppointmentTransferTimeRequestDTO requestDTO, List<String> unavailableTime,
                                     Date sqlRequestDate) {

        List<String> finaltime = new ArrayList<>();

        List<ActualDateAndTimeResponseDTO> actualDateAndTime = repository.getActualTimeByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

        for (ActualDateAndTimeResponseDTO actual : actualDateAndTime) {

            List<Date> actualDates = getActualdate(repository.getDayOffDaysByRosterId(actual.getId()),
                    getDates(actual.getFromDate(), actual.getToDate()));

            for (Date actualDate : actualDates) {

                if (actualDate.equals(sqlRequestDate)) {

                    String code = requestDTO.getDate().toString().substring(0, 3);

                    WeekDayAndTimeDTO codeAndTime = repository.getWeekDaysByCode(requestDTO.getDoctorId(), code);

                    List<String> actualTime = getGapDuration(codeAndTime.getStartTime(), codeAndTime.getEndTime(),
                            actual.getGapDuration());

                    finaltime = getVacantTime(actualTime, unavailableTime, actualDate);
                }
            }
        }
        return finaltime;
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
        throw new NoContentFoundException(DoctorDutyRoster.class);
    };

    private Appointment fetchAppointmentById(Long appointmentId) {
        return appointmentRepository.fetchAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Doctor fetchDoctorById(Long id) {
        return doctorRepository.fetchDoctorById(id)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private AppointmentTransactionRequestLog fetchByTransactionNumber(String transactionNumber) {
        return transactionRequestLogRepository.fetchByTransactionNumber(transactionNumber)
                .orElseThrow(() -> APPOINTMENT_TRANSACTION_REQUEST_LOG_WITH_GIVEN_TXN_NUMBER_NOT_FOUND
                        .apply(transactionNumber));
    }

    private Specialization fetchSpecializationById(Long id) {
        return specializationRepository.fetchActiveSpecializationById(id)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetailByappointmentId(Long appointmentId) {
        return transactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private void save(Appointment appointment, AppointmentTransfer appointmentTransfer) {
        appointmentRepository.save(appointment);
        repository.save(appointmentTransfer);
    }

    private void saveTransferDetails(AppointmentTransferTransactionDetail transferTransactionDetail,
                                     AppointmentTransferTransactionRequestLog transferTransactionRequestLog,
                                     AppointmentTransactionDetail transactionDetail,
                                     AppointmentTransactionRequestLog transactionRequestLog) {
        transferTransactionRepository.save(transferTransactionDetail);
        transferTransactionRequestLogRepository.save(transferTransactionRequestLog);
        transactionDetailRepository.save(transactionDetail);
        transactionRequestLogRepository.save(transactionRequestLog);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR, id);
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SPECIALIZATION, id);
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_TRANSACTION_DETAIL, appointmentId);
        throw new NoContentFoundException(AppointmentTransactionDetail.class, "appointmentId", appointmentId.toString());
    };

    private Function<String, NoContentFoundException> APPOINTMENT_TRANSACTION_REQUEST_LOG_WITH_GIVEN_TXN_NUMBER_NOT_FOUND = (transactionNumber) -> {
        log.error(CONTENT_NOT_FOUND_BY_APPOINTMENT_NUMBER, APPOINTMENT_TRANSACTION_REQUEST_LOG, transactionNumber);
        throw new NoContentFoundException(AppointmentTransactionRequestLog.class, "transactionNumber", transactionNumber.toString());
    };


}
