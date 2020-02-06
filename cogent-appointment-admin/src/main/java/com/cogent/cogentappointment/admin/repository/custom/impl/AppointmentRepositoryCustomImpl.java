package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.*;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.AppointmentUtils;
import com.cogent.cogentappointment.admin.utils.commons.AgeConverterUtils;
import com.cogent.cogentappointment.persistence.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

;
;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AppointmentBookedTimeResponseDTO> checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT)
                .setParameter(QueryConstants.DATE, utilDateToSqlDate(requestDTO.getAppointmentDate()))
                .setParameter(QueryConstants.DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(QueryConstants.SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, AppointmentBookedTimeResponseDTO.class);
    }

    @Override
    public String generateAppointmentNumber(String nepaliCreatedDate) {

        int year = getYearFromNepaliDate(nepaliCreatedDate);
        int month = getMonthFromNepaliDate(nepaliCreatedDate);

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER)
                .setParameter(QueryConstants.FROM_DATE, fetchStartingFiscalYear(year, month))
                .setParameter(QueryConstants.TO_DATE, fetchEndingFiscalYear(year, month));

        return AppointmentUtils.generateAppointmentNumber(query.getResultList());
    }

    @Override
    public List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                              Date toDate,
                                                                              Long doctorId,
                                                                              Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES)
                .setParameter(QueryConstants.FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(QueryConstants.TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(QueryConstants.DOCTOR_ID, doctorId)
                .setParameter(QueryConstants.SPECIALIZATION_ID, specializationId);

        return transformQueryToResultList(query, AppointmentBookedDateResponseDTO.class);
    }

    @Override
    public Long fetchBookedAppointmentCount(Date fromDate, Date toDate, Long doctorId, Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT)
                .setParameter(QueryConstants.FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(QueryConstants.TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(QueryConstants.DOCTOR_ID, doctorId)
                .setParameter(QueryConstants.SPECIALIZATION_ID, specializationId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_APPOINTMENT.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentMinimalResponseDTO> results = transformQueryToResultList(
                query, AppointmentMinimalResponseDTO.class);

        if (results.isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public AppointmentPendingApprovalSearchDetailDTO findPendingApprovalList(Long hospitalId, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_VISIT_APPROVAL_DETAILS.apply(hospitalId));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentPendingApprovalResponseDTO> results = transformQueryToResultList(
                query, AppointmentPendingApprovalResponseDTO.class);

        AppointmentPendingApprovalSearchDetailDTO pendingApprovalSearchDetailDTO = new AppointmentPendingApprovalSearchDetailDTO();
        pendingApprovalSearchDetailDTO.setPendingApprovalList(results);

        Query query1 = createQuery.apply(entityManager, " SELECT sum(appointmentAmount) FROM appointmentTransactionDetail");

        Double totalAmount = (Double) query1.getSingleResult();


//        Double totalAmount = 0.00;
//        for (AppointmentPendingApprovalResponseDTO pendingApprovalResponseDTO : results) {
//
//            if (pendingApprovalResponseDTO.getAppointmentAmount() != null) {
//                totalAmount += pendingApprovalResponseDTO.getAppointmentAmount();
//            }
//        }

        pendingApprovalSearchDetailDTO.setTotalAmount(totalAmount);

        if (results.isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return pendingApprovalSearchDetailDTO;
        }
    }

    @Override
    public AppointmentResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_DETAILS)
                .setParameter(QueryConstants.ID, id);
        try {
            return transformQueryToSingleResult(query, AppointmentResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    @Override
    public AppointmentPendingApprovalSearchDetailDTO searchPendingVisitApprovals(AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_PENDING_APPOINTMENT_VISIT_APPROVAL_DETAILS.apply(searchRequestDTO))
                .setParameter("fromDate", searchRequestDTO.getFromDate()).setParameter("toDate", searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentPendingApprovalResponseDTO> results = transformQueryToResultList(
                query, AppointmentPendingApprovalResponseDTO.class);

        AppointmentPendingApprovalSearchDetailDTO pendingApprovalSearchDetailDTO = new AppointmentPendingApprovalSearchDetailDTO();
        pendingApprovalSearchDetailDTO.setPendingApprovalList(results);

        results.forEach(patientResponseDTO -> {
            String age = AgeConverterUtils.calculateAge(patientResponseDTO.getPatientDob());
            patientResponseDTO.setPatientAge(age);
        });

        Double totalAmount = 0.00;
        for (AppointmentPendingApprovalResponseDTO appointmentPendingApprovalResponseDTO : results) {

            if (appointmentPendingApprovalResponseDTO.getAppointmentAmount() != null) {
                totalAmount += appointmentPendingApprovalResponseDTO.getAppointmentAmount();
            }
        }

        pendingApprovalSearchDetailDTO.setTotalAmount(totalAmount);

        if (results.isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return pendingApprovalSearchDetailDTO;
        }
    }

//    @Override
//    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO) {
//
//        Query query = createNativeQuery.apply(entityManager,
//                QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(requestDTO))
//                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
//                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));
//
//        if (!Objects.isNull(requestDTO.getDoctorId()))
//            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());
//
//        if (!Objects.isNull(requestDTO.getSpecializationId()))
//            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());
//
//        List<Object[]> results = query.getResultList();
//
//        return parseQueryResultToAppointmentStatusResponseDTOS(results);
//    }


    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = ()
            -> new NoContentFoundException(Appointment.class);

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };
}
