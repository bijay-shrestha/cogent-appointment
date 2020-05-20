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
                            " GROUP_CONCAT(hdr.room_id) as roomList" +
                            " FROM" +
                            " hospital_department hd" +
                            " LEFT JOIN hospital_department_charge hdc ON hdc.hospital_department_id=hd.id" +
                            " LEFT JOIN hospital_department_room_info  hdr ON hdr.hospital_department_id=hd.id" +
                            " LEFT JOIN hospital_department_doctor_info  hdd ON hdd.hospital_department_id=hd.id" +
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

        whereClause += " ORDER BY hd.id DESC";

        return whereClause;
    }

    public static String DEPARTMENT_AUDITABLE_QUERY() {
        return " d.createdBy as createdBy," +
                " d.createdDate as createdDate," +
                " d.lastModifiedBy as lastModifiedBy," +
                " d.lastModifiedDate as lastModifiedDate";
    }
}
