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
                    " CONCAT(h.alias,'-',r.roomNumber) AS label" +                 //[1]
                    " FROM Room r " +
                    " LEFT JOIN Hospital h ON h.id = r.hospital.id" +
                    " WHERE r.status = 'Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.id=:hospitalId" +
                    " AND h.isCompany='N'"+
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_ROOM_FOR_DROPDOWN =
            "SELECT r.id as value," +                                             //[0]
                    " CONCAT(h.alias,'-',r.roomNumber) AS label" +                 //[1]
                    " FROM Room r " +
                    " LEFT JOIN Hospital h ON h.id = r.hospital.id" +
                    " WHERE r.status != 'D'" +
                    " AND h.status = 'Y'" +
                    " AND h.id=:hospitalId" +
                    " AND h.isCompany='N'"+
                    " ORDER BY label ASC";

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_ROOM =
            "SELECT r.id as id," +                                                //[0]
                    " r.roomNumber as roomNumber," +                              //[1]
                    " r.status as status," +                                     //[2]
                    " h.name as hospitalName" +                                //[3]
                    " FROM Room r " +
                    " LEFT JOIN Hospital h ON h.id = r.hospital.id" +
                    " WHERE" +
                    " h.isCompany='N'";

    public static Function<RoomSearchRequestDTO, String> QUERY_TO_SEARCH_ROOM =
            (roomSearchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_ROOM +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION(roomSearchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION
            (RoomSearchRequestDTO searchRequestDTO) {

        String whereClause = " AND r.status!='D'";

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            whereClause += " AND h.id = " + searchRequestDTO.getHospitalId();

        if (!Objects.isNull(searchRequestDTO.getId()))
            whereClause += " AND r.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND r.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause + " ORDER BY r.id DESC";
    }


}
