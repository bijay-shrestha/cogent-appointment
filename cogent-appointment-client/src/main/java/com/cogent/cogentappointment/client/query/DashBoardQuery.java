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

}
