package com.cogent.cogentappointment.query;

import com.cogent.cogentappointment.dto.request.admin.AdminSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author smriti on 2019-08-05
 */
public class AdminQuery {

    public static final String QUERY_TO_FIND_ADMIN_FOR_VALIDATION =
            "SELECT " +
                    " a.username," +                            //[0]
                    " a.email," +                               //[1]
                    " a.mobileNumber" +                        //[2]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " a.status != 'D'" +
                    " AND h.status!='D'" +
                    " AND" +
                    " (a.username =:username OR a.email =:email OR a.mobileNumber = :mobileNumber)" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_FIND_ADMIN_EXCEPT_CURRENT_ADMIN =
            "SELECT " +
                    " a.email," +                               //[0]
                    " a.mobileNumber" +                        //[1]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " a.status != 'D'" +
                    " AND h.status!='D'" +
                    " AND a.id !=:id" +
                    " AND" +
                    " (a.email =:email OR a.mobileNumber = :mobileNumber)" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_FETCH_ACTIVE_ADMIN_FOR_DROPDOWN =
            " SELECT" +
                    " id as value," +                     //[0]
                    " username as label" +               //[1]
                    " FROM" +
                    " Admin" +
                    " WHERE status ='Y'";

    public static String QUERY_TO_SEARCH_ADMIN(AdminSearchRequestDTO searchRequestDTO) {
        return SELECT_CLAUSE_TO_FETCH_ADMIN + "," +
                " tbl1.profileName as profileName," +
                " tbl2.fileUri as fileUri" +
                " FROM" +
                " admin a" +
                " LEFT JOIN admin_meta_info ami ON a.id = ami.admin_id" +
                " LEFT JOIN admin_category ac On ac.id = a.admin_category_id" +
                " RIGHT JOIN" +
                " (" +
                QUERY_TO_SEARCH_ADMIN_PROFILE(searchRequestDTO) +
                " ) tbl1 ON tbl1.adminId = a.id" +
                " LEFT JOIN" +
                " (" +
                QUERY_TO_FETCH_ADMIN_AVATAR +
                " )tbl2 ON tbl2.adminId = a.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_ADMIN(searchRequestDTO);
    }

    private static final String QUERY_TO_FETCH_ADMIN_AVATAR =
            " SELECT av.admin_id as adminId," +
                    " av.file_uri as fileUri" +
                    " FROM admin_avatar av" +
                    " WHERE av.status = 'Y'";

    private static final String SELECT_CLAUSE_TO_FETCH_ADMIN =
            " SELECT" +
                    " a.id as id," +                                            //[0]
                    " a.full_name as fullName," +                               //[1]
                    " a.username as username," +                                //[2]
                    " a.email as email," +                                      //[3]
                    " a.mobile_number as mobileNumber," +                       //[4]
                    " a.status as status," +                                    //[5]
                    " a.has_mac_binding as hasMacBinding," +                    //[6]
                    " ac.name as adminCategoryName";                           //[7]

    private static String QUERY_TO_SEARCH_ADMIN_PROFILE(AdminSearchRequestDTO requestDTO) {
        String query = "SELECT ap.admin_id as adminId," +
                " GROUP_CONCAT(p.name) as profileName" +
                " FROM admin_profile ap" +
                " LEFT JOIN profile p ON p.id = ap.profile_id" +
                " WHERE ap.status = 'Y'";

        if (!Objects.isNull(requestDTO.getProfileId()))
            query += " AND p.id=" + requestDTO.getProfileId();

        return query + " GROUP BY ap.admin_id";
    }

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_ADMIN(AdminSearchRequestDTO searchRequestDTO) {
        String whereClause = " WHERE a.status != 'D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND a.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAdminMetaInfoId()))
            whereClause += " AND ami.id=" + searchRequestDTO.getAdminMetaInfoId();

        whereClause += " ORDER BY a.id DESC";

        return whereClause;
    }

    private static final String QUERY_TO_FETCH_ADMIN_PROFILE =
            "SELECT ap.admin_id as adminId," +
                    " GROUP_CONCAT(ap.id) as adminProfileId," +
                    " GROUP_CONCAT(p.id) as profileId," +
                    " GROUP_CONCAT(p.name) as profileName," +
                    " GROUP_CONCAT(ap.application_module_id) as applicationModuleId," +
                    " GROUP_CONCAT(am.name) as applicationModuleName" +
                    " FROM admin_profile ap" +
                    " LEFT JOIN profile p ON p.id = ap.profile_id" +
                    " LEFT JOIN application_module am ON am.id = ap.application_module_id" +
                    " WHERE ap.status = 'Y'" +
                    " GROUP BY ap.admin_id";

