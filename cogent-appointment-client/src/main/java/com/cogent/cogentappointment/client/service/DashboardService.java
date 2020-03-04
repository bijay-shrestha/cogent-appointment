package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueTrendResponseDTO;

import java.util.Date;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    RevenueStatisticsResponseDTO getRevenueStatistics(Date previousToDate,
                                                      Date previousFromDate,
                                                      Date currentToDate,
                                                      Date currentFromDate,
                                                      Character filterType);

    AppointmentCountResponseDTO countOverallAppointments(Date toDate, Date fromDate);

    Long getPatientStatistics();

    RevenueTrendResponseDTO getRevenueTrend(Date toDate, Date fromDate);
}
