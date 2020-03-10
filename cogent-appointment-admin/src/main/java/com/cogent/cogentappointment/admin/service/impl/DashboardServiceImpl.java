package com.cogent.cogentappointment.admin.service.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DoctorRevenueResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentTransactionDetailRepository;
import com.cogent.cogentappointment.admin.repository.PatientRepository;
import com.cogent.cogentappointment.admin.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
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
    public RevenueStatisticsResponseDTO getRevenueStatistics(GenerateRevenueRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REVENUE_GENERATED);

        Double currentTransaction = appointmentTransactionDetailRepository.getRevenueByDates(
                requestDTO.getCurrentToDate(),
                requestDTO.getCurrentFromDate(),
                requestDTO.getHospitalId());

        Double previousTransaction = appointmentTransactionDetailRepository.getRevenueByDates(
                requestDTO.getPreviousToDate(),
                requestDTO.getPreviousFromDate(),
                requestDTO.getHospitalId());

        RevenueStatisticsResponseDTO responseDTO = parseToGenerateRevenueResponseDTO(currentTransaction,
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
    public Long getPatientStatistics(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, OVER_ALL_REGISTERED_PATIENTS);

        Long resgisteredPatients = patientRepository.countOverallRegisteredPatients(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, OVER_ALL_REGISTERED_PATIENTS, getDifferenceBetweenTwoTime(startTime));

        return resgisteredPatients;
    }

    @Override
    public RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REVENUE_STATISTICS);

        Character filter = dateDifference(dashBoardRequestDTO.getToDate(),
                dashBoardRequestDTO.getFromDate());

        RevenueTrendResponseDTO revenueTrendResponseDTO = appointmentTransactionDetailRepository
                .getRevenueTrend(dashBoardRequestDTO, filter);

        Map<String, String> map = revenueTrendResponseDTO.getData();

        if (!isMapContainsEveryField
                (map, dashBoardRequestDTO.getToDate(), filter)) {
            map = addRemainingFields
                    (revenueTrendResponseDTO.getData(),
                            dashBoardRequestDTO.getFromDate(),
                            dashBoardRequestDTO.getToDate(), filter);
        }

        revenueTrendResponseDTO.setData(map);

        log.info(FETCHING_PROCESS_COMPLETED, REVENUE_STATISTICS, getDifferenceBetweenTwoTime(startTime));

        return revenueTrendResponseDTO;
    }

    @Override
    public List<DoctorRevenueResponseDTO> getDoctorRevenueList(DoctorRevenueRequestDTO requestDTO,
                                                               Pageable pagable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_REVENUE);

        List<DoctorRevenueResponseDTO> doctorRevenueResponseDTO = appointmentTransactionDetailRepository
                .getDoctorRevenue(requestDTO, pagable);

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_REVENUE, getDifferenceBetweenTwoTime(startTime));

        return doctorRevenueResponseDTO;

    }

}
