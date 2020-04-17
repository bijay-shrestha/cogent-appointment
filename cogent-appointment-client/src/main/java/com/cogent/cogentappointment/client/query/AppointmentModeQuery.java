package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversitySearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class AppointmentModeQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT COUNT(am.id)" +
                    " FROM AppointmentMode am" +
                    " WHERE" +
                    " am.status !='D'" +
                    " AND am.name=:name";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT COUNT(am.id)" +
                    " FROM AppointmentMode am" +
                    " WHERE" +
                    " am.status!='D'" +
                    " AND am.id!=:id" +
                    " AND am.name=:name";

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_UNIVERSITY =
            "SELECT am.id as id," +
                    " am.name as name," +
                    " am.code as code," +
                    " am.status as status" +
                    " FROM AppointmentMode am";

    public static Function<AppointmentModeSearchRequestDTO, String> QUERY_TO_SEARCH_APPOINTMENT_MODE =
            (searchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_UNIVERSITY +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_UNIVERSITY(searchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_UNIVERSITY
            (AppointmentModeSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE am.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getCode()))
            whereClause += " AND am.code = '" + searchRequestDTO.getCode()+"'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getId()))
            whereClause += " AND am.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND am.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_APPOINTMENT_MODE_DETAILS =
            "SELECT" +
                    " am.name as name," +
                    " am.code as code," +
                    " am.status as status," +
                    " am.remarks as remarks," +
                    " am.description as description," +
                    " am.is_editable as isEditable," +
                    " DATE_FORMAT(am.created_date,'%Y-%m-%d') as createdDate," +
                    " am.created_by as createdBy," +
                    " DATE_FORMAT(am.last_modified_date,'%Y-%m-%d') as modifiedDate," +
                    " am.last_modified_by as modifiedBy" +
                    " FROM appointment_mode am " +
                    " WHERE am.status != 'D'" +
                    " AND am.id =:id";

    public static final String QUERY_TO_FETCH_ACTIVE_APPOINTMENT_MODE =
            "SELECT" +
                    " am.id as value," +
                    " am.name as label" +
                    " FROM AppointmentMode am " +
                    " WHERE am.status = 'Y'";
}
