package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.request.appointment.AppointmentSearchRequestDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentTimeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRepositoryCustom")
public interface AppointmentRepositoryCustom {

    List<AppointmentTimeResponseDTO> checkAvailability(Date date, Long doctorId,
                                                       Long specializationId);

    String generateAppointmentNumber(String nepaliCreatedDate);

    List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO, Pageable pageable);

    AppointmentResponseDTO fetchDetailsById(Long id);

//    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);

//    List<AppointmentDateResponseDTO> fetchBookedAppointmentDates(AppointmentDateRequestDTO requestDTO);
}
