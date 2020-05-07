package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti on 7/9/19
 * NOTE : HOSPITAL IS COMPANY FOR COMPANY PROFILE SETUP
 */
public class CompanyProfileQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +                                       //[0]
                    " FROM Profile p" +
                    " LEFT JOIN Hospital h ON h.id = p.company.id" +
                    " WHERE " +
                    " p.name =:name" +
                    " AND h.id =:companyId" +
                    " AND p.isCompanyProfile= 'Y'" +
                    " AND h.status != 'D'" +
                    " AND p.status != 'D'";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            "SELECT " +
                    " COUNT(p.id)" +                                        //[0]
                    " FROM Profile p" +
                    " LEFT JOIN Hospital h ON h.id = p.company.id" +
                    " WHERE " +
                    " p.id!= :id" +
                    " AND p.name =:name AND h.id =:companyId" +
                    " AND p.isCompanyProfile= 'Y'" +
                    " AND h.status != 'D'" +
                    " AND p.status != 'D'";

    private static Function<CompanyProfileSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCH_COMPANY_PROFILE =
            (searchRequestDTO) -> {
                String whereClause = " WHERE p.status!='D' AND h.status!='D' AND p.isCompanyProfile= 'Y'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getCompanyProfileId()))
                    whereClause += " AND p.id =" + searchRequestDTO.getCompanyProfileId();

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND p.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getCompanyId()))
                    whereClause += " AND h.id=" + searchRequestDTO.getCompanyId();

                whereClause += " ORDER BY p.id DESC";

                return whereClause;
            };

    public static Function<CompanyProfileSearchRequestDTO, String> QUERY_TO_SEARCH_COMPANY_PROFILE
            = (searchRequestDTO) -> {

        return " SELECT" +
                " p.id as id," +                                            //[0]
                " p.name as name," +                                        //[1]
                " p.status as status," +                                    //[2]
                " h.name as companyName" +                                  //[3]
                " FROM" +
                " Profile p" +
                " LEFT JOIN Hospital h ON h.id = p.company.id" +
                GET_WHERE_CLAUSE_FOR_SEARCH_COMPANY_PROFILE.apply(searchRequestDTO);
    };

    public static final String QUERY_TO_FETCH_COMPANY_PROFILE_DETAILS =
            " SELECT" +
                    " p.name as name," +                                    //[0]
                    " p.status as status," +                                //[1]
                    " p.description as description," +                      //[2]
                    " p.remarks as remarks," +                              //[3]
                    " h.id as companyId," +                                 //[4]
                    " h.name as companyName," +                             //[5]
                    " p.isAllRoleAssigned as isAllRoleAssigned," +
                    COMPANY_PROFILES_AUDITABLE_QUERY() +            //[6]
                    " FROM" +
                    " Profile p" +
                    " LEFT JOIN Hospital h ON h.id = p.company.id" +
                    " WHERE" +
                    " p.id=:id" +
                    " AND p.status!='D'" +
                    " AND h.status!='D'" +
                    " AND p.isCompanyProfile= 'Y'";

    public static final String QUERY_TO_FETCH_COMPANY_PROFILE_MENU_DETAILS =
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

    public static final String QUERY_TO_FETCH_ACTIVE_COMPANY_PROFILES_FOR_DROPDOWN =
            " SELECT" +
                    " p.id as value," +                             //[0]
                    " p.name as label" +                            //[1]
                    " FROM Profile p" +
                    " WHERE" +
                    " p.status ='Y'" +
                    " AND p.isCompanyProfile= 'Y'" +
                    " ORDER BY p.name ASC";

    public static final String QUERY_TO_FETCH_ACTIVE_COMPANY_PROFILES_BY_COMPANY_ID =
            " SELECT" +
                    " p.id as value," +                             //[0]
                    " p.name as label" +                            //[1]
                    " FROM Profile p" +
                    " WHERE" +
                    " p.status ='Y'" +
                    " AND p.isCompanyProfile= 'Y'" +
                    " AND p.company.id =:companyId" +
                    " ORDER BY p.name ASC";

    public static String COMPANY_PROFILES_AUDITABLE_QUERY() {
        return " p.createdBy as createdBy," +
                " p.createdDate as createdDate," +
                " p.lastModifiedBy as lastModifiedBy," +
                " p.lastModifiedDate as lastModifiedDate";
    }

    public static String COMPANY_PROFILE_MENU_AUDITABLE_QUERY() {
        return " pm.createdBy as createdBy," +
                " pm.createdDate as createdDate," +
                " pm.lastModifiedBy as lastModifiedBy," +
                " pm.lastModifiedDate as lastModifiedDate";
    }

}
