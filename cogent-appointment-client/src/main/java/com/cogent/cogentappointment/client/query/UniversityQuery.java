package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.university.UniversitySearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti on 08/11/2019
 */
public class UniversityQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT COUNT(u.id)" +
                    " FROM University u" +
                    " WHERE" +
                    " u.status !='D'" +
                    " AND u.name=:name";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT COUNT(u.id)" +
                    " FROM University u" +
                    " WHERE" +
                    " u.status!='D'" +
                    " AND u.id!=:id" +
                    " AND u.name=:name";

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_UNIVERSITY =
            "SELECT u.id as id," +                                                //[0]
                    " u.name as name," +                                          //[1]
                    " u.address as address," +                                    //[2]
                    " c.id as countryId," +                                        //[3]
                    " c.name as countryName," +                                   //[4]
                    " u.status as status" +                                       //[5]
                    " FROM University u" +
                    " LEFT JOIN Country c ON c.id = u.country.id";

    public static Function<UniversitySearchRequestDTO, String> QUERY_TO_SEARCH_UNIVERSITY =
            (searchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_UNIVERSITY +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_UNIVERSITY(searchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_UNIVERSITY
            (UniversitySearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE u.status!='D'";

        if (!Objects.isNull(searchRequestDTO.getUniversityId()))
            whereClause += " AND u.id = " + searchRequestDTO.getUniversityId();

        if (!Objects.isNull(searchRequestDTO.getCountryId()))
            whereClause += " AND c.id=" + searchRequestDTO.getCountryId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND u.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_UNIVERSITY_DETAILS =
            "SELECT" +
                    " u.name as name," +                                          //[0]
                    " u.address as address," +                                    //[1]
                    " u.status as status," +                                      //[2]
                    " u.remarks as remarks," +                                    //[3]
                    " c.name as countryName," +                                   //[4]
                    " c.id as countryId" +                                        //[5]
                    " FROM University u " +
                    " LEFT JOIN Country c ON c.id = u.country.id" +
                    " WHERE u.status != 'D'" +
                    " AND u.id =:id";

    public static final String QUERY_TO_FETCH_ACTIVE_UNIVERSITY =
            "SELECT" +
                    " u.id as value," +                         //[0]
                    " u.name as label" +                        //[1]
                    " FROM University u" +
                    " WHERE u.status = 'Y'";
}
