package com.cogent.cogentappointment.query;

import com.cogent.cogentappointment.dto.request.hospital.HospitalSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author Rupak
 */
public class HospitalQuery {

    public static final String QUERY_TO_FIND_HOSPITAL_COUNT_BY_NAME =
            "SELECT COUNT(h.id) FROM Hospital h WHERE h.name =:name AND h.status != 'D'";

    public static final String QUERY_TO_FIND_HOSPITAL_COUNT_BY_ID_AND_NAME =
            "SELECT COUNT(h.id) FROM Hospital h WHERE h.id!= :id AND h.name =:name AND h.status != 'D'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN =
            " SELECT" +
                    " h.id as value," +
                    " h.name as label" +
                    " FROM" +
                    " Hospital h" +
                    " WHERE h.status ='Y'";

    public static String QUERY_TO_SEARCH_HOSPITAL(HospitalSearchRequestDTO searchRequestDTO) {
        return " SELECT" +
                " h.id as id," +
                " h.name as name," +
                " h.status as status," +
                " h.remarks as remarks" +
                " FROM" +
                " Hospital h" +
                GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL.apply(searchRequestDTO);
    }

    private static Function<HospitalSearchRequestDTO, String> GET_WHERE_CLAUSE_FOR_SEARCHING_HOSPITAL =
            (searchRequestDTO) -> {
                String whereClause = " WHERE h.status!='D'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
                    whereClause += " AND h.status='" + searchRequestDTO.getStatus() + "'";

                if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
                    whereClause += " AND h.name='" + searchRequestDTO.getName() + "'";

                whereClause += " ORDER BY h.id DESC";

                return whereClause;
            };

}
