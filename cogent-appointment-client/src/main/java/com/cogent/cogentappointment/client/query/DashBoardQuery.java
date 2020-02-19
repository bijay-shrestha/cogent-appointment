package com.cogent.cogentappointment.client.query;


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
                    " AND a.status='A'"+
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
                    " LEFT JOIN HospitalPatientInfo hpi ON a.patientId.id=hpi.patientId" +
                    " WHERE hpi.isRegistered='Y'" +
                    " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";


    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalPatientInfo hpi ON a.patientId.id=hpi.patientId" +
                    " WHERE hpi.isRegistered='N'" +
                    " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                    " AND  a.hospitalId.id=:hospitalId";


    public static String QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS =
            "SELECT" +
                    " COUNT(hpi.id)" +
                    " FROM HospitalPatientInfo hpi" +
                    " WHERE " +
                    " hpi.isRegistered='Y'" +
                    " AND hpi.hospitalId=:hospitalId";

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
                    " AND a.status='A'" +
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
                    " AND a.status='A'" +
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
                    " AND a.status='A'" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " GROUP BY atd.transactionDate" +
                    " ORDER BY atd.transactionDate";

    public static String QUERY_TO_FETCH_REVENUE_DAILY =
            "SELECT" +
                    " 'DAILY'," +
                    " COALESCE(SUM(atd.appointmentAmount),0) as revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a On a.id=atd.appointment.id" +
                    " LEFT JOIN Hospital h ON h.id=a.hospitalId.id" +
                    " WHERE atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND a.status='A'" +
                    " AND a.hospitalId.id=:hospitalId";
}
