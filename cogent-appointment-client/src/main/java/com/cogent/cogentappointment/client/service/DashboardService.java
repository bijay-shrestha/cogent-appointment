package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.dashboard.AppointmentCountRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.GenerateRevenueResponseDTO;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public interface DashboardService {
    GenerateRevenueResponseDTO getRevenueGeneratedDetail(GenerateRevenueRequestDTO requestDTO);

    AppointmentCountResponseDTO countOverallAppointments(AppointmentCountRequestDTO appointmentCountRequestDTO);

    Long countOverallRegisteredPatients();
}
