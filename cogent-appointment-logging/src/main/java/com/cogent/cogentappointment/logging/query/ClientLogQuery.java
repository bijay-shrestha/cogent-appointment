package com.cogent.cogentappointment.logging.query;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import org.springframework.util.ObjectUtils;

/**
 * @author Rupak
 */
public class ClientLogQuery {

    public static String QUERY_TO_FETCH_USER_LOGS_STATICS(ClientLogSearchRequestDTO searchRequestDTO) {

        return " SELECT " +
                " al.feature as feature," +
                " count(al.id) as count" +
                " from AdminLog al" +
                " LEFT JOIN Admin a ON al.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Hospital h ON h.id=p.company" +
                WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(searchRequestDTO) +
                " GROUP BY al.feature " +
                " ORDER BY count(al.id) DESC";


    }

    public static String QUERY_TO_SEARCH_CLIENT_LOGS(ClientLogSearchRequestDTO searchRequestDTO) {

        return " SELECT " +
                " al.logDate as logDate," +
                " a.username as userName," +
                " al.ipAddress as ipAddress," +
                " al.feature as feature," +
                " al.actionType as actionType," +
                " al.logDescription as logDescription" +
                " FROM AdminLog al" +
                " LEFT JOIN Admin a ON al.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Hospital h ON h.id=p.company" +
                WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(searchRequestDTO);
    }

    public static String WHERE_CLAUSE_TO_SEARCH_CLIENT_LOGS(ClientLogSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE h.id=:hospitalId AND al.status != 'D'" +
                " AND al.logDate BETWEEN :fromDate AND :toDate";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getUserName()))
            whereClause += " AND (a.username =:username OR a.email =:username OR a.mobileNumber = :username)";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getParentId()))
            whereClause += " AND al.parentId=" + searchRequestDTO.getParentId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getRoleId()))
            whereClause += " AND al.roleId=" + searchRequestDTO.getRoleId();

        return whereClause;

    }

}
