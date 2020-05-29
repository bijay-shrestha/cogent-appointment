package com.cogent.cogentappointment.admin.query;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
public class HospitalBillingModeInfoQuery {

    public static String QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID =
            "SELECT" +
                    " hb.billingMode.id as value," +
                    " hb.billingMode.name as label" +
                    " FROM HospitalBillingModeInfo hb" +
                    " WHERE hb.hospital.id=:hospitalId" +
                    " AND hb.status!='D'";

}
