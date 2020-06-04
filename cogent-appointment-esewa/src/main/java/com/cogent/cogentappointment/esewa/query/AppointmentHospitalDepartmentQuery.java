package com.cogent.cogentappointment.esewa.query;

import java.util.Objects;

/**
 * @author smriti on 01/06/20
 */
public class AppointmentHospitalDepartmentQuery {

    /*USED IN CHECK AVAILABILITY OF HOSPITAL DEPARTMENT APPOINTMENT*/
    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_HOSPITAL_DEPT_WISE(Long hospitalDepartmentRoomInfoId) {

        String query = "SELECT DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime" +               //[0]
                " FROM Appointment a" +
                " LEFT JOIN AppointmentHospitalDepartmentInfo ah ON a.id = ah.appointment.id" +
                " WHERE" +
                " a.appointmentDate = :date" +
                " AND ah.hospitalDepartment.id = :hospitalDepartmentId" +
                " AND a.status = 'PA'";

        if (!Objects.isNull(hospitalDepartmentRoomInfoId))
            query += " AND ah.hospitalDepartmentRoomInfo.id =" + hospitalDepartmentRoomInfoId;

        return query;
    }

    public static String QUERY_TO_VALIDATE_IF_APPOINTMENT_EXISTS_DEPT_WISE(Long hospitalDepartmentRoomInfoId) {

        String query = "SELECT COUNT(a.id)" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentHospitalDepartmentInfo ah ON a.id = ah.appointment.id" +
                " WHERE" +
                " a.appointmentDate =:appointmentDate" +
                " AND ah.hospitalDepartment.id =:hospitalDepartmentId" +
                " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime" +
                " AND a.status='PA'";

        if (!Objects.isNull(hospitalDepartmentRoomInfoId))
            query += " AND ah.hospitalDepartmentRoomInfo.id =" + hospitalDepartmentRoomInfoId;

        return query;
    }

}
