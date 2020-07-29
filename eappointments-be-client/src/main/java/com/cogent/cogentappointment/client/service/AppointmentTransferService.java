package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.*;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.previewDTO.AppointmentTransferPreviewResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public interface AppointmentTransferService {
    List<Date> fetchAvailableDatesByDoctorIdAndSpecializationId(AppointmentDateRequestDTO requestDTO);

    List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO);

    Double fetchDoctorChargeByDoctorId(DoctorChargeRequestDTO requestDTO);

    void appointmentTransfer(AppointmentTransferRequestDTO requestDTO);

    AppointmentTransferLogResponseDTO searchTransferredAppointment(AppointmentTransferSearchRequestDTO requestDTO,
                                                                   Pageable pageable);

    AppointmentTransferPreviewResponseDTO fetchAppointmentTransferDetailById(Long id);
}
