package com.cogent.cogentappointment.admin.query;

/**
 * @author Sauravi Thapa ON 5/21/20
 */
public class HospitalDepartmentRoomInfoQuery {
    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT" +
                    " hdri.room.id," +
                    " hdri.room.roomNumber" +
                    " FROM  HospitalDepartmentRoomInfo hdri" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id=hdri.hospitalDepartment.id" +
                    " WHERE" +
                    " hdri.status !='D'" +
                    " AND hd.hospital.status !='D'" +
                    " AND (hdri.room.id=:roomId)" +
                    " AND hdri.hospitalDepartment.hospital.id =:hospitalId";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_ROOM_INFO =
            " SELECT hdi" +
                    " FROM HospitalDepartmentRoomInfo hdi" +
                    " WHERE" +
                    " hdi.hospitalDepartment.id=:hospitalDepartmentId" +
                    " AND hdi.id=:hospitalDepartmentRoomInfoId" +
                    " AND hdi.status='Y'" +
                    " AND hdi.hospitalDepartment.status = 'Y'";
}
