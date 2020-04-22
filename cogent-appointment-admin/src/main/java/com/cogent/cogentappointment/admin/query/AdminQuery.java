package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.utils.GenderUtils;
import com.cogent.cogentappointment.persistence.enums.Gender;
import org.springframework.util.ObjectUtils;

/**
 * @author smriti on 2019-08-05
 */
public class AdminQuery {

    public static final String QUERY_TO_FIND_ADMIN_FOR_VALIDATION =
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
                    " AND" +
                    " (a.email =:email OR a.mobileNumber = :mobileNumber)" +
                    " AND h.id=:hospitalId";

    public static final String QUERY_TO_VALIDATE_ADMIN_COUNT =
            " SELECT " +
                    " COUNT(a.id)," +                   //[0]
                    " h.numberOfAdmins" +               //[1]
                    " FROM Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Department d ON d.id = p.department.id" +
                    " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                    " WHERE h.id = :hospitalId" +
                    " AND a.status !='D'";

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
                    " email as label" +                  //[1]
                    " FROM" +
                    " Admin" +
                    " WHERE status ='Y'" +
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
                    " CONCAT(h.alias, '-', p.name) as profileName," +           //[7]
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
                    " d.id as departmentId," +                                      //[13]
                    " d.name as departmentName," +                                  //[14]
                    " h.alias as hospitalAlias"+                                    //[15]
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

    public static final String QUERY_TO_FETCH_ADMIN_META_INFO =
            " SELECT" +
                    " ami.id as adminMetaInfoId," +                   //[0]
                    " ami.metaInfo as metaInfo" +                   //[1]
                    " FROM AdminMetaInfo ami" +
                    " LEFT JOIN Admin a ON a.id=ami.admin.id" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " WHERE a.status !='D'" +
                    " AND p.isCompanyProfile='N'";
}