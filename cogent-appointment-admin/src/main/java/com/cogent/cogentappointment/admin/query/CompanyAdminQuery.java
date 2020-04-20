package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.utils.GenderUtils;
import com.cogent.cogentappointment.persistence.enums.Gender;
import org.springframework.util.ObjectUtils;

/**
 * @author smriti on 2019-08-05
 */
public class CompanyAdminQuery {

    public static final String QUERY_TO_FIND_COMPANY_ADMIN_FOR_VALIDATION =
            "SELECT " +
                    " a.email," +                               //[0]
                    " a.mobileNumber" +                        //[1]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId" +
                    " LEFT JOIN Hospital h ON h.id = p.company.id" +
                    " WHERE" +
                    " a.status != 'D'" +
                    " AND h.status!='D'" +
                    " AND" +
                    " (a.email =:email OR a.mobileNumber = :mobileNumber)" +
                    " AND h.isCompany='Y'";


    public static final String QUERY_TO_FIND_COMPANY_ADMIN_EXCEPT_CURRENT_COMPANY_ADMIN =
            "SELECT " +
                    " a.email," +                               //[0]
                    " a.mobileNumber" +                        //[1]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId.id" +
                    " LEFT JOIN Hospital h ON h.id = p.company.id" +
                    " WHERE" +
                    " a.status != 'D'" +
                    " AND h.status!='D'" +
                    " AND h.isCompany='Y'" +
                    " AND a.id !=:id" +
                    " AND" +
                    " (a.email =:email OR a.mobileNumber = :mobileNumber)" +
                    " AND h.id=:companyId";

    public static final String QUERY_TO_FETCH_ACTIVE_COMPANY_ADMIN_FOR_DROPDOWN =
            " SELECT" +
                    " a.id as value," +                     //[0]
                    " a.email as label" +               //[1]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " WHERE a.status ='Y'" +
                    " AND p.isCompanyProfile='Y'" +
                    " AND p.status='Y'" +
                    " ORDER by a.id  DESC  ";


    public static String QUERY_TO_SEARCH_COMPANY_ADMIN(CompanyAdminSearchRequestDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_FETCH_COMPANY_ADMIN +
                " FROM" +
                " Admin a" +
                " LEFT JOIN AdminMetaInfo ami ON a.id = ami.admin.id" +
                " LEFT JOIN Profile p ON p.id = a.profileId.id" +
                " LEFT JOIN AdminAvatar av ON a.id = av.admin.id" +
                " LEFT JOIN Hospital h ON h.id = p.company.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_COMAPNY_ADMIN(searchRequestDTO);
    }

    private static final String SELECT_CLAUSE_TO_FETCH_COMPANY_ADMIN =
            " SELECT" +
                    " a.id as id," +                                            //[0]
                    " a.fullName as fullName," +                                //[1]
                    " a.email as email," +                                      //[3]
                    " a.mobileNumber as mobileNumber," +                        //[4]
                    " a.status as status," +                                    //[5]
                    " a.hasMacBinding as hasMacBinding," +                      //[6]
                    " a.gender as gender," +                                    //[7]
                    " p.name as profileName," +
                    " h.name as companyName," +                                //[8]
                    " CASE WHEN" +
                    " (av.status IS NULL OR av.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " av.fileUri" +
                    " END as fileUri";                                           //[9]

    private static final String GET_WHERE_CLAUSE_TO_FETCH_ADMIN =
            " WHERE a.status != 'D' AND h.status !='D' AND p.status !='D' AND h.isCompany='Y'";

    private static String GET_WHERE_CLAUSE_FOR_SEARCH_COMAPNY_ADMIN(CompanyAdminSearchRequestDTO searchRequestDTO) {
        String whereClause = GET_WHERE_CLAUSE_TO_FETCH_ADMIN;

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAdminMetaInfoId()))
            whereClause += " AND ami.id=" + searchRequestDTO.getAdminMetaInfoId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND a.status='" + searchRequestDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getProfileId()))
            whereClause += " AND p.id=" + searchRequestDTO.getProfileId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCompanyId()))
            whereClause += " AND h.id=" + searchRequestDTO.getCompanyId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getGenderCode())) {
            Gender gender = GenderUtils.fetchGenderByCode(searchRequestDTO.getGenderCode());
            whereClause += " AND a.gender ='" + gender + "'";
        }

        whereClause += " ORDER BY a.id DESC";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_COMPANY_ADMIN_DETAIL =
            SELECT_CLAUSE_TO_FETCH_COMPANY_ADMIN + "," +
                    " a.remarks as remarks," +                                      //[10]
                    " h.id as companyId," +                                        //[11]
                    " p.id as profileId" +                                         //[12]
                    " FROM" +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id = a.profileId.id" +
                    " LEFT JOIN AdminAvatar av ON a.id = av.admin.id" +
                    " LEFT JOIN Hospital h ON h.id = p.company.id" +
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

    public static final String QUERY_TO_FETCH_COMPANY_ADMIN_INFO =
            " SELECT" +
                    " a.id as adminId," +                                                   //[0]
                    " a.email as email," +                                                //[1]
                    " a.fullName as fullName," +
                    " CASE " +
                    "    WHEN (av.status = 'N' OR  av.status IS NULL) THEN null" +
                    "    ELSE av.fileUri END as fileUri," +                                //[2]
                    " p.id as profileId," +                                                 //[3]
                    " p.name as profileName," +                                             //[4]
                    " h.id as hospitalId," +                                                //[5]
                    " h.name as hospitalName," +                                             //[6]
                    " h.isCompany as isCompany" +                                            //[7]
                    " FROM Admin a" +
                    " LEFT JOIN AdminAvatar av ON av.admin.id=a.id" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " LEFT JOIN Hospital h ON h.id=p.company.id" +
                    " WHERE " +
                    " (a.email =:email OR a.mobileNumber=:email)" +
                    " AND a.status='Y'" +
                    " AND h.isCompany='Y'";

    public static final String QUERY_TO_FETCH_COMPANY_ADMIN_META_INFO =
            " SELECT ami.id as adminMetaInfoId," +                   //[0]
                    " ami.metaInfo as metaInfo" +                   //[1]
                    " FROM AdminMetaInfo ami" +
                    " LEFT JOIN Admin a ON a.id=ami.admin.id" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " WHERE a.status !='D'" +
                    " AND p.isCompanyProfile='Y'" +
                    " ORDER BY ami.id DESC";

    public static final String QUERY_TO_GET_LOGGED_COMPANY_ADMIN_INFO =
            "SELECT" +
                    " a.id as id," +
                    " a.email as email," +
                    " a.password as password," +
                    " h.isCompany as isCompany," +
                    " h.code as companyCode," +
                    " h.id as companyId," +
                    " hai.apiKey as apiKey," +
                    " hai.apiSecret as apiSecret" +
                    " FROM " +
                    " Admin a" +
                    " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                    " LEFT JOIN Hospital h ON h.id=p.company.id" +
                    " LEFT JOIN HmacApiInfo hai ON hai.hospital.id=h.id" +
                    " WHERE" +
                    " (a.mobileNumber=:email OR a.email=:email)" +
                    " AND a.status = 'Y'" +
                    " AND h.isCompany='Y'";

}