    public static final String QUERY_TO_FETCH_ADMIN_DETAIL =
            SELECT_CLAUSE_TO_FETCH_ADMIN + "," +
                    " ac.id as adminCategoryId," +                                                  //[8]
                    " a.remarks as remarks," +                                                      //[9]
                    " h.name as hospitalName," +                                                    //[10]
                    " h.id as hospitalId," +                                                        //[11]
                    " tbl1.fileUri as fileUri," +                                                    //[12]
                    " tbl2.adminProfileId as adminProfileId," +                                     //[13]
                    " tbl2.profileId as profileId," +                                               //[14]
                    " tbl2.profileName as profileName," +                                           //[15]
                    " tbl2.applicationModuleId as applicationModuleId," +                           //[16]
                    " tbl2.applicationModuleName as applicationModuleName" +                       //[17]
                    " FROM admin a" +
                    " LEFT JOIN hospital h ON h.id = a.hospital_id" +
                    " LEFT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_ADMIN_AVATAR +
                    " )tbl1 ON tbl1.adminId = a.id" +
                    " RIGHT JOIN" +
                    " (" +
                    QUERY_TO_FETCH_ADMIN_PROFILE +
                    " )tbl2 ON tbl2.adminId = a.id" +
                    " WHERE a.id = :id" +
                    " AND a.status !='D'";

    public static final String QUERY_FO_FETCH_MAC_ADDRESS_INFO =
            "SELECT m.id as id," +                                  //[0]
                    " m.macAddress as macAddress" +               //[1]
                    " FROM MacAddressInfo m" +
                    " WHERE" +
                    " m.status = 'Y'" +
                    " AND m.admin.id = :id";

    public static final String QUERY_TO_FETCH_ADMIN_BY_USERNAME_OR_EMAIL =
            " SELECT a FROM Admin a" +
                    " WHERE" +
                    " (a.username=:username OR a.email =:email)" +
                    " AND a.status != 'D'";

    public static final String QUERY_TO_FETCH_ADMIN_INFO =
            " SELECT a.id as adminId," +                                                //[0]
                    " a.username as username," +                                        //[1]
                    " a.fullName as fullName," +                                        //[2]
                    " sd.id as subDepartmentId," +                                      //[3]
                    " sd.name as subDepartmentName," +                                  //[4]
                    " sd.department.id as departmentId," +                               //[5]
                    " p.name as profileName," +                                         //[6]
                    " p.id as profileId" +                                              //[7]
                    " FROM Admin a " +
                    " LEFT JOIN AdminProfile ap ON ap.adminId = a.id" +
                    " LEFT JOIN Profile p ON p.id = ap.profileId" +
                    " LEFT JOIN SubDepartment sd ON sd.id = p.subDepartment.id" +
                    " WHERE" +
                    " a.status = 'Y'" +
                    " AND (a.username=:username OR a.email =:email)" +
                    " AND sd.code=:code";

    public static final String QUERY_TO_FETCH_ADMIN_INFO_BY_USERNAME =
            " SELECT GROUP_CONCAT(sd.code)," +                                                              //[0]
                    " a.password" +                                                                         //[1]
                    " FROM admin_profile ap" +
                    " LEFT JOIN application_module am ON ap.application_module_id=am.id" +
                    " LEFT JOIN sub_department sd ON sd.id = am.sub_department_id" +
                    " LEFT JOIN admin a ON a.id = ap.admin_id" +
                    " WHERE am.status = 'Y'" +
                    " AND ap.status = 'Y'" +
                    " AND a.status ='Y'" +
                    " AND (a.username =:username OR a.email =:email)" +
                    " GROUP BY ap.admin_id";

    public static final String QUERY_TO_FETCH_LOGGED_IN_ADMIN_SUB_DEPARTMENT_LIST =
            "SELECT" +
                    " sd.id as subDepartmentId," +                              //[0]
                    " sd.name as subDepartmentName," +                          //[1]
                    " sd.code as subDepartmentCode" +                           //[2]
                    " FROM SubDepartment sd" +
                    " LEFT JOIN Profile p ON p.subDepartment.id=sd.id" +
                    " LEFT JOIN AdminProfile ap ON ap.profileId=p.id" +
                    " LEFT JOIN Admin a ON a.id=ap.adminId" +
                    " WHERE a.username=:username" +
                    " AND a.status='Y'";

    public static final String QUERY_TO_FETCH_ADMIN_META_INFO =
            " SELECT a.id as adminMetaInfoId," +                   //[0]
                    " a.metaInfo as metaInfo" +                   //[1]
                    " FROM AdminMetaInfo a" +
                    " WHERE a.admin.status !='D'";
}