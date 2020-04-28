package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.admin.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE;
import static com.cogent.cogentappointment.admin.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

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

    public static String QUERY_TO_FETCH_REFUND_APPOINTMENTS(AppointmentRefundSearchDTO searchDTO) {
        return " SELECT" +
                "  a.id as appointmentId," +
                "  a.appointmentDate as appointmentDate," +
                "  a.appointmentNumber as appointmentNumber," +
                "  DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +
                "  p.name as patientName," +
                "  p.eSewaId as eSewaId," +
                "  p.mobileNumber as mobileNumber," +
                "  CASE WHEN" +
                "  (hpi.registrationNumber IS NULL)" +
                "  THEN 'N/A'" +
                "  ELSE" +
                "  hpi.registrationNumber" +
                "  END as registrationNumber," +
                "  d.name as doctorName," +
                "  s.name as specializationName," +
                "  atd.transactionNumber as transactionNumber," +
                "  DATE_FORMAT(ard.cancelledDate,'%M %d %Y') as cancelledDate," +
                "  p.gender as gender," +
                " ard.refundAmount as refundAmount," +
                " a.appointmentModeId.name as appointmentMode, " +
                QUERY_TO_CALCULATE_PATIENT_AGE +
                " FROM Appointment a" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id AND pm.status='Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(AppointmentRefundSearchDTO searchDTO) {
        String whereClause = " WHERE ard.status = 'PA'";

        if (!ObjectUtils.isEmpty(searchDTO.getFromDate()) && !ObjectUtils.isEmpty(searchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(searchDTO.getFromDate())
                    + "' AND '"
                    + utilDateToSqlDate(searchDTO.getToDate()) + "' )";

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
            whereClause += " AND hpi.isRegistered='" + searchDTO.getPatientType() + "'";

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
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
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
                QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE + "," +                                           //[8]
                " a.id as appointmentId" +                                                              //[9]
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

        SQL += " GROUP BY a.appointment_date, a.doctor_id, a.specialization_id, a.id" +
                " ORDER BY appointment_date";

        return SQL;
    }

    public static Function<AppointmentPendingApprovalSearchDTO, String> QUERY_TO_FETCH_PENDING_APPROVALS =
            (searchRequestDTO) ->
                    "SELECT" +
                            " a.id as appointmentId," +                                                    //[0]
                            " a.appointmentDate as appointmentDate," +                                   //[1]
                            " a.appointmentNumber as appointmentNumber," +                               //[2]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[3]
                            " CASE WHEN" +
                            " (hpi.registrationNumber IS NULL)" +
                            " THEN 'N/A'" +
                            " ELSE" +
                            " hpi.registrationNumber" +
                            " END as registrationNumber," +                                               //[4]
                            " p.name as patientName," +                                                  //[5]
                            " p.mobileNumber as mobileNumber," +                                        //[6]
                            " sp.name as specializationName," +                                         //[7]
                            " d.name as doctorName," +
                            " a.appointmentModeId.name as appointmentMode," +
                            " atd.appointmentAmount as appointmentAmount" +                                                  //[8]
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON a.patientId=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                            " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(
            AppointmentPendingApprovalSearchDTO pendingApprovalSearchDTO) {

        String whereClause = " WHERE " +
                " sp.status='Y' " +
                " AND a.status='PA'";

        if (!ObjectUtils.isEmpty(pendingApprovalSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(pendingApprovalSearchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '"
                    + utilDateToSqlDate(pendingApprovalSearchDTO.getFromDate()) + "' AND '"
                    + utilDateToSqlDate(pendingApprovalSearchDTO.getToDate()) + "')";

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

        if (!Objects.isNull(pendingApprovalSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + pendingApprovalSearchDTO.getDoctorId();

        if (!ObjectUtils.isEmpty(pendingApprovalSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + pendingApprovalSearchDTO.getAppointmentNumber() + "%'";

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }

    public static Function<AppointmentLogSearchDTO, String> QUERY_TO_FETCH_APPOINTMENT_LOGS =
            (appointmentLogSearchDTO) ->
                    "SELECT" +
                            " h.name as hospitalName," +                                    //[0]
                            " a.appointmentDate as appointmentDate," +                      //[1]
                            " a.appointmentNumber as appointmentNumber," +                  //[2]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[3]
                            " p.eSewaId as esewaId," +                                      //[4]
                            " hpi.registrationNumber as registrationNumber," +                //[5]
                            " p.name as patientName," +                                     //[6]
                            " p.gender as patientGender," +                                 //[7]
                            " p.dateOfBirth as patientDob," +                               //[8]
                            " hpi.isRegistered as isRegistered," +                          //[9]
                            " p.mobileNumber as mobileNumber," +                            //[10]
                            " sp.name as specializationName," +                             //[11]
                            " atd.transactionNumber as transactionNumber," +                //[12]
                            " atd.appointmentAmount as appointmentAmount," +                //[13]
                            " d.name as doctorName," +                                     //[14]
                            " a.status as status," +                                       //[15]
                            " ard.refundAmount as refundAmount," +                         //[16]
                            " hpi.address as patientAddress," +                            //[17]
                            " atd.transactionDate as transactionDate," +                    //[18]
                            " am.name as appointmentMode," +                                //[19]
                            " a.isFollowUp as isFollowUp" +                                //[20]
                            " FROM Appointment a" +
                            " LEFT JOIN AppointmentMode am On am.id=a.appointmentModeId.id" +
                            " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                            " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(appointmentLogSearchDTO);


    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(
            AppointmentLogSearchDTO appointmentLogSearchDTO) {

        String whereClause = " WHERE " +
                " sp.status='Y' ";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(appointmentLogSearchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(appointmentLogSearchDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(appointmentLogSearchDTO.getToDate()) + "')";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentLogSearchDTO.getStatus()) && !appointmentLogSearchDTO.getStatus().equals(""))
            whereClause += " AND a.status = '" + appointmentLogSearchDTO.getStatus() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + appointmentLogSearchDTO.getHospitalId();

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + appointmentLogSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(appointmentLogSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + appointmentLogSearchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentLogSearchDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getAppointmentCategory()))
            whereClause += " AND a.isSelf = '" + appointmentLogSearchDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentLogSearchDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentDate DESC ";

        return whereClause;
    }


    public static Function<AppointmentRescheduleLogSearchDTO, String> QUERY_TO_RESCHEDULE_APPOINTMENT_LOGS =
            (appointmentRescheduleLogSearchDTO) ->
                    " SELECT" +
                            " h.name as hospitalName," +                                                 //[0]
                            " p.eSewaId as eSewaId," +                                                   //[1]
                            " arl.previousAppointmentDate as previousAppointmentDate," +                 //[2]
                            " arl.rescheduleDate as rescheduleDate," +                                   //[3]
                            " a.appointmentNumber as appointmentNumber," +                               //[4]
                            " hpi.registrationNumber as registeredNumber," +                             //[5]
                            " p.name as patientName," +                                                  //[6]
                            " p.dateOfBirth as dateOfBirth," +                                           //[7]
                            " p.gender as gender," +                                                     //[8]
                            " p.mobileNumber as mobileNumber," +                                         //[9]
                            " sp.name as specializationName," +                                         //[10]
                            " d.name as doctorName," +                                                  //[11]
                            " atd.transactionNumber as transactionNumber," +                            //[12]
                            " atd.appointmentAmount as appointmentAmount," +                            //[13]
                            " arl.remarks as remarks" +                                                 //[14]
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

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(AppointmentRescheduleLogSearchDTO appointmentRescheduleLogSearchDTO) {

        String whereClause = " WHERE " +
                " hpi.status='Y' " +
                " AND arl.status='RES' " +
                " AND arl.rescheduleDate BETWEEN :fromDate AND :toDate";

        if (!ObjectUtils.isEmpty(appointmentRescheduleLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentRescheduleLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getEsewaId()))
            whereClause += " AND p.eSewaId = '" + appointmentRescheduleLogSearchDTO.getEsewaId() + "'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + appointmentRescheduleLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + appointmentRescheduleLogSearchDTO.getHospitalId();

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

    public static Function<AppointmentQueueRequestDTO, String> QUERY_TO_FETCH_APPOINTMENT_QUEUE =
            (appointmentQueueSearchDTO) ->
                    "SELECT" +
                            " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +
                            " d.name as doctorName," +
                            " p.name as patientName," +
                            " p.mobileNumber as patientMobileNumber," +
                            " s.name as specializationName," +
                            " CASE WHEN" +
                            " (dv.status is null OR dv.status = 'N')" +
                            " THEN null" +
                            " ELSE" +
                            " dv.fileUri" +
                            " END as doctorAvatar" +
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                            " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                            " LEFT JOIN DoctorSpecialization ds ON ds.doctorId.id = d.id" +
                            " LEFT JOIN DoctorAvatar dv ON dv.doctorId.id = d.id" +
                            " LEFT JOIN Specialization s ON s.id = a.specializationId.id AND s.status='Y' " +
                            " LEFT JOIN Hospital h ON h.id = a.hospitalId.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(appointmentQueueSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(AppointmentQueueRequestDTO appointmentQueueRequestDTO) {

        String whereClause = " WHERE" +
                " a.status='PA'" +
                " AND DATE(a.appointmentDate) = :date";

        if (!Objects.isNull(appointmentQueueRequestDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentQueueRequestDTO.getDoctorId();

        if (!Objects.isNull(appointmentQueueRequestDTO.getHospitalId()))
            whereClause += " AND h.id = " + appointmentQueueRequestDTO.getHospitalId();

        whereClause += " ORDER BY a.appointmentTime ASC";

        return whereClause;
    }

    public static String QUERY_TO_FETCH_PENDING_APPROVAL_DETAIL_BY_ID =
            "SELECT" +
                    " a.id as appointmentId," +                                                  //[0]
                    " a.appointmentDate as appointmentDate," +                                   //[1]
                    " a.appointmentNumber as appointmentNumber," +                               //[2]
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[3]
                    " p.eSewaId as esewaId," +                                                   //[4]
                    " CASE WHEN" +
                    " (hpi.registrationNumber IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " hpi.registrationNumber" +
                    " END as registrationNumber," +                                              //[5]
                    " p.name as patientName," +                                                  //[6]
                    " p.gender as patientGender," +                                              //[7]
                    " p.dateOfBirth as patientDob," +                                            //[8]
                    " hpi.isRegistered as isRegistered," +                                       //[9]
                    " p.mobileNumber as mobileNumber," +                                        //[10]
                    " sp.name as specializationName," +                                         //[11]
                    " atd.transactionNumber as transactionNumber," +                            //[12]
                    " COALESCE(atd.appointmentAmount,0) as appointmentAmount," +                //[13]
                    " d.name as doctorName," +                                                  //[14]
                    " a.isSelf as isSelf," +                                                      //[15]
                    " h.name as hospitalName," +
                    " a.appointmentModeId.name as appointmentMode" +                                                   //[16]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE " +
                    " sp.status='Y' " +
                    " AND a.status='PA'" +
                    " AND a.id=:appointmentId";


    public static String QUERY_TO_REFUNDED_DETAIL_BY_ID =
            "SELECT" +
                    " a.appointmentDate as appointmentDate," +
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +
                    " a.appointmentNumber as appointmentNumber," +
                    " h.name as hospitalName," +
                    " p.name as patientName," +
                    " CASE WHEN" +
                    " (hpi.registrationNumber IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " hpi.registrationNumber" +
                    " END as registrationNumber," +
                    " p.gender as gender," +
                    " p.eSewaId  as eSewaId," +
                    " p.mobileNumber as mobileNumber," +
                    " d.name as doctorName," +
                    " s.name as specializationName," +
                    " atd.transactionNumber as transactionNumber," +
                    " ard.refundAmount as refundAmount," +
                    " atd.appointmentAmount as appointmentCharge," +
                    " DATE_FORMAT(ard.cancelledDate,'%Y-%m-%d') as cancelledDate," +
                    " a.appointmentModeId.name as appointmentMode," +
                    QUERY_TO_CALCULATE_PATIENT_AGE +
                    " FROM" +
                    " AppointmentRefundDetail ard" +
                    " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id" +
                    " LEFT JOIN Hospital h ON h.id=a.hospitalId.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id=a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id=a.specializationId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id =a.id" +
                    " WHERE ard.appointmentId.id=:appointmentId" +
                    " AND ard.status='PA'";

    private static String SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT =
            "SELECT" +
                    " COALESCE (SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " sp.status='Y'";

    private static String SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE";

    public static String QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT;

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT_EXCLUDING_BOOKED(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT +
                " AND a.status!='PA'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }


    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='PA'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='A'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='C'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = "SELECT" +
                " COUNT(a.id)," +
                " COALESCE (SUM(ard.refundAmount ),0) as amount" +
                " FROM Appointment a" +
                " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                " WHERE" +
                " a.status='RE'";


        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = "SELECT" +
                " COUNT(a.id)," +
                " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as amount" +
                " FROM Appointment a" +
                " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                " WHERE" +
                " a.status='RE'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String QUERY_TO_SEARCH_BY_DATES(String query, AppointmentLogSearchDTO searchRequestDTO) {

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            query += " AND h.id = " + searchRequestDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getFromDate())
                && !ObjectUtils.isEmpty(searchRequestDTO.getToDate()))
            query += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(searchRequestDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(searchRequestDTO.getToDate()) + "')";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentNumber()))
            query += " AND a.appointmentNumber LIKE '%" + searchRequestDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(searchRequestDTO.getStatus()) && !searchRequestDTO.getStatus().equals(""))
            query += " AND a.status = '" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getPatientMetaInfoId()))
            query += " AND pi.id = " + searchRequestDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchRequestDTO.getSpecializationId()))
            query += " AND sp.id = " + searchRequestDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientType()))
            query += " AND hpi.isRegistered = '" + searchRequestDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentCategory()))
            query += " AND a.isSelf = '" + searchRequestDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(searchRequestDTO.getDoctorId()))
            query += " AND d.id = " + searchRequestDTO.getDoctorId();

        query += " ORDER BY a.appointmentDate DESC ";

        return query;
    }

    public static String QUERY_TO_FETCH_FOLLOW_UP_DETAILS(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.isFollowUp='Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }
}
