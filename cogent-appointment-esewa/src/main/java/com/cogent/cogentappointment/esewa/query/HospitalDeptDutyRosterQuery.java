package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 29/05/20
 */
public class HospitalDeptDutyRosterQuery {

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_INFO =
            " SELECT h FROM HospitalDepartmentDutyRoster h" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = h.hospitalDepartment.id" +
                    " WHERE h.status = 'Y'" +
                    " AND hd.status = 'Y'" +
                    " AND :date BETWEEN h.fromDate AND h.toDate" +
                    " AND hd.id =:hospitalDepartmentId";
}
