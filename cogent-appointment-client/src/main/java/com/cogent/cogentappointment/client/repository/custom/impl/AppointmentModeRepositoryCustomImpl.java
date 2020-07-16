package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentModeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.AppointmentMode.APPOINTMENT_MODE;
import static com.cogent.cogentappointment.client.query.AppointmentModeQuery.QUERY_TO_FETCH_ACTIVE_APPOINTMENT_MODE;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentModeRepositoryCustomImpl implements AppointmentModeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveMinAppointmentMode() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_APPOINTMENT_MODE);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw APPOINTMENT_MODE_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> APPOINTMENT_MODE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT_MODE);
        throw new NoContentFoundException(AppointmentMode.class);
    };
}
