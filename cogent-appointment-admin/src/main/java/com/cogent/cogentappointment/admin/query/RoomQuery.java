package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.room.RoomSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
public class RoomQuery {
    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT COUNT(r.id)" +
                    " FROM Room r" +
                    " WHERE" +
                    " r.status !='D'" +
                    " AND r.roomNumber=:roomNumber" +
                    " AND r.hospital.id =:hospitalId";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT COUNT(r.id)" +
                    " FROM Room r" +
                    " WHERE" +
                    " r.status !='D'" +
                    " AND r.id!=:id" +
                    " AND r.roomNumber=:roomNumber" +
                    " AND r.hospital.id =:hospitalId";

    public static final String QUERY_TO_FETCH_ACTIVE_ROOM_FOR_DROPDOWN =
            "SELECT r.id as value," +                                             //[0]
                    " CONCAT(r.hospital.alias,' - ',r.roomNumber) AS label" +                 //[1]
                    " FROM Room r " +
                    " WHERE r.status = 'Y'" +
                    " AND r.hospital.status = 'Y'" +
                    " AND r.hospital.id=:hospitalId" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_ROOM_FOR_DROPDOWN =
            "SELECT r.id as value," +                                                         //[0]
                    " CONCAT(r.hospital.alias,' - ',r.roomNumber) AS label" +                 //[1]
                    " FROM Room r " +
                    " WHERE r.status != 'D'" +
                    " AND r.hospital.status = 'Y'" +
                    " AND r.hospital.id=:hospitalId" +
                    " ORDER BY label ASC";

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_ROOM =
            "SELECT r.id as id," +                                                //[0]
                    " r.roomNumber as roomNumber," +                              //[1]
                    " r.status as status," +                                     //[2]
                    " r.hospital.id as hospitalId," +                            //[3]
                    " r.hospital.name as hospitalName" +                         //[4]
                    " FROM Room r " +
                    " WHERE";

    public static Function<RoomSearchRequestDTO, String> QUERY_TO_SEARCH_ROOM =
            (roomSearchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_ROOM +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION(roomSearchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION
            (RoomSearchRequestDTO searchRequestDTO) {

        String whereClause = " r.status!='D'";

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            whereClause += " AND r.hospital.id = " + searchRequestDTO.getHospitalId();

        if (!Objects.isNull(searchRequestDTO.getId()))
            whereClause += " AND r.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND r.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause + " ORDER BY r.id DESC";
    }

    public static final String QUERY_TO_FETCH_ROOM_FOR_DROPDOWN_BY_HOSPITAL_DEPARTMENT_ID =
            "SELECT hdri.id as value," +                                                  //[0]
                    " CONCAT('Room No',' - ',r.roomNumber) AS label" +                 //[1]
                    " FROM Room r" +
                    " LEFT JOIN HospitalDepartmentRoomInfo hdri ON hdri.room.id=r.id" +
                    " WHERE hdri.hospitalDepartment.id=:hospitalDepartmentId " +
                    " AND r.status != 'D'" +
                    " AND hdri.status != 'D'" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_ACTIVE_ROOM_FOR_DROPDOWN_BY_HOSPITAL_DEPARTMENT_ID =
            "SELECT hdri.id as value," +                                                  //[0]
                    " CONCAT('Room No',' - ',r.roomNumber) AS label" +                 //[1]
                    " FROM Room r" +
                    " LEFT JOIN HospitalDepartmentRoomInfo hdri ON hdri.room.id=r.id" +
                    " WHERE hdri.hospitalDepartment.id=:hospitalDepartmentId " +
                    " AND r.status = 'Y'" +
                    " AND hdri.status = 'Y'" +
                    " ORDER BY label ASC";



}
