package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.client.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE;
import static com.cogent.cogentappointment.client.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE;

/**
 * @author smriti on 2019-10-22
 */
public class AppointmentQuery {

    /*eSewa*/
    public static String QUERY_TO_VALIDATE_APPOINTMENT_EXISTS =
            "SELECT COUNT(a.id)" +
                    " FROM  Appointment a" +
                    " WHERE a.appointmentDate =:appointmentDate" +
                    " AND a.doctorId.id =:doctorId" +
                    " AND a.specializationId.id =:specializationId" +
                    " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime" +
                    " AND a.status='PA'";

    /*eSewa*/
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

    /*USED IN DOCTOR DUTY ROSTER*/
    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES =
            "SELECT" +
                    " a.appointmentDate as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    /*USED IN DOCTOR DUTY ROSTER*/
    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.appointmentDate) as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";


    /*%H - hour (e.g., 00,01,02,…12) IN 24 HOUR FORMAT
    * %h - hour (e.g., 00,01,02,…12) IN 12 HOUR FORMAT
     * %i - minutes (e.g., 00,01,02,…12)
     * %p - AM/PM
     * */
    /*eSewa*/
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

    /*eSewa*/
    public static final String QUERY_TO_FETCH_PENDING_APPOINTMENTS =
            QUERY_TO_FETCH_MIN_APPOINTMENT +
                    " WHERE a.status = 'PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " ORDER BY a.appointmentDate DESC";

    /*eSewa*/
    public static final String QUERY_TO_FETCH_REFUND_AMOUNT =
            " SELECT" +
                    " (h.refundPercentage * atd.appointmentAmount)/100" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " WHERE a.id =:id";

    /*eSewa*/
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

    /*eSewa*/
    public static final String QUERY_TO_FETCH_APPOINTMENT_HISTORY =
            QUERY_TO_FETCH_MIN_APPOINTMENT +
                    " WHERE a.status = 'A'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " ORDER BY a.appointmentDate DESC";

    /*admin*/
    public static Function<AppointmentRescheduleLogSearchDTO, String> QUERY_TO_RESCHEDULE_APPOINTMENT_LOGS =
            (appointmentRescheduleLogSearchDTO) ->
                    " SELECT" +
                            " p.eSewaId as eSewaId," +                                                   //[0]
                            " arl.previousAppointmentDate as previousAppointmentDate," +                 //[1]
                            " arl.rescheduleDate as rescheduleDate," +                                   //[2]
                            " a.appointmentNumber as appointmentNumber," +                               //[3]
                            " hpi.registrationNumber as registeredNumber," +                             //[4]
                            " p.name as patientName," +                                                  //[5]
                            " p.dateOfBirth as dateOfBirth," +                                           //[6]
                            " p.gender as gender," +                                                     //[7]
                            " p.mobileNumber as mobileNumber," +                                         //[8]
                            " sp.name as specializationName," +                                         //[9]
                            " d.name as doctorName," +                                                  //[10]
                            " atd.transactionNumber as transactionNumber," +                            //[11]
                            " atd.appointmentAmount as appointmentAmount," +                            //[12]
                            " arl.remarks as remarks" +                                                 //[13]
                            " from AppointmentRescheduleLog arl" +
                            " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                            " LEFT JOIN Patient p ON p.id=a.patientId" +
                            " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                            " LEFT JOIN Specialization sp ON sp.id=a.specializationId" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                            " LEFT JOIN Doctor d ON d.id=a.doctorId.id" +
                            GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(appointmentRescheduleLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(
            AppointmentRescheduleLogSearchDTO appointmentRescheduleLogSearchDTO) {

        String whereClause = " WHERE " +
                " hpi.status='Y' " +
                " AND arl.status='RES'" +
                " AND arl.rescheduleDate BETWEEN :fromDate AND :toDate" +
                " AND h.id =:hospitalId";

        if (!ObjectUtils.isEmpty(appointmentRescheduleLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentRescheduleLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getEsewaId()))
            whereClause += " AND p.eSewaId = '" + appointmentRescheduleLogSearchDTO.getEsewaId() + "'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + appointmentRescheduleLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id = " + appointmentRescheduleLogSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + appointmentRescheduleLogSearchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(appointmentRescheduleLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentRescheduleLogSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentRescheduleLogSearchDTO.getDoctorId();

        whereClause += " ORDER BY arl.rescheduleDate";

        return whereClause;
    }

    /*admin*/
    public static String QUERY_TO_FETCH_REFUND_APPOINTMENTS(AppointmentRefundSearchDTO searchDTO) {
        return " SELECT" +
                " a.id as appointmentId," +                                             //[0]
                " a.appointmentDate as appointmentDate," +                              //[1]
                " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +      //[2]
                " a.appointmentNumber as appointmentNumber," +                          //[3]
                " h.name as hospitalName," +                                            //[4]
                " p.name as patientName," +                                             //[5]
                " hpi.registrationNumber as registrationNumber," +                        //[6]
                " p.gender as gender," +                                                //[7]
                " d.name as doctorName," +                                              //[8]
                " s.name as specializationName," +                                      //[9]
                " p.eSewaId as eSewaId," +                                              //[10]
                " atd.transactionNumber as transactionNumber," +                        //[11]
                " ard.cancelledDate as cancelledDate," +                                //[12]
                " ard.refundAmount as refundAmount," +                                  //[13]
                " a.remarks as cancellationRemarks," +                                  //[14]
                " p.mobileNumber as mobileNumber," +                                    //[15]
                QUERY_TO_CALCULATE_PATIENT_AGE +                                        //[16]
                " FROM Appointment a" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(AppointmentRefundSearchDTO searchDTO) {
        String whereClause = " WHERE ard.status = 'PA'" +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate " +
                " AND h.id =:hospitalId";

        if (!ObjectUtils.isEmpty(searchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + searchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(searchDTO.getPatientMetaInfoId()))
            whereClause += " AND pm.id =" + searchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchDTO.getDoctorId()))
            whereClause += " AND d.id=" + searchDTO.getDoctorId();

        if (!Objects.isNull(searchDTO.getSpecializationId()))
            whereClause += " AND s.id=" + searchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(searchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered='" + searchDTO.getPatientType() + "'";

        return whereClause + " ORDER BY a.appointmentDate DESC";
    }

    /*admin*/
    public static String QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(AppointmentRefundSearchDTO searchDTO) {
        return " SELECT SUM(ard.refundAmount)" +
                " FROM Appointment a" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    /*admin*/
    public static Function<AppointmentPendingApprovalSearchDTO, String> QUERY_TO_FETCH_PENDING_APPROVALS =
            (searchRequestDTO) ->
                    "SELECT" +
                            " a.appointmentDate as appointmentDate," +                                   //[0]
                            " a.appointmentNumber as appointmentNumber," +                               //[1]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[2]
                            " p.eSewaId as esewaId," +                                                   //[3]
                            " hpi.registrationNumber as registrationNumber," +                           //[4]
                            " p.name as patientName," +                                                  //[5]
                            " p.gender as patientGender," +                                              //[6]
                            " p.dateOfBirth as patientDob," +                                            //[7]
                            " hpi.isRegistered as isRegistered," +                                       //[8]
                            " p.mobileNumber as mobileNumber," +                                        //[9]
                            " sp.name as specializationName," +                                         //[10]
                            " atd.transactionNumber as transactionNumber," +                            //[11]
                            " atd.appointmentAmount as appointmentAmount," +                            //[12]
                            " d.name as doctorName," +                                                  //[13]
                            " ard.refundAmount as refundAmount," +                                      //[14]
                            " a.id as appointmentId," +                                                 //[15]
                            " a.isSelf as isSelf" +                                                     //[16]
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON a.patientId=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                            " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(searchRequestDTO);


    private static String GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(
            AppointmentPendingApprovalSearchDTO pendingApprovalSearchDTO) {

        String whereClause = " WHERE " +
                " sp.status='Y' " +
                " AND a.status='PA'" +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                " AND h.id =:hospitalId";

        if (!Objects.isNull(pendingApprovalSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + pendingApprovalSearchDTO.getAppointmentId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + pendingApprovalSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + pendingApprovalSearchDTO.getSpecializationId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + pendingApprovalSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(pendingApprovalSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + pendingApprovalSearchDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }

    /*admin*/
    public static Function<AppointmentLogSearchDTO, String> QUERY_TO_FETCH_APPOINTMENT_LOGS =
            (appointmentLogSearchDTO) ->
                    "SELECT" +
                            " a.appointmentDate as appointmentDate," +                              //[0]
                            " a.appointmentNumber as appointmentNumber," +                          //[1]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +     //[2]
                            " p.eSewaId as esewaId," +                                              //[3]
                            " hpi.registrationNumber as registrationNumber," +                      //[4]
                            " p.name as patientName," +                                             //[5]
                            " p.gender as patientGender," +                                         //[6]
                            " p.dateOfBirth as patientDob," +                                       //[7]
                            " hpi.isRegistered as isRegistered," +                                  //[8]
                            " p.mobileNumber as mobileNumber," +                                   //[9]
                            " sp.name as specializationName," +                                    //[10]
                            " atd.transactionNumber as transactionNumber," +                       //[11]
                            " atd.appointmentAmount as appointmentAmount," +                       //[12]
                            " d.name as doctorName," +                                             //[13]
                            " a.status as status," +                                               //[14]
                            " ard.refundAmount as refundAmount," +                                 //[15]
                            " hpi.address as patientAddress" +                                     //[16]
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                            " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(appointmentLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(
            AppointmentLogSearchDTO appointmentLogSearchDTO) {

        String whereClause = " WHERE " +
                " sp.status='Y' " +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                " AND h.id=:hospitalId";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentLogSearchDTO.getStatus()) && !appointmentLogSearchDTO.getStatus().equals(""))
            whereClause += " AND a.status = '" + appointmentLogSearchDTO.getStatus() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + appointmentLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + appointmentLogSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(appointmentLogSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + appointmentLogSearchDTO.getSpecializationId();

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentLogSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentLogSearchDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }

    /*admin*/
    public static String QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(AppointmentStatusRequestDTO requestDTO) {

        String SQL = " SELECT" +
                " a.appointment_date as date," +                                                        //[0]
                " GROUP_CONCAT(DATE_FORMAT(a.appointment_time, '%H:%i'), '-', a.status)" +
                " as startTimeDetails," +                                                               //[1]
                " d.id as doctorId," +                                                                  //[2]
                " s.id as specializationId," +                                                          //[3]
                " a.appointment_number as appointmentNumber," +                                         //[4]
                " p.name as patientName," +                                                             //[5]
                " p.gender as gender," +                                                                //[6]
                " p.mobile_number as mobileNumber," +                                                   //[7]
                QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE +                                                 //[8]
                " FROM appointment a" +
                " LEFT JOIN doctor d ON d.id = a.doctor_id" +
                " LEFT JOIN specialization s ON s.id = a.specialization_id" +
                " LEFT JOIN hospital h ON h.id = a.hospital_id" +
                " LEFT JOIN patient p ON p.id = a.patient_id" +
                " WHERE" +
                " a.appointment_date BETWEEN :fromDate AND :toDate" +
                " AND a.status IN ('PA', 'A', 'C')" +
                " AND h.id =:hospitalId";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            SQL += " AND d.id =:doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            SQL += " AND s.id = :specializationId ";

        if ((!ObjectUtils.isEmpty(requestDTO.getStatus())) && (!(requestDTO.getStatus().equals(VACANT))))
            SQL += " AND a.status='" + requestDTO.getStatus() + "'";

        SQL += " GROUP BY a.appointment_date, a.doctor_id, a.specialization_id, a.id" +
                " ORDER BY appointment_date";

        return SQL;
    }


}
