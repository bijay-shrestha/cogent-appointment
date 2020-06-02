package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti on 02/06/20
 */
public class AppointmentHospitalDepartmentReservationLogUtils {

    public static AppointmentHospitalDepartmentReservationLog parseToAppointmentHospitalDepartmentReservation(
            AppointmentHospitalDeptFollowUpRequestDTO requestDTO,
            Hospital hospital,
            HospitalDepartment hospitalDepartment,
            BillingMode billingMode,
            Room room) {

        AppointmentHospitalDepartmentReservationLog reservation = new AppointmentHospitalDepartmentReservationLog();
        reservation.setHospital(hospital);
        reservation.setHospitalDepartment(hospitalDepartment);
        reservation.setBillingMode(billingMode);
        reservation.setRoom(room);
        reservation.setAppointmentDate(requestDTO.getAppointmentDate());
        reservation.setAppointmentTime(parseAppointmentTime(
                requestDTO.getAppointmentDate(), requestDTO.getAppointmentTime()));
        reservation.setUserId(requestDTO.getUserId());
        reservation.setCreatedDate(new Date());
        return reservation;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }
}
