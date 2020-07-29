package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.HospitalAppointmentServiceTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.APPOINTMENT_SERVICE_TYPE_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.query.HospitalAppointmentServiceTypeQuery.QUERY_TO_UPDATE_IS_PRIMARY_STATUS;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;

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
    public void updateIsPrimaryStatus(Long hospitalId, Long appointmentServiceTypeId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_UPDATE_IS_PRIMARY_STATUS)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, appointmentServiceTypeId);

        try {
            query.executeUpdate();
        } catch (NoResultException e) {
            throw new NoContentFoundException(AppointmentServiceType.class, "appointmentServiceTypeId",
                    appointmentServiceTypeId.toString());
        }
    }
}
