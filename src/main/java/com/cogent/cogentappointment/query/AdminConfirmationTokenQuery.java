package com.cogent.cogentappointment.query;

/**
 * @author smriti on 2019-09-23
 */
public class AdminConfirmationTokenQuery {

    public static final String QUERY_TO_FETCH_CONFIRMATION_TOKEN_STATUS =
            "SELECT a.status" +
                    " FROM AdminConfirmationToken a" +
                    " WHERE" +
                    " a.confirmationToken = :confirmationToken";

}
