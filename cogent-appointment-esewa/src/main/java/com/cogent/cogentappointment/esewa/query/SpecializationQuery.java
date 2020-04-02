package com.cogent.cogentappointment.esewa.query;

import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.function.Function;

/**
 * @author smriti on 2019-09-25
 */
public class SpecializationQuery {

    public static final String QUERY_TO_FETCH_SPECIALIZATION_BY_HOSPITAL_ID =
            " SELECT" +
                    " s.id as value," +                                      //[0]
                    " s.name as label" +                                     //[1]
                    " FROM" +
                    " Specialization s" +
                    " LEFT JOIN Hospital h ON h.id = s.hospital.id" +
                    " WHERE s.status ='Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.isCompany='N'" +
                    " AND h.id =:hospitalId" +
                    " ORDER BY s.name";
}
