package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.department.DepartmentSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti ON 25/01/2020
 */
public class DepartmentQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT d.name," +
                    " d.code" +
                    " FROM  Department d" +
                    " LEFT JOIN Hospital  h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " d.status !='D'" +
                    " AND h.status !='D'" +
                    " AND (d.name=:name OR d.code=:code)" +
                    " AND h.id =:hospitalId";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT d.name," +
                    " d.code" +
                    " FROM  Department d" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " d.status !='D'" +
                    " AND h.status !='D'" +
                    " AND d.id!=:id" +
                    " AND (d.name=:name OR d.code=:code)" +
                    " AND h.id =:hospitalId";

    public static final Function<DepartmentSearchRequestDTO, String> QUERY_TO_SEARCH_DEPARTMENT =
            (searchRequestDTO ->
                    " SELECT " +
                            " d.id as id," +                            //[0]
                            " d.name as name," +                        //[1]
                            " d.code as departmentCode," +              //[2]
                            " d.status as status" +                     //[3]
                            " FROM Department d" +
                            " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                            GET_WHERE_CLAUSE_FOR_SEARCH(searchRequestDTO));

    private static String GET_WHERE_CLAUSE_FOR_SEARCH(DepartmentSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE d.status != 'D' AND h.id= :hospitalId";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getId()))
            whereClause += " AND d.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
            whereClause += " AND d.name LIKE '%" + searchRequestDTO.getName() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getDepartmentCode()))
            whereClause += " AND d.code LIKE '%" + searchRequestDTO.getDepartmentCode() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND d.status='" + searchRequestDTO.getStatus() + "'";

        whereClause += " ORDER BY d.id DESC";

        return whereClause;
    }

    public final static String QUERY_TO_FETCH_DETAILS =
            "SELECT" +
                    " d.name as name," +                            //[0]
                    " d.code as departmentCode," +                  //[1]
                    " d.status as status," +                        //[2]
                    " d.remarks as remarks" +                      //[3]
                    " FROM Department d" +
                    " LEFT JOIN Hospital h ON h.id =d.hospital.id" +
                    " WHERE d.id =:id" +
                    " AND d.status != 'D'" +
                    " AND h.id= :hospitalId" +
                    " AND h.status!='D'";

    public static final String QUERY_TO_FETCH_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " d.id as value," +
                    " d.name as label" +
                    " FROM Department d" +
                    " WHERE d.status != 'D'" +
                    " AND d.hospital.id= :hospitalId" +
                    " ORDER BY d.id DESC";

    public static final String QUERY_TO_FETCH_ACTIVE_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " d.id as value," +
                    " d.name as label" +
                    " FROM Department d" +
                    " WHERE d.status = 'Y'" +
                    " AND d.hospital.id= :hospitalId" +
                    " ORDER BY d.id DESC";
}
