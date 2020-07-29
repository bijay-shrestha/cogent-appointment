package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentServiceType.AppointmentServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentServiceTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentServiceTypeLog.APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.admin.query.AppointmentServiceTypeQuery.QUERY_TO_FETCH_APPOINTMENT_SERVICE_TYPE_NAME_AND_CODE;
import static com.cogent.cogentappointment.admin.query.AppointmentServiceTypeQuery.QUERY_TO_FETCH_MIN_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

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
    public List<AppointmentServiceTypeDropDownResponseDTO> fetchServiceTypeNameAndCodeList() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_SERVICE_TYPE_NAME_AND_CODE);

        List<AppointmentServiceTypeDropDownResponseDTO> results = transformQueryToResultList(query,
                AppointmentServiceTypeDropDownResponseDTO.class);

        if (results.isEmpty())
            NO_APPOINTMENT_SERVICE_TYPE_FOUND.get();

        return results;
    }

    private Supplier<NoContentFoundException> NO_APPOINTMENT_SERVICE_TYPE_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT_SERVICE_TYPE);
        throw new NoContentFoundException(AppointmentServiceType.class);
    };

}
