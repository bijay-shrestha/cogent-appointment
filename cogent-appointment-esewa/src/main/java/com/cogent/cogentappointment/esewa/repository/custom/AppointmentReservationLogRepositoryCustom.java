package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility.AppointmentCheckAvailabilityRequestDTO;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 19/02/20
 */
public interface AppointmentReservationLogRepositoryCustom {

    List<String> fetchBookedAppointmentReservations(AppointmentCheckAvailabilityRequestDTO requestDTO);

    Long fetchAppointmentReservationLogId(Date appointmentDate, String appointmentTime,
                                          Long doctorId, Long specializationId, Long hospitalId);
}
