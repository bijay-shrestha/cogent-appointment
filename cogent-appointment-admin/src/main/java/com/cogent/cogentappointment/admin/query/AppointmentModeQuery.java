package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class AppointmentModeQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT am.name," +
                    " am.code" +
                    " FROM AppointmentMode am" +
                    " WHERE" +
                    " am.status !='D'" +
                    " AND (am.name=:name OR am.code=:code)";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT am.name," +
                    " am.code" +
                    " FROM  AppointmentMode am" +
                    " WHERE" +
                    " am.status !='D'" +
                    " AND am.id!=:id" +
                    " AND (am.name=:name OR am.code=:code)";

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_APPOINTMENT_MODE =
            "SELECT am.id as id," +
                    " am.name as name," +
                    " am.code as code," +
                    " am.status as status," +
                    " am.isEditable as isEditable" +
                    " FROM AppointmentMode am";

    public static Function<AppointmentModeSearchRequestDTO, String> QUERY_TO_SEARCH_APPOINTMENT_MODE =
            (searchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_APPOINTMENT_MODE +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_APPOINTMENT_MODE(searchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_APPOINTMENT_MODE
            (AppointmentModeSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE am.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCode()))
            whereClause += " AND am.code = '" + searchRequestDTO.getCode()+"'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getId()))
            whereClause += " AND am.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND am.status='" + searchRequestDTO.getStatus() + "'";

        whereClause += " ORDER BY am.id DESC";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_APPOINTMENT_MODE_DETAILS =
            "SELECT" +
                    " am.name as name," +
                    " am.code as code," +
                    " am.status as status," +
                    " am.remarks as remarks," +
                    " am.description as description," +
                    " DATE_FORMAT(am.created_date,'%d %M, %Y, %h:%i %p') as createdDate," +
                    " am.created_by as createdBy," +
                    " DATE_FORMAT(am.last_modified_date,'%d %M, %Y, %h:%i %p') as modifiedDate," +
                    " am.last_modified_by as modifiedBy" +
                    " FROM appointment_mode am " +
                    " WHERE am.status != 'D'" +
                    " AND am.id =:id";

    public static final String QUERY_TO_FETCH_ACTIVE_APPOINTMENT_MODE =
            "SELECT" +
                    " am.id as value," +
                    " am.name as label" +
                    " FROM AppointmentMode am " +
                    " WHERE am.status = 'Y'" +
                    " ORDER BY am.id DESC";

    public static final String QUERY_TO_FETCH_APPOINTMENT_MODE =
            "SELECT" +
                    " am.id as value," +
                    " am.name as label" +
                    " FROM AppointmentMode am " +
                    " WHERE am.status != 'D'" +
                    " ORDER BY am.id DESC";
}
