package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa २०/२/२
 */
public class HmacApiInfoQuery {

    public static final String QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_AUTHENTICATION =
            " SELECT" +
                    " h.code as hospitalCode," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " WHERE" +
                    " h.code=:hospitalCode" +
                    " AND" +
                    " hai.apiKey=:apiKey" +
                    " AND" +
                    " h.status='Y'";

    public static final String QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_HMAC_GENERATION =
            " SELECT" +
                    " h.code as hospitalCode," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " WHERE" +
                    " h.code=:hospitalCode" +
                    " AND" +
                    " h.status='Y'";

    public static final String QUERY_TO_VERIFY_LOGGED_IN_ADMIN =
            " SELECT" +
                    " a.fullName as fullName," +
                    " a.username as username," +
                    " a.email as email," +
                    " a.password as password," +
                    " h.code as hospitalCode," +
                    " h.id as hospitalId," +
                    " h.isCogentAdmin as isCogentAdmin," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " LEFT JOIN Department d ON d.hospital.id=h.id" +
                    " LEFT JOIN Profile p ON p.department.id=d.id" +
                    " LEFT JOIN Admin a ON a.profileId.id=p.id" +
                    " WHERE" +
                    " a.username=:username" +
                    " AND" +
                    " a.status='Y'" +
                    " AND" +
                    " h.code=:hospitalCode";

    public static final String QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION =
            " SELECT" +
                    " a.fullName as fullName," +
                    " a.username as username," +
                    " a.email as email," +
                    " h.code as hospitalCode," +
                    " h.isCogentAdmin as isCogentAdmin," +
                    " h.id as hospitalId," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " LEFT JOIN Department d ON d.hospital.id=h.id" +
                    " LEFT JOIN Profile p ON p.department.id=d.id" +
                    " LEFT JOIN Admin a ON a.profileId.id=p.id" +
                    " WHERE" +
                    " a.username=:username" +
                    " AND" +
                    " a.status='Y'" +
                    " AND" +
                    " h.code=:hospitalCode" +
                    " AND" +
                    " hai.apiKey=:apiKey";

}
