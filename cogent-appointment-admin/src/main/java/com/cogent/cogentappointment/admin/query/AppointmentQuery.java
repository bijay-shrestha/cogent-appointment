package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;

/**
 * @author smriti on 2019-10-22
 */
public class AppointmentQuery {

    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES =
            "SELECT" +
                    " a.appointmentDate as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.appointmentDate) as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    /* AGE CALCULATION:
        TIMESTAMPDIFF(YEAR, date_of_birth , CURDATE() ) as _year
        TIMESTAMPDIFF(MONTH, date_of_birth, CURDATE() ) % 12 as _month
        FLOOR( TIMESTAMPDIFF( DAY, date_of_birth ,  CURDATE()) % 30.4375 ) as _day
    * */
    public static String QUERY_TO_CALCULATE_PATIENT_AGE =
            " CASE" +
                    " WHEN" +
                    " (((TIMESTAMPDIFF(YEAR, p.dateOfBirth, CURDATE()))<=0) AND" +
                    " ((TIMESTAMPDIFF(MONTH, p.dateOfBirth, CURDATE()) % 12)<=0))" +
                    " THEN" +
                    " CONCAT((FLOOR(TIMESTAMPDIFF(DAY, p.dateOfBirth, CURDATE()) % 30.4375)), ' days')" +
                    " WHEN" +
                    " ((TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))<=0)" +
                    " THEN" +
                    " CONCAT(((TIMESTAMPDIFF(MONTH, p.dateOfBirth, CURDATE()) % 12)), ' months')" +
                    " ELSE" +
                    " CONCAT(((TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))), ' years')" +
                    " END AS age";

    public static String QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE =
            " CASE" +
                    " WHEN" +
                    " (((TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()))<=0) AND" +
                    " ((TIMESTAMPDIFF(MONTH, p.date_of_birth, CURDATE()) % 12)<=0))" +
                    " THEN" +
                    " CONCAT((FLOOR(TIMESTAMPDIFF(DAY, p.date_of_birth, CURDATE()) % 30.4375)), ' days')" +
                    " WHEN" +
                    " ((TIMESTAMPDIFF(YEAR, p.date_of_birth ,CURDATE()))<=0)" +
                    " THEN" +
                    " CONCAT(((TIMESTAMPDIFF(MONTH, p.date_of_birth, CURDATE()) % 12)), ' months')" +
                    " ELSE" +
                    " CONCAT(((TIMESTAMPDIFF(YEAR, p.date_of_birth ,CURDATE()))), ' years')" +
                    " END AS age";

