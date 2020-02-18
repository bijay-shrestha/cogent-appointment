package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentReservation;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;

import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author smriti on 18/02/20
 */
public class AppointmentReservationUtils {

    public static AppointmentReservation parseToAppointmentReservation(AppointmentFollowUpRequestDTO requestDTO,
                                                                       Hospital hospital,
                                                                       Doctor doctor,
                                                                       Specialization specialization) {

        AppointmentReservation reservation = new AppointmentReservation();
        reservation.setReservedAppointmentDate(requestDTO.getAppointmentDate());
        reservation.setReservedAppointmentTime(parseAppointmentTime(
                requestDTO.getAppointmentDate(),
                requestDTO.getAppointmentTime()));
        reservation.setHospitalId(hospital);
        reservation.setDoctorId(doctor);
        reservation.setSpecializationId(specialization);
        return reservation;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }

}
