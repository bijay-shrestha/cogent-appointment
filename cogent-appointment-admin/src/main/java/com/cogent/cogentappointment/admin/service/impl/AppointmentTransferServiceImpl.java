package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentDateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentTransferService;
import com.cogent.cogentappointment.admin.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransferLog.APPOINTMENT_TRANSFER;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransferLog.FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransferLog.FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.utils.AppointmentTransferUtils.getActualdate;
import static com.cogent.cogentappointment.admin.utils.AppointmentTransferUtils.mergeOverrideAndActualDateList;
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
}
