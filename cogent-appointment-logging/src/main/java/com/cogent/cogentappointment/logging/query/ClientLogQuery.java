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
                " LEFT JOIN Hospital h ON h.id=p.company" +
                WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(searchRequestDTO) +
                " GROUP BY cl.feature " +
                " ORDER BY count(cl.id) DESC";


    }

    public static String QUERY_TO_SEARCH_CLIENT_LOGS(ClientLogSearchRequestDTO searchRequestDTO) {

        return " SELECT " +
                " DATE_FORMAT(cl.logDateTime,'%M %d %Y %h:%i %p') as logDateTime," +
                " cl.browser as browser," +
                " cl.operatingSystem as os," +
                " a.username as userName," +
                " cl.ipAddress as ipAddress," +
                " cl.feature as feature," +
                " cl.actionType as actionType," +
                " cl.logDescription as logDescription," +
                " cl.status as status" +
                " FROM ClientLog cl" +
                " LEFT JOIN Admin a ON cl.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Hospital h ON h.id=p.company" +
                WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(searchRequestDTO)
                + " ORDER BY cl.logDateTime DESC";
    }

    public static String WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(ClientLogSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE h.id=:hospitalId AND cl.status != 'D'" +
                " AND cl.logDate BETWEEN :fromDate AND :toDate";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getUserName()) || !searchRequestDTO.getUserName().equals(""))

            whereClause += " AND (a.username ='" + searchRequestDTO.getUserName() + "' OR a.email ='" + searchRequestDTO.getUserName()
                    + "' OR a.mobileNumber ='" + searchRequestDTO.getUserName() + " OR a.fullName LIKE %" + searchRequestDTO.getUserName() + "%" + "')";


        if (!ObjectUtils.isEmpty(searchRequestDTO.getParentId()))
            whereClause += " AND cl.parentId=" + searchRequestDTO.getParentId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getRoleId()))
            whereClause += " AND cl.roleId=" + searchRequestDTO.getRoleId();

        return whereClause;

    }

}
