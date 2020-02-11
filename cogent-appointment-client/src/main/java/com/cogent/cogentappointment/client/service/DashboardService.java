package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.GenerateRevenueResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueStatisticsResponseDTO;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    GenerateRevenueResponseDTO getRevenueGeneratedDetail(GenerateRevenueRequestDTO requestDTO);

    AppointmentCountResponseDTO countOverallAppointments(DashBoardRequestDTO dashBoardRequestDTO);

    Long countOverallRegisteredPatients(Long hospitalId);

    RevenueStatisticsResponseDTO getRevenueStatistic(DashBoardRequestDTO dashBoardRequestDTO);
}
