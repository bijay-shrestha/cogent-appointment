package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorMinResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.DoctorRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Doctor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.query.DoctorQuery.QUERY_TO_FETCH_DOCTOR_APPOINTMENT_CHARGE;
import static com.cogent.cogentappointment.esewa.query.DoctorQuery.QUERY_TO_FETCH_DOCTOR_APPOINTMENT_FOLLOW_UP_CHARGE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.DOCTOR_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.query.DoctorQuery.QUERY_TO_FETCH_MIN_DOCTOR_INFO;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformNativeQueryToResultList;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Transactional(readOnly = true)
public class DoctorRepositoryCustomImpl implements DoctorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<DoctorMinResponseDTO> fetchDoctorMinInfo(Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_MIN_DOCTOR_INFO)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorMinResponseDTO> results = transformNativeQueryToResultList(query, DoctorMinResponseDTO.class);

        if (results.isEmpty()) {
            throw DOCTOR_NOT_FOUND.get();
        }

        return results;
    }

    @Override
    public Double fetchDoctorAppointmentFollowUpCharge(Long doctorId, Long hospitalId) {
        try {
            Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_APPOINTMENT_FOLLOW_UP_CHARGE)
                    .setParameter(DOCTOR_ID, doctorId)
                    .setParameter(HOSPITAL_ID, hospitalId);
            return (Double) query.getSingleResult();
        } catch (NoResultException ex) {
            throw DOCTOR_NOT_FOUND.get();
        }
    }

    @Override
    public Double fetchDoctorAppointmentCharge(Long doctorId, Long hospitalId) {
        try {
            Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_APPOINTMENT_CHARGE)
                    .setParameter(DOCTOR_ID, doctorId)
                    .setParameter(HOSPITAL_ID, hospitalId);

            return (Double) query.getSingleResult();
        } catch (NoResultException ex) {
            throw DOCTOR_NOT_FOUND.get();
        }
    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () ->
            new NoContentFoundException(Doctor.class);

}
