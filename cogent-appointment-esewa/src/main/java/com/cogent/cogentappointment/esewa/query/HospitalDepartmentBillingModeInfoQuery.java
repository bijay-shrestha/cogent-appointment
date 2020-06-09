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
                    " AND hb.hospitalDepartment.status = 'Y'" +
                    " AND hb.hospitalDepartment.id =:hospitalDepartmentId";

    /*DIFFERENCE BETWEEN QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_DEPARTMENT_ID
    * AND QUERY_TO_FETCH_MIN_BILLING_MODE -> FETCH BILLING MODE SEPARATELY OR
    * WITH COMBINATION OF HOSPITAL DEPARTMENT API*/
    public static final String QUERY_TO_FETCH_MIN_BILLING_MODE =
            "SELECT " +
                    " hb.id as hospitalDepartmentBillingModeId," +                                 //[0]
                    " hb.billingMode.name as billingModeName" +                                   //[1]
                    " FROM" +
                    " HospitalDepartmentBillingModeInfo hb" +
                    " WHERE" +
                    " hb.status='Y'" +
                    " AND hb.hospitalDepartment.status = 'Y'" +
                    " AND hb.hospitalDepartment.id =:hospitalDepartmentId"+
                    " ORDER BY billingModeName ASC";

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
