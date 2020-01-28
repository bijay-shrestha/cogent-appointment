package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.*;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentResponseDTO;
import com.cogent.cogentappointment.dto.request.appointment.*;
import com.cogent.cogentappointment.dto.response.appointment.*;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentCheckAvailabilityResponseDTO checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO);

    String save(AppointmentRequestDTO appointmentRequestDTO);

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
