package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    RevenueStatisticsResponseDTO getRevenueStatistics(GenerateRevenueRequestDTO requestDTO);

    AppointmentCountResponseDTO countOverallAppointments(DashBoardRequestDTO dashBoardRequestDTO);

    Long getPatientStatistics();

    RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO);

    List<DashboardFeatureResponseDTO> getDashboardFeaturesByAdmin(Long adminId);

    List<DashboardFeatureResponseDTO> fetchAllDashboardFeature();

    Double calculateTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO);

    DoctorRevenueResponseDTO calculateOverallDoctorRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,
                                                   Pageable pageable);
}
