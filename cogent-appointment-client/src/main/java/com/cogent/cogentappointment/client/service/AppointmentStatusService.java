package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author smriti ON 16/12/2019
 */
public interface AppointmentStatusService {

    AppointmentStatusDTO fetchAppointmentStatusResponseDTO( Date toDate, Date fromDate, Long specializationId,
                                                            Long doctorId, String status);
}
