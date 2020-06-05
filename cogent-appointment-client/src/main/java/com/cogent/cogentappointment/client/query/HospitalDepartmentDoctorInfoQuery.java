package com.cogent.cogentappointment.client.query;

/**
 * @author smriti on 05/06/20
 */
public class HospitalDepartmentDoctorInfoQuery {

    public static String QUERY_TO_FETCH_ASSIGNED_HOSPITAL_DEPARTMENT_DOCTOR =

            " SELECT" +
                    " hd.id as value," +                                    //[0]
                    " d.name as label," +                                   //[1]
                    " CASE WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " da.fileUri" +
                    " END AS fileUri" +                                        //[2]
                    " FROM HospitalDepartment h" +
                    " LEFT JOIN HospitalDepartmentDoctorInfo hd ON h.id = hd.hospitalDepartment.id" +
                    " LEFT JOIN Doctor d ON d.id = hd.doctor.id" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                    " WHERE h.status = 'Y'" +
                    " AND hd.status = 'Y'" +
                    " AND d.status = 'Y'" +
                    " AND h.id =:hospitalDepartmentId";
}
