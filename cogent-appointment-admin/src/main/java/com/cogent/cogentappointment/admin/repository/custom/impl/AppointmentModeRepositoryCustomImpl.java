package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentMode.AppointmentModeMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentMode.AppointmentModeResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentModeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.CODE;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.NAME;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentModeLog.APPOINTMENT_MODE;
import static com.cogent.cogentappointment.admin.query.AppointmentModeQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

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
    public List<Object[]> validateDuplicity(AppointmentModeRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode());

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(AppointmentModeUpdateRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, requestDTO.getId())
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode());

        return query.getResultList();
    }

    @Override
    public List<AppointmentModeMinimalResponseDTO> search(AppointmentModeSearchRequestDTO searchRequestDTO,
                                                          Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_APPOINTMENT_MODE.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentModeMinimalResponseDTO> results = transformQueryToResultList(
                query, AppointmentModeMinimalResponseDTO.class);

        if (results.isEmpty()) throw APPOINTMENT_MODE_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public AppointmentModeResponseDTO fetchDetailsById(Long id) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_MODE_DETAILS)
                .setParameter(ID, id);
        try {
            return transformNativeQueryToSingleResult(query, AppointmentModeResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_MODE_NOT_FOUND.get();
        }
    }

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
