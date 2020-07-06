package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 06/07/20
 */
public class PKIAuthenticationInfoQuery {

    public static final String QUERY_TO_FETCH_PKI_SERVER_PRIVATE_KEY =
            " SELECT" +
                    " pki.serverPrivateKey" +
                    " FROM PKIAuthenticationInfo pki" +
                    " WHERE pki.clientId =:clientId" +
                    " AND pki.status = 'Y'";

    public static final String QUERY_TO_FETCH_PKI_CLIENT_PUBLIC_KEY =
            " SELECT" +
                    " pki.clientPublicKey" +
                    " FROM PKIAuthenticationInfo pki" +
                    " WHERE pki.clientId =:clientId" +
                    " AND pki.status = 'Y'";
}
