package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalAppointmentServiceTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.APPOINTMENT_SERVICE_TYPE_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentServiceTypeLog.APPOINTMENT_SERVICE_TYPE;
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
    public HospitalAppointmentServiceType fetchHospitalAppointmentServiceType(Long appointmentServiceTypeId,
                                                                              Long hospitalId) {

        try {
            return entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE,
                    HospitalAppointmentServiceType.class)
                    .setParameter(APPOINTMENT_SERVICE_TYPE_ID, appointmentServiceTypeId)
                    .setParameter(HOSPITAL_ID, hospitalId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw APPOINTMENT_SERVICE_TYPE_NOT_FOUND.apply(appointmentServiceTypeId);
        }
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_SERVICE_TYPE_NOT_FOUND = (appointmentServiceTypeId) -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT_SERVICE_TYPE);
        throw new NoContentFoundException(AppointmentServiceType.class, "appointmentServiceTypeId",
                appointmentServiceTypeId.toString());
    };
}
