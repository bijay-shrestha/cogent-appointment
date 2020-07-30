package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.department.DepartmentSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti ON 25/01/2020
 */
public class DepartmentQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT d.name," +
                    " d.code" +
                    " FROM Department d" +
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
                            " d.id as id," +                                                    //[0]
                            " CONCAT(h.alias, '-',d.name) as name," +                           //[1]
                            " d.code as departmentCode," +                                      //[2]
                            " d.status as status," +                                           //[3]
                            " h.name as hospitalName" +                                        //[4]
                            " FROM Department d" +
                            " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                            GET_WHERE_CLAUSE_FOR_SEARCH(searchRequestDTO));

    private static String GET_WHERE_CLAUSE_FOR_SEARCH(DepartmentSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE d.status != 'D'";

        if (!Objects.isNull(searchRequestDTO.getId()))
            whereClause += " AND d.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getDepartmentCode()))
            whereClause += " AND d.code LIKE '%" + searchRequestDTO.getDepartmentCode() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND d.status='" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            whereClause += " AND h.id=" + searchRequestDTO.getHospitalId();

        whereClause += " ORDER BY d.id DESC";

        return whereClause;
    }

    public final static String QUERY_TO_FETCH_DETAILS =
            "SELECT" +
                    " d.name as name," +                            //[0]
                    " d.code as departmentCode," +                  //[1]
                    " d.status as status," +                        //[2]
                    " d.remarks as remarks," +                      //[3]
                    " h.id as hospitalId," +                        //[4]
                    " h.name as hospitalName," +                     //[5]
                    " h.alias as hospitalAlias," +
                    DEPARTMENT_AUDITABLE_QUERY() +//[6]
                    " FROM Department d" +
                    " LEFT JOIN Hospital h ON h.id =d.hospital.id" +
                    " WHERE d.id =:id" +
                    " AND d.status != 'D'" +
                    " AND h.status!='D'";

    public static final String QUERY_TO_FETCH_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " d.id as value," +
                    " CONCAT(h.alias, '-',d.name) as label" +
                    " FROM Department d" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE d.status != 'D'" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_ACTIVE_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " d.id as value," +
                    " CONCAT(h.alias, '-',d.name) as label" +
                    " FROM Department d" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE d.status = 'Y'" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_DEPARTMENT_BY_HOSPITAL_ID =
            "SELECT" +
                    " d.id as value," +
                    " CONCAT(h.alias, '-',d.name) as label" +
                    " FROM Department d" +
                    " LEFT JOIN Hospital h ON h.id =d.hospital.id" +
                    " WHERE d.status = 'Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.id=:hospitalId" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_ALL_DEPARTMENT_BY_HOSPITAL_ID =
            "SELECT" +
                    " d.id as value," +
                    " CONCAT(h.alias, '-',d.name) as label" +
                    " FROM Department d" +
                    " LEFT JOIN Hospital h ON h.id =d.hospital.id" +
                    " WHERE d.status != 'D'" +
                    " AND h.status = 'Y'" +
                    " AND h.id=:hospitalId" +
                    " ORDER BY label ASC";


    private static String DEPARTMENT_AUDITABLE_QUERY() {
        return " d.createdBy as createdBy," +
                " d.createdDate as createdDate," +
                " d.lastModifiedBy as lastModifiedBy," +
                " d.lastModifiedDate as lastModifiedDate";
    }

}
