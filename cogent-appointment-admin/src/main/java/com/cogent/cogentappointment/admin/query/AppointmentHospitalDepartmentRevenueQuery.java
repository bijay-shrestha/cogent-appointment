package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author smriti on 19/06/20
 */
public class AppointmentHospitalDepartmentRevenueQuery {

    private static String SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN HospitalAppointmentServiceType has ON has.id=a.hospitalAppointmentServiceType.id " +
                    " WHERE" +
                    " has.appointmentServiceType.code = :appointmentServiceTypeCode" +
                    " AND hd.status!='D'";

    private static String GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(String query,
                                                                                AppointmentLogSearchDTO searchRequestDTO) {

        if (!ObjectUtils.isEmpty(searchRequestDTO.getFromDate())
                && !ObjectUtils.isEmpty(searchRequestDTO.getToDate()))
            query += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(searchRequestDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(searchRequestDTO.getToDate()) + "')";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentNumber()))
            query += " AND a.appointmentNumber LIKE '%" + searchRequestDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(searchRequestDTO.getStatus()) && !searchRequestDTO.getStatus().equals(""))
            query += " AND a.status = '" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            query += " AND h.id = " + searchRequestDTO.getHospitalId();

        if (!Objects.isNull(searchRequestDTO.getPatientMetaInfoId()))
            query += " AND pi.id = " + searchRequestDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchRequestDTO.getHospitalDepartmentId()))
            query += " AND hd.id = " + searchRequestDTO.getHospitalDepartmentId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientType()))
            query += " AND hpi.isRegistered = '" + searchRequestDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentCategory()))
            query += " AND a.isSelf = '" + searchRequestDTO.getAppointmentCategory() + "'";

        return query;
    }

    public static String QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'N'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP
            (AppointmentLogSearchDTO searchRequestDTO) {

        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'Y'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A' AND a.isFollowUp = 'N'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_WITH_FOLLOW_UP(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A' AND a.isFollowUp = 'Y'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C' AND a.isFollowUp = 'N'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C' AND a.isFollowUp = 'Y'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE (SUM(ard.refundAmount ),0) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " LEFT JOIN HospitalAppointmentServiceType has ON has.id=a.hospitalAppointmentServiceType.id " +
                    " WHERE" +
                    " a.status='RE'" +
                    " AND has.appointmentServiceType.code = :appointmentServiceTypeCode" +
                    " AND hd.status!='D'";

    public static String QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_FROM_HOSPITAL_DEPARTMENT_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " LEFT JOIN HospitalAppointmentServiceType has ON has.id=a.hospitalAppointmentServiceType.id " +
                    " WHERE" +
                    " a.status='RE'" +
                    " AND has.appointmentServiceType.code = :appointmentServiceTypeCode" +
                    " AND hd.status!='D'";

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_FROM_HOSPITAL_DEPARTMENT_APPOINTMENT(
            AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_FROM_HOSPITAL_DEPARTMENT_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_FROM_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_FROM_HOSPITAL_DEPARTMENT_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT =
            "SELECT" +
                    " COALESCE (SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType has ON has.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id = h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND has.appointmentServiceType.code = :appointmentServiceTypeCode";

    public static String QUERY_TO_FETCH_TOTAL_HOSPITAL_DEPARTMENT_APPOINTMENT_AMOUNT(
            AppointmentLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT;

        return GET_WHERE_CLAUSE_TO_FETCH_APPOINTMENT_REVENUE_DETAILS(query, searchRequestDTO);
    }

}
