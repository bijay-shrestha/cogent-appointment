package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.qualification.QualificationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti on 11/11/2019
 */
public class QualificationQuery {

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_QUALIFICATION =
            "SELECT q.id as id," +                                               //[0]
                    " q.name as name," +                                        //[1]
                    " q.university as university," +                             //[2]
                    " q.country.name as countryName," +                          //[3]
                    " q.qualificationAlias.name as qualificationAliasName," +   //[4]
                    " q.status as status" +                                     //[5]
                    " FROM Qualification q ";

    public static Function<QualificationSearchRequestDTO, String> QUERY_TO_SEARCH_QUALIFICATION =
            (qualificationSearchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_QUALIFICATION +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION(qualificationSearchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION
            (QualificationSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE q.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getName()))
            whereClause += " AND q.name LIKE '%" + searchRequestDTO.getName() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getUniversity()))
            whereClause += " AND q.university LIKE '%" + searchRequestDTO.getUniversity() + "%'";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_QUALIFICATION_DETAILS =
            "SELECT" +
                    " q.name as name," +                                        //[0]
                    " q.university as university," +                            //[1]
                    " q.country.id as countryId," +                             //[2]
                    " q.country.name as countryName," +                         //[3]
                    " q.qualificationAlias.id as qualificationAliasId," +       //[4]
                    " q.qualificationAlias.name as qualificationAliasName," +    //[5]
                    " q.status as status," +                                    //[6]
                    " q.remarks as remarks" +                                   //[7]
                    " FROM Qualification q " +
                    " WHERE q.status != 'D'" +
                    " AND q.id =:id";

    public static final String QUERY_TO_FETCH_ACTIVE_QUALIFICATION_FOR_DROPDOWN =
            "SELECT q.id as id," +                                               //[0]
                    " q.name as name," +                                        //[1]
                    " q.university as university," +                             //[2]
                    " q.country.name as countryName," +                          //[3]
                    " q.qualificationAlias.name as qualificationAliasName" +   //[4]
                    " FROM Qualification q " +
                    " WHERE q.status = 'Y'";
}
