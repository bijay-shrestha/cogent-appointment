package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti on 11/11/2019
 */
public class QualificationQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT COUNT(q.id)" +
                    " FROM Qualification q" +
                    " WHERE" +
                    " q.status !='D'" +
                    " AND q.name=:name";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT COUNT(q.id)" +
                    " FROM Qualification q" +
                    " WHERE" +
                    " q.status !='D'" +
                    " AND q.id!=:id" +
                    " AND q.name=:name";

    private static final String SELECT_CLAUSE_TO_FETCH_MINIMAL_QUALIFICATION =
            "SELECT q.id as id," +                                                //[0]
                    " q.name as name," +                                          //[1]
                    " u.name as universityName," +                                //[2]
                    " qa.name as qualificationAliasName," +                       //[3]
                    " q.status as status" +                                       //[4]
                    " FROM Qualification q " +
                    " LEFT JOIN University u ON u.id = q.university.id" +
                    " LEFT JOIN QualificationAlias qa ON qa.id = q.qualificationAlias.id";

    public static Function<QualificationSearchRequestDTO, String> QUERY_TO_SEARCH_QUALIFICATION =
            (qualificationSearchRequestDTO -> (
                    SELECT_CLAUSE_TO_FETCH_MINIMAL_QUALIFICATION +
                            GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION(qualificationSearchRequestDTO)
            ));

    private static String GET_WHERE_CLAUSE_FOR_SEARCHING_QUALIFICATION
            (QualificationSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE q.status!='D'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getQualificationId()))
            whereClause += " AND q.id = " + searchRequestDTO.getQualificationId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getUniversityId()))
            whereClause += " AND u.id=" + searchRequestDTO.getUniversityId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getQualificationAliasId()))
            whereClause += " AND qa.id=" + searchRequestDTO.getQualificationAliasId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND q.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_QUALIFICATION_DETAILS =
            "SELECT" +
                    " q.name as name," +                                        //[0]
                    " u.id as universityId," +                                  //[1]
                    " u.name as universityName," +                              //[2]
                    " qa.id as qualificationAliasId," +                         //[3]
                    " qa.name as qualificationAliasName," +                     //[4]
                    " q.status as status," +                                    //[5]
                    " q.remarks as remarks" +                                   //[6]
                    " FROM Qualification q " +
                    " LEFT JOIN University u ON u.id = q.university.id" +
                    " LEFT JOIN QualificationAlias qa ON qa.id = q.qualificationAlias.id" +
                    " WHERE q.status != 'D'" +
                    " AND q.id =:id";

    public static final String QUERY_TO_FETCH_ACTIVE_QUALIFICATION_FOR_DROPDOWN =
            "SELECT q.id as id," +                                               //[0]
                    " q.name as qualificationName," +                            //[1]
                    " u.name as universityName," +                               //[2]
                    " qa.name as qualificationAliasName" +                       //[3]
                    " FROM Qualification q " +
                    " LEFT JOIN University u ON u.id = q.university.id" +
                    " LEFT JOIN QualificationAlias qa ON qa.id = q.qualificationAlias.id" +
                    " WHERE q.status = 'Y'";

    public static final String QUERY_TO_FETCH_MIN_QUALIFICATION =
            "SELECT q.id as value," +                                           //[0]
                    " q.name as label" +                                        //[1]
                    " FROM Qualification q " +
                    " WHERE q.status != 'D'";

}
