package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalAppointmentServiceTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalLog.HOSPITAL_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.query.HospitalAppointmentServiceTypeQuery.QUERY_TO_FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.query.HospitalAppointmentServiceTypeQuery.QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE;

/**
 * @author smriti on 27/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalAppointmentServiceTypeRepositoryCustomImpl implements HospitalAppointmentServiceTypeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public HospitalAppointmentServiceType fetchHospitalAppointmentServiceType(Long hospitalId,
                                                                              String appointmentServiceTypeCode) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE,
                    HospitalAppointmentServiceType.class)
                    .setParameter(HOSPITAL_ID, hospitalId)
                    .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw HOSPITAL_APPOINTMENT_SERVICE_TYPE_NOT_FOUND.get();
        }
    }

    @Override
    public HospitalAppointmentServiceType fetchAssignedAppointmentServiceType(Long hospitalAppointmentServiceTypeId) {

        try {
            return entityManager.createQuery(QUERY_TO_FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE,
                    HospitalAppointmentServiceType.class)
                    .setParameter(ID, hospitalAppointmentServiceTypeId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw HOSPITAL_APPOINTMENT_SERVICE_TYPE_NOT_FOUND.get();
        }
    }

    private Supplier<NoContentFoundException> HOSPITAL_APPOINTMENT_SERVICE_TYPE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_APPOINTMENT_SERVICE_TYPE);
        throw new NoContentFoundException(HospitalAppointmentServiceType.class);
    };
}
