package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.profile.ProfileSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti on 7/9/19
 */
public class ProfileQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +                        //[0]
                    " FROM Profile p" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE " +
                    " p.name =:name AND h.id =:hospitalId" +
                    " AND h.status != 'D'" +
                    " AND p.status != 'D'";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            "SELECT " +
                    " COUNT(p.id)" +                        //[0]
                    " FROM Profile p" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE " +
                    " p.id!= :id" +
                    " AND p.name =:name AND h.id =:hospitalId" +
                    " AND h.status != 'D'" +
                    " AND p.status != 'D'";

    private static Function<ProfileSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCH_PROFILE =
            (searchRequestDTO) -> {
                String whereClause = " WHERE p.status!='D' AND h.status!='D' AND h.id=:hospitalId";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
                    whereClause += " AND p.name LIKE '%" + searchRequestDTO.getName() + "%'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND p.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getDepartmentId()))
                    whereClause += " AND d.id=" + searchRequestDTO.getDepartmentId();

                whereClause += " ORDER BY p.id DESC";

                return whereClause;
            };

    public static Function<ProfileSearchRequestDTO, String> QUERY_TO_SEARCH_PROFILE = (searchRequestDTO) -> {
        return " SELECT" +
                " p.id as id," +                                             //[0]
                " p.name as name," +                                        //[1]
                " p.status as status," +                                    //[2]
                " d.name as departmentName," +                               //[3]
                " h.name as hospitalName" +
                " FROM" +
                " Profile p" +
                " LEFT JOIN Department d ON d.id = p.department.id" +
                " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_PROFILE.apply(searchRequestDTO);
    };

    public static final String QUERY_TO_FETCH_PROFILE_DETAILS =
            " SELECT" +
                    " p.name as name," +                                    //[0]
                    " p.status as status," +                               //[1]
                    " p.description as description," +                     //[2]
                    " p.remarks as remarks," +                             //[3]
                    " d.id as departmentId," +                             //[4]
                    " d.name as departmentName," +                         //[5]
                    " h.id as hospitalId," +                               //[6]
                    " h.name as hospitalName" +                            //[7]
                    " FROM" +
                    " Profile p" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " p.id=:id" +
                    " AND p.status!='D'" +
                    " AND h.status!='D'" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_FETCH_PROFILE_MENU_DETAILS =
            " SELECT" +
                    " pm.id as profileMenuId," +                              //[0]
                    " pm.roleId as roleId," +                                 //[1]
                    " pm.userMenuId as userMenuId," +                         //[2]
                    " pm.parentId as parentId," +                             //[3]
                    " pm.status as status" +                                  //[4]
                    " FROM" +
                    " ProfileMenu pm" +
                    " LEFT JOIN Profile p ON pm.profile.id = p.id" +
                    " WHERE" +
                    " pm.profile.id=:id" +
                    " AND pm.status='Y'";

    public static final String QUERY_TO_FETCH_ACTIVE_PROFILES_FOR_DROPDOWN =
            " SELECT" +
                    " p.id as value," +
                    " p.name as label" +
                    " FROM Profile p" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " WHERE p.status ='Y'" +
                    " AND d.hospital.id=:hospitalId";

    public static final String QUERY_TO_FETCH_PROFILE_BY_DEPARTMENT_ID =
            " SELECT p.id as value," +
                    " p.name as label" +
                    " FROM Profile p" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " WHERE p.status ='Y'" +
                    " AND d.status ='Y'" +
                    " AND d.id =:id" +
                    " AND d.hospital.id=:hospitalId";

    public static final String QUERY_TO_FETCH_ASSIGNED_PROFILE_RESPONSE =
            "SELECT" +
                    " pm.parent_id as parentId," +                                      //[0]
                    " pm.user_menu_id as userMenuId," +                                 //[1]
                    " GROUP_CONCAT(pm.role_id) as roleId" +                             //[2]
                    " FROM profile_menu pm" +
                    " LEFT JOIN profile p ON p.id =pm.profile_id" +
                    " LEFT JOIN admin a ON a.profile_id = p.id" +
                    " LEFT JOIN department d ON d.id = p.department_id" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                    " WHERE" +
                    " pm.status = 'Y'" +
                    " AND d.status ='Y'" +
                    " AND h.status ='Y'" +
                    " AND p.status = 'Y'" +
                    " AND a.status ='Y'" +
                    " AND (a.username = :username OR a.email=:email)" +
                    " AND h.code =:hospitalCode" +
                    " GROUP BY pm.parent_id, pm.user_menu_id, pm.profile_id";
}
