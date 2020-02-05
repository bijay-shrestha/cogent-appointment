package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.*;
import com.cogent.cogentappointment.client.dto.response.appointment.*;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentCheckAvailabilityResponseDTO checkAvailabilityWithEndTime(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentCheckAvailabilityMinResponseDTO checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentSuccessResponseDTO save(AppointmentRequestDTO appointmentRequestDTO);

    List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                       Date toDate,
                                                                       Long doctorId,
                                                                       Long specializationId);

    Long fetchBookedAppointmentCount(Date fromDate, Date toDate,
                                     Long doctorId, Long specializationId);

    void update(AppointmentUpdateRequestDTO updateRequestDTO);

    void cancel(AppointmentCancelRequestDTO cancelRequestDTO);

    List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO,
                                               Pageable pageable);

    AppointmentResponseDTO fetchDetailsById(Long id);


    void rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

//    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);
//

}
