package com.cogent.cogentappointment.admin.query;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashBoardQuery {

    public static String CLAUSE_TO_FIND_BY_HOSPITAL_ID(Long hospitalId) {
        if (hospitalId != null) {
            return " AND a.hospitalId=" + hospitalId;
        }
        return "";
    }

    public static String CLAUSE_TO_FIND_BY_HOSPITAL_ID_FOR_OVERALL_PATIENT(Long hospitalId) {
       if (hospitalId > 0){
            return  " AND hpi.hospitalId="+hospitalId ;
        }
        return "";
    }

    public static String QUERY_TO_GET_REVENUE_BY_DATE(Long hospitalId) {
        return "SELECT" +
                " SUM(atd.appointmentAmount)" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " WHERE " +
                " (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                " AND a.status='A'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_OVER_ALL_APPOINTMENTS(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id)" +
                " FROM Appointment a" +
                " WHERE " +
                " (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_COUNT_REGISTERED_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id)" +
                " FROM Appointment a" +
                " LEFT JOIN HospitalPatientInfo hpi ON a.patientId=hpi.patientId" +
                " WHERE hpi.isRegistered='Y'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id)" +
                " FROM Appointment a" +
                " LEFT JOIN HospitalPatientInfo hpi ON a.patientId=hpi.patientId" +
                " WHERE hpi.isRegistered='N'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS(Long hospitalId) {
        return "SELECT" +
                " COUNT(hpi.id)" +
                " FROM HospitalPatientInfo hpi" +
                " WHERE " +
                " hpi.isRegistered='Y'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID_FOR_OVERALL_PATIENT(hospitalId);
    }

    ;

    public static String QUERY_TO_FETCH_REVENUE_WEEKLY(Long hospitalId) {
        return "SELECT" +
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
                " AND a.status='A'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY atd.transactionDate" +
                " ORDER BY atd.transactionDate";
    }


    public static String QUERY_TO_FETCH_REVENUE_YEARLY(Long hospitalId) {
        return " SELECT" +
                "  MONTHNAME(atd.transactionDate) as monthName," +
                "  COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                "  FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " WHERE" +
                " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " AND a.status='A'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY MONTHNAME(atd.transactionDate)";
    }


    public static String QUERY_TO_FETCH_REVENUE_MONTHLY(Long hospitalId) {
        return "SELECT" +
                " DATE_FORMAT(atd.transactionDate, '%b %e') AS Week," +
                " COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " WHERE" +
                " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " AND a.status='A'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY DATE_FORMAT(atd.transactionDate, '%b %e')";
    }

    public static String QUERY_TO_FETCH_REVENUE_DAILY(Long hospitalId) {
        return "SELECT" +
                " 'DAILY'," +
                " COALESCE(SUM(atd.appointmentAmount),0) as revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a On a.id=atd.appointment.id" +
                " LEFT JOIN Hospital h ON h.id=a.hospitalId.id" +
                " WHERE atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " AND a.status='A'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

}
