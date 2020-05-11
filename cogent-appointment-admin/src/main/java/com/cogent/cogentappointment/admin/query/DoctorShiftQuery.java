package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 10/05/20
 */
public class DoctorShiftQuery {

    public static String QUERY_TO_FETCH_DOCTOR_SHIFTS =
            "SELECT s.id as value," +
                    " s.name as label" +
                    " FROM Doctor d" +
                    " LEFT JOIN DoctorShift ds ON d.id = ds.doctor.id" +
                    " LEFT JOIN Shift s ON s.id = ds.shift.id" +
                    " WHERE d.id =:doctorId" +
                    " AND ds.status  = 'Y'" +
                    " AND d.status  = 'Y'";

    public static String QUERY_TO_FETCH_ASSIGNED_DOCTOR_SHIFTS =
            "SELECT s.id as value" +
                    " FROM Doctor d" +
                    " LEFT JOIN DoctorShift ds ON d.id = ds.doctor.id" +
                    " LEFT JOIN Shift s ON s.id = ds.shift.id" +
                    " WHERE d.id =:doctorId" +
                    " AND ds.status  = 'Y'" +
                    " AND d.status  = 'Y'";

    public static String QUERY_TO_FETCH_DOCTOR_SHIFT_COUNT(String shiftIds,
                                                           Long doctorId) {
        return "SELECT" +
                " COUNT(ds.id)" +
                " FROM DoctorShift ds" +
                " WHERE ds.doctor.id = " + doctorId +
                " AND ds.shift.id IN (" + shiftIds + ")" +
                " AND ds.status = 'Y'";
    }
}
