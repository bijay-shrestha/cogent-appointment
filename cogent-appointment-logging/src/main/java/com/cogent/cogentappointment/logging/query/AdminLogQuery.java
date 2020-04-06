package com.cogent.cogentappointment.logging.query;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import static com.cogent.cogentappointment.logging.utils.common.DateUtils.utilDateToSqlDate;

/**
 * @author Rupak
 */
public class AdminLogQuery {

    public static String QUERY_TO_SEARCH_ADMIN_LOGS(AdminLogSearchRequestDTO searchRequestDTO) {

        return "SELECT" +
                " al.logDate as logDate," +
                " a.username as userName," +
                " al.ipAddress as ipAddress," +
                " al.feature as feature," +
                " al.actionType as actionType," +
                " al.logDescription as logDescription" +
                " FROM adminLog al" +
                " LEFT JOIN admin a ON al.adminId.id =a.id" +
                WHERE_CLAUSE_TO_SEARCH_ADMIN_LOGS(searchRequestDTO);
    }

    public static String WHERE_CLAUSE_TO_SEARCH_ADMIN_LOGS(AdminLogSearchRequestDTO searchRequestDTO) {

        String whereClause = "WHERE al.status != 'D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAdminId()))
            whereClause += " AND a.id=" + searchRequestDTO.getAdminId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getParentId()))
            whereClause += " AND al.parentId=" + searchRequestDTO.getParentId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getRoleId()))
            whereClause += " AND al.roleId=" + searchRequestDTO.getRoleId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getDate()))
            whereClause += " AND al.logDate=:" + utilDateToSqlDate(searchRequestDTO.getDate());

        return whereClause;

    }
}
