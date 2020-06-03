package com.cogent.cogentappointment.esewa.query;

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

    public static final String QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_DEPARTMENT_ID =
            "SELECT " +
                    " hb.id as value," +
                    " hb.billingMode.name as label" +
                    " FROM" +
                    " HospitalDepartmentBillingModeInfo hb" +
                    " WHERE" +
                    " hb.status='Y'" +
                    " hb.hospitalDepartment.status = 'Y'" +
                    " AND hb.hospitalDepartment.id =:hospitalDepartmentId";

}
