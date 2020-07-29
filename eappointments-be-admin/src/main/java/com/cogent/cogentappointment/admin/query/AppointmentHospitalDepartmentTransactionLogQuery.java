package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author smriti on 21/06/20
 */
public class AppointmentHospitalDepartmentTransactionLogQuery {

    public static Function<TransactionLogSearchDTO, String> QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOGS =
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
                            " CASE WHEN" +
                            " a.status = 'RE'" +
                            " THEN " +
                            " (COALESCE(ard.refundAmount,0))" +
                            " ELSE" +
                            " 0" +
                            " END AS refundAmount," +                                                           //[14]
                            " hpi.address as patientAddress," +                                                 //[15]
                            " atd.transactionDate as transactionDate," +                                         //[16]
                            " a.appointmentModeId.name as appointmentMode," +                                    //[17]
                            " a.isFollowUp as isFollowUp," +                                                     //[18]
                            " CASE WHEN" +
                            " a.status!= 'RE'" +
                            " THEN" +
                            " atd.appointmentAmount" +
                            " ELSE" +
                            " (atd.appointmentAmount - COALESCE(ard.refundAmount ,0)) " +
                            " END AS revenueAmount," +                                                           //[19]
                            QUERY_TO_CALCULATE_PATIENT_AGE + "," +                                               //[20]
                            " hb.billingMode.name as billingModeName," +                                        //[21]
                            " case when hr.id is null then null" +
                            " when hr.id is not null then r.roomNumber" +
                            " end as roomNumber," +                                                             //[22]
                            " p.dateOfBirth as patientDob," +                                                  //[23]
                            " DATE_FORMAT(atd.transactionDate, '%h:%i %p') as transactionTime" +              //[24]
                            " FROM Appointment a" +
                            " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                            " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                            " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                            " LEFT JOIN HospitalBillingModeInfo hb ON hb.id = ahd.hospitalDepartmentBillingModeInfo.id" +
                            " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                            " LEFT JOIN Room r ON r.id = hr.room.id" +
                            " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId" +
                            " WHERE apst.appointmentServiceType.code = :appointmentServiceTypeCode"
                            + GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(appointmentLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(
            TransactionLogSearchDTO searchRequestDTO) {

        String whereClause = " AND hd.status!='D'";

        String fromDate = utilDateToSqlDate(searchRequestDTO.getFromDate()) + " 00:00:00";
        String toDate = utilDateToSqlDate(searchRequestDTO.getToDate()) + " 23:59:59";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getFromDate())
                && !ObjectUtils.isEmpty(searchRequestDTO.getToDate()))
            whereClause += " AND atd.transactionDate BETWEEN '" + fromDate + "' AND '" + toDate + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getTransactionNumber()))
            whereClause += " AND atd.transactionNumber LIKE '%" + searchRequestDTO.getTransactionNumber() + "%'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + searchRequestDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(searchRequestDTO.getStatus()) && !searchRequestDTO.getStatus().equals(""))
            whereClause += " AND a.status = '" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            whereClause += " AND h.id = " + searchRequestDTO.getHospitalId();

        if (!Objects.isNull(searchRequestDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + searchRequestDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + searchRequestDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentCategory()))
            whereClause += " AND a.isSelf = '" + searchRequestDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(searchRequestDTO.getHospitalDepartmentId()))
            whereClause += " AND hd.id = " + searchRequestDTO.getHospitalDepartmentId();

        if (!Objects.isNull(searchRequestDTO.getHospitalDepartmentRoomInfoId()))
            whereClause += " AND hr.id = " + searchRequestDTO.getHospitalDepartmentRoomInfoId();

        whereClause += " ORDER BY a.appointmentDate DESC ";

        return whereClause;
    }

    public static String QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP
            (TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='PA'" +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A'" +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='A'" +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C'" +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT +
                " AND a.status='C'" +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_FETCH_HOSPITAL_DEPARTMENT_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);

    }

    public static String QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_FETCH_HOSPITAL_DEPARTMENT_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT +
                " AND a.isFollowUp = 'N'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT +
                " AND a.isFollowUp = 'Y'" +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_TOTAL_HOSPITAL_APPOINTMENT_APPOINTMENT_AMOUNT(
            TransactionLogSearchDTO searchRequestDTO) {

        return SELECT_CLAUSE_TO_GET_TOTAL_HOSPITAL_DEPARTMENT_APPOINTMENT_AMOUNT +
                GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG_DETAILS(searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_GET_HOSPITAL_DEPARTMENT_AMOUNT_AND_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND apst.appointmentServiceType.code = :appointmentServiceTypeCode";

    private static String SELECT_CLAUSE_TO_FETCH_HOSPITAL_DEPARTMENT_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE (SUM(ard.refundAmount ),0) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND a.status='RE'" +
                    " AND apst.appointmentServiceType.code = :appointmentServiceTypeCode";

    private static String SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND a.status='RE'" +
                    " AND apst.appointmentServiceType.code = :appointmentServiceTypeCode";

    private static String SELECT_CLAUSE_TO_GET_TOTAL_HOSPITAL_DEPARTMENT_APPOINTMENT_AMOUNT =
            "SELECT" +
                    " COALESCE (SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN HospitalAppointmentServiceType apst ON apst.id=a.hospitalAppointmentServiceType.id " +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                    " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " hd.status!='D'" +
                    " AND apst.appointmentServiceType.code = :appointmentServiceTypeCode";


}
