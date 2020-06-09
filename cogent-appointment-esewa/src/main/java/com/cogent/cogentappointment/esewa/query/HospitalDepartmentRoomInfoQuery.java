package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 03/06/20
 */
public class HospitalDepartmentRoomInfoQuery {

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_ROOM_INFO =
            " SELECT hdi" +
                    " FROM HospitalDepartmentRoomInfo hdi" +
                    " WHERE" +
                    " hdi.hospitalDepartment.id=:hospitalDepartmentId" +
                    " AND hdi.id=:hospitalDepartmentRoomInfoId" +
                    " AND hdi.status='Y'" +
                    " AND hdi.hospitalDepartment.status = 'Y'";
}
