package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.constants.QueryConstants;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.ApptServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.PrimaryAppointmentServiceTypeResponse;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.query.AppointmentServiceTypeQuery;
import com.cogent.cogentappointment.client.repository.custom.AppointmentServiceTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.AppointmentServiceTypeLog.APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.client.query.AppointmentServiceTypeQuery.QUERY_TO_FETCH_APPOINTMENT_SERVICE_TYPE_NAME_AND_CODE;
import static com.cogent.cogentappointment.client.query.AppointmentServiceTypeQuery.QUERY_TO_FETCH_MIN_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.client.query.AppointmentServiceTypeQuery.QUERY_TO_FETCH_PRIMARY_APPOINTMENT_SERVICE_TYPE_BY_HOSPIAL_ID;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 26/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentServiceTypeRepositoryCustomImpl implements AppointmentServiceTypeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveMinInfo() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MIN_APPOINTMENT_SERVICE_TYPE);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty())
            NO_APPOINTMENT_SERVICE_TYPE_FOUND.get();

        return results;
    }

    @Override
    public List<ApptServiceTypeDropDownResponseDTO> fetchSerivceTypeNameAndCodeList() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_SERVICE_TYPE_NAME_AND_CODE);

        List<ApptServiceTypeDropDownResponseDTO> results = transformQueryToResultList(query,
                ApptServiceTypeDropDownResponseDTO.class);

        if (results.isEmpty())
            NO_APPOINTMENT_SERVICE_TYPE_FOUND.get();

        return results;
    }

    @Override
    public PrimaryAppointmentServiceTypeResponse fetchAppointmentServiceTypeByHospital(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PRIMARY_APPOINTMENT_SERVICE_TYPE_BY_HOSPIAL_ID)
                .setParameter(HOSPITAL_ID,hospitalId);

        PrimaryAppointmentServiceTypeResponse results = transformQueryToSingleResult(query,
                PrimaryAppointmentServiceTypeResponse.class);

        if (results==null)
            NO_APPOINTMENT_SERVICE_TYPE_FOUND.get();

        return results;
    }

    private Supplier<NoContentFoundException> NO_APPOINTMENT_SERVICE_TYPE_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT_SERVICE_TYPE);
        throw new NoContentFoundException(AppointmentServiceType.class);
    };

}
