package com.cogent.cogentappointment.client.query;

import static com.cogent.cogentappointment.client.query.CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR;

/**
 * @author smriti on 05/06/20
 */
public class HospitalDepartmentDoctorInfoQuery {

    public static String QUERY_TO_FETCH_ASSIGNED_HOSPITAL_DEPARTMENT_DOCTOR =

            " SELECT" +
                    " hd.id as value," +                                    //[0]
                    " d.name as label," +                                   //[1]
                    QUERY_TO_FETCH_DOCTOR_AVATAR +
                    " FROM HospitalDepartment h" +
                    " LEFT JOIN HospitalDepartmentDoctorInfo hd ON h.id = hd.hospitalDepartment.id" +
                    " LEFT JOIN Doctor d ON d.id = hd.doctor.id" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                    " WHERE h.status = 'Y'" +
                    " AND hd.status = 'Y'" +
                    " AND d.status = 'Y'" +
                    " AND h.id =:hospitalDepartmentId";

    public static String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_DOCTOR_INFO(String hospitalDepartmentDoctorInfoId) {
        return " SELECT h FROM HospitalDepartmentDoctorInfo h " +
                " WHERE" +
                " h.id IN (" + hospitalDepartmentDoctorInfoId + ")" +
                " AND h.status = 'Y'";
    }
}
