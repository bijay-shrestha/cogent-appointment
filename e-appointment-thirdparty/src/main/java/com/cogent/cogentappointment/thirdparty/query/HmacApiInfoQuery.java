package com.cogent.cogentappointment.thirdparty.query;

/**
 * @author Sauravi Thapa २०/२/२
 */
public class HmacApiInfoQuery {

    public static final String QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_AUTHENTICATION =
            " SELECT" +
                    " h.code as companyCode," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " WHERE" +
                    " h.code=:companyCode" +
                    " AND" +
                    " hai.apiKey=:apiKey" +
                    " AND" +
                    " h.status='Y'";

    public static final String QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_HMAC_GENERATION =
            " SELECT" +
                    " h.code as companyCode," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " WHERE" +
                    " h.code=:companyCode" +
                    " AND" +
                    " h.status='Y'";


}
