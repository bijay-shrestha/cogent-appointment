package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class HospitalDepartmentBillingModeInfoQuery {

    public static final String QUERY_TO_GET_CHARGE_BY_BILLING_MODE_AND_HOSPITAL_DEPARTMENT_ID =
            "SELECT" +
                    " hb.appointmentCharge as appointmentCharge," +
                    " hb.appointmentFollowUpCharge as followUpCharge" +
                    " FROM" +
                    " HospitalDepartmentBillingModeInfo hb" +
                    " WHERE hb.billingMode.id=:billingModeId " +
                    " AND hb.hospitalDepartment.id =:hospitalDepartmentId ";

}
