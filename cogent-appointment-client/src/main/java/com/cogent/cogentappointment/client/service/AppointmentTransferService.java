package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferTimeRequestDTO;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public interface AppointmentTransferService {
    List<Date> fetchAvailableDatesByDoctorId(Long doctorId);

    List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO);
}
