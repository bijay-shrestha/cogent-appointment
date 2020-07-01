package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentHistorySearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentResponseWithStatusDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientRelationInfoResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.PatientRepository;
import com.cogent.cogentappointment.esewa.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.esewa.utils.AppointmentUtils;
import com.cogent.cogentappointment.persistence.model.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_DATE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_TIME;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ROOM_INFO_ID;
import static com.cogent.cogentappointment.esewa.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.esewa.query.AppointmentHospitalDepartmentQuery.QUERY_TO_FETCH_BOOKED_APPOINTMENT_HOSPITAL_DEPT_WISE;
import static com.cogent.cogentappointment.esewa.query.AppointmentHospitalDepartmentQuery.QUERY_TO_VALIDATE_IF_APPOINTMENT_EXISTS_DEPT_WISE;
import static com.cogent.cogentappointment.esewa.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentRoomInfoQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_ROOM_NUMBER;
import static com.cogent.cogentappointment.esewa.utils.AppointmentUtils.parseToAppointmentHistory;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final PatientRepository patientRepository;

    public AppointmentRepositoryCustomImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Long validateIfAppointmentExists(Date appointmentDate, String appointmentTime,
                                            Long doctorId, Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_APPOINTMENT_EXISTS)
                .setParameter(APPOINTMENT_DATE, utilDateToSqlDate(appointmentDate))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(APPOINTMENT_TIME, appointmentTime);

        return (Long) query.getSingleResult();
    }

    /*USED IN APPOINTMENT CHECK AVAILABILITY*/
    @Override
    public List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT)
                .setParameter(DATE, utilDateToSqlDate(requestDTO.getAppointmentDate()))
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, AppointmentBookedTimeResponseDTO.class);
    }

    /*eg. 2076-10-10 lies in between 2076-04-01 to 2077-03-31 Fiscal Year ie. 2076/2077*/
    @Override
    public String generateAppointmentNumber(String nepaliCreatedDate,
                                            Long hospitalId,
                                            String hospitalCode) {

        int year = getYearFromNepaliDate(nepaliCreatedDate);
        int month = getMonthFromNepaliDate(nepaliCreatedDate);

        String startingFiscalYear = fetchStartingFiscalYear(year, month);
        String endingFiscalYear = fetchEndingFiscalYear(year, month);

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER)
                .setParameter(FROM_DATE, startingFiscalYear)
                .setParameter(TO_DATE, endingFiscalYear)
                .setParameter(HOSPITAL_ID, hospitalId);

        System.out.println("startingFiscalYear------>"+startingFiscalYear);
        System.out.println("endingFiscalYear------>"+endingFiscalYear);
        System.out.println("HOSPITAL_ID------>"+hospitalId);


        System.out.println("QUERY-------TEST"+QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER);

        System.out.println("QUERY-------" + query.toString());

        System.out.println("appointment number ===== TEST"+AppointmentUtils.generateAppointmentNumber(query.getResultList(),
                startingFiscalYear, endingFiscalYear, hospitalCode));

        return AppointmentUtils.generateAppointmentNumber(query.getResultList(),
                startingFiscalYear, endingFiscalYear, hospitalCode);
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentHistorySearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPOINTMENTS)
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        List<AppointmentMinResponseDTO> pendingAppointments =
                transformQueryToResultList(query, AppointmentMinResponseDTO.class);

        if (pendingAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        return pendingAppointments;
    }

    @Override
    public Double calculateRefundAmount(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_AMOUNT)
                .setParameter(ID, appointmentId);

        try {
            return Double.parseDouble(query.getSingleResult().toString());
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
        }
    }

    @Override
    public List<AppointmentBookedTimeResponseDTO> fetchBookedAppointmentDeptWise(
            Date appointmentDate, Long hospitalDepartmentId, Long hospitalDepartmentRoomInfoId) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_HOSPITAL_DEPT_WISE(hospitalDepartmentRoomInfoId))
                .setParameter(DATE, utilDateToSqlDate(appointmentDate))
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        return transformQueryToResultList(query, AppointmentBookedTimeResponseDTO.class);
    }

    @Override
    public Long validateIfAppointmentExistsDeptWise(Date appointmentDate,
                                                    String appointmentTime,
                                                    Long hospitalDepartmentId,
                                                    Long hospitalDepartmentRoomInfoId) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_VALIDATE_IF_APPOINTMENT_EXISTS_DEPT_WISE(hospitalDepartmentRoomInfoId))
                .setParameter(APPOINTMENT_DATE, utilDateToSqlDate(appointmentDate))
                .setParameter(APPOINTMENT_TIME, appointmentTime)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        return (Long) query.getSingleResult();
    }

    @Override
    public AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_DETAILS_BY_ID)
                .setParameter(ID, appointmentId);

        List<AppointmentDetailResponseDTO> appointmentDetails =
                transformQueryToResultList(query, AppointmentDetailResponseDTO.class);

        if (appointmentDetails.isEmpty()) throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);

        return appointmentDetails.get(0);
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentHistorySearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_HISTORY)
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        List<AppointmentMinResponseDTO> appointmentHistory =
                transformQueryToResultList(query, AppointmentMinResponseDTO.class);

        if (appointmentHistory.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        return appointmentHistory;
    }

    @Override
    public AppointmentResponseWithStatusDTO searchAppointmentsForSelf(AppointmentSearchDTO searchDTO) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_SEARCH_APPOINTMENT_FOR_SELF(searchDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()))
                .setParameter(NAME, searchDTO.getName())
                .setParameter(MOBILE_NUMBER, searchDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(searchDTO.getDateOfBirth()))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, searchDTO.getAppointmentServiceTypeCode());

        List<AppointmentResponseDTO> appointmentHistory =
                transformQueryToResultList(query, AppointmentResponseDTO.class);

        if (appointmentHistory.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        appointmentHistory.forEach(appointment -> {
            if (!Objects.isNull(appointment.getHospitalDepartmentRoomInfoId())) {
                String roomNumber = fetchRoomNumber(appointment.getHospitalDepartmentRoomInfoId());
                appointment.setRoomNumber(roomNumber);
            }
        });

        return parseToAppointmentHistory(appointmentHistory);
    }

    @Override
    public AppointmentResponseWithStatusDTO searchAppointmentsForOthers(AppointmentSearchDTO searchDTO) {

        List<PatientRelationInfoResponseDTO> patientRelationInfo =
                patientRepository.fetchPatientRelationInfoHospitalWise(
                        searchDTO.getName(),
                        searchDTO.getMobileNumber(),
                        searchDTO.getDateOfBirth(),
                        searchDTO.getHospitalId()
                );

        String childPatientIds = patientRelationInfo.stream()
                .map(info -> info.getChildPatientId().toString())
                .collect(Collectors.joining(COMMA_SEPARATED));

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_SEARCH_APPOINTMENT_FOR_OTHERS(searchDTO, childPatientIds))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, searchDTO.getAppointmentServiceTypeCode());

        List<AppointmentResponseDTO> appointmentHistory =
                transformQueryToResultList(query, AppointmentResponseDTO.class);

        if (appointmentHistory.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        appointmentHistory.forEach(appointment -> {
            if (!Objects.isNull(appointment.getHospitalDepartmentRoomInfoId())) {
                String roomNumber = fetchRoomNumber(appointment.getHospitalDepartmentRoomInfoId());
                appointment.setRoomNumber(roomNumber);
            }
        });

        return parseToAppointmentHistory(appointmentHistory);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT);
        throw new NoContentFoundException(Appointment.class);
    };

    private String fetchRoomNumber(Long hospitalDepartmentRoomInfoId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_ROOM_NUMBER)
                .setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, hospitalDepartmentRoomInfoId);

        try {
            return (String) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
