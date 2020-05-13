package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.*;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public interface AppointmentTransferService {
    List<Date> fetchAvailableDatesByDoctorId(AppointmentDateRequestDTO requestDTO);

    List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO);

    Double fetchDoctorChargeByDoctorId(DoctorChargeRequestDTO requestDTO);

    AppointmentTransferLogResponseDTO searchTransferredAppointment(AppointmentTransferSearchRequestDTO requestDTO,
                                                                   Pageable pageable);
}
