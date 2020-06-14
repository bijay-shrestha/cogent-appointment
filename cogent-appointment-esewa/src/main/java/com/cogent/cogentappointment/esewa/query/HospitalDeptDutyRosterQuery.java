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

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_WITHOUT_ROOM =
            " SELECT h FROM HospitalDepartmentDutyRoster h" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = h.hospitalDepartment.id" +
                    " WHERE" +
                    " h.status = 'Y'" +
                    " AND hd.status = 'Y'" +
                    " AND h.isRoomEnabled = 'N'" +
                    " AND :date BETWEEN h.fromDate AND h.toDate" +
                    " AND hd.id =:hospitalDepartmentId";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_WITH_ROOM =
            " SELECT h FROM HospitalDepartmentDutyRoster h" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = h.hospitalDepartment.id" +
                    " LEFT JOIN HospitalDepartmentDutyRosterRoomInfo hr ON h.id = hr.hospitalDepartmentDutyRoster.id" +
                    " WHERE" +
                    " h.status = 'Y'" +
                    " AND hd.status = 'Y'" +
                    " AND h.isRoomEnabled = 'Y'" +
                    " AND :date BETWEEN h.fromDate AND h.toDate" +
                    " AND hd.id =:hospitalDepartmentId" +
                    " AND hr.hospitalDepartmentRoomInfo.id =:hospitalDepartmentRoomInfoId";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_BY_HOSPITAL_DEPARTMENT_ID =
            " SELECT hddr FROM HospitalDepartmentDutyRoster hddr" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = hddr.hospitalDepartment.id" +
                    " WHERE" +
                    " hddr.status = 'Y'" +
                    " AND hddr.status = 'Y'" +
                    " AND (hddr.fromDate>=CURDATE() OR hddr.toDate >=CURDATE())" +
                    " AND hd.id =:hospitalDepartmentId";
}
