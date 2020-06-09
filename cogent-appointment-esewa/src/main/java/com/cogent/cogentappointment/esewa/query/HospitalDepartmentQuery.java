package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 28/05/20
 */
public class HospitalDepartmentQuery {

    public static final String QUERY_TO_FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " hd.id as value," +                            //[0]
                    " hd.name as label" +                           //[1]
                    " FROM HospitalDepartment hd" +
                    " WHERE hd.status = 'Y'" +
                    " AND hd.hospital.id= :hospitalId" +
                    " ORDER BY label ASC";

    public static final String QUERY_TO_FETCH_ACTIVE_MIN_HOSPITAL_DEPARTMENT =
            "SELECT" +
                    " hd.id as hospitalDepartmentId," +                            //[0]
                    " hd.name as hospitalDepartmentName" +                         //[1]
                    " FROM HospitalDepartment hd" +
                    " WHERE hd.status = 'Y'" +
                    " AND hd.hospital.id= :hospitalId" +
                    " ORDER BY hospitalDepartmentName ASC";
}
