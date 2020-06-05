package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class BillingModeQuery {

    public static String QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_ID =
            "SELECT " +
                    " bm" +
                    " FROM" +
                    " BillingMode bm" +
                    " LEFT JOIN HospitalBillingModeInfo hb ON hb.billingMode.id = bm.id AND hb.status='Y'" +
                    " WHERE" +
                    " bm.id =:billingModeId" +
                    " AND bm.status='Y'" +
                    " AND hb.hospital.id =:hospitalId";

    public static String QUERY_TO_GET_BILLING_MODE_BY_HOSPITAL_ID =
            "SELECT " +
                    " bm" +
                    " FROM" +
                    " BillingMode bm" +
                    " LEFT JOIN HospitalBillingModeInfo hb ON hb.billingMode.id = bm.id AND hb.status!='D'" +
                    " AND hb.status != 'D'" +
                    " WHERE" +
                    " bm.id =:billingModeId" +
                    " AND hb.hospital.id =:hospitalId";


    public static String QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID =
            "SELECT" +
                    " hb.billingMode.id as value," +
                    " hb.billingMode.name as label" +
                    " FROM HospitalBillingModeInfo hb" +
                    " WHERE hb.hospital.id=:hospitalId" +
                    " AND hb.status!='D'";

    public static String QUERY_TO_GET_ACTIVE_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID =
            "SELECT" +
                    " hb.billingMode.id as value," +
                    " hb.billingMode.name as label" +
                    " FROM HospitalBillingModeInfo hb" +
                    " WHERE hb.hospital.id=:hospitalId" +
                    " AND hb.status='Y'";

}