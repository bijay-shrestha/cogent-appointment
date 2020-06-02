package com.cogent.cogentappointment.esewa.query;

import java.util.Objects;

/**
 * @author smriti on 19/02/20
 */
public class AppointmentHospitalDepartmentReservationLogQuery {

    public static String QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG =
            "SELECT DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime" +               //[0]
                    " FROM AppointmentReservationLog a" +
                    " WHERE" +
                    " a.appointmentDate = :date" +
                    " AND a.doctorId = :doctorId" +
                    " AND a.specializationId= :specializationId";

    public static String QUERY_TO_FETCH_APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG_ID(Long roomId) {

        String query = "SELECT a.id" +
                " FROM AppointmentHospitalDepartmentReservationLog a" +
                " WHERE" +
                " a.appointmentDate =:appointmentDate" +
                " AND a.hospitalDepartment.id =:hospitalDepartmentId" +
                " AND a.hospital.id =:hospitalId" +
                " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime";

        if (!Objects.isNull(roomId))
            query += " AND a.room.id =" + roomId;

        return query;
    }


}
