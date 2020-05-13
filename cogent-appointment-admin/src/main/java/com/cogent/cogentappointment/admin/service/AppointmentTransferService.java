package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentDateRequestDTO;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public interface AppointmentTransferService {
    List<Date> fetchAvailableDatesByDoctorId(AppointmentDateRequestDTO requestDTO);
}
