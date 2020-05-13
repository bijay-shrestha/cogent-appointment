package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentTransferService;
import com.cogent.cogentappointment.admin.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
