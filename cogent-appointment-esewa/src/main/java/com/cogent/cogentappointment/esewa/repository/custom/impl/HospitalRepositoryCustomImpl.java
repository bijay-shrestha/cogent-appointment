package com.cogent.cogentappointment.esewa.repository.custom.impl;


import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalAppointmentServiceTypeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalFollowUpResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalLog.HOSPITAL_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.query.HospitalQuery.*;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;

/**
 * @author sauravi ON 02/04/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalMinResponseDTO> fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_MIN_HOSPITAL(searchRequestDTO));

        List<HospitalMinResponseDTO> results = transformNativeQueryToResultList(query, HospitalMinResponseDTO.class);

        if (results.isEmpty()) {
            log.error(CONTENT_NOT_FOUND, HOSPITAL);
            throw HOSPITAL_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public HospitalFollowUpResponseDTO fetchFollowUpDetails(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_DETAILS)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return transformQueryToSingleResult(query, HospitalFollowUpResponseDTO.class);
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND, HOSPITAL);
            throw HOSPITAL_NOT_FOUND.get();
        }
    }

    @Override
    public List<HospitalAppointmentServiceTypeResponseDTO> fetchHospitalAppointmentServiceType(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE_MIN_INFO)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<HospitalAppointmentServiceTypeResponseDTO> hospitalAppointmentServiceType =
                transformQueryToResultList(query, HospitalAppointmentServiceTypeResponseDTO.class);

        if (ObjectUtils.isEmpty(hospitalAppointmentServiceType))
            HOSPITAL_APPOINTMENT_SERVICE_TYPE_NOT_FOUND.get();

        return hospitalAppointmentServiceType;
    }

    private Supplier<NoContentFoundException> HOSPITAL_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL);
        throw new NoContentFoundException(Hospital.class);
    };

    private Supplier<NoContentFoundException> HOSPITAL_APPOINTMENT_SERVICE_TYPE_NOT_FOUND = () -> {

        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_APPOINTMENT_SERVICE_TYPE);
        throw new NoContentFoundException(HospitalAppointmentServiceType.class);
    };
}



