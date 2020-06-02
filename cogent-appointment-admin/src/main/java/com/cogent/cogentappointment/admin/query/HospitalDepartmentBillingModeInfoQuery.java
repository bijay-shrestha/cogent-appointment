package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

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
