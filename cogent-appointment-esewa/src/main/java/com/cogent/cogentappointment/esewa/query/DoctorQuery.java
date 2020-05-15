package com.cogent.cogentappointment.esewa.query;

/**
 * @author smriti on 2019-09-29
 */
public class DoctorQuery {

    public static final String QUERY_TO_FETCH_MIN_DOCTOR_INFO =
            " SELECT" +
                    " d.id as doctorId," +                                              //[0]
                    " d.name as doctorName," +                                          //[1]
                    " CASE WHEN" +
                    " (da.status IS NULL" +
                    " OR da.status = 'N')" +
                    " THEN NULL" +
                    " ELSE" +
                    " da.file_uri" +
                    " END as fileUri," +                                                //[2]
                    " s.id as specializationId," +                                      //[3]
                    " s.name as specializationName," +                                  //[4]
                    " tbl1.qualificationAlias as qualificationAlias," +                 //[5]
                    " d.nmc_number as nmcNumber," +                                     //[6]
                    " dc.appointment_charge as doctorCharge,"+                          //[7]
                    " d.salutation as doctorSalutation"+                                //[8]
                    " FROM" +
                    " doctor d" +
                    " LEFT JOIN doctor_avatar da ON d.id = da.doctor_id" +
                    " LEFT JOIN doctor_specialization ds ON d.id = ds.doctor_id" +
                    " LEFT JOIN specialization s ON s.id = ds.specialization_id" +
                    " LEFT JOIN(" +
                    " SELECT" +
                    " GROUP_CONCAT(qa.name) as qualificationAlias," +
                    " dq.doctor_id as doctorId" +
                    " FROM" +
                    " doctor_qualification dq" +
                    " LEFT JOIN qualification q ON q.id = dq.qualification_id" +
                    " LEFT JOIN qualification_alias qa ON qa.id = q.qualification_alias" +
                    " WHERE" +
                    " qa.status = 'Y'" +
                    " AND q.status = 'Y'" +
                    " AND dq.status = 'Y'" +
                    " GROUP BY" +
                    " dq.doctor_id" +
                    " )tbl1 ON tbl1.doctorId = d.id" +
                    " LEFT JOIN hospital h ON h.id = d.hospital_id" +
                    " LEFT JOIN doctor_appointment_charge dc ON d.id = dc.doctor_id"+
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND ds.status = 'Y'" +
                    " AND s.status = 'Y'" +
                    " AND h.status = 'Y'" +
                    " AND h.is_company='N'" +
                    " AND h.id =:hospitalId" +
                    " ORDER BY d.name";

    public static String QUERY_TO_FETCH_DOCTOR_APPOINTMENT_FOLLOW_UP_CHARGE =
            " SELECT da.appointmentFollowUpCharge" +
                    " FROM DoctorAppointmentCharge da " +
                    " WHERE da.doctorId.id = :doctorId" +
                    " AND da.doctorId.hospital.id = :hospitalId";

    public static String QUERY_TO_FETCH_DOCTOR_APPOINTMENT_CHARGE =
            " SELECT da.appointmentCharge" +
                    " FROM DoctorAppointmentCharge da " +
                    " WHERE da.doctorId.id = :doctorId" +
                    " AND da.doctorId.hospital.id = :hospitalId";

}