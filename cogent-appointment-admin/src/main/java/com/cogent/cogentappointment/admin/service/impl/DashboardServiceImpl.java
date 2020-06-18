package com.cogent.cogentappointment.admin.service.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.*;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.DashboardMessages.DOCTOR_REVENUE_NOT_FOUND;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DashboardConstants.DYNAMIC_DASHBOARD_FEATURE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.DashboardLog.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseToAppointmentCountResponseDTO;
import static com.cogent.cogentappointment.admin.utils.DashboardUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateConverterUtils.dateDifference;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.MathUtils.calculatePercentage;


/**
 * @author Sauravi Thapa २०/२/१०
 */

@Service
@Slf4j
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final AppointmentRepository appointmentRepository;

    private final AdminRepository adminRepository;

    private final PatientRepository patientRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    public DashboardServiceImpl(AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                AppointmentRepository appointmentRepository,
                                AdminRepository adminRepository,
                                PatientRepository patientRepository,
                                AppointmentRefundDetailRepository appointmentRefundDetailRepository) {
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.adminRepository = adminRepository;
        this.patientRepository = patientRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
    }

    //    todo:change api signature
    //todo: remove amount
    @Override
    public RevenueStatisticsResponseDTO getRevenueStatistics(GenerateRevenueRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REVENUE_GENERATED);

        Double currentTransaction = appointmentTransactionDetailRepository.getRevenueByDates(
                requestDTO.getCurrentToDate(),
                requestDTO.getCurrentFromDate(),
                requestDTO.getHospitalId(),
                requestDTO.getAppointmentServiceTypeCode());

        Double previousTransaction = appointmentTransactionDetailRepository.getRevenueByDates(
                requestDTO.getPreviousToDate(),
                requestDTO.getPreviousFromDate(),
                requestDTO.getHospitalId(),
                requestDTO.getAppointmentServiceTypeCode());

        AppointmentRevenueStatisticsResponseDTO appointmentStatistics =
                appointmentTransactionDetailRepository.calculateAppointmentStatistics(
                        requestDTO.getCurrentToDate(),
                        requestDTO.getCurrentFromDate(),
                        requestDTO.getHospitalId(),
                        requestDTO.getAppointmentServiceTypeCode()
                );

        RevenueStatisticsResponseDTO responseDTO = parseToGenerateRevenueResponseDTO(
                currentTransaction,
                calculatePercentage(currentTransaction, previousTransaction),
                requestDTO.getFilterType(),
                appointmentStatistics);

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

        Long followUpPatient = appointmentRepository.countFollowUpPatientByHospitalId(
                dashBoardRequestDTO);

        Character pillType = dateDifference(dashBoardRequestDTO.getToDate(),
                dashBoardRequestDTO.getFromDate());

        log.info(FETCHING_PROCESS_COMPLETED, OVER_ALL_APPOINTMETS, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentCountResponseDTO(overAllAppointment, newPatient, registeredPatient, followUpPatient, pillType);
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
    public Double calculateTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, TOTAL_REFUNDED_AMOUNT);

        Double totalRefundedAmount = appointmentRefundDetailRepository.getTotalRefundedAmount(refundAmountRequestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, TOTAL_REFUNDED_AMOUNT, getDifferenceBetweenTwoTime(startTime));

        return totalRefundedAmount;
    }

    /* 1. CALCULATE ACTUAL DOCTOR REVENUE i.e. APPOINTMENT WITH STATUS EXCEPT REFUNDED ('RE')
     * 2. CALCULATE CANCELLED/COMPANY REVENUE i.e. APPOINTMENT WITH TRANSACTION STATUS 'A' AND
     * APPOINTMENT WITH STATUS 'RE'
     * 3. UNION (1) & (2).
      * ADD TO FINAL LIST CONSIDERING DISTINCT ELEMENTS NEEDS TO BE ADDED AND
      * BASED ON CONDITION-> SAME DOCTOR ID & SPECIALIZATION ID
      * */
    @Override
    public DoctorRevenueResponseDTO calculateOverallDoctorRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,
                                                                  Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_REVENUE);

        List<DoctorRevenueDTO> doctorRevenue =
                appointmentTransactionDetailRepository.calculateDoctorRevenue(doctorRevenueRequestDTO, pageable);

        List<DoctorRevenueDTO> cancelledRevenue =
                appointmentTransactionDetailRepository.calculateCancelledRevenue(doctorRevenueRequestDTO, pageable);

        validateDoctorRevenue(doctorRevenue, cancelledRevenue);

        List<DoctorRevenueDTO> mergedList = mergeDoctorAndCancelledRevenue(doctorRevenue, cancelledRevenue);

        DoctorRevenueResponseDTO responseDTO = parseToDoctorRevenueResponseDTO(mergedList);

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_REVENUE, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DashboardFeatureResponseDTO> getDashboardFeaturesByAdmin(Long adminId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DYNAMIC_DASHBOARD_FEATURE);

        List<DashboardFeatureResponseDTO> responseDTOS =
                adminRepository.fetchDashboardFeaturesByAdmin(adminId);

        log.info(FETCHING_PROCESS_COMPLETED, DYNAMIC_DASHBOARD_FEATURE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DashboardFeatureResponseDTO> fetchAllDashboardFeature() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DYNAMIC_DASHBOARD_FEATURE);

        List<DashboardFeatureResponseDTO> responseDTOS =
                adminRepository.fetchOverAllDashboardFeature();

        log.info(FETCHING_PROCESS_COMPLETED, DYNAMIC_DASHBOARD_FEATURE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private void validateDoctorRevenue(List<DoctorRevenueDTO> doctorRevenue,
                                       List<DoctorRevenueDTO> cancelledRevenue) {

        if (ObjectUtils.isEmpty(doctorRevenue) && ObjectUtils.isEmpty(cancelledRevenue)) {
            log.error(CONTENT_NOT_FOUND, DOCTOR_REVENUE);
            throw new NoContentFoundException(DOCTOR_REVENUE_NOT_FOUND);
        }
    }
}
