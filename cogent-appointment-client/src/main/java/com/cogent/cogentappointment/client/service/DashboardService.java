package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueResponseListDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueTrendResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    RevenueStatisticsResponseDTO getRevenueStatistics(GenerateRevenueRequestDTO requestDTO);

    AppointmentCountResponseDTO countOverallAppointments(DashBoardRequestDTO dashBoardRequestDTO);

    Long getPatientStatistics();

    RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO);

    Double calculateTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO);

    DoctorRevenueResponseListDTO getDoctorRevenueList(Date toDate, Date fromDate, DoctorRevenueRequestDTO doctorRevenueRequestDTO, Pageable pagable);
}
