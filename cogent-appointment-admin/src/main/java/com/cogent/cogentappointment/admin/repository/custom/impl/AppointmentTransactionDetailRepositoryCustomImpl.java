package com.cogent.cogentappointment.admin.repository.custom.impl;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.HospitalDepartmentRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DoctorRevenueDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.FollowUpResponse;
import com.cogent.cogentappointment.admin.dto.response.dashboard.HospitalDepartmentRevenueDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentRevenueStatisticsUtils.*;
import static com.cogent.cogentappointment.admin.utils.DashboardUtils.revenueStatisticsResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDateInString;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;


/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentTransactionDetailRepositoryCustomImpl implements AppointmentTransactionDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Double getRevenueByDates(Date toDate, Date fromDate, Long hospitalId, String appointmentServiceTypeCode) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_REVENUE_BY_DATE(hospitalId))
                .setParameter(TO_DATE, utilDateToSqlDateInString(toDate))
                .setParameter(FROM_DATE, utilDateToSqlDateInString(fromDate))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        Double amount = (Double) query.getSingleResult();
        return (amount == null) ? 0D : amount;
    }

    @Override
    public AppointmentRevenueStatisticsResponseDTO calculateAppointmentStatistics(String toDate,
                                                                                  String fromDate,
                                                                                  Long hospitalId,
                                                                                  String appointmentServiceTypeCode) {

        AppointmentRevenueStatisticsResponseDTO responseDTO = new AppointmentRevenueStatisticsResponseDTO();

        calculateBookedAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO, appointmentServiceTypeCode);

        calculateCheckedInAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO, appointmentServiceTypeCode);

        calculateCancelledAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO, appointmentServiceTypeCode);

        calculateRefundedAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO, appointmentServiceTypeCode);

        calculateRevenueFromRefundedAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO, appointmentServiceTypeCode);

        calculateTotalRevenueExcludingBooked(responseDTO);

        calculateTotalRevenueIncludingBooked(responseDTO);

        return responseDTO;
    }

    /*Revenue from Booked*/
    private void calculateBookedAppointmentStatistics(String toDate,
                                                      String fromDate,
                                                      Long hospitalId,
                                                      AppointmentRevenueStatisticsResponseDTO responseDTO,
                                                      String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_REVENUE(hospitalId))
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> results = query.getResultList();

        parseBookedAppointmentDetails(results.get(0), responseDTO);
    }

    /*Revenue from Checked-In*/
    private void calculateCheckedInAppointmentStatistics(String toDate,
                                                         String fromDate,
                                                         Long hospitalId,
                                                         AppointmentRevenueStatisticsResponseDTO responseDTO,
                                                         String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_REVENUE(hospitalId))
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> results = query.getResultList();

        parseCheckedInAppointmentDetails(results.get(0), responseDTO);
    }

    /*Revenue from Cancelled*/
    private void calculateCancelledAppointmentStatistics(String toDate,
                                                         String fromDate,
                                                         Long hospitalId,
                                                         AppointmentRevenueStatisticsResponseDTO responseDTO,
                                                         String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_APPOINTMENT_REVENUE(hospitalId))
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> results = query.getResultList();

        parseCancelledAppointmentDetails(results.get(0), responseDTO);
    }

    /*Refund Amount*/
    private void calculateRefundedAppointmentStatistics(String toDate,
                                                        String fromDate,
                                                        Long hospitalId,
                                                        AppointmentRevenueStatisticsResponseDTO responseDTO,
                                                        String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUNDED_APPOINTMENT_AMOUNT(hospitalId))
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> results = query.getResultList();

        parseRefundedAppointmentDetails(results.get(0), responseDTO);
    }

    /*Revenue from Refund*/
    private void calculateRevenueFromRefundedAppointmentStatistics(String toDate,
                                                                   String fromDate,
                                                                   Long hospitalId,
                                                                   AppointmentRevenueStatisticsResponseDTO responseDTO,
                                                                   String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REVENUE_FROM_REFUNDED_APPOINTMENT(hospitalId))
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> results = query.getResultList();

        parseRevenueFromRefundedAppointmentDetails(results.get(0), responseDTO);
    }

    /*Client Revenue Amount = Revenue from Checked-In+ Revenue from Refund + Revenue from Cancelled */
    private void calculateTotalRevenueExcludingBooked(AppointmentRevenueStatisticsResponseDTO responseDTO) {
        Double totalAmount = responseDTO.getCheckedInAmount() +
                responseDTO.getRevenueFromRefundedAmount() +
                responseDTO.getCancelAmount();
        responseDTO.setTotalAmountExcludingBooked(totalAmount);
    }

    /*Client Revenue Amount Including Booked Appointments  =
     Revenue from Checked-In + Revenue from Refund + Revenue from Cancelled + Revenue from Booked*/
    private void calculateTotalRevenueIncludingBooked(AppointmentRevenueStatisticsResponseDTO responseDTO) {
        Double totalAmount = responseDTO.getCheckedInAmount() +
                responseDTO.getRevenueFromRefundedAmount() +
                responseDTO.getCancelAmount() +
                responseDTO.getBookedAmount();
        responseDTO.setTotalAmount(totalAmount);
    }

    @Override
    public RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO, Character filter) {

        final String queryByFilter = getQueryByFilter(dashBoardRequestDTO.getHospitalId(), filter);

        Query query = createQuery.apply(entityManager, queryByFilter)
                .setParameter(TO_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getToDate()))
                .setParameter(FROM_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getFromDate()))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, "DOC");

        List<Object[]> objects = query.getResultList();

        RevenueTrendResponseDTO responseDTO = revenueStatisticsResponseDTO(objects, filter);

        return responseDTO;
    }

    @Override
    public List<DoctorRevenueDTO> calculateDoctorRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_CALCULATE_DOCTOR_REVENUE(doctorRevenueRequestDTO))
                .setParameter(HOSPITAL_ID, doctorRevenueRequestDTO.getHospitalId())
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, DOCTOR_CONSULTATION_CODE);

        List<DoctorRevenueDTO> doctorRevenueDTOList = transformQueryToResultList(query, DoctorRevenueDTO.class);

        doctorRevenueDTOList.forEach(doctorRevenueDTO -> {
            Query queryToGetFollowUp = createQuery.apply(entityManager, QUERY_TO_GET_FOLLOW_UP)
                    .setParameter(SPECIALIZATION_ID, doctorRevenueDTO.getSpecializationId())
                    .setParameter(FROM_DATE, utilDateToSqlDateInString(doctorRevenueRequestDTO.getFromDate()))
                    .setParameter(TO_DATE, utilDateToSqlDateInString(doctorRevenueRequestDTO.getToDate()))
                    .setParameter(DOCTOR_ID, doctorRevenueDTO.getDoctorId());
            FollowUpResponse followUpResponse = transformQueryToSingleResult(queryToGetFollowUp,
                    FollowUpResponse.class);
            doctorRevenueDTO.setTotalFollowUp(followUpResponse.getCount());
            doctorRevenueDTO.setDoctorRevenue(doctorRevenueDTO.getDoctorRevenue() + followUpResponse.getAmount());
        });

        return doctorRevenueDTOList;
    }

    @Override
    public List<DoctorRevenueDTO> calculateCancelledRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO, Pageable pageable) {

        Query cancelled = createQuery.apply(entityManager, QUERY_TO_CALCULATE_DOCTOR_COMPANY_REVENUE(doctorRevenueRequestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDateInString(doctorRevenueRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDateInString(doctorRevenueRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, doctorRevenueRequestDTO.getHospitalId());

        addPagination.accept(pageable, cancelled);

        List<DoctorRevenueDTO> revenueDTOList = transformQueryToResultList(cancelled, DoctorRevenueDTO.class);

        return revenueDTOList;
    }

    @Override
    public List<HospitalDepartmentRevenueDTO> calculateHospitalDepartmentRevenue(
            HospitalDepartmentRevenueRequestDTO requestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_CALCULATE_HOSPITAL_DEPARTMENT_REVENUE(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        addPagination.accept(pageable, query);

        List<HospitalDepartmentRevenueDTO> revenueDTOList = transformQueryToResultList(query,
                HospitalDepartmentRevenueDTO.class);

        revenueDTOList.forEach(revenueDTO -> {
            Query queryToGetFollowUp = createQuery.apply(entityManager, QUERY_TO_GET_HOSPITAL_DEPARTMENT_FOLLOW_UP)
                    .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                    .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                    .setParameter(HOSPITAL_DEPARTMENT_ID, revenueDTO.getHospitalDepartmentId());

            FollowUpResponse followUpResponse = transformQueryToSingleResult(queryToGetFollowUp,
                    FollowUpResponse.class);

            revenueDTO.setTotalFollowUp(followUpResponse.getCount());
            revenueDTO.setDepartmentRevenue(revenueDTO.getDepartmentRevenue() + followUpResponse.getAmount());
        });

        return revenueDTOList;
    }

    @Override
    public List<HospitalDepartmentRevenueDTO> calculateCancelledHospitalDepartmentRevenue(
            HospitalDepartmentRevenueRequestDTO requestDTO, Pageable pageable) {
        Query cancelled = createQuery.apply(entityManager, QUERY_TO_CALCULATE_HOSPITAL_DEPT_COMPANY_REVENUE(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        addPagination.accept(pageable, cancelled);

        List<HospitalDepartmentRevenueDTO> revenueDTOList = transformQueryToResultList(cancelled, HospitalDepartmentRevenueDTO.class);

        return revenueDTOList;

    }

    private String getQueryByFilter(Long hospitalId, Character filter) {
        Map<Character, String> queriesWithFilterAsKey = new HashMap<>();
        queriesWithFilterAsKey.put('D', QUERY_TO_FETCH_REVENUE_DAILY(hospitalId));
        queriesWithFilterAsKey.put('W', QUERY_TO_FETCH_REVENUE_WEEKLY(hospitalId));
        queriesWithFilterAsKey.put('M', QUERY_TO_FETCH_REVENUE_MONTHLY(hospitalId));
        queriesWithFilterAsKey.put('Y', QUERY_TO_FETCH_REVENUE_YEARLY(hospitalId));

        return queriesWithFilterAsKey.get(filter);
    }

    public static String QUERY_TO_CALCULATE_DOCTOR_COMPANY_REVENUE(DoctorRevenueRequestDTO requestDTO) {

        return"SELECT" +
                " d.id as doctorId," +                                          //[0]
                " CASE WHEN" +
                " (d.salutation is null)" +
                " THEN d.name" +
                " ELSE" +
                " CONCAT_WS(' ',d.salutation, d.name)" +
                " END as doctorName," +                                          //[1]
                " CASE WHEN" +
                " (da.status is null OR da.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " da.fileUri" +
                " END as fileUri," +                                            //[2]
                " s.id as specializationId," +                                  //[3]
                " s.name as specializationName," +                              //[4]
                " COUNT(a.id) as cancelledAppointments," +                      //[5]
                " CASE" +
                " WHEN ard.status='PA'THEN COALESCE(SUM(atd.appointmentAmount ),0)" +
                " WHEN ard.status='R'THEN COALESCE(SUM(atd.appointmentAmount ),0)" +
                " WHEN ard.status='A'THEN (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0 )) " +
                " END  as cancelledRevenue" +
                " FROM Appointment a" +
                " LEFT JOIN AppointmentDoctorInfo ad ON a.id = ad.appointment.id" +
                " LEFT JOIN Doctor d ON d.id= ad.doctor.id" +
                " LEFT JOIN DoctorAvatar da ON d.id = da.doctorId.id" +
                " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                " LEFT JOIN Specialization s ON s.id = ad.specialization.id" +
                " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id" +
                " LEFT JOIN Hospital h ON h.id=d.hospital.id" +
                " WHERE" +
                " a.status IN ('RE','C')" +
                GET_WHERE_CLAUSE_TO_CALCULATE_DOCTOR_CANCELLED_REVENUE(requestDTO);
    }

    private static String GET_WHERE_CLAUSE_TO_CALCULATE_DOCTOR_CANCELLED_REVENUE(
            DoctorRevenueRequestDTO requestDTO) {

        String whereClause = " AND h.id=:hospitalId ";

        if (requestDTO.getDoctorId() != 0 && !Objects.isNull(requestDTO.getDoctorId()))
            whereClause += " AND d.id=" + requestDTO.getDoctorId();

        if (requestDTO.getSpecializationId() != 0 && !Objects.isNull(requestDTO.getSpecializationId()))
            whereClause += " AND s.id=" + requestDTO.getSpecializationId();


        whereClause += " AND DATE_FORMAT(atd.transactionDate,'%Y-%m-%d') BETWEEN :fromDate AND :toDate" +
                " GROUP BY d.id,s.id  ";

        return whereClause;
    }
}
