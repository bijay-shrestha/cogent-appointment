package com.cogent.cogentappointment.client.query;


import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueRequestDTO;

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
                    " LEFT JOIN HospitalPatientInfo hpi ON a.patientId=hpi.patientId" +
                    " WHERE hpi.isRegistered='Y'" +
                    " AND hpi.hospitalId=:hospitalId" +
                    " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)";


    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalPatientInfo hpi ON a.patientId=hpi.patientId" +
                    " WHERE hpi.isRegistered='N'" +
                    " AND  hpi.hospitalId=:hospitalId" +
                    " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)";


    public static String QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS =
            "SELECT" +
                    " COUNT(hpi.id)" +
                    " FROM HospitalPatientInfo hpi" +
                    " WHERE " +
                    " hpi.hospitalId=:hospitalId" +
                    " AND hpi.isRegistered='Y'";

    public static String QUERY_TO_FETCH_REVENUE_WEEKLY =
            "SELECT" +
                    "  CASE" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 1" +
                    "    THEN 'SUN'" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 2" +
                    "    THEN 'MON'" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 3" +
                    "    THEN 'TUE'" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 4" +
                    "    THEN 'WED'" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 5" +
                    "    THEN 'THU'" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 6" +
                    "    THEN 'FRI'" +
                    "  WHEN DAYOFWEEK(atd.transactionDate) = 7" +
                    "    THEN 'SAT'" +
                    "  END AS day," +
                    "  COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " WHERE " +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND" +
                    " a.hospitalId.id=:hospitalId" +
                    " GROUP BY atd.transactionDate" +
                    " ORDER BY atd.transactionDate";


    public static String QUERY_TO_FETCH_REVENUE_YEARLY =
            " SELECT" +
                    "  MONTHNAME(atd.transactionDate) as monthName," +
                    "  COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                    "  FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND" +
                    " a.hospitalId.id=:hospitalId" +
                    " GROUP BY MONTHNAME(atd.transactionDate)";

    public static String QUERY_TO_FETCH_REVENUE_MONTHLY =
            "SELECT" +
                    " DATE_FORMAT(atd.transactionDate, '%b %e') AS Week," +
                    " COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND" +
                    " a.hospitalId.id=:hospitalId" +
                    " GROUP BY DATE_FORMAT(atd.transactionDate, '%b %e')";

    public static Function<AppointmentQueueRequestDTO, String> QUERY_TO_FETCH_TODAY_APPOINTMENT_QUEUE =
            (appointmentqueueSearchDTO) ->
                    "SELECT" +
                            " a.appointmentTime as appointmentTime," +
                            " d.name as doctorName," +
                            " p.name as patientName," +
                            " p.mobileNumber as patientMobileNumber," +
                            " s.name as specializationName," +
                            " dv.fileUri as doctorAvatar" +
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                            " LEFT JOIN Doctor d ON d.id=a.doctorId.id" +
                            " LEFT JOIN DoctorSpecialization ds ON ds.doctorId.id=d.id" +
                            " LEFT JOIN DoctorAvatar dv ON dv.doctorId.id=d.id" +
                            " LEFT JOIN Specialization s ON s.id=ds.specializationId.id" +
                            " LEFT JOIN Hospital h ON h.id=a.hospitalId.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(appointmentqueueSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(AppointmentQueueRequestDTO appointmentQueueRequestDTO) {

        String whereClause = " WHERE " +
                " s.status='Y' " +
                " AND a.status='PA'" +
                " AND DATE(a.appointmentDate) = CURDATE()" +
                " AND h.id= :hospitalId";

        if (!Objects.isNull(appointmentQueueRequestDTO.getDoctorId()))
            whereClause += " AND d.id = '" + appointmentQueueRequestDTO.getDoctorId() + "'";

        whereClause += " ORDER BY a.appointmentTime DESC";

        return whereClause;
    }
}
