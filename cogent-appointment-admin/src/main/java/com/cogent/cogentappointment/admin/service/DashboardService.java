package com.cogent.cogentappointment.admin.service;


import com.cogent.cogentappointment.admin.dto.request.dashboard.*;
import com.cogent.cogentappointment.admin.dto.response.dashboard.*;
import org.springframework.data.domain.Pageable;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    RevenueStatisticsResponseDTO getRevenueStatistics(GenerateRevenueRequestDTO requestDTO);

    AppointmentCountResponseDTO countOverallAppointments(DashBoardRequestDTO dashBoardRequestDTO) throws NoSuchAlgorithmException;

    Long getPatientStatistics(Long hospitalId);

    RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO);

    Double calculateTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO);

    DoctorRevenueResponseDTO calculateOverallDoctorRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,
                                                           Pageable pageable);

    HospitalDepartmentRevenueResponseDTO calculateOverallHospitalDeptRevenue(HospitalDepartmentRevenueRequestDTO revenueRequestDTO,
                                                           Pageable pageable);

    List<DashboardFeatureResponseDTO> getDashboardFeaturesByAdmin(Long adminId);

    List<DashboardFeatureResponseDTO> fetchAllDashboardFeature();
}
