package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 19/02/20
 */
public interface AppointmentReservationLogRepositoryCustom {

    List<String> fetchBookedAppointmentReservations(AppointmentCheckAvailabilityRequestDTO requestDTO);

    Long validateIfAppointmentReservationExists(Date appointmentDate,
                                                String appointmentTime,
                                                Long doctorId,
                                                Long specializationId);
}
