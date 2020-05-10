package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentDateRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferTimeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.DoctorChargeRequestDTO;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public interface AppointmentTransferService {
    List<Date> fetchAvailableDatesByDoctorId(AppointmentDateRequestDTO requestDTO);

    List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO);

    Double fetchDoctorChargeByDoctorId(DoctorChargeRequestDTO requestDTO);

    void appointmentTransfer(AppointmentTransferRequestDTO requestDTO);
}
