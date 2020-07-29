package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import org.springframework.util.ObjectUtils;

import static com.cogent.cogentappointment.client.query.CdnFileQuery.QUERY_TO_FETCH_ADMIN_AVATAR;
import static com.cogent.cogentappointment.client.query.CdnFileQuery.QUERY_TO_FETCH_HOSPITAL_LOGO;
import static com.cogent.cogentappointment.client.utils.GenderUtils.fetchGenderByCode;

/**
 * @author smriti on 2019-08-05
 */
public class AdminQuery {

    public static final String QUERY_TO_VALIDATE_ADMIN_COUNT =
            " SELECT COUNT(a.id)," +
                    " h.numberOfAdmins" +
                    " FROM Hospital h" +
                    " LEFT JOIN Department d ON h.id=d.hospital.id" +
                    " LEFT JOIN Profile p ON d.id=p.department.id" +
                    " LEFT JOIN Admin a ON a.profileId.id=p.id AND a.status!='D'" +
                    " WHERE h.id=:hospitalId";

    public static final String QUERY_TO_FIND_ADMIN_FOR_VALIDATION =
            "SELECT " +
                    " a.email," +                               //[0]
                    " a.mobileNumber," +                        //[1]
                    " COALESCE(h.id, ho.id)" +                 //[2]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId.id" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital ho ON ho.id = p.company.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " a.status != 'D'" +
                    " AND" +
                    " (a.email =:email OR a.mobileNumber = :mobileNumber)";

    public static final String QUERY_TO_FIND_ADMIN_EXCEPT_CURRENT_ADMIN =
            "SELECT " +
                    " a.email," +                               //[0]
                    " a.mobileNumber," +                         //[1]
                    " COALESCE(h.id, ho.id)" +                 //[2]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId.id" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital ho ON ho.id = p.company.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " a.status != 'D'" +
                    " AND a.id !=:id" +
                    " AND" +
                    " (a.email =:email OR a.mobileNumber = :mobileNumber)";

    public static final String QUERY_TO_FETCH_ACTIVE_ADMIN_FOR_DROPDOWN =
            " SELECT" +
                    " a.id as value," +                     //[0]
                    " a.email as label" +                //[1]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE a.status ='Y'" +
                    " AND h.id=:hospitalId" +
                    " ORDER BY label ASC";

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
                    " a.email as email," +                                      //[2]
                    " a.mobileNumber as mobileNumber," +                        //[3]
                    " a.status as status," +                                    //[4]
                    " a.hasMacBinding as hasMacBinding," +                      //[5]
                    " a.gender as gender," +                                    //[6]
                    " p.name as profileName," +                                 //[7]
                    " d.name as departmentName," +                              //[8]
                    QUERY_TO_FETCH_ADMIN_AVATAR;                                //[9]

    private static final String GET_WHERE_CLAUSE_TO_FETCH_ADMIN =
            " WHERE a.status != 'D' AND h.status !='D' AND p.status !='D' AND d.status != 'D'" +
                    " AND h.id =:hospitalId";

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

        if (!ObjectUtils.isEmpty(searchRequestDTO.getGenderCode())) {
            Gender gender = fetchGenderByCode(searchRequestDTO.getGenderCode());
            whereClause += " AND a.gender='" + gender + "'";
        }

        whereClause += " ORDER BY a.id DESC";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_ADMIN_DETAIL =
            SELECT_CLAUSE_TO_FETCH_ADMIN + "," +
                    " a.remarks as remarks," +                                      //[11]
                    " p.id as profileId," +                                         //[12]
                    " d.id as departmentId," +                                      //[13]
                    " d.name as departmentName," +                                   //[14]
                    ADMIN_AUDITABLE_QUERY() +
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
                    " am.macAddress as macAddress" +                //[1]
                    " FROM AdminMacAddressInfo am" +
                    " WHERE" +
                    " am.status = 'Y'" +
                    " AND am.admin.id = :id";

    public static final String QUERY_TO_FETCH_ADMIN_BY_EMAIL =
            " SELECT a FROM Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE" +
                    " (a.email =:email)" +
                    " AND a.status != 'D'" +
                    " AND h.isCompany='N'" +
                    " AND h.status='Y'";

    public static final String QUERY_TO_FETCH_ADMIN_INFO =
            " SELECT" +
                    " a.id as adminId," +                                                   //[0]
                    " a.email as email," +                                                 //[1]
                    " a.fullName as fullName," +
                    QUERY_TO_FETCH_ADMIN_AVATAR + "," +
                    " p.id as profileId," +                                                //[3]
                    " p.name as profileName," +                                            //[4]
                    " d.id as departmentId," +                                             //[5]
                    " d.name as departmentName," +
                    " h.name as hospitalName," +                                           //[6]
                    " p.isAllRoleAssigned as isAllRoleAssigned," +                         //[7]
                    " af.isSideBarCollapse as isSideBarCollapse," +                        //[8]
                    QUERY_TO_FETCH_HOSPITAL_LOGO +
                    " END as hospitalLogo," +                                              //[9]
                    " h.code as hospitalCode" +                                             //[10]
                    " FROM Admin a" +
                    " LEFT JOIN AdminAvatar av ON av.admin.id=a.id" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " LEFT JOIN Department d ON d.id=p.department.id" +
                    " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                    " LEFT JOIN HospitalLogo hl ON hl.hospital.id=h.id" +
                    " LEFT JOIN AdminFeature af ON a.id = af.admin.id" +
                    " WHERE " +
                    " (a.email =:email OR a.mobileNumber=:email)" +
                    " AND a.status='Y'" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_FETCH_ADMIN_META_INFO =
            " SELECT ami.id as adminMetaInfoId," +                   //[0]
                    " ami.metaInfo as metaInfo" +                   //[1]
                    " FROM AdminMetaInfo ami" +
                    " LEFT JOIN Admin a On a.id=ami.admin.id" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " LEFT JOIN Department d ON d.id=p.department.id" +
                    " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                    " WHERE ami.status !='D'" +
                    " AND h.id=:hospitalId" +
                    " ORDER BY metaInfo ASC";

    public static final String QUERY_TO_GET_LOGGED_ADMIN_INFO =
            "SELECT" +
                    " a.id as id," +
                    " a.email as email," +
                    " a.password as password," +
                    " h.isCompany as isCompany," +
                    " h.code as hospitalCode," +
                    " h.id as hospitalId," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM " +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " LEFT JOIN Department d ON d.id=p.department.id" +
                    " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                    " LEFT JOIN HmacApiInfo hai ON hai.hospital.id=h.id" +
                    " WHERE" +
                    " (a.mobileNumber=:email OR a.email=:email)" +
                    " AND a.status = 'Y'" +
                    " AND h.isCompany='N'" +
                    " AND h.status='Y'";

    public static String ADMIN_AUDITABLE_QUERY() {
        return " a.createdBy as createdBy," +
                " a.createdDate as createdDate," +
                " a.lastModifiedBy as lastModifiedBy," +
                " a.lastModifiedDate as lastModifiedDate";
    }


}