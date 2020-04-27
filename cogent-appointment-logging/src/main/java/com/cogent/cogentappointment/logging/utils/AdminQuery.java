package com.cogent.cogentappointment.logging.utils;
/**
 * @author smriti on 2019-08-05
 */
public class AdminQuery {

    public static final String QUERY_TO_VALIDATE_ADMIN_COUNT =
            " SELECT " +
                    " COUNT(a.id)," +                   //[0]
                    " h.numberOfAdmins" +               //[1]
                    " FROM Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE h.id = :hospitalId" +
                    " AND a.status !='D'";

    public static final String QUERY_TO_FIND_ADMIN_FOR_VALIDATION =
            "SELECT " +
                    " a.email," +                               //[1]
                    " a.mobileNumber" +                        //[2]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " a.status != 'D'" +
                    " AND h.status!='D'" +
                    " AND" +
                    " (a.email =:email OR a.mobileNumber = :mobileNumber)" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_GET_LOGGED_ADMIN_INFO =
            "SELECT" +
                    " a.id as id," +
                    " a.email as email," +
                    " a.password as password," +
                    " h.isCompany as isCompany," +
                    " h.code as hospitalCode," +
                    " h.id as hospitalId," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM " +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " LEFT JOIN Department d ON d.id=p.department.id" +
                    " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                    " LEFT JOIN HmacApiInfo hai ON hai.hospital.id=h.id" +
                    " WHERE" +
                    " (a.mobileNumber=:email OR a.email=:email)" +
                    " AND a.status = 'Y'" +
                    " AND h.isCompany='N'";



}