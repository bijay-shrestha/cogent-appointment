package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentDateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentTransferTimeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.DoctorChargeRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentTransferService;
import com.cogent.cogentappointment.admin.service.DoctorService;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SEARCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SEARCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransferLog.*;
import static com.cogent.cogentappointment.admin.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.admin.utils.AppointmentTransferUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Service
@Slf4j
@Transactional
public class AppointmentTransferServiceImpl implements AppointmentTransferService {

    private final AppointmentTransferRepository repository;

    private final DoctorService doctorService;

    private final AppointmentTransferTransactionDetailRepository transferTransactionRepository;

    private final AppointmentTransferTransactionRequestLogRepository transferTransactionRequestLogRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTransactionDetailRepository transactionDetailRepository;

    private final AppointmentTransactionRequestLogRepository transactionRequestLogRepository;

    private final DoctorRepository doctorRepository;

    private final SpecializationRepository specializationRepository;

    public AppointmentTransferServiceImpl(AppointmentTransferRepository repository,
                                          DoctorService doctorService,
                                          AppointmentTransferTransactionDetailRepository transferTransactionRepository,
                                          AppointmentTransferTransactionRequestLogRepository transferTransactionRequestLogRepository,
                                          AppointmentRepository appointmentRepository,
                                          AppointmentTransactionDetailRepository transactionDetailRepository,
                                          AppointmentTransactionRequestLogRepository transactionRequestLogRepository,
                                          DoctorRepository doctorRepository,
                                          SpecializationRepository specializationRepository) {
        this.repository = repository;
        this.doctorService = doctorService;
        this.transferTransactionRepository = transferTransactionRepository;
        this.transferTransactionRequestLogRepository = transferTransactionRequestLogRepository;
        this.appointmentRepository = appointmentRepository;
        this.transactionDetailRepository = transactionDetailRepository;
        this.transactionRequestLogRepository = transactionRequestLogRepository;
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
    }

    @Override
    public List<Date> fetchAvailableDatesByDoctorId(AppointmentDateRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<Date> actualDate = new ArrayList<>();

        List<Date> overrideDate = new ArrayList<>();

        List<DoctorDatesResponseDTO> rosterDates = repository.getDatesByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

        rosterDates.forEach(rosterId -> {

            List<Date> dates = getActualdate(repository.getDayOffDaysByRosterId(rosterId.getId()),
                    getDates(rosterId.getFromDate(), rosterId.getToDate()));

            actualDate.addAll(dates);
        });

        List<DoctorDatesResponseDTO> overrideDates = repository.getOverrideDatesByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

        overrideDates.forEach(date -> {
            overrideDate.addAll(getDates(date.getFromDate(), date.getToDate()));
        });

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return utilDateListToSqlDateList(mergeOverrideAndActualDateList(overrideDate, actualDate));
    }

    @Override
    public List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<String> time = new ArrayList<>();

        List<String> unavailableTime = repository.getUnavailableTimeByDateAndDoctorId(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getDate(),
                requestDTO.getHospitalId());

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

    @Override
    public Double fetchDoctorChargeByDoctorId(DoctorChargeRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        AppointmentChargeResponseDTO charge = repository.getAppointmentChargeByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getHospitalId());

        Double response = requestDTO.getFollowUp().equals(NO) ? charge.getActualCharge() : charge.getFollowUpCharge();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public AppointmentTransferLogResponseDTO searchTransferredAppointment(AppointmentTransferSearchRequestDTO requestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        AppointmentTransferLogResponseDTO appointmentTransferLogDTOS = repository.getApptTransferredList(requestDTO,
                pageable);

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
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

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
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId());

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

}
