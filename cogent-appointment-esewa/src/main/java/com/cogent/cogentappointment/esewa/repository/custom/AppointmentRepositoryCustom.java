package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRepositoryCustom")
public interface AppointmentRepositoryCustom {

    Long validateIfAppointmentExists(Date appointmentDate, String appointmentTime,
                                     Long doctorId, Long specializationId);

    String generateAppointmentNumber(String nepaliCreatedDate, Long hospitalId);

    List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentSearchDTO searchDTO);

    AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId);

    List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentSearchDTO searchDTO);

    List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(AppointmentCheckAvailabilityRequestDTO requestDTO);

    Double calculateRefundAmount(Long appointmentId);
}