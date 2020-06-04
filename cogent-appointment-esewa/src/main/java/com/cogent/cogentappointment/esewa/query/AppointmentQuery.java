package com.cogent.cogentappointment.esewa.query;


import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.cogent.cogentappointment.esewa.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE;

/**
 * @author smriti on 2019-10-22
 */
public class AppointmentQuery {

    public static String QUERY_TO_VALIDATE_APPOINTMENT_EXISTS =
            "SELECT COUNT(a.id)" +
                    " FROM  Appointment a" +
                    " WHERE a.appointmentDate =:appointmentDate" +
                    " AND a.doctorId.id =:doctorId" +
                    " AND a.specializationId.id =:specializationId" +
                    " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime" +
                    " AND a.status='PA'";

    public static String QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER =
            "SELECT a.appointment_number" +
                    " FROM appointment a" +
                    " LEFT JOIN hospital h ON h.id =a.hospital_id" +
                    " WHERE" +
                    " str_to_date(a.created_date_nepali,'%Y-%m-%d')" +
                    " BETWEEN :fromDate AND :toDate" +
                    " AND h.id =:hospitalId" +
                    " ORDER BY a.id DESC LIMIT 1";

    /*USED IN DOCTOR DUTY ROSTER*/
    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT =
            "SELECT DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime" +               //[0]
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.appointmentDate = :date" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId" +
                    " AND a.status = 'PA'";


    /*%H - hour (e.g., 00,01,02,…12) IN 24 HOUR FORMAT
    * %h - hour (e.g., 00,01,02,…12) IN 12 HOUR FORMAT
     * %i - minutes (e.g., 00,01,02,…12)
     * %p - AM/PM
     * */

    private static final String QUERY_TO_FETCH_MIN_APPOINTMENT =
            " SELECT" +
                    " a.id as appointmentId," +                                             //[0]
                    " a.appointmentDate as appointmentDate," +                              //[1]
                    " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +      //[2]
                    " a.appointmentNumber as appointmentNumber," +                          //[3]
                    " p.name as patientName," +                                             //[4]
                    " d.name as doctorName," +                                              //[5]
                    " s.name as specializationName," +                                      //[6]
                    " h.name as hospitalName," +                                             //[7]
                    " atd.appointmentAmount as appointmentAmount" +                          //[8]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id";


    public static final String QUERY_TO_FETCH_PENDING_APPOINTMENTS =
            QUERY_TO_FETCH_MIN_APPOINTMENT +
                    " WHERE a.status = 'PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " ORDER BY a.appointmentDate DESC";


    public static final String QUERY_TO_FETCH_REFUND_AMOUNT =
            " SELECT" +
                    " (h.refundPercentage * atd.appointmentAmount)/100" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " WHERE a.id =:id";

    public static final String QUERY_TO_FETCH_APPOINTMENT_DETAILS_BY_ID =
            " SELECT" +
                    " a.appointmentDate as appointmentDate," +                              //[0]
                    " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +      //[1]
                    " a.appointmentNumber as appointmentNumber," +                          //[2]
                    " d.name as doctorName," +                                              //[3]
                    " s.name as specializationName," +                                      //[4]
                    " h.name as hospitalName," +                                            //[5]
                    " p.name as patientName," +                                             //[6]
                    " p.mobileNumber as mobileNumber," +                                    //[7]
                    " p.dateOfBirth as dateOfBirth," +                                       //[8]
                    " atd.appointmentAmount as appointmentAmount," +                        //[9]
                    " atd.taxAmount as taxAmount," +                                        //[10]
                    " atd.discountAmount as discountAmount," +                             //[11]
                    " atd.serviceChargeAmount as serviceChargeAmount" +                    //[12]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " WHERE a.id =:id";


    public static final String QUERY_TO_FETCH_APPOINTMENT_HISTORY =
            QUERY_TO_FETCH_MIN_APPOINTMENT +
                    " WHERE a.status = 'A'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " ORDER BY a.appointmentDate DESC";

    private static String SELECT_CLAUSE_TO_SEARCH_APPOINTMENT =
            " SELECT" +
                    " a.id as appointmentId," +                                             //[0]
                    " h.id as hospitalId," +                                                //[1]
                    " h.name as hospitalName," +                                            //[2]
                    " p.name as patientName," +                                             //[3]
                    " p.mobileNumber as mobileNumber," +                                    //[4]
                    " p.gender as gender," +                                                //[5]
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +                                  //[6]
                    " a.appointmentDate as appointmentDate," +                              //[7]
                    " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +      //[8]
                    " a.appointmentNumber as appointmentNumber," +                          //[9]
                    " atd.appointmentAmount as appointmentAmount," +                        //[10]
                    " d.id as doctorId," +                                                  //[11]
                    " d.name as doctorName," +                                              //[12]
                    " s.id as specializationId," +                                          //[13]
                    " s.name as specializationName," +                                      //[14]
                    " CASE WHEN " +
                    " a.status = 'PA' " +
                    " THEN 'BOOKED'" +
                    " WHEN" +
                    " a.status = 'A'" +
                    " THEN 'CHECKED-IN'" +
                    " WHEN" +
                    " a.status = 'C'" +
                    " THEN 'CANCELLED'" +
                    " WHEN" +
                    " a.status = 'C'" +
                    " THEN 'CANCELLED'" +
                    " WHEN" +
                    " a.status = 'RE'" +
                    " THEN 'REFUNDED'" +
                    " WHEN" +
                    " a.status = 'R'" +
                    " THEN 'REJECTED'" +
                    " END AS status," +                                                   //[15]
                    " hpi.registrationNumber AS registrationNumber" +                      //[16]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id";

    public static String QUERY_TO_FETCH_SEARCH_APPOINTMENT_FOR_SELF(AppointmentSearchDTO searchDTO) {

        String query = SELECT_CLAUSE_TO_SEARCH_APPOINTMENT +
                " WHERE a.isSelf = 'Y'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                " AND p.name =:name" +
                " AND p.mobileNumber = :mobileNumber" +
                " AND p.dateOfBirth =: dateOfBirth";

        if (!ObjectUtils.isEmpty(searchDTO.getStatus()) && !Objects.isNull(searchDTO.getStatus()))
            query += " AND a.status = '" + searchDTO.getStatus() + "'";

        if (!Objects.isNull(searchDTO.getHospitalId()))
            query += " AND h.id =" + searchDTO.getHospitalId();

        return query + " ORDER BY a.appointmentDate DESC";
    }

    public static String QUERY_TO_FETCH_SEARCH_APPOINTMENT_FOR_OTHERS(AppointmentSearchDTO searchDTO,
                                                                      String childPatientIds) {

        String query = SELECT_CLAUSE_TO_SEARCH_APPOINTMENT +
                " WHERE a.isSelf = 'N'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                " AND p.id IN (" + childPatientIds + ")";

        if (!ObjectUtils.isEmpty(searchDTO.getStatus()))
            query += " AND a.status = '" + searchDTO.getStatus() + "'";

        return query + " ORDER BY a.appointmentDate DESC";
    }



}
