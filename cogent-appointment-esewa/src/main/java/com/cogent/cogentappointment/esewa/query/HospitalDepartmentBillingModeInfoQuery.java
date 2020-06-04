package com.cogent.cogentappointment.esewa.query;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class HospitalDepartmentBillingModeInfoQuery {

    public static final String QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_DEPARTMENT_ID =
            "SELECT " +
                    " hb.id as value," +                                                //[0]
                    " hb.billingMode.name as label" +                                   //[1]
                    " FROM" +
                    " HospitalDepartmentBillingModeInfo hb" +
                    " WHERE" +
                    " hb.status='Y'" +
                    " hb.hospitalDepartment.status = 'Y'" +
                    " AND hb.hospitalDepartment.id =:hospitalDepartmentId";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_CHARGE=
            " SELECT" +
                    " hb.appointmentCharge as appointmentCharge" +                      //[0]
                    " FROM HospitalDepartmentBillingModeInfo hb " +
                    " WHERE" +
                    " hb.id=:hospitalDepartmentBillingModeId " +
                    " AND hb.hospitalDepartment.id =:hospitalDepartmentId ";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOLLOW_UP_CHARGE=
            " SELECT" +
                    " hb.appointmentFollowUpCharge as appointmentCharge" +               //[0]
                    " FROM HospitalDepartmentBillingModeInfo hb " +
                    " WHERE" +
                    " hb.id=:hospitalDepartmentBillingModeId " +
                    " AND hb.hospitalDepartment.id =:hospitalDepartmentId ";

}
