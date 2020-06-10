package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 10/06/20
 */
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfoQuery {

    public static final String QUERY_TO_FETCH_AVAILABLE_DOCTORS =
            " SELECT d.name" +
                    " FROM HospitalDepartmentDutyRoster h" +
                    " LEFT JOIN HospitalDepartmentWeekDaysDutyRoster hw ON h.id = hw.hospitalDepartmentDutyRoster.id" +
                    " LEFT JOIN HospitalDepartmentWeekDaysDutyRosterDoctorInfo hwd ON hw.id = hwd.hospitalDepartmentWeekDaysDutyRoster.id" +
                    " LEFT JOIN HospitalDepartmentDoctorInfo hd ON hd.id = hwd.hospitalDepartmentDoctorInfo.id" +
                    " LEFT JOIN Doctor d ON d.id = hd.doctor.id" +
                    " WHERE h.status = 'Y'" +
                    " AND hwd.status = 'Y'" +
                    " AND d.status = 'Y'" +
                    " AND hw.weekDays.code =:code" +
                    " AND h.id =:hddRosterId";
}
