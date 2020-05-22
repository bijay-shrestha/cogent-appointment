package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public class HospitalDepartmentQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT hd.name," +
                    " hd.code" +
                    " FROM  HospitalDepartment hd" +
                    " WHERE" +
                    " hd.status !='D'" +
                    " AND hd.hospital.status !='D'" +
                    " AND (hd.name=:name OR hd.code=:code)" +
                    " AND hd.hospital.id =:hospitalId";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT hd.name," +
                    " hd.code" +
                    " FROM  HospitalDepartment hd" +
                    " WHERE" +
                    " hd.status !='D'" +
                    " AND hd.hospital.status !='D'" +
                    " AND hd.id!=:id" +
                    " AND (hd.name=:name OR hd.code=:code)" +
                    " AND hd.hospital.id =:hospitalId";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " hd.id as value," +
                    " hd.name as label" +
                    " FROM HospitalDepartment hd" +
                    " WHERE hd.status != 'D'" +
                    " AND hd.hospital.id= :hospitalId" +
                    " ORDER BY hd.id DESC";

    public static final String QUERY_TO_FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " hd.id as value," +
                    " hd.name as label" +
                    " FROM HospitalDepartment hd" +
                    " WHERE hd.status = 'Y'" +
                    " AND hd.hospital.id= :hospitalId" +
                    " ORDER BY hd.id DESC";

    public static final Function<HospitalDepartmentSearchRequestDTO, String> QUERY_TO_SEARCH_DEPARTMENT =
            (searchRequestDTO ->
                    " SELECT" +
                            " hd.id as id," +
                            " hd.name as name," +
                            " hd.status as status," +
                            " hdc.appointment_charge as appointmentCharge," +
                            " hdc.appointment_follow_up_charge  as followUpCharge," +
                            " CASE" +
                            " WHEN GROUP_CONCAT(DISTINCT hdr.room_id) IS NULL THEN 'N/A'" +
                            " ELSE GROUP_CONCAT(DISTINCT hdr.room_id)" +
                            " END as roomList" +
                            " FROM" +
                            " hospital_department hd" +
                            " LEFT JOIN hospital_department_charge hdc ON hdc.hospital_department_id=hd.id" +
                            " LEFT JOIN hospital_department_room_info  hdr ON hdr.hospital_department_id=hd.id AND hdr.status!='D'" +
                            " LEFT JOIN hospital_department_doctor_info  hdd ON hdd.hospital_department_id=hd.id  AND hdd.status!='D'" +
                            " WHERE " +
                            " hd.hospital_id=:hospitalId " +
                            GET_WHERE_CLAUSE_FOR_SEARCH(searchRequestDTO));

    private static String GET_WHERE_CLAUSE_FOR_SEARCH(HospitalDepartmentSearchRequestDTO searchRequestDTO) {

        String whereClause = " AND hd.status != 'D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getId()))
            whereClause += " AND hd.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCode()))
            whereClause += " AND hd.code LIKE '%" + searchRequestDTO.getCode() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND hd.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getDoctorId()))
            whereClause += " AND hdd.doctor_id=" + searchRequestDTO.getDoctorId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getRoomId()))
            whereClause += " AND hdr.room_id=" + searchRequestDTO.getRoomId();

        whereClause += " GROUP BY hd.id" +
                " ORDER BY hd.id DESC";

        return whereClause;
    }

    public static String QUERY_TO_GET_DETAILS =
            "SELECT " +
                    "  hd.name as name, " +
                    "  hd.code as code, " +
                    "  hd.description as description, " +
                    "  hd.remarks as remarks, " +
                    "  hdc.appointmentCharge as appointmentCharge, " +
                    "  hdc.appointmentFollowUpCharge  as followUpCharge," +
                    "  hd.status as status, " +
                    HOSPITAL_DEPARTMENT_AUDITABLE_QUERY() +
                    "  FROM " +
                    "  HospitalDepartment hd " +
                    "  LEFT JOIN HospitalDepartmentCharge hdc ON hdc.hospitalDepartment.id=hd.id  " +
                    "  WHERE hd.id=:hospitalDepartmentId" +
                    "  AND hd.hospital.id=:hospitalId" +
                    "  AND hd.status!='D'";

    public static String QUERY_TO_GET_DOCTOR_LIST_BY_HOSPITAL_DEPARTMENT_ID =
            "SELECT" +
                    " hddi.doctor.id as value," +
                    " hddi.doctor.name as label," +
                    " CASE WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " da.fileUri" +
                    " END as fileUri" +
                    " FROM" +
                    " HospitalDepartmentDoctorInfo hddi" +
                    " LEFT JOIN DoctorAvatar da ON da.doctorId=hddi.doctor.id" +
                    " WHERE" +
                    " hddi.hospitalDepartment.id = :hospitalDepartmentId" +
                    " AND hddi.status='Y'" +
                    " AND hddi.hospitalDepartment.status!='D'";

    public static String QUERY_TO_GET_ROOM_LIST_BY_HOSPITAL_DEPARTMENT_ID =
            "SELECT" +
                    " hdri.room.id as value," +
                    " hdri.room.roomNumber as label" +
                    " FROM" +
                    " HospitalDepartmentRoomInfo hdri" +
                    " WHERE" +
                    " hdri.hospitalDepartment.id = :hospitalDepartmentId" +
                    " AND hdri.status='Y'" +
                    " AND hdri.hospitalDepartment.status!='D'";


    public static String HOSPITAL_DEPARTMENT_AUDITABLE_QUERY() {
        return " hd.createdBy as createdBy," +
                " hd.createdDate as createdDate," +
                " hd.lastModifiedBy as lastModifiedBy," +
                " hd.lastModifiedDate as lastModifiedDate";
    }
}
