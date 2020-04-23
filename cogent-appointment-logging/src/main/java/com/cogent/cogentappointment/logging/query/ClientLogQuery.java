package com.cogent.cogentappointment.logging.query;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import org.springframework.util.ObjectUtils;

/**
 * @author Rupak
 */
public class ClientLogQuery {

    public static String QUERY_TO_FETCH_USER_LOGS_STATICS(ClientLogSearchRequestDTO searchRequestDTO) {

        return " SELECT " +
                " cl.feature as feature," +
                " count(cl.id) as count" +
                " from ClientLog cl" +
                " LEFT JOIN Admin a ON cl.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Department d ON d.id = p.department.id " +
                " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                " LEFT JOIN AdminMetaInfo ami ON ami.admin.id=a.id" +
                WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(searchRequestDTO) +
                " GROUP BY cl.feature " +
                " ORDER BY count(cl.id) DESC";


    }

    public static String QUERY_TO_FETCH_USER_LOGS_STATICS_FOR_PIE_CHART(ClientLogSearchRequestDTO searchRequestDTO) {

        return " SELECT" +
                " cl.feature as feature," +
                " count(cl.id) as count" +
                " from ClientLog cl" +
                " LEFT JOIN Admin a ON cl.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Department d ON d.id = p.department.id" +
                " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                " LEFT JOIN AdminMetaInfo ami ON ami.admin.id=a.id" +
                WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(searchRequestDTO) +
                " GROUP BY cl.feature" +
                " ORDER BY count(cl.id) DESC";


    }

    public static String QUERY_TO_SEARCH_CLIENT_LOGS(ClientLogSearchRequestDTO searchRequestDTO) {

        return " SELECT " +
                " cl.logDate as logDate," +
                " DATE_FORMAT(cl.logDateTime,'%h:%i %p') as logTime," +
                " cl.browser as browser," +
                " cl.operatingSystem as os," +
                " a.email as email," +
                " a.mobileNumber as mobileNumber," +
                " cl.ipAddress as ipAddress," +
                " cl.feature as feature," +
                " cl.actionType as actionType," +
                " cl.logDescription as logDescription," +
                " cl.status as status" +
                " FROM ClientLog cl" +
                " LEFT JOIN Admin a ON cl.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Department d ON d.id = p.department.id" +
                " LEFT JOIN Hospital h ON h.id = d.hospital.id" +
                " LEFT JOIN AdminMetaInfo ami ON ami.admin.id=a.id" +
                WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(searchRequestDTO)
                + " ORDER BY cl.logDateTime DESC";
    }

    public static String WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(ClientLogSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE cl.status != 'D'" +
                " AND cl.logDate BETWEEN :fromDate AND :toDate";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getClientId()))
            whereClause += " AND h.id=" + searchRequestDTO.getClientId();


        if (!ObjectUtils.isEmpty(searchRequestDTO.getAdminMetaInfoId()))
            whereClause += " AND h.id=" + searchRequestDTO.getAdminMetaInfoId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAdminMetaInfoId()))
            whereClause += " AND ami.id=" + searchRequestDTO.getAdminMetaInfoId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getParentId()))
            whereClause += " AND cl.parentId=" + searchRequestDTO.getParentId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getRoleId()))
            whereClause += " AND cl.roleId=" + searchRequestDTO.getRoleId();

        return whereClause;

    }

}
