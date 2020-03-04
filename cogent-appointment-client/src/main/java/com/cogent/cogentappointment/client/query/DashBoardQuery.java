package com.cogent.cogentappointment.client.query;


import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashBoardQuery {

    public static String QUERY_TO_GET_REVENUE_BY_DATE =
            "SELECT" +
                    " SUM(atd.appointmentAmount)" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " WHERE " +
                    " (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                    " AND (a.status='PA' OR a.status= 'A')" +
                    " AND a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_OVER_ALL_APPOINTMENTS =
            "SELECT" +
                    " COUNT(a.id)" +
                    " FROM Appointment a" +
                    " WHERE " +
                    " (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_COUNT_REGISTERED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =a.patientId.id" +
                    " AND hpi.hospital.id = a.hospitalId.id" +
                    " WHERE hpi.isRegistered='Y'" +
                    " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =a.patientId.id" +
                    " AND hpi.hospital.id = a.hospitalId.id" +
                    " WHERE hpi.isRegistered='N'" +
                    " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                    " AND  a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS =
            "SELECT" +
                    " COUNT(hpi.id)" +
                    " FROM HospitalPatientInfo hpi" +
                    " WHERE " +
                    " hpi.isRegistered='Y'" +
                    " AND hpi.hospital.id=:hospitalId";

    public static String QUERY_TO_FETCH_REVENUE_WEEKLY =
            "SELECT" +
                    "  CASE" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 1" +
                    "    THEN CONCAT('SUN,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 2" +
                    "    THEN CONCAT('MON,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 3" +
                    "    THEN CONCAT('TUE,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 4" +
                    "    THEN CONCAT('WED,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 5" +
                    "    THEN CONCAT('THU,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 6" +
                    "    THEN CONCAT('FRI,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 7" +
                    "    THEN CONCAT('SAT,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                    "  END AS day," +
                    "  COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " WHERE " +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND (a.status='PA' OR a.status= 'A')" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " GROUP BY atd.transactionDate" +
                    " ORDER BY atd.transactionDate";


    public static String QUERY_TO_FETCH_REVENUE_YEARLY =
            " SELECT" +
                    " DATE_FORMAT(atd.transactionDate, '%b,%Y')," +
                    " COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND (a.status='PA' OR a.status= 'A')" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " GROUP BY DATE_FORMAT(atd.transactionDate, '%b,%Y')" +
                    " ORDER BY DATE_FORMAT(atd.transactionDate, '%b,%Y')";

    public static String QUERY_TO_FETCH_REVENUE_MONTHLY =
            "SELECT" +
                    " DATE_FORMAT(atd.transactionDate , '%e %b') As transactionDate," +
                    " COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND (a.status='PA' OR a.status= 'A')" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " GROUP BY atd.transactionDate" +
                    " ORDER BY atd.transactionDate";

    public static String QUERY_TO_FETCH_REVENUE_DAILY =
            "SELECT" +
                    " DATE_FORMAT(atd.transactionDate , '%e %b,%Y') As transactionDate," +
                    " COALESCE(SUM(atd.appointmentAmount),0) as revenue" +
                    " FROM" +
                    " AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON" +
                    " a.id = atd.appointment.id" +
                    " LEFT JOIN Hospital h ON" +
                    " h.id = a.hospitalId.id" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND (a.status='PA' OR a.status= 'A')" +
                    " AND a.hospitalId.id =:hospitalId" +
                    " GROUP BY" +
                    " atd.transactionDate" +
                    " ORDER BY" +
                    " atd.transactionDate";

    public static Function<AppointmentQueueRequestDTO, String> QUERY_TO_FETCH_TODAY_APPOINTMENT_QUEUE =
            (searchDTO) ->
                    "SELECT" +
                            " DATE_FORMAT(a.appointmentTime,'%H:%i %p') as appointmentTime," +
                            " d.name as doctorName," +
                            " p.name as patientName," +
                            " p.mobileNumber as patientMobileNumber," +
                            " s.name as specializationName," +
                            " CASE WHEN" +
                            " (dv.status is null OR dv.status = 'N')" +
                            " THEN null" +
                            " ELSE" +
                            " dv.fileUri" +
                            " END as doctorAvatar" +
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                            " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                            " LEFT JOIN DoctorSpecialization ds ON ds.doctorId.id = d.id" +
                            " LEFT JOIN DoctorAvatar dv ON dv.doctorId.id = d.id" +
                            " LEFT JOIN Specialization s ON s.id = ds.specializationId.id" +
                            " LEFT JOIN Hospital h ON h.id = a.hospitalId.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(searchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(AppointmentQueueRequestDTO appointmentQueueRequestDTO) {

        String whereClause = " WHERE " +
                " s.status='Y' " +
                " AND a.status='PA'" +
                " AND DATE(a.appointmentDate) = CURDATE()" +
                " AND h.id= :hospitalId";

        if (!Objects.isNull(appointmentQueueRequestDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentQueueRequestDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentTime DESC";

        return whereClause;
    }
}
