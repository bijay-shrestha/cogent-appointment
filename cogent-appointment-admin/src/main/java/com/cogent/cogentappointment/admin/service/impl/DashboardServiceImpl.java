package com.cogent.cogentappointment.admin.service.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.GenerateRevenueResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentTransactionDetailRepository;
import com.cogent.cogentappointment.admin.repository.PatientRepository;
import com.cogent.cogentappointment.admin.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.DashboardLog.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseToAppointmentCountResponseDTO;
import static com.cogent.cogentappointment.admin.utils.DashboardUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateConverterUtils.dateDifference;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.MathUtils.calculatePercenatge;


/**
 * @author Sauravi Thapa २०/२/१०
 */

@Service
@Slf4j
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    public DashboardServiceImpl(AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public GenerateRevenueResponseDTO getRevenueGeneratedDetail(GenerateRevenueRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REVENUE_GENERATED);

        Double currentTransaction = appointmentTransactionDetailRepository.getRevenueByDates(requestDTO.getCurrentToDate(),
                requestDTO.getCurrentFromDate(), requestDTO.getHospitalId());

        Double previousTransaction = appointmentTransactionDetailRepository.getRevenueByDates(requestDTO.getPreviousToDate(),
                requestDTO.getPreviousFromDate(), requestDTO.getHospitalId());

        GenerateRevenueResponseDTO responseDTO = parseToGenerateRevenueResponseDTO(currentTransaction,
                calculatePercenatge(currentTransaction, previousTransaction),
                requestDTO.getFilterType());

        log.info(FETCHING_PROCESS_COMPLETED, REVENUE_GENERATED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentCountResponseDTO countOverallAppointments(DashBoardRequestDTO dashBoardRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, OVER_ALL_APPOINTMETS);

        Long overAllAppointment = appointmentRepository.countOverAllAppointment(dashBoardRequestDTO);

        Long newPatient = appointmentRepository.countNewPatientByHospitalId(dashBoardRequestDTO);

        Long registeredPatient = appointmentRepository.countRegisteredPatientByHospitalId(dashBoardRequestDTO);

        Character pillType = dateDifference(dashBoardRequestDTO.getToDate(),
                dashBoardRequestDTO.getFromDate());

        log.info(FETCHING_PROCESS_COMPLETED, OVER_ALL_APPOINTMETS, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentCountResponseDTO(overAllAppointment, newPatient, registeredPatient, pillType);
    }

    @Override
    public Long countOverallRegisteredPatients(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, OVER_ALL_REGISTERED_PATIENTS);

        Long resgisteredPatients = patientRepository.countOverallRegisteredPatients(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, OVER_ALL_REGISTERED_PATIENTS, getDifferenceBetweenTwoTime(startTime));

        return resgisteredPatients;
    }

    @Override
    public RevenueStatisticsResponseDTO getRevenueStatistic(DashBoardRequestDTO dashBoardRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REVENUE_STATISTICS);

        Character filter = dateDifference(dashBoardRequestDTO.getToDate(),
                dashBoardRequestDTO.getFromDate());

        RevenueStatisticsResponseDTO revenueStatisticsResponseDTO = appointmentTransactionDetailRepository
                .getRevenueStatistics(dashBoardRequestDTO, filter);

        Map<String, String> map = revenueStatisticsResponseDTO.getData();

        if (!isMapContainsEveryField(map, dashBoardRequestDTO.getToDate(), filter)) {
            map = addRemainingFields(revenueStatisticsResponseDTO.getData(),
                    dashBoardRequestDTO.getFromDate(),
                    dashBoardRequestDTO.getToDate(), filter);
        }

        revenueStatisticsResponseDTO.setData(map);
        log.info(FETCHING_PROCESS_COMPLETED, REVENUE_STATISTICS, getDifferenceBetweenTwoTime(startTime));

        return revenueStatisticsResponseDTO;
    }
}
