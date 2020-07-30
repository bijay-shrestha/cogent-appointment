package com.cogent.cogentappointment.admin.query;

import static com.cogent.cogentappointment.admin.query.CdnFileQuery.QUERY_TO_FETCH_DOCTOR_AVATAR;

/**
 * @author smriti on 07/06/20
 */
public class HospitalDeptWeekDaysDutyRosterDoctorInfoQuery {

    public static final String QUERY_TO_FETCH_WEEK_DAYS_DOCTOR_INFO =
            " SELECT" +
                    " h.id as hospitalDepartmentWeekDaysDutyRosterDoctorInfoId," +                  //[0]
                    QUERY_TO_FETCH_DOCTOR_AVATAR +                                                   //[1]
                    " h.hospitalDepartmentDoctorInfo.id as value," +                                //[2]
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +
                    " END as label," +                                                                  //[3]
                    " d.status as isDoctorActive"+
                    " FROM HospitalDepartmentWeekDaysDutyRosterDoctorInfo h" +
                    " LEFT JOIN HospitalDepartmentDoctorInfo hd ON hd.id = h.hospitalDepartmentDoctorInfo.id" +
                    " LEFT JOIN Doctor d ON d.id = hd.doctor.id" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                    " WHERE h.hospitalDepartmentWeekDaysDutyRoster.id =:id" +
                    " AND h.status = 'Y'";
}
