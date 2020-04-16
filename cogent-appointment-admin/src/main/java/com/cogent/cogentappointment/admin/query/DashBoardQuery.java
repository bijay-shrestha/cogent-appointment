package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;

import java.util.Objects;

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

    public static String QUERY_TO_GET_REVENUE_BY_DATE(Long hospitalId) {
        return "SELECT" +
                " SUM(atd.appointmentAmount)" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " WHERE " +
                " (atd.transactionDate BETWEEN :fromDate AND :toDate)" +
                " AND (a.status='PA' OR a.status = 'A')" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_OVER_ALL_APPOINTMENTS(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id)" +
                " FROM Appointment a" +
                " WHERE " +
                " (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_COUNT_REGISTERED_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id)" +
                " FROM Appointment a" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =a.patientId.id" +
                " AND hpi.hospital.id = a.hospitalId.id" +
                " WHERE hpi.isRegistered='Y'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId);
    }


    public static String QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT(Long hospitalId) {
        return "SELECT" +
                " COUNT(a.id)" +
                " FROM Appointment a" +
                " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =a.patientId.id" +
                " AND hpi.hospital.id = a.hospitalId.id" +
                " WHERE hpi.isRegistered='N'" +
                " AND (a.appointmentDate BETWEEN :fromDate AND :toDate)" +
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
                "  COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " WHERE " +
                " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " AND (a.status='PA' OR a.status = 'A')" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY atd.transactionDate" +
                " ORDER BY atd.transactionDate";
    }

    public static String QUERY_TO_FETCH_REVENUE_YEARLY(Long hospitalId) {
        return " SELECT" +
                " DATE_FORMAT(atd.transactionDate, '%b,%Y')," +
                " COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " WHERE" +
                " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " AND (a.status='PA' OR a.status = 'A')" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY DATE_FORMAT(atd.transactionDate, '%b,%Y')" +
                " ORDER BY DATE_FORMAT(atd.transactionDate, '%b,%Y')";
    }

    public static String QUERY_TO_FETCH_REVENUE_MONTHLY(Long hospitalId) {
        return "SELECT" +
                " DATE_FORMAT(atd.transactionDate , '%e %b') As transactionDate," +
                " COALESCE(SUM(atd.appointmentAmount),0) AS revenue" +
                " FROM AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON a.id=atd.appointment.id" +
                " WHERE" +
                " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " AND (a.status='PA' OR a.status = 'A')" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY atd.transactionDate" +
                " ORDER BY atd.transactionDate";
    }

    public static String QUERY_TO_FETCH_REVENUE_DAILY(Long hospitalId) {
        return "SELECT" +
                " DATE_FORMAT(atd.transactionDate , '%e %b,%Y') As transactionDate," +
                " COALESCE(SUM(atd.appointmentAmount),0) as revenue" +
                " FROM" +
                " AppointmentTransactionDetail atd" +
                " LEFT JOIN Appointment a ON" +
                " a.id = atd.appointment.id" +
                " LEFT JOIN Hospital h ON" +
                " h.id = a.hospitalId.id" +
                " WHERE" +
                " atd.transactionDate BETWEEN :fromDate AND :toDate" +
                " AND (a.status='PA' OR a.status= 'A')" +
                CLAUSE_TO_FIND_BY_HOSPITAL_ID(hospitalId) +
                " GROUP BY" +
                " atd.transactionDate" +
                " ORDER BY" +
                " atd.transactionDate";
    }

    public static String QUERY_TO_FETCH_REFUND_AMOUNT(Long doctorId, Long specializationId, Long hospitalId) {
        String query = "SELECT" +
                " COALESCE(SUM(ard.refundAmount ),0) as totalRefundedAmount" +
                " FROM" +
                " AppointmentRefundDetail ard" +
                " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id" +
                " WHERE" +
                " ard.status = 'A'" +
                " AND ard.refundedDate BETWEEN :fromDate AND :toDate";

        if (!Objects.isNull(hospitalId))
            query += " AND a.hospitalId.id = " + hospitalId;

        if (!Objects.isNull(doctorId))
            query += " AND a.doctorId.id = " + doctorId;

        if (!Objects.isNull(specializationId))
            query += " AND a.specializationId.id = " + specializationId;

        return query;
    }

    public static String QUERY_TO_GENERATE_DOCTOR_REVENUE_LIST(DoctorRevenueRequestDTO requestDTO) {


        return "SELECT" +
                " d.id as doctorId," +
                " d.name as doctorName," +
                " da.fileUri as fileUri," +
                " s.name as specialization," +
                " COUNT(d.id) as totalAppointmentCount," +
                " COALESCE(SUM(atd.appointmentAmount),0) as revenueAmount" +
                " FROM Appointment a" +
                " LEFT JOIN Doctor d ON d.id= a.doctorId.id" +
                " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN Specialization s ON s.id=a.specializationId.id" +
                " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                GET_WHERE_CLAUSE_GENERATE_DOCTOR_REVENUE_LIST(requestDTO);
    }

    private static String GET_WHERE_CLAUSE_GENERATE_DOCTOR_REVENUE_LIST(DoctorRevenueRequestDTO requestDTO) {
        String whereClause = " WHERE h.id=:hospitalId ";

        if (requestDTO.getSpecializationId() != 0 && !Objects.isNull(requestDTO.getSpecializationId()))
            whereClause += " AND s.id=" + requestDTO.getSpecializationId();

        if (requestDTO.getDoctorId() != 0 && !Objects.isNull(requestDTO.getDoctorId()))
            whereClause += " AND d.id=" + requestDTO.getDoctorId();

        whereClause +=
                " AND atd.transactionDate BETWEEN :fromDate AND :toDate" +
                        " GROUP BY d.id,da.id,s.id " +
                        " ORDER BY SUM(atd.appointmentAmount) DESC ";


        return whereClause;
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

    public static final String QUERY_TO_VALIDATE_DASHBOARD_FEATURE_COUNT(String ids) {

        return " SELECT " +
                " df " +                   //[0]
                " FROM DashboardFeature df" +
                " WHERE df.status ='Y'" +
                " AND df.id IN (" + ids + ")";

    }


}
