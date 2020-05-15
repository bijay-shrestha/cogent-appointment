package com.cogent.cogentappointment.esewa.query;

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

    public static final String QUERY_TO_FETCH_APPOINTMENT_TXN_REQUEST_LOG_STATUS =
            " SELECT a.transactionStatus" +
                    " FROM AppointmentTransactionRequestLog a" +
                    " WHERE" +
                    " a.transactionNumber =:transactionNumber" +
                    " AND a.patientName =:name";
}
