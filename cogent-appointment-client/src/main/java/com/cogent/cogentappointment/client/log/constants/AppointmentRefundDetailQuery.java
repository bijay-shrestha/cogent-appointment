package com.cogent.cogentappointment.client.log.constants;

import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.cogent.cogentappointment.client.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class AppointmentRefundDetailQuery {

    public static String QUERY_TO_FETCH_REFUND_APPOINTMENTS(RefundStatusSearchRequestDTO searchDTO) {
        return "SELECT" +
                " ard.id as id," +
                " a.id  as appointmentId," +
                " a.appointmentDate as appointmentDate," +
                " a.appointmentNumber as appointmentNumber," +
                " DATE_FORMAT(a.appointmentTime , '%h:%i %p') as appointmentTime," +
                " CASE WHEN (hpi.registrationNumber IS NULL) THEN 'N/A'" +
                " ELSE hpi.registrationNumber END as registrationNumber," +
                " atd.transactionNumber as transactionNumber," +
                " DATE_FORMAT(ard.cancelledDate ,'%M %d, %Y') as cancelledDate," +
                " ard.refundAmount as refundAmount," +
                " hpi.isRegistered as isRegistered," +
                " CASE WHEN (da.fileUri IS NULL) THEN 'N/A'" +
                " ELSE da.fileUri END as fileUri," +
                " ard.status," +
                " a.patientId.name as patientName," +
                " a.patientId.gender as gender," +
                " a.patientId.mobileNumber as mobileNumber," +
                " a.doctorId.name as doctorName," +
                " a.specializationId.name as specializationName," +
                " a.patientId.eSewaId as eSewaId," +
                " a.appointmentModeId.name as appointmentMode," +
                " a.hospitalId.esewaMerchantCode as eSewaMerchantCode," +
                QUERY_TO_CALCULATE_PATIENT_AGE +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id = ard.appointmentId.id" +
                " LEFT JOIN DoctorAvatar da ON da.doctorId.id = a.doctorId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN PatientMetaInfo pm ON pm.patient.id = a.patientId.id AND pm.status = 'Y'" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id = a.patientId.id AND hpi.hospital.id = a.hospitalId.id" +
                " WHERE" +
                " a.status='C' " +
                " AND ard.status='PA'" +
                " AND a.hospitalId.id=:hospitalId" +
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

        if (!Objects.isNull(searchDTO.getPatientMetaInfoId()))
            whereClause += " AND pm.id =" + searchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchDTO.getDoctorId()))
            whereClause += " AND a.doctorId.id=" + searchDTO.getDoctorId();

        if (!Objects.isNull(searchDTO.getSpecializationId()))
            whereClause += " AND a.specializationId.id=" + searchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(searchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered='" + searchDTO.getPatientType() + "'";

        return whereClause + " ORDER BY a.appointmentDate DESC";
    }

    public static String QUERY_TO_GET_TOTAL_REFUND_AMOUNT =
            "SELECT" +
                    " COALESCE(SUM(ard.refundAmount),0) as totalRefundAmount" +
                    " FROM" +
                    " AppointmentRefundDetail ard" +
                    " LEFT JOIN Appointment a ON a.id = ard.appointmentId.id" +
                    " WHERE" +
                    " a.status='C' " +
                    " AND ard.status='PA'" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " ORDER BY a.id";

}
