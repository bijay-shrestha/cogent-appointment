package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedTimeResponseDTO;
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

    List<AppointmentBookedTimeResponseDTO> checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO);

    String generateAppointmentNumber(String nepaliCreatedDate);

    List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                       Date toDate,
                                                                       Long doctorId,
                                                                       Long specializationId);

    Long fetchBookedAppointmentCount(Date fromDate, Date toDate, Long doctorId, Long specializationId);

    List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO, Pageable pageable);

    AppointmentResponseDTO fetchDetailsById(Long id);

//    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);


}
