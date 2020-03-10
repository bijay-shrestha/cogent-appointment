package com.cogent.cogentappointment.admin.service;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DoctorRevenueResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
import org.springframework.data.domain.Pageable;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    RevenueStatisticsResponseDTO getRevenueStatistics(GenerateRevenueRequestDTO requestDTO);

    AppointmentCountResponseDTO countOverallAppointments(DashBoardRequestDTO dashBoardRequestDTO) throws NoSuchAlgorithmException;

    Long getPatientStatistics(Long hospitalId);

    RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO);

    List<DoctorRevenueResponseDTO> getDoctorRevenueList(DoctorRevenueRequestDTO requestDTO, Pageable pagable);

}
