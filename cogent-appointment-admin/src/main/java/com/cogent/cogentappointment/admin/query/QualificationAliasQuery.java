package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.qualificationAlias.QualificationAliasSearchRequestDTO;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti on 11/11/2019
 */
public class QualificationAliasQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT COUNT(qa.id)" +
                    " FROM QualificationAlias qa" +
                    " WHERE" +
                    " qa.status !='D'" +
                    " AND qa.name=:name";

    public static final String QUERY_TO_FETCH_ACTIVE_QUALIFICATION_ALIAS =
            "SELECT" +
                    " qa.id as value," +
                    " qa.name as label" +
                    " FROM QualificationAlias qa" +
                    " WHERE qa.status = 'Y'";

    private static  String SELECT_CLAUSE_TO_FETCH_MINIMAL_QUALIFICATION_ALIAS =
            "SELECT qa.id as id," +                                                //[0]
                    " qa.name as name," +                                          //[1]
                    " qa.status as status" +                                       //[2]
                    " FROM QualificationAlias qa ";

    public static Function<QualificationAliasSearchRequestDTO, String> QUERY_TO_SEARCH_QUALIFICATION_ALIAS =
            (qualificationSearchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_QUALIFICATION_ALIAS +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION_ALIAS(qualificationSearchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION_ALIAS
            (QualificationAliasSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE qa.status!='D'";

        if (!Objects.isNull(searchRequestDTO.getQualificationAliasId()))
            whereClause += " AND qa.id = " + searchRequestDTO.getQualificationAliasId();




        return whereClause;
    }

}
