package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author smriti on 18/02/20
 */
public class AppointmentReservationUtils {

    public static AppointmentReservationLog parseToAppointmentReservation(AppointmentFollowUpRequestDTO requestDTO) {

        AppointmentReservationLog reservation = new AppointmentReservationLog();
        reservation.setReservedAppointmentDate(requestDTO.getAppointmentDate());
        reservation.setReservedAppointmentTime(parseAppointmentTime(
                requestDTO.getAppointmentDate(),
                requestDTO.getAppointmentTime()));
        reservation.setHospitalId(requestDTO.getHospitalId());
        reservation.setDoctorId(requestDTO.getDoctorId());
        reservation.setSpecializationId(requestDTO.getSpecializationId());
        reservation.setCreatedDate(new Date());
        return reservation;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }

}
