package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 06/07/20
 */
public class PKIAuthenticationInfoQuery {

    public static final String QUERY_TO_FETCH_PKI_SERVER_PRIVATE_KEY =
            " SELECT" +
                    " p.serverPrivateKey as name" +
                    " FROM PKIAuthenticationInfo p" +
                    " WHERE p.clientId =:clientId" +
                    " AND p.status = 'Y'";

    public static final String QUERY_TO_FETCH_PKI_CLIENT_PUBLIC_KEY =
            " SELECT" +
                    " p.clientPublicKey" +
                    " FROM PKIAuthenticationInfo p" +
                    " WHERE p.clientId =:clientId" +
                    " AND p.status = 'Y'";
}
