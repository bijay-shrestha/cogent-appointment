package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 07/06/20
 */
public class HospitalDeptWeekDaysDutyRosterDoctorInfoQuery {

    public static final String QUERY_TO_FETCH_WEEK_DAYS_DOCTOR_INFO =
            " SELECT" +
                    " h.id as hospitalDepartmentWeekDaysDutyRosterDoctorInfoId," +                  //[0]
                    " h.hospitalDepartmentDoctorInfo.id as hospitalDepartmentDoctorInfoId," +       //[1]
                    " d.name as doctorName," +                                                      //[2]
                    " CASE WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " da.fileUri" +
                    " END AS fileUri" +                                                            //[3]
                    " FROM HospitalDepartmentWeekDaysDutyRosterDoctorInfo h" +
                    " LEFT JOIN HospitalDepartmentDoctorInfo hd ON hd.id = h.hospitalDepartmentDoctorInfo.id" +
                    " LEFT JOIN Doctor d ON d.id = hd.doctor.id" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                    " WHERE h.hospitalDepartmentWeekDaysDutyRoster.id =:id" +
                    " AND h.status = 'Y'";
}
