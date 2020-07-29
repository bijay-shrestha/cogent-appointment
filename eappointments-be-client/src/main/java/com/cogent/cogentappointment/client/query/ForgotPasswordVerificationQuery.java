package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 2019-09-20
 */
public class ForgotPasswordVerificationQuery {

    public static final String QUERY_TO_FETCH_EXPIRATION_TIME =
            "SELECT f.expirationDate" +
                    " FROM ForgotPasswordVerification f" +
                    " WHERE" +
                    " f.resetCode = :resetCode" +
                    " AND f.status = 'Y'";
}
