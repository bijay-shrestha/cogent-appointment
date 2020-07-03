package com.cogent.cogentappointment.client.query;

import static com.cogent.cogentappointment.client.query.PatientQuery.*;

/**
 * @author smriti on 18/06/20
 */
public class AppointmentDoctorInfoQuery {

    public static String QUERY_TO_FETCH_APPOINTMENT_DETAIL_FOR_DOCTOR_WISE_CHECK_IN =
            "SELECT" +
                    " p.name as name," +                                                 //[0]
                    QUERY_TO_CALCULATE_PATIENT_AGE_YEAR + "," +                          //[1]
                    QUERY_TO_CALCULATE_PATIENT_AGE_MONTH + "," +                        //[2]
                    QUERY_TO_CALCULATE_PATIENT_AGE_DAY + "," +                          //[3]
                    " p.gender as gender," +                                             //[4]
                    " d.value as district," +                                           //[5]
                    " vm.value as vdc," +                                               //[6]
                    " COALESCE(hpi.wardNumber, '') as wardNo," +                        //[7]
                    " hpi.address AS address," +                                        //[8]
                    " p.mobileNumber as mobileNo," +                                    //[9]
                    " COALESCE(hpi.email, '') as emailAddress," +                       //[10]
                    " d.name as doctorName," +                                           //[11]
                    " s.name as specializationName," +                                  //[12]
                    " a.appointmentNumber as appointmentNo," +                          //[13]
                    " hpi.hospitalNumber as patientId" +                                //[14]
                    " FROM Appointment a" +
                    " INNER JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                    " LEFT JOIN Address d ON d.id = hpi.district.id" +
                    " LEFT JOIN Address vm ON vm.id = hpi.vdcOrMunicipality.id" +
                    " WHERE a.id =:appointmentId" +
                    " AND a.status='PA'" +
                    " AND h.id =:hospitalId";


    public static String QUERY_TO_GET_CURRENT_APPOINTMENT_DOCTOR_INFO =
            "SELECT" +
                    " a.id as id," +
                    " a.appointment as appointment," +
                    " a.specialization as specialization," +
                    " a.doctor as doctor" +
                    " FROM AppointmentDoctorInfo a" +
                    " WHERE a.appointment.id=:appointmentId";

}
