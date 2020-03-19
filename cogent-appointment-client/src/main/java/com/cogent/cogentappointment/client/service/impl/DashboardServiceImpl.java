package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.repository.AppointmentTransactionDetailRepository;
import com.cogent.cogentappointment.client.repository.PatientRepository;
import com.cogent.cogentappointment.client.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.client.constants.CacheConstant.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.DashboardLog.*;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.parseToAppointmentCountResponseDTO;
import static com.cogent.cogentappointment.client.utils.DashboardUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateConverterUtils.dateDifference;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.MathUtils.calculatePercenatge;
import static com.cogent.cogentappointment.client.utils.commons.MathUtils.calculateTotalTransactionAmount;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author Sauravi Thapa २०/२/१०
 */

@Service
@Slf4j
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    public DashboardServiceImpl(AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Cacheable(CACHE_REVENUE_STATISTICS)
    public RevenueStatisticsResponseDTO getRevenueStatistics(Date previousToDate,
                                                             Date previousFromDate,
                                                             Date currentToDate,
                                                             Date currentFromDate,
                                                             Character filterType) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REVENUE_STATISTICS);

        Long hospitalId = getLoggedInHospitalId();

        Double currentTransaction = appointmentTransactionDetailRepository.getRevenueByDates(
                currentToDate,
                currentFromDate,
                hospitalId);

        Double currentRefundedAmount = appointmentRefundDetailRepository.getRefundByDates(currentToDate,
                currentFromDate,
                hospitalId);

        Double previousTransaction = appointmentTransactionDetailRepository.getRevenueByDates(
                previousToDate,
                previousFromDate,
                hospitalId);

        Double previousRefundedAmount = appointmentRefundDetailRepository.getRefundByDates(previousToDate,
                previousFromDate,
                hospitalId);

        RevenueStatisticsResponseDTO responseDTO = parseToGenerateRevenueResponseDTO(
                calculateTotalTransactionAmount(currentTransaction, currentRefundedAmount),
                calculatePercenatge(calculateTotalTransactionAmount(currentTransaction, currentRefundedAmount),
                        calculateTotalTransactionAmount(previousTransaction, previousRefundedAmount)),
                filterType);

        log.info(FETCHING_PROCESS_COMPLETED, REVENUE_STATISTICS, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    @Cacheable(CACHE_OVERALL_APPOINTMENTS)
    public AppointmentCountResponseDTO countOverallAppointments(Date toDate, Date fromDate) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, OVER_ALL_APPOINTMETS);

        Long hospitalId = getLoggedInHospitalId();

        Long overAllAppointment = appointmentRepository.countOverAllAppointment(toDate, fromDate, hospitalId);

        Long newPatient = appointmentRepository.countNewPatientByHospitalId(toDate, fromDate, hospitalId);

        Long registeredPatient = appointmentRepository.countRegisteredPatientByHospitalId(
                toDate, fromDate, hospitalId);

        Character pillType = dateDifference(toDate,
                fromDate);

        log.info(FETCHING_PROCESS_COMPLETED, OVER_ALL_APPOINTMETS, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentCountResponseDTO(overAllAppointment, newPatient, registeredPatient, pillType);
    }

    @Override
    @Cacheable(CACHE_OVERALL_REGISTERED_PATIENTS)
    public Long getPatientStatistics() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, OVER_ALL_REGISTERED_PATIENTS);

        Long hospitalId = getLoggedInHospitalId();

        Long resgisteredPatients = patientRepository.countOverallRegisteredPatients(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, OVER_ALL_REGISTERED_PATIENTS, getDifferenceBetweenTwoTime(startTime));

        return resgisteredPatients;
    }

    @Override
    @Cacheable(CACHE_REVENUE_TREND)
    public RevenueTrendResponseDTO getRevenueTrend(Date toDate, Date fromDate) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REVENUE_TREND);

        Long hospitalId = getLoggedInHospitalId();

        Character filter = dateDifference(toDate,
                fromDate);

        RevenueTrendResponseDTO revenueTrendResponseDTO = appointmentTransactionDetailRepository
                .getRevenueTrend(toDate, fromDate, hospitalId, filter);

        Map<String, String> map = revenueTrendResponseDTO.getData();

        if (!isMapContainsEveryField
                (map, toDate, filter)) {
            map = addRemainingFields
                    (revenueTrendResponseDTO.getData(),
                            fromDate,
                            toDate, filter);
        }

        revenueTrendResponseDTO.setData(map);

        log.info(FETCHING_PROCESS_COMPLETED, REVENUE_TREND, getDifferenceBetweenTwoTime(startTime));

        return revenueTrendResponseDTO;
    }

    @Override
    @Cacheable(CACHE_DOCTOR_REVENUE_TRACKER)
    public List<DoctorRevenueResponseDTO> getDoctorRevenueTracker(Date toDate, Date fromDate, Pageable pagable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_REVENUE);

        List<DoctorRevenueResponseDTO> doctorRevenueResponseDTO = appointmentTransactionDetailRepository
                .getDoctorRevenueTracker(toDate, fromDate, getLoggedInHospitalId(), pagable);

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_REVENUE, getDifferenceBetweenTwoTime(startTime));

        return doctorRevenueResponseDTO;

    }

}
