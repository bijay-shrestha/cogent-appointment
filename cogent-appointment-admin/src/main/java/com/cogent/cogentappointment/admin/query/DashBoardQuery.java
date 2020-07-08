package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.HospitalDepartmentRevenueRequestDTO;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static org.springframework.util.ObjectUtils.*;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashBoardQuery {

    private static String CLAUSE_TO_FIND_BY_HOSPITAL_ID(Long hospitalId) {
        if (hospitalId != null) {
            return " AND a.hospitalId=" + hospitalId;
        }
        return "";
    }

    private static String CLAUSE_TO_FIND_BY_HOSPITAL_ID_FOR_OVERALL_PATIENT(Long hospitalId) {
        if (hospitalId > 0) {
            return " AND hpi.hospital.id=" + hospitalId;
        }
        return "";
    }

    /*REVENUE STATISTICS*/
    public static String QUERY_TO_GET_REVENUE_BY_DATE(Long hospitalId) {
        return "SELECT" +
                " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                " WHERE " +
                " DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') >= :fromDate" +
                " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') <= :toDate" +
                " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

    private static String SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS =
            " SELECT " +
                    " COUNT(a.id)," +                                   //[0]
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +        //[1]
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id ";

    private static String GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS(Long hospitalId) {
        return " WHERE " +
                " (DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') BETWEEN :fromDate AND :toDate)" +
                " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode"+
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_REVENUE(Long hospitalId) {
        return SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS(hospitalId) +
                " AND a.status='PA'";
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_REVENUE(Long hospitalId) {
        return SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS(hospitalId) +
                " AND a.status='A'";
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT_REVENUE(Long hospitalId) {
        return SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS(hospitalId) +
                " AND a.status='C'";
    }

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT_AMOUNT(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id) as refundedAppointmentsCount," +
                " COALESCE (SUM(ard.refundAmount ),0) as refundedAmount" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id "+
                GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS(hospitalId) +
                " AND a.status='RE' AND ard.status = 'A'";
    }

    public static String QUERY_TO_FETCH_REVENUE_FROM_REFUNDED_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id) as revenueFromRefundedAppointmentsCount," +
                " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as revenueFromRefundedAmount" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id "+
                GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS(hospitalId) +
                " AND a.status='RE' AND ard.status = 'A'";
    }

    public static String QUERY_TO_OVER_ALL_APPOINTMENTS(Long hospitalId) {
        return "SELECT" +
                " COUNT(ast.id)" +
                " FROM AppointmentStatistics ast" +
                " LEFT JOIN Appointment a ON a.id=ast.appointmentId.id AND (a.status!='C' AND a.status!='RE')" +
                " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                " WHERE " +
                " (DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') BETWEEN :fromDate AND :toDate)" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

    public static String QUERY_TO_COUNT_REGISTERED_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(ast.id)" +
                " FROM AppointmentStatistics ast" +
                " LEFT JOIN Appointment a ON a.id=ast.appointmentId.id AND (a.status!='C' AND a.status!='RE')" +
                " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                " WHERE" +
                " ast.isRegistered='Y' " +
                " AND (DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') BETWEEN :fromDate AND :toDate)" +
                " AND a.isFollowUp='N'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

    public static String QUERY_TO_COUNT_FOLLOW_UP_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(ast.id)" +
                " FROM AppointmentStatistics ast" +
                " LEFT JOIN Appointment a ON a.id=ast.appointmentId.id AND (a.status!='C' AND a.status!='RE')" +
                " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                " WHERE" +
                " ast.isRegistered='Y' " +
                " AND (DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') BETWEEN :fromDate AND :toDate)" +
                " AND a.isFollowUp='Y'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }

    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(ast.id)" +
                " FROM AppointmentStatistics ast" +
                " LEFT JOIN Appointment a ON a.id=ast.appointmentId.id AND (a.status!='C' AND a.status!='RE')" +
                " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                " WHERE" +
                " ast.isNew='Y' " +
                " AND (DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') BETWEEN :fromDate AND :toDate)" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS(Long hospitalId) {
        return "SELECT" +
                " COUNT(hpi.id)" +
                " FROM HospitalPatientInfo hpi" +
                " WHERE " +
                " hpi.isRegistered='Y'" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID_FOR_OVERALL_PATIENT(hospitalId);
    }

    public static String QUERY_TO_FETCH_REVENUE_WEEKLY(Long hospitalId) {
        return "SELECT" +
                "  CASE" +
                "  WHEN DAYOFWEEK(atd.transactionDate) = 1" +
                "    THEN CONCAT('SUN,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                "  WHEN DAYOFWEEK(atd.transactionDate) = 2" +
                "    THEN CONCAT('MON,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                "  WHEN DAYOFWEEK(atd.transactionDate) = 3" +
                "    THEN CONCAT('TUE,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                "  WHEN DAYOFWEEK(atd.transactionDate) = 4" +
                "    THEN CONCAT('WED,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                "  WHEN DAYOFWEEK(atd.transactionDate) = 5" +
                "    THEN CONCAT('THU,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                "  WHEN DAYOFWEEK(atd.transactionDate) = 6" +
                "    THEN CONCAT('FRI,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                "  WHEN DAYOFWEEK(atd.transactionDate) = 7" +
                "    THEN CONCAT('SAT,',DATE_FORMAT(atd.transactionDate, '%b %e'))" +
                "  END AS day," +
                " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                " WHERE " +
                " DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') >= :fromDate" +
                " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') <= :toDate" +
                " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY DATE_FORMAT(atd.transactionDate, '%b %e')" +
                " ORDER BY DATE_FORMAT(atd.transactionDate, '%b %e')";
    }

    public static String QUERY_TO_FETCH_REVENUE_YEARLY(Long hospitalId) {
        return " SELECT" +
                " DATE_FORMAT(atd.transactionDate, '%b,%Y')," +
                " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                " WHERE" +
                " DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') >= :fromDate" +
                " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') <= :toDate" +
                " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY DATE_FORMAT(atd.transactionDate, '%b,%Y')" +
                " ORDER BY DATE_FORMAT(atd.transactionDate, '%b,%Y')";
    }

    public static String QUERY_TO_FETCH_REVENUE_MONTHLY(Long hospitalId) {
        return "SELECT" +
                " DATE_FORMAT(atd.transactionDate , '%e %b') As transactionDate," +
                " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                " WHERE" +
                " DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') >= :fromDate" +
                " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') <= :toDate" +
                " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY DATE_FORMAT(atd.transactionDate , '%e %b')" +
                " ORDER BY DATE_FORMAT(atd.transactionDate , '%e %b')";
    }

    public static String QUERY_TO_FETCH_REVENUE_DAILY(Long hospitalId) {
        return "SELECT" +
                " DATE_FORMAT(atd.transactionDate , '%e %b,%Y') As transactionDate," +
                " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as revenue" +
                " FROM" +
                " AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id = atd.appointment.id" +
                " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                " WHERE" +
                " DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') >= :fromDate" +
                " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') <= :toDate" +
                " AND hast.appointmentServiceType.code=:appointmentServiceTypeCode" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY" +
                " DATE_FORMAT(atd.transactionDate , '%e %b,%Y')" +
                " ORDER BY" +
                " atd.transactionDate";
    }

    public static String QUERY_TO_FETCH_REFUND_AMOUNT(Long doctorId, Long specializationId, Long hospitalId) {
        String query = "SELECT" +
                " COALESCE(SUM(ard.refundAmount ),0) as totalRefundedAmount" +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id" +
                " INNER JOIN AppointmentDoctorInfo adi ON adi.appointment.id=a.id"+
                " WHERE" +
                " ard.status = 'A'" +
                " AND ard.refundedDate BETWEEN :fromDate AND :toDate";

        if (!Objects.isNull(hospitalId))
            query += " AND a.hospitalId.id = " + hospitalId;

        if (!Objects.isNull(doctorId))
            query += " AND adi.doctor.id = " + doctorId;

        if (!Objects.isNull(specializationId))
            query += " AND adi.specialization.id = " + specializationId;

        return query;
    }

    /*APPOINTMENT EXCEPT REFUNDED ONES (i.e APPOINTMENT STATUS != 'RE')*/
    public static String QUERY_TO_CALCULATE_DOCTOR_REVENUE(DoctorRevenueRequestDTO requestDTO) {

        return "SELECT" +
                " d.id as doctorId," +                                                           //[0]
                " CASE WHEN" +
                " (d.salutation is null)" +
                " THEN d.name" +
                " ELSE" +
                " CONCAT_WS(' ',d.salutation, d.name)" +
                " END as doctorName," +                                                           //[1]
                " CASE WHEN" +
                " (da.status is null OR da.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " da.fileUri" +
                " END as fileUri," +                                                             //[2]
                " s.id as specializationId," +                                                   //[3]
                " s.name as specializationName," +                                               //[4]
                " COUNT(a.id) as successfulAppointments," +                                      //[5]
                " COALESCE(SUM(atd.appointmentAmount),0) as doctorRevenue" +                     //[6]
                " FROM Appointment a" +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id " +
                " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN Doctor d ON d.id= ad.doctor.id" +
                " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN Specialization s ON s.id=ad.specialization.id" +
                " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                " WHERE" +
                " (a.status !='RE' AND a.status !='C')" +
                " AND a.isFollowUp='N'" +
                GET_WHERE_CLAUSE_TO_CALCULATE_DOCTOR_REVENUE(requestDTO);
    }

    public static String QUERY_TO_GET_FOLLOW_UP =
            "SELECT" +
                    " Count(a.id) as count," +
                    " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN Doctor d ON d.id= ad.doctor.id" +
                    " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                    " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                    " WHERE" +
                    " (a.status !='RE' AND a.status !='C')" +
                    " AND a.isFollowUp='Y'" +
                    " AND d.id=:doctorId" +
                    " AND s.id=:specializationId" +
                    " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') >= :fromDate" +
                    " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') <= :toDate";

    private static String GET_WHERE_CLAUSE_TO_CALCULATE_DOCTOR_REVENUE(DoctorRevenueRequestDTO requestDTO) {
        String whereClause = " AND h.id=:hospitalId " +
                " AND hast.appointmentServiceType.code= :appointmentServiceTypeCode";

        if (requestDTO.getSpecializationId() != 0 && !Objects.isNull(requestDTO.getSpecializationId()))
            whereClause += " AND s.id=" + requestDTO.getSpecializationId();

        if (requestDTO.getDoctorId() != 0 && !Objects.isNull(requestDTO.getDoctorId()))
            whereClause += " AND d.id=" + requestDTO.getDoctorId();

        String fromDate = utilDateToSqlDate(requestDTO.getFromDate()) + " 00:00:00";
        String toDate = utilDateToSqlDate(requestDTO.getToDate()) + " 23:59:59";

        whereClause += " AND atd.transactionDate BETWEEN '" + fromDate + "' AND '" + toDate + "'" +
                " GROUP BY d.id, s.id ";

        return whereClause;
    }


    private static final String QUERY_TO_SELECT_DASHBOARD_FEATURES =
            " SELECT" +
                    " df.id as id," +
                    " df.name as name," +
                    " df.code as code" +
                    " FROM DashboardFeature df";

    public static String QUERY_TO_FETCH_DASHBOARD_FEATURES(Long adminId) {

        return QUERY_TO_SELECT_DASHBOARD_FEATURES +
                "  LEFT JOIN AdminDashboardFeature adf ON adf.dashboardFeatureId.id =df.id" +
                " LEFT JOIN Admin a ON a.id=adf.adminId.id" +
                " LEFT JOIN Profile p ON p.id=a.profileId.id" +
                " LEFT JOIN Department d ON d.id=p.department.id" +
                " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                " WHERE a.id=" + adminId +
                " AND adf.status='Y'";
    }

    public static final String QUERY_TO_FETCH_DASHBOARD_FEATURES =
            QUERY_TO_SELECT_DASHBOARD_FEATURES +
                    " WHERE df.status='Y'";

    public static String QUERY_TO_VALIDATE_DASHBOARD_FEATURE_COUNT(String ids) {

        return " SELECT " +
                " df " +                   //[0]
                " FROM DashboardFeature df" +
                " WHERE df.status ='Y'" +
                " AND df.id IN (" + ids + ")";

    }

    /*APPOINTMENT EXCEPT REFUNDED ONES (i.e APPOINTMENT STATUS != 'RE')*/
    public static String QUERY_TO_CALCULATE_HOSPITAL_DEPARTMENT_REVENUE(HospitalDepartmentRevenueRequestDTO requestDTO) {

        return "SELECT" +
                " ad.hospitalDepartment.id as hospitalDepartmentId," +
                " ad.hospitalDepartment.name as hospitalDepartmentName," +
                " COUNT(a.id) as successfulAppointments," +
                " COALESCE(SUM(atd.appointmentAmount),0) as departmentRevenue" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN HospitalDepartment hd On hd.id=ad.hospitalDepartment.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                " LEFT JOIN Hospital h ON h.id=hd.hospital.id" +
                " WHERE" +
                " (a.status !='RE' AND a.status !='C')" +
                " AND a.isFollowUp='N'" +
                GET_WHERE_CLAUSE_TO_CALCULATE_HOSPITAL_DEPARTMENT_REVENUE(requestDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_CALCULATE_HOSPITAL_DEPARTMENT_REVENUE(HospitalDepartmentRevenueRequestDTO requestDTO) {
        String whereClause = " AND h.id=:hospitalId ";

        if (requestDTO.getHospitalDepartmentId() != 0 && !Objects.isNull(requestDTO.getHospitalDepartmentId()))
            whereClause += " AND ad.hospitalDepartment.id=" + requestDTO.getHospitalDepartmentId();


        whereClause += " AND atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " GROUP BY ad.hospitalDepartment.id ";

        return whereClause;
    }


    public static String QUERY_TO_GET_HOSPITAL_DEPARTMENT_FOLLOW_UP =
            "SELECT" +
                    " Count(a.id) as count," +
                    " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN HospitalDepartment hd On hd.id=ad.hospitalDepartment.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                    " LEFT JOIN Hospital h ON h.id=hd.hospital.id" +
                    " WHERE" +
                    " (a.status !='RE' OR a.status !='C')" +
                    " AND a.isFollowUp='Y'" +
                    " AND ad.hospitalDepartment.id=:hospitalDepartmentId" +
                    " AND atd.transactionDate BETWEEN :fromDate AND :toDate";



    public static String QUERY_TO_CALCULATE_HOSPITAL_DEPT_COMPANY_REVENUE(HospitalDepartmentRevenueRequestDTO requestDTO) {

        return "SELECT" +
                " ad.hospitalDepartment.id as hospitalDepartmentId," +
                " ad.hospitalDepartment.name as hospitalDepartmentName," +
                " COUNT(a.id) as cancelledAppointments," +
                " CASE" +
                " WHEN ard.status='PA'THEN COALESCE(SUM(atd.appointmentAmount ),0)" +
                " WHEN ard.status='R'THEN COALESCE(SUM(atd.appointmentAmount ),0)" +
                " WHEN ard.status='A'THEN (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0 )) " +
                " END  as cancelledRevenue" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN HospitalDepartment hd On hd.id=ad.hospitalDepartment.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                " LEFT JOIN Hospital h ON h.id=hd.hospital.id" +
                " WHERE" +
                " a.status IN ('RE','C')" +
                GET_WHERE_CLAUSE_TO_CALCULATE_HOSPITAL_DEPARTMENT_REVENUE(requestDTO);
    }


}
