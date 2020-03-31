package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 31/03/20
 */
public class AppointmentTransactionRequestLogQuery {

    public static final String QUERY_TO_FETCH_APPOINTMENT_TXN_REQUEST_LOG =
            " SELECT a" +
                    " FROM AppointmentTransactionRequestLog a" +
                    " WHERE" +
                    " a.transactionNumber =:transactionNumber" +
                    " AND a.patientName =:name";
}
