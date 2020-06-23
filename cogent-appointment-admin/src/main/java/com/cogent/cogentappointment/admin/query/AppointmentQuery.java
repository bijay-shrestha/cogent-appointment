package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.DepartmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.HospitalDepartmentAppointmentRescheduleLogSearchDTO;
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

    public static String QUERY_TO_FETCH_APPOINTMENT_CANCEL_APPROVALS(AppointmentCancelApprovalSearchDTO searchDTO) {
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
                " CASE WHEN" +
                " (d.salutation is null)" +
                " THEN d.name" +
                " ELSE" +
                " CONCAT_WS(' ',d.salutation, d.name)" +
                " END as doctorName," +
                " s.name as specializationName," +
                " atd.transactionNumber as transactionNumber," +
                " DATE_FORMAT(ard.cancelledDate,'%M %d, %Y ') as cancelledDate," +
                " p.gender as gender," +
                " ard.refundAmount as refundAmount," +
                " a.appointmentModeId.name as appointmentMode," +
                " hpi.isRegistered as isRegistered," +
                QUERY_TO_CALCULATE_PATIENT_AGE + "," +
                " da.fileUri as fileUri" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id AND pm.status='Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(AppointmentCancelApprovalSearchDTO searchDTO) {
        String whereClause = " WHERE ard.status = 'PA'" +
                " AND s.status!='D'" +
                " AND d.status!='D'";

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


    public static final String QUERY_TO_FETCH_DEPARTMENT_APPOINTMENT_CANCEL_APPROVALS(DepartmentCancelApprovalSearchDTO searchDTO) {

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
                "  hd.name as departmentName," +
                " atd.transactionNumber as transactionNumber," +
                " DATE_FORMAT(ard.cancelledDate,'%M %d, %Y ') as cancelledDate," +
                " p.gender as gender," +
                " ard.refundAmount as refundAmount," +
                " a.appointmentModeId.name as appointmentMode," +
                " hpi.isRegistered as isRegistered," +
                " r.roomNumber  as roomNumber,"+
                QUERY_TO_CALCULATE_PATIENT_AGE +
                " FROM Appointment a" +
                " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                " LEFT JOIN HospitalDepartmentRoomInfo hdri ON hdri.hospitalDepartment.id = ahd.hospitalDepartment.id" +
                " LEFT JOIN Room r ON r.id = hdri.room.id" +
                " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = p.id AND pm.status='Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                GET_WHERE_CLAUSE_TO_FETCH_DEPARTMENT_REFUND_APPOINTMENTS(searchDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_FETCH_DEPARTMENT_REFUND_APPOINTMENTS(DepartmentCancelApprovalSearchDTO searchDTO) {
        String whereClause = " WHERE ard.status = 'PA'" +
                " AND hd.status!='D'" +
                " AND apst.status!='D'" +
                " AND hdri.status!='D'" +
                " AND r.status!='D'";

        if (!ObjectUtils.isEmpty(searchDTO.getFromDate()) && !ObjectUtils.isEmpty(searchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(searchDTO.getFromDate())
                    + "' AND '"
                    + utilDateToSqlDate(searchDTO.getToDate()) + "' )";

        if (!ObjectUtils.isEmpty(searchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + searchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(searchDTO.getPatientMetaInfoId()))
            whereClause += " AND pm.id =" + searchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchDTO.getHospitalDepartmentId()))
            whereClause += " AND hd.id=" + searchDTO.getHospitalDepartmentId();

        if (!Objects.isNull(searchDTO.getRoomId()))
            whereClause += " AND r.id=" + searchDTO.getRoomId();

        if (!Objects.isNull(searchDTO.getHospitalId()))
            whereClause += " AND h.id=" + searchDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered='" + searchDTO.getPatientType() + "'";

        return whereClause + " ORDER BY a.appointmentDate DESC";
    }


    public static String QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(AppointmentCancelApprovalSearchDTO searchDTO) {
        return " SELECT SUM(ard.refundAmount)" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
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
                " a.id as appointmentId," +                                                             //[9]
                " a.is_follow_up as isFollowUp," +                                                      //[10]
                " a.has_transferred as hastransferred" +                                                //[11]
                " FROM appointment a" +
                " INNER JOIN appointment_doctor_info ad ON a.id = ad.appointment_id" +
                " INNER JOIN doctor d ON d.id = ad.doctor_id" +
                " INNER JOIN specialization s ON s.id = ad.specialization_id" +
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

        if (!ObjectUtils.isEmpty(requestDTO.getAppointmentNumber()))
            SQL += " AND a.appointment_number='" + requestDTO.getAppointmentNumber() + "'";

        SQL += " GROUP BY a.appointment_date, d.id, s.id, a.id" +
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
                            " d.name as doctorName," +                                                  //[8]
                            " a.appointmentModeId.name as appointmentMode," +
                            " atd.appointmentAmount as appointmentAmount," +
                            " da.fileUri as fileUri," +
                            " hpi.hospitalNumber as hospitalNumber," +
                            " p.id as patientId," +
                            " p.gender as gender," +
                            " hpi.address as address," +
                            " hpi.isRegistered as isRegistered," +
                            QUERY_TO_CALCULATE_PATIENT_AGE +
                            " FROM Appointment a" +
                            " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                            " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                            " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN HospitalAppointmentServiceType has ON has.id = a.hospitalAppointmentServiceType.id" +
                            GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(
            AppointmentPendingApprovalSearchDTO pendingApprovalSearchDTO) {

        String whereClause = " WHERE " +
                " has.appointmentServiceType.code =:appointmentServiceTypeCode" +
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
                            " CASE WHEN" +
                            " (d.salutation is null)" +
                            " THEN d.name" +
                            " ELSE" +
                            " CONCAT_WS(' ',d.salutation, d.name)" +
                            " END as doctorName," +                                          //[14]
                            " a.status as status," +                                       //[15]
                            " ard.refundAmount as refundAmount," +                         //[16]
                            " hpi.address as patientAddress," +                            //[17]
                            " atd.transactionDate as transactionDate," +                    //[18]
                            " am.name as appointmentMode," +                                //[19]
                            " a.isFollowUp as isFollowUp," +                                //[20]
                            " (atd.appointmentAmount - COALESCE(ard.refundAmount,0)) as revenueAmount," + //[21]
                            " da.fileUri as fileUri" +
                            " FROM Appointment a" +
                            " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN AppointmentMode am On am.id = a.appointmentModeId.id" +
                            " LEFT JOIN Patient p ON a.patientId.id = p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                            " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                            " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                            " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                            " LEFT JOIN PatientMetaInfo pi ON p.id= pi.patient.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(appointmentLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_LOG_DETAILS(
            AppointmentLogSearchDTO appointmentLogSearchDTO) {

        String whereClause = " WHERE " +
                " sp.status!='D'" +
                " AND d.status!='D'";

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

    public static Function<AppointmentRescheduleLogSearchDTO, String> QUERY_TO_FETCH_RESCHEDULE_APPOINTMENT_LOGS =
            (appointmentRescheduleLogSearchDTO) ->
                    " SELECT" +
                            " h.name as hospitalName," +                                                 //[0]
                            " p.eSewaId as esewaId," +                                                   //[1]
                            " arl.previousAppointmentDate as previousAppointmentDate," +                 //[2]
                            " DATE_FORMAT(arl.previousAppointmentDate, '%h:%i %p') as previousAppointmentTime," +   //[3]
                            " arl.rescheduleDate as rescheduleAppointmentDate," +                                   //[4]
                            " DATE_FORMAT(arl.rescheduleDate, '%h:%i %p') as rescheduleAppointmentTime," +          //[5]
                            " a.appointmentNumber as appointmentNumber," +                               //[6]
                            " hpi.registrationNumber as registrationNumber," +                           //[7]
                            " p.name as patientName," +                                                  //[8]
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
                            " END AS patientAge," +                                                     //[9]
                            " p.gender as patientGender," +                                             //[10]
                            " p.mobileNumber as mobileNumber," +                                         //[11]
                            " sp.name as specializationName," +                                         //[12]
                            " CASE WHEN" +
                            " (d.salutation is null)" +
                            " THEN d.name" +
                            " ELSE" +
                            " CONCAT_WS(' ',d.salutation, d.name)" +
                            " END as doctorName," +                                                     //[13]
                            " atd.transactionNumber as transactionNumber," +                            //[14]
                            " atd.appointmentAmount as appointmentAmount," +                            //[15]
                            " arl.remarks as remarks," +                                                 //[16]
                            " a.isFollowUp as isFollowUp," +                                             //[17]
                            " da.fileUri as fileUri" +                                                   //[18]
                            " FROM AppointmentRescheduleLog arl" +
                            " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                            " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN Patient p ON p.id=a.patientId" +
                            " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                            " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                            " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                            " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                            GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(appointmentRescheduleLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(AppointmentRescheduleLogSearchDTO appointmentRescheduleLogSearchDTO) {

        String whereClause = " WHERE " +
                " hpi.status='Y' " +
                " AND arl.status='RES'" +
                " AND sp.status!='D'" +
                " AND d.status!='D'" +
                " AND (arl.rescheduleDate BETWEEN :fromDate AND :toDate)";

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

    public static String QUERY_TO_CALCULATE_TOTAL_RESCHEDULE_AMOUNT(AppointmentRescheduleLogSearchDTO searchDTO) {
        return
                "SELECT" +
                        " COALESCE(SUM(atd.appointmentAmount),0)" +
                        " FROM AppointmentRescheduleLog arl" +
                        " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                        " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                        " LEFT JOIN Patient p ON p.id=a.patientId" +
                        " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                        " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                        " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                        " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                        " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                        " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                        GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(searchDTO);
    }

    public static Function<AppointmentQueueRequestDTO, String> QUERY_TO_FETCH_DOCTOR_APPOINTMENT_QUEUE =
            (appointmentQueueSearchDTO) ->
                    "SELECT" +
                            " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +
                            " CASE WHEN" +
                            " (d.salutation is null)" +
                            " THEN d.name" +
                            " ELSE" +
                            " CONCAT_WS(' ',d.salutation, d.name)" +
                            " END as doctorName," +
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
                            " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                            " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                            " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                            " LEFT JOIN DoctorSpecialization ds ON ds.doctorId.id = d.id" +
                            " LEFT JOIN DoctorAvatar dv ON dv.doctorId.id = d.id" +
                            " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                            " LEFT JOIN Hospital h ON h.id = a.hospitalId.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(appointmentQueueSearchDTO);

    public static Function<AppointmentQueueRequestDTO, String> QUERY_TO_FETCH_DEPARTMENT_APPOINTMENT_QUEUE =
            (appointmentQueueSearchDTO) ->
                    "SELECT" +
                            " DATE_FORMAT(a.appointmentTime,'%h:%i %p') as appointmentTime," +
                            " a.patientId.name as patientName," +
                            " a.patientId.mobileNumber as patientMobileNumber," +
                            " ad.hospitalDepartment.name as hospitalDepartmentName" +
                            " FROM Appointment a" +
                            " LEFT JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                            " LEFT JOIN Hospital h ON h.id = a.hospitalId.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(appointmentQueueSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(AppointmentQueueRequestDTO appointmentQueueRequestDTO) {

        String whereClause = " WHERE" +
                " a.status='PA'" +
                " AND DATE(a.appointmentDate) = :date" +
                " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode";

        if (!Objects.isNull(appointmentQueueRequestDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentQueueRequestDTO.getDoctorId();

        if (!Objects.isNull(appointmentQueueRequestDTO.getHospitalDepartmentId()))
            whereClause += " AND ad.hospitalDepartment.id = " + appointmentQueueRequestDTO.getHospitalDepartmentId();

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
                    " a.isSelf as isSelf," +                                                    //[15]
                    " h.name as hospitalName," +                                                //[16]
                    " a.appointmentModeId.name as appointmentMode," +                           //[17]
                    " da.fileUri as fileUri," +                                                 //[18]
                    " d.id as doctorId," +                                                      //[19]
                    " sp.id as specializationId," +                                             //[20]
                    " a.isFollowUp as followUp," +                                              //[21]
                    " h.id as hospitalId" +                                                   //[22]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE " +
                    " sp.status='Y' " +
                    " AND a.status='PA'" +
                    " AND a.id=:appointmentId";

    public static String QUERY_TO_REFUNDED_DETAIL_BY_ID =
            "SELECT" +
                    " a.id as appointmentId," +
                    " a.appointmentModeId.id as appointmentModeId," +
                    " a.appointmentDate as appointmentDate," +
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +
                    " a.appointmentNumber as appointmentNumber," +
                    " h.name as hospitalName," +
                    " h.id as hospitalId," +
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
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +
                    " END as doctorName," +
                    " s.name as specializationName," +
                    " atd.transactionNumber as transactionNumber," +
                    " ard.refundAmount as refundAmount," +
                    " atd.appointmentAmount as appointmentCharge," +
                    " DATE_FORMAT(ard.cancelledDate,'%M %d, %Y at %h:%i %p') as cancelledDate," +
                    " a.appointmentModeId.name as appointmentMode," +
                    " h.esewaMerchantCode as esewaMerchantCode," +
                    " hpi.isRegistered as isRegistered," +
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +
                    " dv.fileUri as fileUri" +
                    " FROM" +
                    " AppointmentRefundDetail ard" +
                    " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Hospital h ON h.id=a.hospitalId.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN DoctorAvatar dv ON dv.doctorId.id = d.id" +
                    " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id =a.id" +
                    " WHERE ard.appointmentId.id=:appointmentId" +
                    " AND ard.status='PA'";

    private static String SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT =
            "SELECT" +
                    " COALESCE (SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " sp.status!='D'" +
                    " AND d.status!='D'";

    private static String SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE" +
                    " sp.status!='D'" +
                    " AND d.status!='D'";

    public static String QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT;

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP
            (AppointmentLogSearchDTO searchRequestDTO) {

        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A'" +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A'" +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C'" +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C'" +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE (SUM(ard.refundAmount ),0) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " sp.status!='D'" +
                    " AND d.status!='D'" +
                    " AND a.status='RE'";

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = ad.doctor.id" +
                    " LEFT JOIN Specialization sp ON sp.id = ad.specialization.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " sp.status!='D'" +
                    " AND d.status!='D'" +
                    " AND a.status='RE'";

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

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

    public static String QUERY_TO_GET_CANCELLED_APPOINTMENT =
            "SELECT" +
                    " a" +
                    " FROM" +
                    " Appointment a " +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id " +
                    " WHERE atd.transactionNumber=:transactionNumber" +
                    " AND a.patientId.eSewaId =:esewaId";

    public static Function<HospitalDepartmentAppointmentLogSearchDTO, String> QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOGS =
            (appointmentLogSearchDTO) ->
                    "SELECT" +
                            " h.name as hospitalName," +                                                         //[0]
                            " a.appointmentDate as appointmentDate," +                                           //[1]
                            " a.appointmentNumber as appointmentNumber," +                                       //[2]
                            " DATE_FORMAT(a.appointmentTime , '%h:%i %p') as appointmentTime," +                 //[3]
                            " p.eSewaId as esewaId," +                                                           //[4]
                            " CASE WHEN hpi.registrationNumber IS NULL " +
                            " THEN 'N/A'" +
                            " ELSE hpi.registrationNumber END as registrationNumber," +                          //[5]
                            " p.name as patientName," +                                                          //[6]
                            " p.gender as patientGender," +                                                      //[7]
                            " hpi.isRegistered as isRegistered," +                                              //[8]
                            " p.mobileNumber as mobileNumber," +                                                //[9]
                            " atd.transactionNumber as transactionNumber," +                                    //[10]
                            " COALESCE(atd.appointmentAmount,0) as appointmentAmount," +                        //[11]
                            " hd.name as hospitalDepartmentName," +                                             //[12]
                            " a.status as status, " +                                                           //[13]
                            " COALESCE(ard.refundAmount,0) as refundAmount," +                                  //[14]
                            " hpi.address as patientAddress," +                                                   //[15]
                            " atd.transactionDate as transactionDate," +                                         //[16]
                            " a.appointmentModeId.name as appointmentMode," +                                     //[17]
                            " a.isFollowUp as isFollowUp," +                                                       //[18]
                            " atd.appointmentAmount - COALESCE(ard.refundAmount ,0) as revenueAmount," +           //[19]
                            QUERY_TO_CALCULATE_PATIENT_AGE +                                                        //[20]
                            " FROM Appointment a" +
                            " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                            " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                            " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                            " LEFT JOIN HospitalBillingModeInfo hbm ON hbm.id = ahd.hospitalDepartmentBillingModeInfo.id" +
                            " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId" +
                            " WHERE apst.appointmentServiceType.id = :appointmentServiceTypeId "
                            + GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(appointmentLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(
            HospitalDepartmentAppointmentLogSearchDTO appointmentLogSearchDTO) {

        String whereClause = " AND hd.status!='D'";

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
            whereClause += " AND pmi.id = " + appointmentLogSearchDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentLogSearchDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getAppointmentCategory()))
            whereClause += " AND a.isSelf = '" + appointmentLogSearchDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getHospitaDepartmentId()))
            whereClause += " AND hd.id = " + appointmentLogSearchDTO.getHospitaDepartmentId();

        whereClause += " ORDER BY a.appointmentDate DESC ";

        return whereClause;
    }

    private static String SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND apst.appointmentServiceType.id = :appointmentServiceTypeId ";

    private static String SELECT_CLAUSE_TO_FETCH_HOSPITAL_DEPARTMENT_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE (SUM(ard.refundAmount ),0) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND a.status='RE'" +
                    " AND apst.appointmentServiceType.id = :appointmentServiceTypeId ";

    private static String SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND a.status='RE'" +
                    " AND apst.appointmentServiceType.id = :appointmentServiceTypeId ";

    private static String SELECT_CLAUSE_TO_GET_TOTAL_HOSPITAL_DEPARTMENT_APPOINTMENT_AMOUNT =
            "SELECT" +
                    " COALESCE (SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND apst.appointmentServiceType.id = :appointmentServiceTypeId ";

    public static String QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT(HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP
            (HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {

        String query = SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A'" +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A'" +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C'" +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C'" +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_HOSPITAL_DEPARTMENT_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_HOSPITAL_DEPARTMENT_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }

    public static String QUERY_TO_FETCH_TOTAL_HOSPITAL_APPOINTMENT_APPOINTMENT_AMOUNT(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_TOTAL_HOSPITAL_DEPARTMENT_APPOINTMENT_AMOUNT +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(searchRequestDTO);

        return query;
    }


    public static Function<HospitalDepartmentAppointmentRescheduleLogSearchDTO, String>
            QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_RESCHEDULE_APPOINTMENT_LOGS =
            (rescheduleLogSearchDTO) ->
                    " SELECT" +
                            " h.name as hospitalName," +                                                 //[0]
                            " p.eSewaId as esewaId," +                                                   //[1]
                            " arl.previousAppointmentDate as previousAppointmentDate," +                 //[2]
                            " DATE_FORMAT(arl.previousAppointmentDate, '%h:%i %p') as previousAppointmentTime," +   //[3]
                            " arl.rescheduleDate as rescheduleAppointmentDate," +                                   //[4]
                            " DATE_FORMAT(arl.rescheduleDate, '%h:%i %p') as rescheduleAppointmentTime," +          //[5]
                            " a.appointmentNumber as appointmentNumber," +                               //[6]
                            " hpi.registrationNumber as registrationNumber," +                           //[7]
                            " p.name as patientName," +                                                  //[8]
                            QUERY_TO_CALCULATE_PATIENT_AGE +                                              // [9]
                            " p.gender as patientGender," +                                             //[10]
                            " p.mobileNumber as mobileNumber," +                                         //[11]                                                  //[13]
                            " atd.transactionNumber as transactionNumber," +                            //[12]
                            " atd.appointmentAmount as appointmentAmount," +                            //[13]
                            " arl.remarks as remarks," +                                                 //[14]
                            " a.isFollowUp as isFollowUp," +                                                //[16]
                            " hd.name as hospitalDepartmentName" +
                            " FROM AppointmentRescheduleLog arl" +
                            " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                            " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                            " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                            " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                            " LEFT JOIN Patient p ON p.id=a.patientId" +
                            " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                            GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_RESCHEDULE_LOG_DETAILS(rescheduleLogSearchDTO);

    public static String QUERY_TO_CALCULATE_TOTAL_HOSPITAL_DEPARTMENT_APPT_RESCHEDULE_AMOUNT(
            HospitalDepartmentAppointmentRescheduleLogSearchDTO searchDTO) {
        return
                "SELECT" +
                        " COALESCE(SUM(atd.appointmentAmount),0)" +
                        " FROM AppointmentRescheduleLog arl" +
                        " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                        " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                        " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                        " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                        " LEFT JOIN Patient p ON p.id=a.patientId" +
                        " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                        " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                        " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                        " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                        GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_RESCHEDULE_LOG_DETAILS(searchDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_RESCHEDULE_LOG_DETAILS(
            HospitalDepartmentAppointmentRescheduleLogSearchDTO rescheduleLogSearchDTO) {

        String whereClause = " WHERE " +
                " hpi.status='Y' " +
                " AND arl.status='RES'" +
                " AND hd.status!='D'" +
                " AND apst.appointmentServiceType.id = :appointmentServiceTypeId ";

        if (!ObjectUtils.isEmpty(rescheduleLogSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(rescheduleLogSearchDTO.getToDate()))
            whereClause += " AND (arl.rescheduleDate BETWEEN '" + utilDateToSqlDate(rescheduleLogSearchDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(rescheduleLogSearchDTO.getToDate()) + "')";

        if (!ObjectUtils.isEmpty(rescheduleLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + rescheduleLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(rescheduleLogSearchDTO.getEsewaId()))
            whereClause += " AND p.eSewaId = '" + rescheduleLogSearchDTO.getEsewaId() + "'";

        if (!Objects.isNull(rescheduleLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + rescheduleLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(rescheduleLogSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + rescheduleLogSearchDTO.getHospitalId();

        if (!Objects.isNull(rescheduleLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id = " + rescheduleLogSearchDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(rescheduleLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + rescheduleLogSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(rescheduleLogSearchDTO.getHospitalDepartmentId()))
            whereClause += " AND hd.id = " + rescheduleLogSearchDTO.getHospitalDepartmentId();

        whereClause += " ORDER BY arl.rescheduleDate";

        return whereClause;
    }

    public static String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_BY_APPT_NUMBER =
            "SELECT" +
                    " DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime," +
                    " a.status as status," +
                    " a.appointmentDate as appointmentDate," +
                    " a.appointmentNumber as appointmentNumber," +
                    " a.patientId.mobileNumber as mobileNumber," +
                    " a.patientId.gender as gender," +
                    " a.patientId.name as patientName," +
                    " a.id as appointmentId," +
                    " a.isFollowUp as isFollowUp," +
                    " a.hasTransferred as hasTransferred," +
                    " hd.id as hospitalDepartmentId, " +
                    " hd.name as hospitalDepartmentName, " +
                    " hdri.id  as hospitalDepartmentRoomInfoId," +
                    " r.roomNumber as roomNumber," +
                    QUERY_TO_CALCULATE_PATIENT_AGE +
                    " FROM" +
                    " Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id=a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id=ahd.hospitalDepartment.id" +
                    " LEFT JOIN HospitalDepartmentRoomInfo hdri On hdri.id= ahd.hospitalDepartmentRoomInfo.id" +
                    " LEFT JOIN Room r on hdri.room.id=r.id" +
                    " WHERE " +
                    " a.appointmentNumber=:appointmentNumber" +
                    " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode";


    public static String QUERY_TO_FETCH_APPOINTMENT_BY_APPT_NUMBER =
            "SELECT" +
                    " DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime," +
                    " a.status as status," +
                    " a.appointmentDate as appointmentDate," +
                    " a.appointmentNumber as appointmentNumber," +
                    " a.patientId.mobileNumber as mobileNumber," +
                    " a.patientId.gender as gender," +
                    " a.patientId.name as patientName," +
                    " a.id as appointmentId," +
                    " a.isFollowUp as isFollowUp," +
                    " a.hasTransferred as hasTransferred," +
                    " adi.doctor.id as doctorId, " +
                    " adi.doctor.name as doctorName, " +
                    " adi.specialization.id as specializationId, " +
                    " adi.specialization.name as specializationName," +
//                    " ds.salutationId.name as doctorSalutation," +
                    " a.hospitalId.id as hospitalId," +
                    QUERY_TO_CALCULATE_PATIENT_AGE +
                    " FROM" +
                    " Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN AppointmentDoctorInfo adi ON adi.appointment.id=a.id" +
                    " LEFT JOIN DoctorSalutation ds ON ds.doctorId.id = adi.doctor.id" +
                    " WHERE " +
                    " a.appointmentNumber=:appointmentNumber" +
                    " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode";


}