    public static String QUERY_TO_FETCH_REFUND_APPOINTMENTS(AppointmentRefundSearchDTO searchDTO) {
        return " SELECT" +
                " a.id as appointmentId," +                                             //[0]
                " a.appointmentDate as appointmentDate," +                              //[1]
                " DATE_FORMAT(a.appointmentTime,'%H:%i %p') as appointmentTime," +      //[2]
                " a.appointmentNumber as appointmentNumber," +                          //[3]
                " h.name as hospitalName," +                                            //[4]
                " p.name as patientName," +                                             //[5]
                " hp.registrationNumber as registrationNumber," +                        //[6]
                " p.gender as gender," +                                                //[7]
                " d.name as doctorName," +                                              //[8]
                " s.name as specializationName," +                                      //[9]
                " p.eSewaId as eSewaId," +                                              //[10]
                " atd.transactionNumber as transactionNumber," +                        //[11]
                " ard.cancelledDate as cancelledDate," +                                //[12]
                " ard.refundAmount as refundAmount," +                                   //[13]
                " a.remarks as cancellationRemarks," +                                   //[14]
                QUERY_TO_CALCULATE_PATIENT_AGE +
                " FROM Appointment a" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id" +
                " LEFT JOIN HospitalPatientInfo hp ON hp.patientId = p.id" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(AppointmentRefundSearchDTO searchDTO) {
        String whereClause = " WHERE ard.status = 'PA'" +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate ";

        if (!ObjectUtils.isEmpty(searchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + searchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(searchDTO.getPatientMetaInfoId()))
            whereClause += " AND pm.id =" + searchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchDTO.getDoctorId()))
            whereClause += " AND d.id=" + searchDTO.getDoctorId();

        if (!Objects.isNull(searchDTO.getSpecializationId()))
            whereClause += " AND s.id=" + searchDTO.getSpecializationId();

        if (!Objects.isNull(searchDTO.getHospitalId()))
            whereClause += " AND h.id=" + searchDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchDTO.getPatientType()))
            whereClause += " AND hp.isRegistered='" + searchDTO.getPatientType() + "'";

        return whereClause + " ORDER BY a.appointmentDate DESC";
    }

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
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

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
                " AND a.status IN ('PA', 'A', 'C')";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            SQL += " AND d.id =:doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            SQL += " AND s.id = :specializationId ";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id =:hospitalId";

        if ((!ObjectUtils.isEmpty(requestDTO.getStatus())) && (!(requestDTO.getStatus().equals(VACANT))))
            SQL += " AND a.status='" + requestDTO.getStatus() + "'";

        SQL += " GROUP BY a.appointment_date, a.doctor_id, a.specialization_id, a.id";

        return SQL;
    }

    public static Function<AppointmentPendingApprovalSearchDTO, String> QUERY_TO_FETCH_PENDING_APPROVALS =
            (searchRequestDTO) ->
                    "SELECT" +
                            " h.name as hospitalName," +                                    //[0]
                            " a.appointmentDate as appointmentDate," +                      //[1]
                            " a.appointmentNumber as appointmentNumber," +                  //[2]
                            " DATE_FORMAT(a.appointmentTime, '%H:%i %p') as appointmentTime," +          //[3]
                            " p.eSewaId as esewaId," +                                      //[4]
                            " hpi.registrationNumber as registrationNumber," +                //[5]
                            " p.name as patientName," +                                     //[6]
                            " p.gender as patientGender," +                                 //[7]
                            " p.dateOfBirth as patientDob," +                               //[8]
                            " hpi.isRegistered as isRegistered," +                            //[9]
                            " hpi.isSelf as isSelf," +                                        //[10]
                            " p.mobileNumber as mobileNumber," +                            //[11]
                            " sp.name as specializationName," +                             //[12]
                            " atd.transactionNumber as transactionNumber," +                //[13]
                            " atd.appointmentAmount as appointmentAmount," +                //[14]
                            " d.name as doctorName," +                                       //[15]
                            " ard.refundAmount as refundAmount," +                           //[16]
                            " a.id as appointmentId"+                                        //[17]
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON a.patientId=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patientId =p.id" +
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
                " hpi.status='Y' " +
                " AND sp.status='Y' " +
                " AND a.status='PA'" +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate";

        if (!Objects.isNull(pendingApprovalSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + pendingApprovalSearchDTO.getAppointmentId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + pendingApprovalSearchDTO.getHospitalId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + pendingApprovalSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + pendingApprovalSearchDTO.getSpecializationId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + pendingApprovalSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientCategory()))
            whereClause += " AND hpi.isSelf = '" + pendingApprovalSearchDTO.getPatientCategory() + "'";

        if (!Objects.isNull(pendingApprovalSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + pendingApprovalSearchDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }

    public static Function<AppointmentLogSearchDTO, String> QUERY_TO_FETCH_APPOINTMENT_LOGS =
            (appointmentLogSearchDTO) ->
                    "SELECT" +
                            " h.name as hospitalName," +                                    //[0]
                            " a.appointmentDate as appointmentDate," +                      //[1]
                            " a.appointmentNumber as appointmentNumber," +                  //[2]
                            " DATE_FORMAT(a.appointmentTime, '%H:%i %p') as appointmentTime," +          //[3]
                            " p.eSewaId as esewaId," +                                      //[4]
                            " hpi.registrationNumber as registrationNumber," +                //[5]
                            " p.name as patientName," +                                     //[6]
                            " p.gender as patientGender," +                                 //[7]
                            " p.dateOfBirth as patientDob," +                               //[8]
                            " hpi.isRegistered as isRegistered," +                            //[9]
                            " hpi.isSelf as isSelf," +                                        //[10]
                            " p.mobileNumber as mobileNumber," +                            //[11]
                            " sp.name as specializationName," +                             //[12]
                            " atd.transactionNumber as transactionNumber," +                //[13]
                            " atd.appointmentAmount as appointmentAmount," +                //[14]
                            " d.name as doctorName," +                                       //[15]
                            " a.status as status," +                                        //[16]
                            " ard.refundAmount as refundAmount" +                           //[17]
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON a.patientId=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patientId =p.id" +
                            " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                            " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(appointmentLogSearchDTO);


    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(
            AppointmentLogSearchDTO appointmentLogSearchDTO) {

        String whereClause = " WHERE " +
                " hpi.status='Y' " +
                " AND sp.status='Y' " +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate";

        if (!Objects.isNull(appointmentLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentLogSearchDTO.getStatus()))
            whereClause += " AND a.status = '" + appointmentLogSearchDTO.getStatus() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + appointmentLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(appointmentLogSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + appointmentLogSearchDTO.getHospitalId();

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + appointmentLogSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(appointmentLogSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + appointmentLogSearchDTO.getSpecializationId();

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentLogSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientCategory()))
            whereClause += " AND hpi.isSelf = '" + appointmentLogSearchDTO.getPatientCategory() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentLogSearchDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }
}
