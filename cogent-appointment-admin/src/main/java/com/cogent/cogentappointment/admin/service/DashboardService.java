package com.cogent.cogentappointment.admin.service;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.GenerateRevenueResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueStatisticsResponseDTO;

import java.security.NoSuchAlgorithmException;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    GenerateRevenueResponseDTO getRevenueGeneratedDetail(GenerateRevenueRequestDTO requestDTO);

    AppointmentCountResponseDTO countOverallAppointments(DashBoardRequestDTO dashBoardRequestDTO) throws NoSuchAlgorithmException;

    Long countOverallRegisteredPatients(Long hospitalId);

    RevenueStatisticsResponseDTO getRevenueStatistic(DashBoardRequestDTO dashBoardRequestDTO);
}
