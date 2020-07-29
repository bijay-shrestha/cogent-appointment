package com.cogent.cogentappointment.logging.query;

/**
 * @author Sauravi Thapa २०/२/२
 */
public class HmacApiInfoQuery {
    public static final String QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION_FOR_ADMIN =
            " SELECT" +
                    " a.fullName as fullName," +
                    " a.email as email," +
                    " h.code as hospitalCode," +
                    " h.isCompany as isCompany," +
                    " h.id as hospitalId," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " LEFT JOIN Profile p ON p.company.id=h.id" +
                    " LEFT JOIN Admin a ON a.profileId.id=p.id" +
                    " WHERE" +
                    " a.email=:email" +
                    " AND" +
                    " a.status='Y'" +
                    " AND" +
                    " h.code=:hospitalCode" +
                    " AND" +
                    " hai.apiKey=:apiKey";

    public static final String QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION_FOR_CLIENT =
            " SELECT" +
                    " a.fullName as fullName," +
                    " a.email as email," +
                    " h.code as hospitalCode," +
                    " h.isCompany as isCompany," +
                    " h.id as hospitalId," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM HmacApiInfo hai" +
                    " LEFT JOIN Hospital h ON h.id=hai.hospital.id" +
                    " LEFT JOIN Department d ON h.id=d.hospital.id"+
                    " LEFT JOIN Profile p ON p.department.id=d.id" +
                    " LEFT JOIN Admin a ON a.profileId.id=p.id" +
                    " WHERE" +
                    " a.email=:email" +
                    " AND" +
                    " a.status='Y'" +
                    " AND" +
                    " h.code=:hospitalCode" +
                    " AND" +
                    " hai.apiKey=:apiKey";


}
