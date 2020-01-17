package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.request.appointment.*;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentAvailabilityResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentAvailabilityResponseDTO checkAvailability(AppointmentRequestDTO appointmentRequestDTO);

    String save(AppointmentRequestDTO appointmentRequestDTO);

    void update(AppointmentUpdateRequestDTO updateRequestDTO);

    void cancel(AppointmentCancelRequestDTO cancelRequestDTO);

    List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO,
                                               Pageable pageable);

    AppointmentResponseDTO fetchDetailsById(Long id);

    void rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

//    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);
//
//    List<AppointmentDateResponseDTO> fetchBookedAppointmentDates(AppointmentDateRequestDTO requestDTO);
}
