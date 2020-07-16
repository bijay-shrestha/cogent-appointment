package com.cogent.cogentappointment.esewa.query;

import java.util.Objects;

/**
 * @author smriti on 19/02/20
 */
public class AppointmentHospitalDepartmentReservationLogQuery {

    public static String QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG(Long hospitalDepartmentRoomInfoId) {
        String query = "SELECT DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime" +               //[0]
                " FROM AppointmentHospitalDepartmentReservationLog a" +
                " WHERE" +
                " a.appointmentDate = :appointmentDate" +
                " AND a.hospitalDepartment.id =:hospitalDepartmentId";

        if (!Objects.isNull(hospitalDepartmentRoomInfoId))
            query += " AND a.hospitalDepartmentRoomInfo.id =" + hospitalDepartmentRoomInfoId;

        return query;
    }

    public static String QUERY_TO_FETCH_APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG_ID(
            Long hospitalDepartmentRoomInfoId) {

        String query = "SELECT a.id" +
                " FROM AppointmentHospitalDepartmentReservationLog a" +
                " WHERE" +
                " a.appointmentDate =:appointmentDate" +
                " AND a.hospitalDepartment.id =:hospitalDepartmentId" +
                " AND a.hospital.id =:hospitalId" +
                " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime";

        if (!Objects.isNull(hospitalDepartmentRoomInfoId))
            query += " AND a.hospitalDepartmentRoomInfo.id =" + hospitalDepartmentRoomInfoId;

        return query;
    }


}
