package com.cogent.cogentappointment.logging.query;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import org.springframework.util.ObjectUtils;

/**
 * @author Rupak
 */
public class AdminLogQuery {

    public static String QUERY_TO_FETCH_USER_LOGS_STATICS(AdminLogSearchRequestDTO searchRequestDTO) {

        return " SELECT " +
                " al.feature as feature," +
                " count(al.id) as count" +
                " from AdminLog al" +
                " LEFT JOIN Admin a ON al.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Hospital h ON h.id=p.company" +
                WHERE_CLAUSE_TO_SEARCH_ADMIN_LOGS(searchRequestDTO) +
                " GROUP BY al.feature " +
                " ORDER BY count(al.id) DESC";


    }

    public static String QUERY_TO_SEARCH_ADMIN_LOGS(AdminLogSearchRequestDTO searchRequestDTO) {

        return " SELECT " +
                " DATE_FORMAT(al.logDateTime,'%D %M %Y %h:%i %p') as logDateTime," +
                " a.username as userName," +
                " al.ipAddress as ipAddress," +
                " al.feature as feature," +
                " al.actionType as actionType," +
                " al.logDescription as logDescription," +
                " al.status as status" +
                " FROM AdminLog al" +
                " LEFT JOIN Admin a ON al.adminId.id =a.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Hospital h ON h.id=p.company" +
                WHERE_CLAUSE_TO_SEARCH_ADMIN_LOGS(searchRequestDTO)
                + " ORDER BY al.logDateTime DESC";
    }

    public static String WHERE_CLAUSE_TO_SEARCH_ADMIN_LOGS(AdminLogSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE al.status != 'D'" +
                " AND al.logDate BETWEEN :fromDate AND :toDate";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getHospitalId()))
            whereClause += " AND h.id=" + searchRequestDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getUserName()) || !searchRequestDTO.getUserName().equals(""))

            whereClause += " AND (a.username ='" + searchRequestDTO.getUserName() + "' OR a.email ='" + searchRequestDTO.getUserName()
                    + "' OR a.mobileNumber ='" + searchRequestDTO.getUserName() + " OR a.fullName LIKE %" + searchRequestDTO.getUserName() + "%" + "')";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getParentId()))
            whereClause += " AND al.parentId=" + searchRequestDTO.getParentId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getRoleId()))
            whereClause += " AND al.roleId=" + searchRequestDTO.getRoleId();

        return whereClause;

    }
}
