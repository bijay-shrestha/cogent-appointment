package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashBoardQuery {

    public static String QUERY_TO_GET_REVENUE_BY_DATE =
            "SELECT" +
                    " SUM(atd.appointmentAmount)" +
                    " FROM AppointmentTransactionDetail atd" +
                    " WHERE " +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate";

    public static String QUERY_TO_COUNT_REGISTERED_APPOINTMENT=
            "SELECT" +
                    " COUNT(a.id)" +
                    " FROM Appointment a" +
                    " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                    " WHERE (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.status='A'" +
                    " AND h.id=:hospitalId";

    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT=
            "SELECT" +
                    " COUNT(a.id)" +
                    " FROM Appointment a" +
                    " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                    " WHERE (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.status!='A'" +
                    " AND h.id=:hospitalId";


}
