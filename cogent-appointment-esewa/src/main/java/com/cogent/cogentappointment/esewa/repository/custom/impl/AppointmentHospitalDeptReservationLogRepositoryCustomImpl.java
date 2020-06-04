package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.repository.custom.AppointmentHospitalDeptReservationLogRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_DATE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_TIME;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.query.AppointmentHospitalDepartmentReservationLogQuery.QUERY_TO_FETCH_APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG_ID;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 02/06/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentHospitalDeptReservationLogRepositoryCustomImpl
        implements AppointmentHospitalDeptReservationLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchAppointmentHospitalDeptReservationLogId(AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG_ID(
                        requestDTO.getHospitalDepartmentRoomInfoId()))
                .setParameter(APPOINTMENT_DATE, utilDateToSqlDate(requestDTO.getAppointmentDate()))
                .setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId())
                .setParameter(APPOINTMENT_TIME, requestDTO.getAppointmentTime())
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        try {
            return (Long) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
