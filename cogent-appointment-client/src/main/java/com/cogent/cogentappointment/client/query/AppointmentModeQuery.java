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
                    " FROM ApppointmentMode am" +
                    " WHERE" +
                    " am.status !='D'" +
                    " AND am.name=:name";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT COUNT(am.id)" +
                    " FROM ApppointmentMode am" +
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

        if (!Objects.isNull(searchRequestDTO.getCode()))
            whereClause += " AND am.code = " + searchRequestDTO.getCode();

        if (!Objects.isNull(searchRequestDTO.getId()))
            whereClause += " AND am.id=" + searchRequestDTO.getId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND am.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_APPOINTMENT_MODE_DETAILS =
            "SELECT" +
                    " am.name as name," +                                          //[0]
                    " am.code as code," +                                    //[1]
                    " am.status as status," +                                      //[2]
                    " am.remarks as remarks," +                                    //[3]
                    " am.description as description," +                                   //[4]
                    " am.is_editable as isEditable," +
                    " am.created_date as createdDate," +
                    " am.created_by as createdBy," +
                    " am.last_modified_date as modifiedDate," +
                    " am.last_modified_by as modifiedBy" +                                        //[5]
                    " FROM appointment_mode am " +
                    " WHERE am.status != 'D'" +
                    " AND am.id =:id";

    public static final String QUERY_TO_FETCH_ACTIVE_APPOINTMENT_MODE =
            "SELECT" +
                    " am.id as value," +                         //[0]
                    " am.name as label" +                        //[1]
                    " FROM AppointmentMode am " +
                    " WHERE am.status = 'Y'";
}
