package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashBoardQuery {

    /*REVENUE STATISTICS*/
    public static String QUERY_TO_GET_REVENUE_BY_DATE =
            "SELECT" +
                    " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE " +
                    " (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";

    private static String SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS =
            " SELECT " +
                    " COUNT(a.id)," +                                   //[0]
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +        //[1]
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id";

    private static String GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS =
            " WHERE " +
                    " (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_REVENUE =
            SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    " AND a.status='PA'";

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_REVENUE =
            SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    " AND a.status='A'";

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT_REVENUE =
            SELECT_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    " AND a.status='C'";

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT_AMOUNT =
            "SELECT" +
                    " COUNT(a.id)," +                                       //[0]
                    " COALESCE (SUM(ard.refundAmount ),0)" +                //[1]
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                    GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    " AND a.status='RE' AND ard.status = 'A'";

    public static String QUERY_TO_FETCH_REVENUE_FROM_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0))" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                    GET_WHERE_CLAUSE_TO_CALCULATE_REVENUE_STATISTICS +
                    " AND a.status='RE' AND ard.status = 'A'";


    public static String QUERY_TO_OVER_ALL_APPOINTMENTS =
            "SELECT" +
                    " COUNT(ast.id)" +
                    " FROM AppointmentStatistics ast" +
                    " LEFT JOIN Appointment a ON a.id=ast.appointmentId.id"+
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                    " WHERE " +
                    " (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_COUNT_REGISTERED_APPOINTMENT =
            "SELECT" +
                    " COUNT(ast.id)" +
                    " FROM AppointmentStatistics ast" +
                    " LEFT JOIN Appointment a ON a.id=ast.appointmentId.id"+
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                    " WHERE" +
                    " ast.isRegistered='Y' " +
                    " AND (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT =
            "SELECT" +
                    " COUNT(ast.id)" +
                    " FROM AppointmentStatistics ast" +
                    " LEFT JOIN Appointment a ON a.id=ast.appointmentId.id"+
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id=atd.appointment.id" +
                    " WHERE" +
                    " ast.isNew='Y' " +
                    " AND (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                    " AND a.hospitalId.id=:hospitalId";

    public static String QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS =
            "SELECT" +
                    " COUNT(hpi.id)" +
                    " FROM HospitalPatientInfo hpi" +
                    " WHERE " +
                    " hpi.isRegistered='Y'" +
                    " AND hpi.hospital.id=:hospitalId";

    public static String QUERY_TO_FETCH_REVENUE_WEEKLY =
            "SELECT" +
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
                    " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE " +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " GROUP BY atd.transactionDate" +
                    " ORDER BY atd.transactionDate";


    public static String QUERY_TO_FETCH_REVENUE_YEARLY =
            " SELECT" +
                    " DATE_FORMAT(atd.transactionDate, '%b,%Y')," +
                    " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " GROUP BY DATE_FORMAT(atd.transactionDate, '%b,%Y')" +
                    " ORDER BY DATE_FORMAT(atd.transactionDate, '%b,%Y')";

    public static String QUERY_TO_FETCH_REVENUE_MONTHLY =
            "SELECT" +
                    " DATE_FORMAT(atd.transactionDate , '%e %b') As transactionDate," +
                    " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) AS revenue" +
                    " FROM AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND a.hospitalId.id=:hospitalId" +
                    " GROUP BY atd.transactionDate" +
                    " ORDER BY atd.transactionDate";

    public static String QUERY_TO_FETCH_REVENUE_DAILY =
            "SELECT" +
                    " DATE_FORMAT(atd.transactionDate , '%e %b,%Y') As transactionDate," +
                    " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) AS revenue" +
                    " FROM" +
                    " AppointmentTransactionDetail atd" +
                    " LEFT JOIN Appointment a ON" +
                    " a.id = atd.appointment.id" +
                    " LEFT JOIN Hospital h ON" +
                    " h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                    " AND a.hospitalId.id =:hospitalId" +
                    " GROUP BY" +
                    " atd.transactionDate" +
                    " ORDER BY" +
                    " atd.transactionDate";

    public static Function<AppointmentQueueRequestDTO, String> QUERY_TO_FETCH_APPOINTMENT_QUEUE =
            (searchDTO) ->
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
                            " LEFT JOIN Specialization s ON s.id = a.specializationId.id AND s.status='Y'" +
                            " LEFT JOIN Hospital h ON h.id = a.hospitalId.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(searchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_QUEUE(AppointmentQueueRequestDTO appointmentQueueRequestDTO) {

        String whereClause = " WHERE " +
                " a.status='PA'" +
                " AND DATE(a.appointmentDate) = :date" +
                " AND h.id= :hospitalId";

        if (!Objects.isNull(appointmentQueueRequestDTO.getDoctorId()))
            whereClause += " AND d.id = " + appointmentQueueRequestDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentTime DESC";

        return whereClause;
    }

    public static String QUERY_TO_GENERATE_DOCTOR_REVENUE_LIST(DoctorRevenueRequestDTO requestDTO) {


        return "SELECT" +
                " d.id as doctorId," +
                " d.name as doctorName," +
                " da.fileUri as fileUri," +
                " s.name as specialization," +
                " COUNT(d.id) as totalAppointmentCount," +
                " COALESCE(SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) AS revenueAmount" +
                " FROM Appointment a" +
                " LEFT JOIN Doctor d ON d.id= a.doctorId.id" +
                " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN Specialization s ON s.id=a.specializationId.id" +
                " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                GET_WHERE_CLAUSE_GENERATE_DOCTOR_REVENUE_LIST(requestDTO);
    }

    private static String GET_WHERE_CLAUSE_GENERATE_DOCTOR_REVENUE_LIST(DoctorRevenueRequestDTO requestDTO) {
        String whereClause = " WHERE h.id=:hospitalId";

        if (requestDTO.getSpecializationId() > 0)
            whereClause += " AND s.id=" + requestDTO.getSpecializationId();

        if (requestDTO.getDoctorId() > 0)
            whereClause += " AND d.id=" + requestDTO.getDoctorId();

        whereClause +=
                " AND atd.transactionDate BETWEEN :fromDate AND :toDate" +
                        " GROUP BY d.id,da.id,s.id " +
                        " ORDER BY SUM(atd.appointmentAmount) DESC ";


        return whereClause;
    }


    public static String QUERY_TO_FETCH_REFUND_AMOUNT(Long doctorId, Long specializationId) {
        String query = "SELECT" +
                " COALESCE(SUM(ard.refundAmount ),0) as totalRefundedAmount" +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id" +
                " WHERE" +
                " ard.status = 'A'" +
                " AND ard.refundedDate BETWEEN :fromDate AND :toDate" +
                " AND a.hospitalId.id=:hospitalId";

        if (!Objects.isNull(doctorId))
            query += " AND a.doctorId.id = " + doctorId;

        if (!Objects.isNull(specializationId))
            query += " AND a.specializationId.id = " + specializationId;

        return query;
    }


    public static final String QUERY_TO_SELECT_DASHBOARD_FEATURES =
            " SELECT" +
                    " df.id as id," +
                    " df.name as name," +
                    " df.code as code" +
                    " FROM DashboardFeature df";

    public static final String QUERY_TO_FETCH_DASHBOARD_FEATURES(Long adminId) {

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
}
