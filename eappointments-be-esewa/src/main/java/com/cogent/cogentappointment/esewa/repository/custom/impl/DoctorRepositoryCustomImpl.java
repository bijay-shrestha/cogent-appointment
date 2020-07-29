package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorMinResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.DoctorRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Doctor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.esewa.query.DoctorQuery.*;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DoctorRepositoryCustomImpl implements DoctorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final MinIOProperties minIOProperties;

    public DoctorRepositoryCustomImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    @Override
    public List<DoctorMinResponseDTO> fetchDoctorMinInfo(Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_MIN_DOCTOR_INFO)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<DoctorMinResponseDTO> results = transformNativeQueryToResultList(query, DoctorMinResponseDTO.class);

        if (results.isEmpty())
            throw DOCTOR_NOT_FOUND();

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
            throw DOCTOR_NOT_FOUND();
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
            throw DOCTOR_NOT_FOUND();
        }
    }

    private NoContentFoundException DOCTOR_NOT_FOUND() {
        log.error(CONTENT_NOT_FOUND, DOCTOR);
        throw new NoContentFoundException(Doctor.class);
    }
}
