package com.cogent.cogentappointment.admin.query;

/**
 * @author Sauravi Thapa ON 6/9/20
 */
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfoQuery {

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_AND_DOCTOR_LIST=
            "SELECT " +
                    " DISTINCT hddi.id as value," +
                    " d.name as label," +
                    " da.fileUri as fileUri" +
                    " FROM HospitalDepartmentWeekDaysDutyRosterDoctorInfo hdwddrdi " +
                    " LEFT JOIN HospitalDepartmentWeekDaysDutyRoster hdwddr ON hdwddr.id=hdwddrdi.hospitalDepartmentWeekDaysDutyRoster.id " +
                    " LEFT JOIN WeekDays wd ON wd.id=hdwddr.weekDays.id " +
                    " LEFT JOIN HospitalDepartmentDoctorInfo hddi ON hddi.id=hdwddrdi.hospitalDepartmentDoctorInfo.id " +
                    " LEFT JOIN Doctor d ON d.id=hddi.doctor.id " +
                    " LEFT JOIN DoctorAvatar  da ON da.doctorId.id=d.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id=hddi.hospitalDepartment.id " +
                    " WHERE hd.id=:hospitalDepartmentId" +
                    " AND wd.name =:weekDayName" +
                    " AND hdwddrdi.status='Y'";

}
