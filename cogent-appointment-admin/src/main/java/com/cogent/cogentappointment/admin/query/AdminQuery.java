package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.enums.Gender;
import com.cogent.cogentappointment.admin.utils.GenderUtils;
import org.springframework.util.ObjectUtils;

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

        return SELECT_CLAUSE_TO_FETCH_ADMIN +
                " FROM" +
                " Admin a" +
                " LEFT JOIN AdminMetaInfo ami ON a.id = ami.admin.id" +
                " LEFT JOIN Profile p ON p.id = a.profileId.id" +
                " LEFT JOIN AdminAvatar av ON a.id = av.admin.id" +
                " LEFT JOIN Department d ON d.id = p.department.id" +
                " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_ADMIN(searchRequestDTO);
    }

    private static final String SELECT_CLAUSE_TO_FETCH_ADMIN =
            " SELECT" +
                    " a.id as id," +                                            //[0]
                    " a.fullName as fullName," +                                //[1]
                    " a.username as username," +                                //[2]
                    " a.email as email," +                                      //[3]
                    " a.mobileNumber as mobileNumber," +                        //[4]
                    " a.status as status," +                                    //[5]
                    " a.hasMacBinding as hasMacBinding," +                      //[6]
                    " a.gender as gender," +                                    //[7]
                    " p.name as profileName," +
                    " h.name as hospitalName," +                                //[8]
                    " CASE WHEN" +
                    " (av.status IS NULL OR av.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " av.fileUri" +
                    " END as fileUri";                                           //[9]

    private static final String GET_WHERE_CLAUSE_TO_FETCH_ADMIN =
            " WHERE a.status != 'D' AND h.status !='D' AND p.status !='D' AND d.status != 'D'";

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_ADMIN(AdminSearchRequestDTO searchRequestDTO) {
        String whereClause = GET_WHERE_CLAUSE_TO_FETCH_ADMIN;

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAdminMetaInfoId()))
            whereClause += " AND ami.id=" + searchRequestDTO.getAdminMetaInfoId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND a.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getDepartmentId()))
            whereClause += " AND d.id=" + searchRequestDTO.getDepartmentId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getProfileId()))
            whereClause += " AND p.id=" + searchRequestDTO.getProfileId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getHospitalId()))
            whereClause += " AND h.id=" + searchRequestDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getGenderCode())) {
            Gender gender = GenderUtils.fetchGenderByCode(searchRequestDTO.getGenderCode());
            whereClause += " AND a.gender LIKE '%" + gender + "%'";
        }

        whereClause += " ORDER BY a.id DESC";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_ADMIN_DETAIL =
            SELECT_CLAUSE_TO_FETCH_ADMIN + "," +
                    " a.remarks as remarks," +                                      //[10]
                    " h.id as hospitalId," +                                        //[11]
                    " p.id as profileId," +                                         //[12]
                    " d.id as departmentId," +
                    " d.name as departmentName" +
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId.id" +
                    " LEFT JOIN AdminAvatar av ON a.id = av.admin.id" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    GET_WHERE_CLAUSE_TO_FETCH_ADMIN +
                    " AND a.id = :id";

    public static final String QUERY_FO_FETCH_MAC_ADDRESS_INFO =
            "SELECT am.id as id," +                                  //[0]
                    " am.macAddress as macAddress" +               //[1]
                    " FROM AdminMacAddressInfo am" +
                    " WHERE" +
                    " am.status = 'Y'" +
                    " AND am.admin.id = :id";

    public static final String QUERY_TO_FETCH_ADMIN_BY_USERNAME_OR_EMAIL =
            " SELECT a FROM Admin a" +
                    " WHERE" +
                    " (a.username=:username OR a.email =:email)" +
                    " AND a.status != 'D'";

    public static final String QUERY_TO_FETCH_ADMIN_INFO =
            " SELECT" +
                    " a.id as adminId," +
                    " a.username as username," +
                    " a.fullName as fullName," +
                    " CASE " +
                    "    WHEN (av.status = 'N' OR  av.status IS NULL) THEN null" +
                    "    ELSE av.fileUri END as fileUri," +
                    " p.id as profileId," +
                    " p.name as profileName," +
                    " d.id as departmentId," +
                    " d.name as departmentName," +
                    " h.id as hospitalId," +
                    " h.name as hospitalName" +
                    " FROM Admin a" +
                    " LEFT JOIN AdminAvatar av ON av.admin.id=a.id" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " LEFT JOIN Department d ON d.id=p.department.id" +
                    " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                    " WHERE (a.username=:username OR a.email =:email)";

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