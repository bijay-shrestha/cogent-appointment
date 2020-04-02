package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;

import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti on 18/02/20
 */
public class AppointmentReservationLogUtils {

    public static AppointmentReservationLog parseToAppointmentReservation(AppointmentFollowUpRequestDTO requestDTO) {

        AppointmentReservationLog reservation = new AppointmentReservationLog();
        reservation.setAppointmentDate(requestDTO.getAppointmentDate());
        reservation.setAppointmentTime(parseAppointmentTime(
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
