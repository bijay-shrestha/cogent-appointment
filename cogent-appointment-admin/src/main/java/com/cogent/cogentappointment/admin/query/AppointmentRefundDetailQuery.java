package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class AppointmentRefundDetailQuery {

    public static String QUERY_TO_FETCH_REFUND_APPOINTMENTS(RefundStatusSearchRequestDTO searchDTO) {
        return "SELECT" +
                " a.id  as appointmentId," +
                " a.appointmentDate as appointmentDate," +
                " a.appointmentNumber as appointmentNumber," +
                " DATE_FORMAT(a.appointmentTime , '%h:%i %p') as appointmentTime," +
                " CASE WHEN (hpi.registrationNumber IS NULL) THEN 'N/A'" +
                " ELSE hpi.registrationNumber END as registrationNumber," +
                " atd.transactionNumber as transactionNumber," +
                " DATE_FORMAT(ard.cancelledDate ,'%M %d, %Y') as cancelledDate," +
                " DATE_FORMAT(ard.cancelledDate ,'%h:%i %p') as cancelledTime," +
                " ard.refundAmount as refundAmount," +
                " hpi.isRegistered as isRegistered," +
                " CASE WHEN (da.fileUri IS NULL) THEN 'N/A'" +
                " ELSE da.fileUri END as fileUri," +
                " ard.status," +
                " a.patientId.name as patientName," +
                " a.patientId.gender as gender," +
                " a.patientId.mobileNumber as mobileNumber," +
                " CASE WHEN" +
                " (d.salutation is null)" +
                " THEN d.name" +
                " ELSE" +
                " CONCAT_WS(' ',d.salutation, d.name)" +
                " END as doctorName," +
                " adi.specialization.name as specializationName," +
                " a.patientId.eSewaId as eSewaId," +
                " a.appointmentModeId.name as appointmentMode," +
                " a.appointmentModeId.id as appointmentModeId," +
                " ard.status as refundStatus," +
                " ard.remarks as remarks," +
                " a.hospitalId.name as hospitalName," +
                QUERY_TO_CALCULATE_PATIENT_AGE +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id = ard.appointmentId.id" +
                " INNER JOIN AppointmentDoctorInfo adi ON adi.appointment.id=a.id"+
                " LEFT JOIN Doctor d ON d.id = adi.doctor.id" +
                " LEFT JOIN DoctorAvatar da ON da.doctorId.id = adi.doctor.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = a.patientId.id AND pm.status = 'Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = a.patientId.id AND hpi.hospital.id = a.hospitalId.id" +
                " WHERE" +
                " a.status IN ('C','RE')" +
                " AND ard.status IN ('PA','A','R')"+
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }


    private static String GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(RefundStatusSearchRequestDTO searchDTO) {
        String whereClause = " ";

        if (!ObjectUtils.isEmpty(searchDTO.getFromDate()) && !ObjectUtils.isEmpty(searchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '"
                    + utilDateToSqlDate(searchDTO.getFromDate()) + "' AND '"
                    + utilDateToSqlDate(searchDTO.getToDate()) + "' )";

        if (!ObjectUtils.isEmpty(searchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + searchDTO.getAppointmentNumber() + "%'";

        if (!ObjectUtils.isEmpty(searchDTO.getStatus()))
            whereClause += " AND ard.status = '" + searchDTO.getStatus() + "'";

        if (!ObjectUtils.isEmpty(searchDTO.getHospitalId()))
            whereClause += " AND a.hospitalId.id =" + searchDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchDTO.getAppointmentModeId()))
            whereClause += " AND a.appointmentModeId.id =" + searchDTO.getAppointmentModeId();

        if (!Objects.isNull(searchDTO.getPatientMetaInfoId()))
            whereClause += " AND pm.id =" + searchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchDTO.getDoctorId()))
            whereClause += " AND adi.doctor.id=" + searchDTO.getDoctorId();

        if (!Objects.isNull(searchDTO.getHospitalDepartmentId()))
            whereClause += " AND ahdi.hospitalDepartment.id=" + searchDTO.getHospitalDepartmentId();

        if (!Objects.isNull(searchDTO.getSpecializationId()))
            whereClause += " AND adi.specialization.id=" + searchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(searchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered='" + searchDTO.getPatientType() + "'";

        return whereClause + " ORDER BY a.appointmentDate DESC";
    }
    public static String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_REFUND_APPOINTMENTS(RefundStatusSearchRequestDTO searchDTO) {
        return "SELECT" +
                " a.id  as appointmentId," +
                " a.appointmentDate as appointmentDate," +
                " a.appointmentNumber as appointmentNumber," +
                " DATE_FORMAT(a.appointmentTime , '%h:%i %p') as appointmentTime," +
                " CASE WHEN (hpi.registrationNumber IS NULL) THEN 'N/A'" +
                " ELSE hpi.registrationNumber END as registrationNumber," +
                " atd.transactionNumber as transactionNumber," +
                " DATE_FORMAT(ard.cancelledDate ,'%M %d, %Y') as cancelledDate," +
                " DATE_FORMAT(ard.cancelledDate ,'%h:%i %p') as cancelledTime," +
                " ard.refundAmount as refundAmount," +
                " hpi.isRegistered as isRegistered," +
                " ard.status," +
                " a.patientId.name as patientName," +
                " a.patientId.gender as gender," +
                " a.patientId.mobileNumber as mobileNumber," +
                " a.patientId.eSewaId as eSewaId," +
                " a.appointmentModeId.name as appointmentMode," +
                "  a.appointmentModeId.id as appointmentModeId," +
                " ard.status as refundStatus," +
                " ard.remarks as remarks," +
                " ahdi.hospitalDepartment.name as hospitalDepartmentName," +
                QUERY_TO_CALCULATE_PATIENT_AGE +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id = ard.appointmentId.id" +
                " INNER JOIN AppointmentHospitalDepartmentInfo ahdi ON adi.appointment.id=a.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = a.patientId.id AND pm.status = 'Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = a.patientId.id AND hpi.hospital.id = a.hospitalId.id" +
                " WHERE" +
                " a.status IN ('C','RE')" +
                " AND ard.status IN ('PA','A','R')" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    }

    public static Function<RefundStatusSearchRequestDTO,String> QUERY_TO_GET_TOTAL_REFUND_AMOUNT= searchDTO -> {
        return  "SELECT" +
                " COALESCE(SUM(ard.refundAmount),0) as totalRefundAmount" +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id = ard.appointmentId.id" +
                " INNER JOIN AppointmentDoctorInfo adi ON adi.appointment.id=a.id" +
                " LEFT JOIN Doctor d ON d.id = adi.doctor.id" +
                " LEFT JOIN DoctorAvatar da ON da.doctorId.id = adi.doctor.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = a.patientId.id AND pm.status = 'Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = a.patientId.id AND hpi.hospital.id = a.hospitalId.id" +
                " WHERE" +
                " a.status IN ('C','RE')" +
                " AND ard.status IN ('PA','A','R')" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    };

    public static Function<RefundStatusSearchRequestDTO, String> QUERY_TO_GET_TOTAL_HOSPITAL_DEPARTMENT__REFUND_AMOUNT = searchDTO -> {
        return "SELECT" +
                " COALESCE(SUM(ard.refundAmount),0) as totalRefundAmount" +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id = ard.appointmentId.id" +
                " INNER JOIN AppointmentHospitalDepartmentInfo ahdi ON adi.appointment.id=a.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = a.patientId.id AND pm.status = 'Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = a.patientId.id AND hpi.hospital.id = a.hospitalId.id" +
                " WHERE" +
                " a.status IN ('C','RE')" +
                " AND ard.status IN ('PA','A','R')" +
                GET_WHERE_CLAUSE_TO_FETCH_REFUND_APPOINTMENTS(searchDTO);
    };

    public static final String QUERY_TO_CALCULATE_PATIENT_AGE =
            " CASE" +
                    " WHEN" +
                    " (((TIMESTAMPDIFF(YEAR, a.patientId.dateOfBirth, CURDATE()))<=0) AND" +
                    " ((TIMESTAMPDIFF(MONTH, a.patientId.dateOfBirth, CURDATE()) % 12)<=0))" +
                    " THEN" +
                    " CONCAT((FLOOR(TIMESTAMPDIFF(DAY, a.patientId.dateOfBirth, CURDATE()) % 30.4375)), ' days')" +
                    " WHEN" +
                    " ((TIMESTAMPDIFF(YEAR, a.patientId.dateOfBirth ,CURDATE()))<=0)" +
                    " THEN" +
                    " CONCAT(((TIMESTAMPDIFF(MONTH, a.patientId.dateOfBirth, CURDATE()) % 12)), ' months')" +
                    " ELSE" +
                    " CONCAT(((TIMESTAMPDIFF(YEAR, a.patientId.dateOfBirth ,CURDATE()))), ' years')" +
                    " END AS age";

    public static String QUERY_TO_GET_APPOINTMENT_REFUND_DETAILS =
            "SELECT" +
                    " ard" +
                    " FROM" +
                    " AppointmentRefundDetail ard" +
                    " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id " +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id " +
                    " WHERE atd.transactionNumber=:transactionNumber" +
                    " AND a.patientId.eSewaId =:esewaId";

    public static String QUERY_TO_REFUNDED_DETAIL_BY_ID =
            "SELECT" +
                    " a.id as appointmentId,"+
                    " a.appointmentModeId.id as appointmentModeId,"+
                    " a.appointmentDate as appointmentDate," +
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +
                    " a.appointmentNumber as appointmentNumber," +
                    " h.name as hospitalName," +
                    " a.patientId.name as patientName," +
                    " CASE WHEN" +
                    " (hpi.registrationNumber IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " hpi.registrationNumber" +
                    " END as registrationNumber," +
                    " a.patientId.gender as gender," +
                    " CASE WHEN" +
                    " (a.patientId.eSewaId IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " a.patientId.eSewaId" +
                    " END as eSewaId," +
                    " a.patientId.mobileNumber as mobileNumber," +
                    " CASE WHEN" +
                    " (d.salutation is null)" +
                    " THEN d.name" +
                    " ELSE" +
                    " CONCAT_WS(' ',d.salutation, d.name)" +
                    " END as doctorName," +
                    " adi.specialization.name as specializationName," +
                    " atd.transactionNumber as transactionNumber," +
                    " DATE_FORMAT(ard.cancelledDate,'%M %d, %Y at %h:%i %p') as cancelledDate," +
                    " ard.refundAmount as refundAmount," +
                    " atd.appointmentAmount as appointmentCharge," +
                    " a.appointmentModeId.name as appointmentMode," +
                    " hpi.isRegistered as isRegistered," +
                    " a.hospitalId.name as hospitalName," +
                    " a.hospitalId.id as hospitalId," +
                    " ard.remarks as remarks," +
                    QUERY_TO_CALCULATE_PATIENT_AGE + "," +
                    " CASE WHEN" +
                    " (dv.status IS NULL" +
                    " OR dv.status = 'N')" +
                    " THEN NULL" +
                    " ELSE" +
                    " dv.fileUri" +
                    " END as fileUri" +
                    " FROM" +
                    " AppointmentRefundDetail ard" +
                    " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id" +
                    " LEFT JOIN Hospital h ON h.id=a.hospitalId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =a.patientId.id AND hpi.hospital.id = a.hospitalId.id" +
                    " INNER JOIN AppointmentDoctorInfo adi ON adi.appointment.id=a.id"+
                    " LEFT JOIN Doctor d ON d.id = adi.doctor.id" +
                    " LEFT JOIN DoctorAvatar dv ON dv.doctorId.id = adi.doctor.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id =a.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON atd.appointment.id =a.id" +
                    " WHERE a.id=:appointmentId" +
                    " AND ard.status IN ('PA','A','R')" +
                    " GROUP BY a.id";

}
